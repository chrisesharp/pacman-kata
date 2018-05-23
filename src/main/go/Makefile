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

#GO_FORMAT = pretty
GO_FORMAT = progress

GO_TEST_CMD = go test  -coverprofile=coverage.out \
							--godog.format=$(GO_FORMAT) \
							--godog.tags="$(shell $(TAG_FIXER))"

GOPATH = $(CURDIR)

export GOPATH
export GOBIN = $(GOPATH)/bin
################################################################################
# Targets
################################################################################

.PHONY: all
all: clean deps build test deploy

.PHONY: clean
clean:
	cd src/pacman/game ; \
	rm -f coverage.out ; \
	rm -f src/pacman/swagger

.PHONY: coverage
coverage:
	cd src/pacman/game; sonar-scanner -Dsonar.login=$(SONAR_TOKEN) \
			-Dsonar.host.url=$(SONAR_URL) \
			-Dsonar.organization=$(SONAR_ORG) \
			-Dsonar.projectKey=org.$(SONAR_ORG).pacman-kata-go \
			-Dsonar.projectName=pacman-kata-go
	$(GOPATH)/bin/godacov -t $(CODACY_PROJECT_TOKEN) -r src/pacman/game/coverage.out -c $(TRAVIS_COMMIT)

.PHONY: deps
deps:
	docker run --rm -v $(CURDIR):/local swaggerapi/swagger-codegen-cli generate \
		-i $(SCOREBOARD_API_YAML) \
		-l go \
		-o /local/src/pacman/swagger
	cd src/pacman/swagger; \
		go get -d -v && go build -v ./...
	cd src/pacman/game; \
		go get -u github.com/DATA-DOG/godog/cmd/godog ; \
		go get -u github.com/schrej/godacov ; \
		go get -u golang.org/x/tools/cmd/stringer; 
	cd src/pacman/dir; PATH="$(PATH):$(GOBIN)" go generate

.PHONY: build
build:
	cd src/pacman/game; go get && go build


.PHONY: test
test:
	cd src/pacman/game; go get -t && $(GO_TEST_CMD) 


.PHONY: deploy
deploy:
	cd src/pacman; go install