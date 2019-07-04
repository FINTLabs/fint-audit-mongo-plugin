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

    public void save(MongoAuditEvent mongoAuditEvent) {
        String collectionName = collectionNameSupplier.apply(mongoAuditEvent);
        log.debug("Try save to {} - {}", collectionName, mongoAuditEvent);
        mongoTemplate.save(mongoAuditEvent, collectionName);
    }

    @Profile(value = "test")
    public List<MongoAuditEvent> getAllEvents() {
        return mongoTemplate.findAll(MongoAuditEvent.class);
    }
}
