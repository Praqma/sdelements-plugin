# Jenkins SD Elements plugin

[![Build Status](https://travis-ci.org/Praqma/sdelements-plugin.svg?branch=master)](https://travis-ci.org/Praqma/sdelements-plugin
)

Jenkins integration for [SD Elements risk assessment](https://www.securitycompass.com/sdelements) by [Security Compass](https://www.securitycompass.com/).

Add a post-build action to pass/fail your job, based on the status of the risk assessment.

Development is in progress - no public release yet.

Developed by Praqma, funded by Security Compass

## GitHub Travis setup

### Releases

We've configured travis releases using the cli with the following commands:

```
$ travis setup releases 
Username: ReleasePraqma
Password for ReleasePraqma: **********
File to Upload: target/sdelements.hpi
Deploy only from Praqma/sdelements-plugin? |yes| yes
Encrypt API key? |yes| yes
```

The `.travis.yml` was modified slightly we added the tags flag to the deployment section: 

```
language: java
jdk:
- oraclejdk8
install: true
cache:
  directories:
  - "$HOME/.m2"
script:
- mvn package |  egrep -v 'Download(ing|ed)'
deploy:
  provider: releases
  api_key:
    secure: SEFzWVEG6H9Zcu7wOzYueGfhAisGiF7o/cMocTdYvGG1z4WwIUtHb1AfKmxbyWna72kybHtdBjnMAJ8l7gwp7UOPpHKsIkBKQ5SXa3S/2Fqj7Aq2UFioeqklqDpOlYSobUyp9epUTJnTwFTUFN4hYKxQG2ZL89xTNk3+5UxRsyH9KbL/4c6Gs8WRpmLKn0h1EHSGHw$
  file: target/sdelements.hpi
  skip_cleanup: true
  on:
    tags: true
```
