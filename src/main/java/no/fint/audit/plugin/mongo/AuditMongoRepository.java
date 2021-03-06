package no.fint.audit.plugin.mongo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@Slf4j
public class AuditMongoRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CollectionNameSupplier collectionNameSupplier;

    public void insert(MongoAuditEvent mongoAuditEvent) {
        String collectionName = collectionNameSupplier.apply(mongoAuditEvent);
        log.debug("Try insert to {} - {}", collectionName, mongoAuditEvent);
        mongoTemplate.insert(mongoAuditEvent, collectionName);
    }

    @Profile(value = "test")
    public List<MongoAuditEvent> getAllEvents() {
        return mongoTemplate.findAll(MongoAuditEvent.class);
    }
}
