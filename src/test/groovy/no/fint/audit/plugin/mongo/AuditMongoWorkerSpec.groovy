package no.fint.audit.plugin.mongo

import no.fint.event.model.Event
import org.springframework.data.mongodb.UncategorizedMongoDbException
import spock.lang.Specification

class AuditMongoWorkerSpec extends Specification {
    private AuditMongoWorker auditMongoWorker
    private AuditMongoRepository repository

    void setup() {
        repository = Mock(AuditMongoRepository)
        auditMongoWorker = new AuditMongoWorker(auditMongoRepository: repository, bufferSize: 10)
        auditMongoWorker.init()
    }

    def "Create new MongoAuditEvent and persist it"() {
        when:
        auditMongoWorker.audit(new Event(), true)
        auditMongoWorker.save()

        then:
        1 * repository.insert(_ as MongoAuditEvent)
    }

    def "Catch exception when an error happens auditing the event"() {
        when:
        auditMongoWorker.audit(new Event(), true)
        auditMongoWorker.save()

        then:
        1 * repository.insert(_ as MongoAuditEvent) >> { throw new UncategorizedMongoDbException('test exception', new Exception()) }
        noExceptionThrown()
    }

}
