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
	BDD="not @leave"
endif

ifndef TRAVIS_COMMIT
  TRAVIS_COMMIT=$(shell git rev-parse HEAD)
endif

JAVA_TEST_CMD	= mvn test -Dcucumber.options="--glue com.example.pacman \
																						--tags $(shell echo $(BDD)|sed "s/not /~/g") \
																					  classpath:features"
GO_TEST_CMD = go test -coverprofile=coverage.out -args $(shell echo $(BDD)|sed "s/not /~/g")
NODE_TEST_CMD = npm test -- --tags $(BDD)
NODE_COVERAGE_CMD = npm run coverage
PYTHON_TEST_CMD = behave -t $(shell echo $(BDD)|sed "s/not /~/g") -k

JAVA_IMG   = java-pacman
JAVASRC    = src

GO_IMG     = go-pacman
GOSRC      = src/main/go

NODE_IMG   = node-pacman
NODESRC    = src/main/node

PYTHON_IMG = python-pacman
PYTHONSRC  = src/main/python

FEATURES   = src/test
VOLUME		 = -v$(CURDIR)
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
local-java: clean-java build-java test-java coverage-java deploy-java

.PHONY: build-java
build-java:
	mvn install

.PHONY: test-java
test-java:
	$(JAVA_TEST_CMD)

.PHONY: clean-java
clean-java:
	mvn clean

.PHONY: coverage-java
coverage-java:
	mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
	    -Dsonar.host.url=https://sonarcloud.io \
	    -Dsonar.organization=chrisesharp-github \
			-Dsonar.projectKey=org.chrisesharp.pacman-kata-java \
			-Dsonar.projectName=pacman-kata-java \
    	-Dsonar.login=$(SONAR_TOKEN)
	mvn com.gavinmogan:codacy-maven-plugin:coverage \
			-DcoverageReportFile=target/site/jacoco-ut/jacoco.xml \
			-DprojectToken=$(CODACY_PROJECT_TOKEN) -DapiToken=7FnGdigREcGP8j88LxQz

.PHONY: deploy-java
deploy-java:
	mvn package

.PHONY: docker-java
docker-java:
	$(DOCKERBUILD) $(JAVA_IMG) . -f Dockerfile.$(JAVA_IMG)
	$(DOCKERTEST)   $(VOLUME)/.m2:/root/.m2 $(VOLUME)/$(JAVASRC):/usr/app/src -e BDD $(JAVA_IMG) $(JAVA_TEST_CMD)

################################################################################
# Golang
################################################################################

.PHONY: local-go
local-go: clean-go build-go test-go coverage-go deploy-go

.PHONY: clean-go
clean-go:
	cd $(GOSRC)/src/pacman

.PHONY: coverage-go
coverage-go: export GOPATH = $(CURDIR)/$(GOSRC)
coverage-go: export GOBIN = $(CURDIR)/$(GOSRC)/bin
coverage-go:
	cd $(GOSRC)/src/pacman; $(GOBIN)/godacov -t $(CODACY_PROJECT_TOKEN) -r ./coverage.out -c $(TRAVIS_COMMIT)

.PHONY: build-go
build-go: export GOPATH = $(CURDIR)/$(GOSRC)
build-go: export GOBIN = $(CURDIR)/$(GOSRC)/bin
build-go:
	cd $(GOSRC)/src/pacman; go get && go build 

.PHONY: test-go
test-go: export GOPATH = $(CURDIR)/$(GOSRC)
test-go: export GOBIN = $(CURDIR)/$(GOSRC)/bin
test-go:
	cd $(GOSRC)/src/pacman; go get -t && $(GO_TEST_CMD) ;sonar-scanner -Dsonar.login=$(SONAR_TOKEN)


.PHONY: deploy-go
deploy-go: export GOPATH = $(CURDIR)/$(GOSRC)
deploy-go: export GOBIN = $(CURDIR)/$(GOSRC)/bin
deploy-go:
	cd $(GOSRC)/src/pacman; go install

.PHONY: docker-go
docker-go:
	$(DOCKERBUILD) $(GO_IMG) . -f Dockerfile.$(GO_IMG)
	$(DOCKERTEST)  $(VOLUME)/$(GOSRC)/src/pacman:/go/src/pacman  \
									$(VOLUME)/$(FEATURES):/go/test  -e BDD $(GO_IMG) $(GO_TEST_CMD)

################################################################################
# Node
################################################################################
.PHONY: local-node
local-node: clean-node build-node test-node coverage-node deploy-node 

.PHONY: clean-node
clean-node:
	cd $(NODESRC) ; rm -rf ./coverage
	
.PHONY: coverage-node
coverage-node:
	cd $(NODESRC) ; npm run coverage && sonar-scanner -Dsonar.login=$(SONAR_TOKEN)

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
local-python: clean-python build-python test-python coverage-python deploy-python

.PHONY: clean-python
clean-python:
	cd $(PYTHONSRC)
	rm -rf ./__pycache__
	coverage erase
	
.PHONY: coverage-python
coverage-python:
	cd $(PYTHONSRC) ; \
		coverage run --source='.' -m behave; \
		coverage xml -i ; \
		sonar-scanner -Dsonar.login=$(SONAR_TOKEN) ; \
		python-codacy-coverage -r coverage.xml


.PHONY: build-python
build-python:

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
