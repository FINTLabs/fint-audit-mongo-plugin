# FINT Mongo Audit Plugin

[![Build Status](https://jenkins.rogfk.no/buildStatus/icon?job=FINTprosjektet/fint-audit-mongo-plugin/master)](https://jenkins.rogfk.no/job/FINTprosjektet/job/fint-audit-mongo-plugin/job/master/)

Interface for auditing

## Installation

build.gradle

```
repositories {
    maven {
        url  "http://dl.bintray.com/fint/maven"
    }
}

compile('no.fint:fint-audit-mongo-plugin:0.0.3')
```

## Usage

Set `@EnableFintAudit` on your application class.