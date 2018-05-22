################################################################################
# Common environment setup for all languages
################################################################################

export DOCKERBUILD = docker build -t
export DOCKERTEST = docker run --rm -t

export SCOREBOARD_API = chrissharp/leaderboard-api/1.0.0
export SCOREBOARD_URL = https://virtserver.swaggerhub.com/$(SCOREBOARD_API)
export SCOREBOARD_API_YAML = https://app.swaggerhub.com/apiproxy/schema/file/$(SCOREBOARD_API)/swagger.yaml

ifndef BDD
	export BDD=not @leave
endif

ifndef UID
	export UID=$(shell id -u)
endif

ifndef TRAVIS_COMMIT
  export TRAVIS_COMMIT=$(shell git rev-parse HEAD)
endif

export TAG_FIXER = echo $(BDD)|sed "s/not /~/g" |sed "s/ or /,/g"

FEATURES = src/test
VOLUME = -v$(CURDIR)
