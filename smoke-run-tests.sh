#!/usr/bin/env bash

sbt scalafmtCheckAll scalafmtSbtCheck
sbt -Dperftest.runSmokeTest=true -DrunLocal=true gatling:test
