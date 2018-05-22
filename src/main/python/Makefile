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

#PYTHON_FORMAT = pretty
PYTHON_FORMAT = progress
PYTHON_TEST_CMD = behave -f $(PYTHON_FORMAT) -t "$(shell $(TAG_FIXER))" -k

FEATURES   = features
DATA			 = data
VOLUME		 = -v$(CURDIR)

################################################################################
# Targets
################################################################################
.PHONY: all
all: clean deps build test deploy

.PHONY: clean
clean:
	rm -rf ./__pycache__ ;\
	coverage erase ; \
	rm -f coverage.xml

.PHONY: deps
deps:
	docker run --rm -v $(CURDIR):/local swaggerapi/swagger-codegen-cli generate \
		-i $(SCOREBOARD_API_YAML) \
		-l python \
		-o /local/swagger
	cd swagger; \
		pip3 install .

.PHONY: build
build:
		
.PHONY: test
test:
	$(PYTHON_TEST_CMD)

.PHONY: coverage
coverage:
	coverage run --source='.' -m behave; \
	coverage xml -i ; \
	sonar-scanner -Dsonar.login=$(SONAR_TOKEN) \
						-Dsonar.host.url=$(SONAR_URL) \
						-Dsonar.organization=$(SONAR_ORG) \
						-Dsonar.projectKey=org.$(SONAR_ORG).pacman-kata-python \
						-Dsonar.projectName=pacman-kata-python ;\
	python-codacy-coverage -r coverage.xml

.PHONY: deploy
deploy:

