package no.fint.audit.plugin.mongo;

import no.fint.audit.FintAuditService;
import no.fint.event.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

public class AuditMongo implements FintAuditService {

    @Autowired
    private AuditMongoRepository auditMongoRepository;

    @Async
    @Override
    public void audit(Event event) {
        MongoAuditEvent mongoAuditEvent = new MongoAuditEvent(event, true);
        auditMongoRepository.save(mongoAuditEvent);
    }

    @Async
    @Override
    public void auditWithEventData(Event event) {
        MongoAuditEvent mongoAuditEvent = new MongoAuditEvent(event, false);
        auditMongoRepository.save(mongoAuditEvent);
    }
}
