# FINT Mongo Audit Plugin

[![Build Status](https://travis-ci.org/FINTlibs/fint-audit-mongo-plugin.svg?branch=master)](https://travis-ci.org/FINTlibs/fint-audit-mongo-plugin)
[![Coverage Status](https://coveralls.io/repos/github/FINTlibs/fint-audit-mongo-plugin/badge.svg?branch=master)](https://coveralls.io/github/FINTlibs/fint-audit-mongo-plugin?branch=master)

Implementation of fint-audit-api using mongodb.

## Installation

build.gradle

```
repositories {
    maven {
        url  "http://dl.bintray.com/fint/maven"
    }
}

compile('no.fint:fint-audit-mongo-plugin:0.0.13')
```

## Usage

- Set `@EnableFintAudit` on your application class
- `@Autowire` in the FintAuditService interface and call `audit(Event event)`. This will automatically clear the event data
- If you need control of when to clear the event data, use `audit(Event event, boolean clearData)`

## Configuration

| Key | Default value |
|-----|---------------|
| fint.audit.mongo.databasename | fint-audit |
| fint.audit.mongo.hostname | localhost |
| fint.audit.mongo.port | 27017 |
| fint.audit.mongo.core-pool-size | 2 |
| fint.audit.mongo.max-pool-size | 4 |
| fint.audit.mongo.queue-capacity | 1000000 |
| fint.audit.test-mode | false |

The `fint.audit.test-mode` will setup an embedded mongodb using [Fongo](https://github.com/fakemongo/fongo).