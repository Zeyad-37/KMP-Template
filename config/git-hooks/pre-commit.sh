#!/bin/bash
if [[ -n $(git rev-parse --abbrev-ref HEAD | grep 'hotfix/\|master\|release/\|develop') ]]; then
  echo -e "\033[31mDirect commit on protected branch is prohibited. Please change your branch name\033[31m"
  exit 1
fi
exit 0
