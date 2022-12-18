#!/bin/bash

DAY="$1"

touch 2022/D${DAY}_small.txt
touch 2022/D${DAY}_full.txt

git add 2022/D${DAY}_small.txt
git add 2022/D${DAY}_full.txt