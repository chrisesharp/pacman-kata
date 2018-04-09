# Repository Structure
```
pacman-kata/
├── Dockerfile.go-pacman                              (Go container)
├── Dockerfile.java-pacman                            (Java container)
├── Dockerfile.node-pacman                            (Node container)
├── Dockerfile.python-pacman                          (Python container)
├── Makefile                                          (Master build script)
├── pom.xml                                           (for Java Maven)
└── src/                                              (for all languages)
    ├── main/
    │   ├── go/                                       (Golang source/GOPATH)
    │   │   ├── bin/
    │   │   │   └── pacman                            (deployed Go build)
    │   │   └── src/
    │   │       └── pacman
    │   │           ├── features -> ../../../../test/resources/features
    │   │           ├── game.go                       (Go implementation)
    │   │           └── game_test.go                  (Go step defs)
    │   ├── java/                                      (regular maven structure)
    │   │   └── com/
    │   │       └── example/
    │   │           └── pacman/
    │   │               └── Game.java                 (Java implementation)
    │   ├── node/                                     (Node source)
    │   │   ├── game.js                               (Node implementation)
    │   │   ├── features -> ../../../../test/resources/features    
    │   │   ├── package.json                          (Node dependencies)
    │   │   └── support/                              (Cucumber support code)
    │   │       ├── steps.js                          (Node step defs)
    │   │       └── world.js                          (World context object)
    │   └── python/                                   (Python source)
    │       ├── features -> ../../test/resources/features
    │       ├── game.py                               (Python implementation)
    │       ├── python_steps/
    │       │   └── game_steps.py                     (Python step defs)
    │       ├── requirements-test.txt                 (Python test dependencies)
    │       └── requirements.txt                      (Python impl dependencies)
    └── test/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── pacman/
        │               ├── GameStepDef.java          (Java step defs )
        │               └── RunGameTest.java          (Cucumber bridge code)
        └── resources/
            ├── data/                                 (language agnostic)
            │   └── pacman.txt                        (sample level maps)
            └── features/                             (language agnostic)
                ├── XXX.feature                       (feature files)
                └── steps/                    (necessary evil for python behave)
                    └── use_steps.py                (python behave bridge code)
 ```
