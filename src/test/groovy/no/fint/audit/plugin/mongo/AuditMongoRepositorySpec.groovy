package no.fint.audit.plugin.mongo

import no.fint.event.model.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest(classes = TestApplication)
class AuditMongoRepositorySpec extends Specification {

    @Autowired
    private AuditMongoRepository auditMongoRepository

    def "Save audit event"() {
        given:
        Event event = new Event("rogfk.no", "FK", "GET", "C")
        MongoAuditEvent mongoAuditEvent = new MongoAuditEvent(event, true)

        when:
        auditMongoRepository.save(mongoAuditEvent)

        then:
        auditMongoRepository.getAllEvents().size() == 1
        auditMongoRepository.getAllEvents().get(0).event.orgId == "rogfk.no"

    }
}
