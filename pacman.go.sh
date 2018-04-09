#!/bin/bash

pushd src/main/go/bin
./pacman $@
popd
