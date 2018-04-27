#!/bin/bash

pushd src/main/go/src/pacman
../../bin/pacman $@
popd
