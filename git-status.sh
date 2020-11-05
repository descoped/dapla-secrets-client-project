#!/usr/bin/env bash

printf '\dapla-secret-manager-project'
echo "----------------------------------------------------------------"
git status --short

while read -r repo
do
  printf '\n%s' "${repo}"
  echo "----------------------------------------------------------------"
  git -C ${repo} status --short
done < repos.txt
