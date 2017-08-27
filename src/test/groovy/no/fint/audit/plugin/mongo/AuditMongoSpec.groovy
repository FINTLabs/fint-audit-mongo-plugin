package no.fint.audit.plugin.mongo

import no.fint.event.model.Event
import no.fint.event.model.Status
import spock.lang.Specification

class AuditMongoSpec extends Specification {
    private AuditMongo auditMongo
    private AsyncAuditMongo asyncAuditMongo
    private Event event

    void setup() {
        asyncAuditMongo = Mock(AsyncAuditMongo)
        auditMongo = new AuditMongo(asyncAuditMongo: asyncAuditMongo)
        event = new Event(corrId: '123', status: Status.NEW)
    }

    def "Audit event and clear data"() {
        when:
        auditMongo.audit(event, true)

        then:
        1 * asyncAuditMongo.audit(_ as Event, true)
    }

    def "Audit event with one new status"() {
        when:
        auditMongo.audit(event, Status.SENT_TO_CLIENT)

        then:
        1 * asyncAuditMongo.audit(_ as Event, true)
        event.status == Status.SENT_TO_CLIENT
    }

    def "Audit event with multiple new statuses"() {
        when:
        auditMongo.audit(event, Status.CACHE, Status.CACHE_RESPONSE, Status.SENT_TO_CLIENT)

        then:
        3 * asyncAuditMongo.audit(_ as Event, true)
        event.status == Status.SENT_TO_CLIENT
    }
}
