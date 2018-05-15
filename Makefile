################################################################################
# This master makefile can build all language implementations.
# The default target of "all" can be switched to whatever you want.
# The target of "docker-all" builds docker images with each language
# implementation.
# Each one can then be run independently as a standalone image to run the game.
#
# The definitions below control the actual behaviour of the build. I recommend
# you leave everything below here well alone, unless you really know what you're
# doing!
#
################################################################################
# COMMAND DEFINITIONS
################################################################################
DOCKERBUILD		= docker build -t
DOCKERTEST		= docker run --rm -t

ifndef BDD
	BDD=not @leave
endif

ifndef TRAVIS_COMMIT
  TRAVIS_COMMIT=$(shell git rev-parse HEAD)
endif

NODE_FORMAT = progress
#NODE_FORMAT = node_modules/cucumber-pretty
JAVA_FORMAT = progress
PYTHON_FORMAT = pretty
GO_FORMAT = progress

TAG_FIXER = echo $(BDD)|sed "s/not /~/g" |sed "s/ or /,/g"

JAVA_TEST_CMD	= mvn test -Dcucumber.options="--glue com.example.pacman \
																						--plugin $(JAVA_FORMAT) \
																						--tags "$(shell $(TAG_FIXER))" \
																					  classpath:features"
GO_TEST_CMD = go test  -coverprofile=coverage.out \
											--godog.format=$(GO_FORMAT) \
											--godog.tags="$(shell $(TAG_FIXER))"
NODE_TEST_CMD = npm test -- -f $(NODE_FORMAT) --tags "$(BDD)"
PYTHON_TEST_CMD = behave -f $(PYTHON_FORMAT) -t "$(shell $(TAG_FIXER))" -k

JAVA_IMG   = java-pacman
JAVASRC    = src

GO_IMG     = go-pacman
GOSRC      = src/main/go
ifndef GOPATH
	GOPATH = $(CURDIR)/$(GOSRC)
endif

NODE_IMG   = node-pacman
NODESRC    = src/main/node

PYTHON_IMG = python-pacman
PYTHONSRC  = src/main/python

FEATURES   = src/test
VOLUME		 = -v$(CURDIR)

################################################################################
# Sonar and Codacy hooks
################################################################################
# SONAR_TOKEN = ** should be passed via to env **
SONAR_URL = https://sonarcloud.io
SONAR_ORG = pacman-kata

# CODACY_PROJECT_TOKEN = ** should be passed via to env **
CODACY_API_TOKEN=7FnGdigREcGP8j88LxQz
################################################################################
# Targets
################################################################################
# Switch between Docker or Local platform here
.PHONY: all
#all: docker-all
all: local-all

.PHONY: docker-all
docker-all: docker-java docker-go docker-node docker-python

.PHONY: local-all
local-all: local-java local-go local-node local-python

################################################################################
# Java
################################################################################
.PHONY: local-java
local-java: clean-java build-java test-java deploy-java

.PHONY: build-java
build-java:
	mvn compile install

.PHONY: test-java
test-java:
	$(JAVA_TEST_CMD)

.PHONY: clean-java
clean-java:
	mvn clean

.PHONY: coverage-java
coverage-java:
	mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
	    -Dsonar.host.url=$(SONAR_URL) \
	    -Dsonar.organization=$(SONAR_ORG) \
			-Dsonar.projectKey=org.$(SONAR_ORG).pacman-kata-java \
			-Dsonar.projectName=pacman-kata-java \
			-Dsonar.exclusions="**/*.xml" \
    	-Dsonar.login=$(SONAR_TOKEN)
	mvn com.gavinmogan:codacy-maven-plugin:coverage \
			-DcoverageReportFile=target/site/jacoco-ut/jacoco.xml \
			-DprojectToken=$(CODACY_PROJECT_TOKEN) -DapiToken=$(CODACY_API_TOKEN)

.PHONY: deploy-java
deploy-java:
	mvn package

.PHONY: docker-java
docker-java:
	$(DOCKERBUILD) $(JAVA_IMG) . -f Dockerfile.$(JAVA_IMG)
	$(DOCKERTEST)   $(VOLUME)/.m2:/root/.m2 $(VOLUME)/$(JAVASRC):/src -e BDD $(JAVA_IMG) $(JAVA_TEST_CMD)

################################################################################
# Golang
################################################################################

.PHONY: local-go
local-go: clean-go build-go test-go deploy-go

.PHONY: clean-go
clean-go:
	cd $(GOSRC)/src/pacman/game ; \
	rm -f coverage.out

.PHONY: coverage-go
coverage-go: export GOPATH = $(CURDIR)/$(GOSRC)
coverage-go: export GOBIN = $(CURDIR)/$(GOSRC)/bin
coverage-go:
	cd $(GOSRC)/src/pacman/game; sonar-scanner -Dsonar.login=$(SONAR_TOKEN) \
																				-Dsonar.host.url=$(SONAR_URL) \
																				-Dsonar.organization=$(SONAR_ORG) \
																				-Dsonar.projectKey=org.$(SONAR_ORG).pacman-kata-go \
																				-Dsonar.projectName=pacman-kata-go
	$(GOPATH)/bin/godacov -t $(CODACY_PROJECT_TOKEN) -r $(GOSRC)/src/pacman/game/coverage.out -c $(TRAVIS_COMMIT)

.PHONY: build-go
build-go: export GOPATH = $(CURDIR)/$(GOSRC)
build-go: export GOBIN = $(CURDIR)/$(GOSRC)/bin
build-go:
	cd $(GOSRC)/src/pacman/game; \
		go get -u github.com/DATA-DOG/godog/cmd/godog ; \
		go get -u github.com/schrej/godacov ; \
		go get -u golang.org/x/tools/cmd/stringer; \
		go get && go build
	cd $(GOSRC)/src/pacman/dir; PATH="$(PATH):$(GOBIN)" go generate
	docker run --rm -v $(CURDIR):/local swaggerapi/swagger-codegen-cli generate \
    -i /local/src/main/resources/swagger.json \
    -l go \
    -o /local/src/main/go/src/swagger

.PHONY: test-go
test-go: export GOPATH = $(CURDIR)/$(GOSRC)
test-go: export GOBIN = $(CURDIR)/$(GOSRC)/bin
test-go:
	cd $(GOSRC)/src/pacman/game; go get -t && $(GO_TEST_CMD) 


.PHONY: deploy-go
deploy-go: export GOPATH = $(CURDIR)/$(GOSRC)
deploy-go: export GOBIN = $(CURDIR)/$(GOSRC)/bin
deploy-go:
	cd $(GOSRC)/src/pacman; go install

.PHONY: docker-go
docker-go:
	$(DOCKERBUILD) $(GO_IMG) . -f Dockerfile.$(GO_IMG)
	$(DOCKERTEST)  $(VOLUME)/$(FEATURES):/test \
	 										-e BDD $(GO_IMG) /bin/bash -c "cd game && $(GO_TEST_CMD)"

################################################################################
# Node
################################################################################
.PHONY: local-node
local-node: clean-node build-node test-node deploy-node 

.PHONY: clean-node
clean-node:
	cd $(NODESRC) ; rm -rf ./coverage
	
.PHONY: coverage-node
coverage-node:
	cd $(NODESRC) ; npm run coverage && sonar-scanner \
																			-Dsonar.login=$(SONAR_TOKEN) \
																			-Dsonar.host.url=$(SONAR_URL) \
																			-Dsonar.organization=$(SONAR_ORG) \
																			-Dsonar.projectKey=org.$(SONAR_ORG).pacman-kata-node \
																			-Dsonar.projectName=pacman-kata-node
.PHONY: build-node
build-node:
	cd $(NODESRC) ; npm install

.PHONY: test-node
test-node:
	cd $(NODESRC) ; $(NODE_TEST_CMD)

.PHONY: deploy-node
deploy-node:
	cd $(NODESRC) ; npm install

.PHONY: docker-node
docker-node:
	$(DOCKERBUILD) $(NODE_IMG) . -f Dockerfile.$(NODE_IMG)
	$(DOCKERTEST)  $(VOLUME)/$(NODESRC):/src/  -e BDD $(NODE_IMG) $(NODE_TEST_CMD)

################################################################################
# Python
################################################################################
.PHONY: local-python
local-python: clean-python build-python test-python deploy-python

.PHONY: clean-python
clean-python:
	cd $(PYTHONSRC) ;\
	rm -rf ./__pycache__ ;\
	coverage erase ; \
	rm -f coverage.xml
	
.PHONY: coverage-python
coverage-python:
	cd $(PYTHONSRC) ; \
		coverage run --source='.' -m behave; \
		coverage xml -i ; \
		sonar-scanner -Dsonar.login=$(SONAR_TOKEN) \
							-Dsonar.host.url=$(SONAR_URL) \
							-Dsonar.organization=$(SONAR_ORG) \
							-Dsonar.projectKey=org.$(SONAR_ORG).pacman-kata-python \
							-Dsonar.projectName=pacman-kata-python ;\
		python-codacy-coverage -r coverage.xml


.PHONY: build-python
build-python:
	docker run --rm -v $(CURDIR):/local swaggerapi/swagger-codegen-cli generate \
		-i /local/src/main/resources/swagger.json \
		-l python \
		-o /local/src/main/python/swagger

.PHONY: test-python
test-python:
	cd $(PYTHONSRC) ; $(PYTHON_TEST_CMD)

.PHONY: deploy-python
deploy-python:

.PHONY: docker-python
docker-python:
	$(DOCKERBUILD) $(PYTHON_IMG) . -f Dockerfile.$(PYTHON_IMG)
	$(DOCKERTEST)  $(VOLUME)/$(PYTHONSRC):/opt/src/pacman \
									$(VOLUME)/$(FEATURES):/opt/test -e BDD \
									$(PYTHON_IMG) $(PYTHON_TEST_CMD)
