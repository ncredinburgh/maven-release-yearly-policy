[![Build Status](https://travis-ci.org/ncredinburgh/maven-release-yearly-policy.svg?branch=master)](https://travis-ci.org/ncredinburgh/maven-release-yearly-policy) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ncredinburgh/maven-release-yearly-policy/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ncredinburgh/maven-release-yearly-policy)

Maven Release Yearly Policy
---------------------------

This versioning policy for use with Maven's 
[maven-release-plugin](http://maven.apache.org/maven-release/maven-release-plugin/) 
specifies a versioning scheme along the lines of that currently used 
by [IntelliJ](https://www.jetbrains.com/idea/download/previous.html), namely `year.major.minor`.  

Whilst not recommended for libraries where major version changes 
typically communicate breaking API changes, this versioning 
strategy is more useful for deploying consumer-facing software 
which does not expose APIs.

## Usage

This plugin requires Java 8 along with a recent version of the release plugin.  To use in your project edit your POM and set the `projectVersionPolicyId` configuration property of the maven-release-plugin to `yearly`, along with adding a dependency to this artifact, like so:

```xml
<plugin>
  <artifactId>maven-release-plugin</artifactId>
  <version>2.5.3</version>
  <configuration>
    <projectVersionPolicyId>yearly</projectVersionPolicyId>
  </configuration>
  <dependencies>
    <dependency>
      <groupId>com.ncredinburgh</groupId>
      <artifactId>maven-release-yearly-policy</artifactId>
      <version>1.0</version>
    </dependency>
  </dependencies>
</plugin>
```
