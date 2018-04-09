#!/bin/bash

pushd src/main/node
npm start -- $@
popd
