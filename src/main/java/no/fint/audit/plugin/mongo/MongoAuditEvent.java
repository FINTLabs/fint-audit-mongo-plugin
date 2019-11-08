package no.fint.audit.plugin.mongo;

import no.fint.audit.model.AuditEvent;
import no.fint.event.model.Event;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "auditEvent")
public class MongoAuditEvent extends AuditEvent {

    @Id
    private String id;

    private String partition;

    public MongoAuditEvent(Event event, boolean clearData) {
        super(event, clearData);
        id = UUID.randomUUID().toString();
        partition = event.getOrgId() + "/" + event.getSource();
    }
}
