[![Build Status](https://travis-ci.org/ncredinburgh/maven-release-yearly-policy.svg?branch=master)](https://travis-ci.org/ncredinburgh/maven-release-yearly-policy)

# Maven Release Yearly Policy

This versioning policy for use with Maven's maven-release plugin 
specifies a versioning scheme along the lines of that currently used 
by IntelliJ, namely `year.major.minor`.  

Whilst not recommended for libraries where major version changes 
typically communicate breaking API changes, this versioning 
strategy is more useful for deploying consumer-facing software 
which does not expose APIs.
