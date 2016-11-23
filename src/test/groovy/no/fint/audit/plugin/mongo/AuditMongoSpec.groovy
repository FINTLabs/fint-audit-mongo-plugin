package no.fint.audit.plugin.mongo

import no.fint.audit.FintAuditService
import no.fint.audit.plugin.mongo.testutils.TestApplication
import no.fint.event.model.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@ContextConfiguration
@SpringBootTest(classes = TestApplication)
class AuditMongoSpec extends Specification {

    @Autowired
    private FintAuditService fintAuditService

    @Autowired
    private AuditMongoRepository auditMongoRepository

    def "Persist an AuditEvent"() {
        given:
        def event = new Event("rogfk.no", "FK", "GET", "C")
        def conditions = new PollingConditions(timeout: 2)

        when:
        fintAuditService.audit(event, true)

        then:
        conditions.eventually {
            def events = auditMongoRepository.allEvents
            assert events.size() == 1
            assert events[0].corrId == event.corrId
        }
    }

}
