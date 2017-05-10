package no.fint.audit.plugin.mongo

import no.fint.event.model.Event
import spock.lang.Specification

class AsyncAuditMongoSpec extends Specification {
    private AsyncAuditMongo asyncAuditMongo
    private AuditMongoRepository repository

    void setup() {
        repository = Mock(AuditMongoRepository)
        asyncAuditMongo = new AsyncAuditMongo(auditMongoRepository: repository)
    }

    def "Create new MongoAuditEvent and persist it"() {
        when:
        asyncAuditMongo.audit(new Event(), true)

        then:
        1 * repository.save(_ as MongoAuditEvent)
    }

    def "Catch exception when an error happens auditing the event"() {
        when:
        asyncAuditMongo.audit(new Event(), true)

        then:
        1 * repository.save(_ as MongoAuditEvent) >> { throw new IllegalStateException('test exception') }
        noExceptionThrown()
    }

}
