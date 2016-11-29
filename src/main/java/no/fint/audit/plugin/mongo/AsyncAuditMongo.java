package no.fint.audit.plugin.mongo;

import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

@Slf4j
public class AsyncAuditMongo {

    @Autowired
    private AuditMongoRepository auditMongoRepository;

    @Async
    public void audit(Event event, boolean clearData) {
        try {
            MongoAuditEvent mongoAuditEvent = new MongoAuditEvent(event, clearData);
            auditMongoRepository.save(mongoAuditEvent);
        } catch (Throwable t) {
            log.error("Exception when trying to audit log event", t);
        }
    }

}
