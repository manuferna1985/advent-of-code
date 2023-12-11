#!/bin/bash

YEAR="$1"
DAY="$2"

touch "${YEAR}/D${DAY}_small.txt"
touch "${YEAR}/D${DAY}_full.txt"

git add "${YEAR}/D${DAY}_small.txt"
git add "${YEAR}/D${DAY}_full.txt"