#!/bin/bash

DAY="$1"

touch 2023/D${DAY}_small.txt
touch 2023/D${DAY}_full.txt

git add 2023/D${DAY}_small.txt
git add 2023/D${DAY}_full.txt