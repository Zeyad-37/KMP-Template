#!/bin/bash

red=$(tput setaf 1)
green=$(tput setaf 2)
reset=$(tput sgr0)
OK="${green}OK${reset}"
CHECK="${green}☑${reset}"
CROSS="${red}☒${reset}"
KO="${red}KO${reset}"
local_branch="$(git rev-parse --abbrev-ref HEAD)"
valid_branch_regex="^(release|hotfix)\/.*$"
echo "0. Check protected branches"
if [[ $local_branch =~ $valid_branch_regex ]]
then
    echo "0. $CROSS Protected branches $KO"
    echo "***********************************************"
    echo -e "\033[31mInvalid branch name: $local_branch. Please note that release/ and hotfix/ are reserved branch name prefixes. \033[0m"
    echo -e "\033[31mYou must rename your branch to a valid name and try again \033[0m"
    echo "***********************************************"
    exit 1
fi
echo "0. $CHECK Check protected branches $OK"
echo " "


# 1. Check if release notes are modified or added
echo "1. Checking release_notes…"
RELEASE_NOTES=$(git diff --name-status HEAD origin/develop | grep 'release_notes' | grep 'M\|D')
if [[ -z "$RELEASE_NOTES" ]]; then
  echo "1. $CROSS release_notes $KO"
  echo "***********************************************"
  echo "           $KO release_notes missing           "
  echo -e "\033[31mPlease add release notes before pushing \033[0m"
  echo "***********************************************"
  exit 1
fi
echo "1. $CHECK release_notes $OK"
echo " "


# 2. a. Check if we have an upstream branch in remote
echo "2. a. Checking if there is an upstream branch in remote…"
if [[ -n $(git status -sb | grep 'origin') ]]; then

  echo "2. a $CHECK There was an upstream branch in remote $OK"

  # 2. b. Check if there are commits to push
  echo "2. b. Checking if there are commits to push…"
  commits=$(git log @{u}..)
  if [[ -z "$commits" ]]; then
    echo "2. b. $CROSS Check commits to push $KO"
    echo "***********************************************"
    echo "        $KO There are no commits to push       "
    echo "***********************************************"
    exit 0
  fi
  echo "$commits"
  echo "2. b. $CHECK Check commits to push $OK"
  echo " "
else
    echo "2. a. $CHECK There was NO upstream branch in remote $OK"
    echo " "
fi


# 3. Run Detekt
echo "3. Running Detekt…"
./gradlew detekt
EXIT_CODE=$?
if [[ ${EXIT_CODE} -ne 0 ]]; then
  echo "3. $CROSS Detekt $KO"
  echo "***********************************************"
  echo "               $KO Detekt failed               "
  echo " Please fix the above issues before committing "
  echo "***********************************************"
  exit ${EXIT_CODE}
fi
echo "3. $CHECK Detekt $OK"
echo " "


# 4. Run Unit Tests
echo "4. Running Unit Tests…"
CMD="./gradlew jacocoFullReport"
${CMD}
RESULT=$?
if [[ ${RESULT} -ne 0 ]]; then
  echo "4. $CROSS Unit Tests $KO"
  echo "***********************************************"
  echo "             $KO Unit Tests failed             "
  echo "$CMD"
  echo "***********************************************"
  exit 1
fi
echo "4. $CHECK Unit Tests $OK"
echo " "


# echo SUCCESS
echo "${green}***********${reset}"
echo "${green}*${reset} SUCCESS ${green}*${reset}"
echo "${green}***********${reset}"


exit 0
