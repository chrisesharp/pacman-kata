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
include makefile.inc

JAVASRC    = src
JAVA_IMG   = java-pacman

NODESRC    = src/main/node
NODE_IMG   = node-pacman

PYTHONSRC  = src/main/python
PYTHON_IMG = python-pacman

GOSRC      = src/main/go
GO_IMG     = go-pacman

################################################################################
# Targets
################################################################################
# Switch between Docker or Local platform here
.PHONY: all
#all: docker-all
all: local-all

.PHONY: docker-all
docker-all: java-docker go-docker node-docker python-docker

.PHONY: local-all
local-all: java go node python

################################################################################
# Java
# Because maven spreads itself out all over the place, we'll run this at the top
# We include the definitions of each target here from the Makefile stored with 
# the rest of the java source.
################################################################################
include src/main/java/Makefile

.PHONY: java
java: clean-java deps-java build-java test-java deploy-java

.PHONY: clean-java
clean-java:
	$(CLEAN_JAVA)

.PHONY: deps-java
deps-java:
	$(DEPS_JAVA)

.PHONY: build-java
build-java:
	$(BUILD_JAVA) 

.PHONY: test-java
test-java:
	$(TEST_JAVA)

.PHONY: coverage-java
coverage-java:
	$(COVERAGE_JAVA)
	
.PHONY: deploy-java
deploy-java:
	$(DEPLOY_JAVA)

.PHONY: java-docker
java-docker: java-docker-package java-docker-image

.PHONY: java-docker-package
java-docker-package:
	$(DOCKERTEST) -u $(UID) -v $(CURDIR):/local swaggerapi/swagger-codegen-cli generate \
		-i $(SCOREBOARD_API_YAML) \
		-l java \
		-o /local/target/generated-sources/swagger
	$(DOCKERTEST) -v"$(CURDIR)/target/generated-sources/swagger":/usr/src/mymaven \
			-v"$(CURDIR)/.m2":/root/.m2 \
			-v"$(CURDIR)/target":/usr/src/mymaven/target \
			-w /usr/src/mymaven \
			-e BDD \
			maven \
			$(DEPLOY_JAVA)
	$(DOCKERTEST) -v"$(CURDIR)":/usr/src/mymaven \
			-v"$(CURDIR)/.m2":/root/.m2 \
			-v"$(CURDIR)/target":/usr/src/mymaven/target \
			-w /usr/src/mymaven \
			-e BDD \
			maven \
			$(DEPLOY_JAVA)

.PHONY: java-docker-test
java-docker-test:
	$(DOCKERTEST) -v "$(CURDIR)":/usr/src/mymaven \
			-v "$(CURDIR)/.m2":/root/.m2 \
			-v "$(CURDIR)/target":/usr/src/mymaven/target \
			-w /usr/src/mymaven \
			-e BDD \
			maven \
			$(TEST_JAVA)
			
.PHONY: java-docker-image
java-docker-image:
	$(DOCKERBUILD) $(JAVA_IMG) . -f Dockerfile.$(JAVA_IMG)
################################################################################
# Golang
# self-contained in the GOSRC directory
################################################################################

.PHONY: go
go:
	cd $(GOSRC) ; ${MAKE}

.PHONY: go-docker
GO_TEST = go test  -coverprofile=coverage.out \
							--godog.format=progress \
							--godog.tags="$(shell $(TAG_FIXER))"
go-docker:
	$(DOCKERBUILD) $(GO_IMG) . -f Dockerfile.$(GO_IMG)
	$(DOCKERTEST)  -v "$(CURDIR)/$(FEATURES)":/test \
	 		-e BDD $(GO_IMG) /bin/bash -c "cd game && $(GO_TEST)"


################################################################################
# Node
# self-contained in the NODESRC directory
################################################################################
.PHONY: node
node: 
	cd $(NODESRC) ; ${MAKE} 

.PHONY: node-docker
node-docker:
	$(DOCKERBUILD) $(NODE_IMG) . -f Dockerfile.$(NODE_IMG)
	$(DOCKERTEST)  -v "$(CURDIR)/$(NODESRC)":/src/  -e BDD  \
	$(NODE_IMG) npm test -- -f progress --tags "$(BDD)"
	
################################################################################
# Python
# self-contained in the PYTHONSRC directory
################################################################################
.PHONY: python
python:
	cd $(PYTHONSRC) ; ${MAKE}

.PHONY: python-docker
python-docker:
	$(DOCKERBUILD) $(PYTHON_IMG) . -f Dockerfile.$(PYTHON_IMG)
	$(DOCKERTEST)  -v "$(CURDIR)/$(PYTHONSRC)":/opt/src/pacman \
			-v "$(CURDIR)/$(FEATURES)":/opt/test -e BDD \
				$(PYTHON_IMG) behave -f progress -t "$(shell $(TAG_FIXER))" -k
