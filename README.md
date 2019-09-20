# FINT Mongo Audit Plugin

[![Build Status](https://travis-ci.org/FINTlabs/fint-audit-mongo-plugin.svg?branch=master)](https://travis-ci.org/FINTlabs/fint-audit-mongo-plugin)
[![Coverage Status](https://coveralls.io/repos/github/FINTLabs/fint-audit-mongo-plugin/badge.svg?branch=master)](https://coveralls.io/github/FINTLabs/fint-audit-mongo-plugin?branch=master)

Implementation of fint-audit-api using mongodb.

## Installation

build.gradle

```
repositories {
    maven {
        url  "http://dl.bintray.com/fint/maven"
    }
}

compile('no.fint:fint-audit-mongo-plugin:1.4.0')
```

## Usage

- Set `@EnableFintAudit` on your application class
- `@Autowire` in the FintAuditService interface and call `audit(Event event)`. This will automatically clear the event data
- Use `audit(Event event, Status... statuses)` will set the status on the event and audit it. Multiple statuses will cause multiple audit log statements
- If you need control of when to clear the event data, use `audit(Event event, boolean clearData)`

## Configuration

| Key | Default value | Comment |
|-----|---------------|---------|
| fint.audit.mongo.connection-string | localhost | Example: `mongodb://app_user:bestPo55word3v3r@localhost/%s`|
| fint.audit.mongo.databasename | fint-audit | |
| fint.audit.mongo.hostname | localhost | if connection-string is set this will be ignored |
| fint.audit.mongo.port | 27017 | if connection-string is set this will be ignored |
| fint.audit.mongo.buffer-size | 100000 | Number of audit events that can be pending delivery. |
| fint.audit.mongo.rate | 5000 | Scheduling rate for worker thread writing events to MongoDB. |
| fint.audit.test-mode | false | |

The `fint.audit.test-mode` will setup an embedded mongodb using [Fongo](https://github.com/fakemongo/fongo).
