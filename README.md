# FINT Mongo Audit Plugin

[![Build Status](https://jenkins.rogfk.no/buildStatus/icon?job=FINTprosjektet/fint-audit-mongo-plugin/master)](https://jenkins.rogfk.no/job/FINTprosjektet/job/fint-audit-mongo-plugin/job/master/)

Implementation of fint-audit-api using mongodb.

## Installation

build.gradle

```
repositories {
    maven {
        url  "http://dl.bintray.com/fint/maven"
    }
}

compile('no.fint:fint-audit-mongo-plugin:0.0.5-SNAPSHOT')
```

## Usage

- Set `@EnableFintAudit` on your application class
- `@Autowire` in the FintAuditService interface and call `audit(event)` or `auditWithEventData(event)`

## Configuration

| Key | Default value |
|-----|---------------|
| fint.audit.mongo.databasename | fint-audit |
| fint.audit.mongo.hostname | localhost |
| fint.audit.mongo.port | 27017 |