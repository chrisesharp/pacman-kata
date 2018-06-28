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
include ../../../makefile.inc

#NODE_FORMAT = node_modules/cucumber-pretty
NODE_FORMAT = progress

NODE_TEST_CMD = npm test -- -f $(NODE_FORMAT)  --tags "($(BDD)) $(NOTRUN)"

NODE_IMG   = node-pacman


#GENERATOR = swaggerapi/swagger-codegen-cli
GENERATOR = openapitools/openapi-generator-cli
################################################################################
# Targets
################################################################################

.PHONY: all
all: clean deps test deploy

.PHONY: clean
clean:
	rm -rf ./coverage ./swagger

.PHONY: deps
deps:
	docker run --rm -u $(UID) -v $(CURDIR):/local $(GENERATOR) generate \
		-i $(SCOREBOARD_API_YAML) \
		-g javascript \
		-o /local/swagger
	npm install --save ./swagger
	cd swagger; npm link 
	npm link swagger

.PHONY: coverage
coverage:
	npm run coverage && sonar-scanner \
			-Dsonar.login=$(SONAR_TOKEN) \
			-Dsonar.host.url=$(SONAR_URL) \
			-Dsonar.organization=$(SONAR_ORG) \
			-Dsonar.projectKey=org.$(SONAR_ORG).pacman-kata-node \
			-Dsonar.projectName=pacman-kata-node

.PHONY: build
build:
	npm install

.PHONY: test
test:
	$(NODE_TEST_CMD)

.PHONY: deploy
deploy:
	npm install