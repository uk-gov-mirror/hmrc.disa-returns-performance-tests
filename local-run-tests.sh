#!/usr/bin/env bash
NO_OF_JSONS=$1

sbt scalafmtCheckAll scalafmtSbtCheck

sbt -DrunLocal=true -DnoOfJsons="${NO_OF_JSONS:=1}" gatling:test
