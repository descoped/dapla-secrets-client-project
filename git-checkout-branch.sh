#!/usr/bin/env bash

if [ $# -eq 0 ]; then
  echo ""
  echo "Usage:"
  echo ""
  echo "    ./git-checkout-branch.sh NEW_BRANCH"
  echo ""
  echo ""
  exit 0
fi

DIRS=$(cat ./git-repos.txt)

for i in $DIRS
do
  CMD="git -C $i checkout -b $1"
  echo "$CMD"
  eval "$CMD"
done
