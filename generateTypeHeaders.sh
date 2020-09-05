#!/bin/bash
origDir=$(pwd)
mkdir -p src/main/kotlin/externaljs/typescript # create the directories for the headers
cd src/main/kotlin/externaljs/typescript || exit

#download typescript headers for the DOM manip lib
curl https://raw.githubusercontent.com/microsoft/TypeScript/master/lib/lib.dom.d.ts -o lib.dom.d.ts

#generate Kotlin external function declaration headers from these, 
#with appropriate package names (to match the dir)
dukat -p externaljs.typescript lib.dom.d.ts
cd $origDir || exit
