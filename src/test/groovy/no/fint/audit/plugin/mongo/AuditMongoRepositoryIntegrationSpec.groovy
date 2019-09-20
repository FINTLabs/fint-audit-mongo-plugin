package no.fint.audit.plugin.mongo

import no.fint.audit.plugin.mongo.testutils.TestApplication
import no.fint.event.model.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest(classes = TestApplication)
class AuditMongoRepositoryIntegrationSpec extends Specification {

    @Autowired
    private AuditMongoRepository auditMongoRepository

    def "Save audit event"() {
        given:
        Event event = new Event('rogfk.no', 'FK', 'GET', 'C')
        MongoAuditEvent mongoAuditEvent = new MongoAuditEvent(event, true)

        when:
        auditMongoRepository.insert(mongoAuditEvent)

        then:
        auditMongoRepository.getAllEvents().size() == 1
        auditMongoRepository.getAllEvents().get(0).isClearData()
        auditMongoRepository.getAllEvents().get(0).event.orgId == 'rogfk.no'
    }
}
