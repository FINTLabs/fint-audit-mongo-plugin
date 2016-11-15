package no.fint.audit.plugin.mongo;

import no.fint.audit.FintAuditService;
import no.fint.event.model.Event;
import org.springframework.beans.factory.annotation.Autowired;

public class AuditMongo implements FintAuditService {

    @Autowired
    AuditMongoRepository auditMongoRepository;

    @Override
    public void audit(Event event, boolean clearData) {
        MongoAuditEvent mongoAuditEvent = new MongoAuditEvent(event, clearData);
        auditMongoRepository.save(mongoAuditEvent);
    }
}
