#!/bin/bash

cd grammar;
java -jar javacc.jar -OUTPUT_DIRECTORY=../src/it.nikmark.parser/ tptpgrammar.jj;
