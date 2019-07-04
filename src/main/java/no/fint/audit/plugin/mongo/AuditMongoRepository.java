package no.fint.audit.plugin.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;


public class AuditMongoRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CollectionNameSupplier collectionNameSupplier;

    public void insert(MongoAuditEvent mongoAuditEvent) {
        mongoTemplate.insert(mongoAuditEvent, collectionNameSupplier.apply(mongoAuditEvent));
    }

    @Profile(value = "test")
    public List<MongoAuditEvent> getAllEvents() {
        return mongoTemplate.findAll(MongoAuditEvent.class);
    }
}
