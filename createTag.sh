#!/bin/bash
BRANCH="master"

# Read changelog
while read -r line
do
  changelog=$changelog$line
done < "CHANGELOG.md"

# Get the last changes and version
changelog=$(echo $changelog | cut -d"#" -f 5)
version=$(echo $changelog | cut -d"-" -f 1)
changelog=${changelog// $version/}
changelog=${changelog//-/\\n-}
changelog=${changelog/\\n-/-}

echo "App version: $version"
echo "Changelog: $changelog"

# Are we on the right branch?
if [ "$TRAVIS_BRANCH" = "$BRANCH" ]; then

  # Is this not a Pull Request?
  if [ "$TRAVIS_PULL_REQUEST" = false ]; then

    # Is this not a build which was triggered by setting a new tag?
    if [ -z "$TRAVIS_TAG" ]; then
      echo -e "Starting to tag commit.\n"

      git config user.email "travis@travis-ci.org"
      git config user.name "Travis"

      # Add tag and push to master.
      git tag -a $version -m "$changelog"
      git push origin --tags
      git fetch origin

      echo -e "Done magic with tags.\n"
    fi
  fi
fi
