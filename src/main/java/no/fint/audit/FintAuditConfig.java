package no.fint.audit;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lombok.extern.slf4j.Slf4j;
import no.fint.audit.plugin.mongo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@Configuration
@EnableScheduling
@EnableMongoRepositories(basePackageClasses = AuditMongoRepository.class)
public class FintAuditConfig extends AbstractMongoConfiguration {


    @Value("${fint.audit.mongo.databasename:fint-audit}")
    private String databaseName;

    @Value("${fint.audit.mongo.collection:auditEvent}")
    private String collectionName;

    @Value("${fint.audit.mongo.hostname:localhost}")
    private String hostname;

    @Value("${fint.audit.mongo.port:27017}")
    private int port;

    @Value("${fint.audit.mongo.connection-string:}")
    private String connectionString;

    @Value("${fint.audit.test-mode:false}")
    private String testMode;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public Mongo mongo() {
        if (Boolean.parseBoolean(testMode)) {
            return new Fongo(databaseName).getMongo();
        } else {
            if (StringUtils.isEmpty(connectionString)) {
                return new MongoClient(hostname, port);
            }
            return new MongoClient(
                    new MongoClientURI(String.format(connectionString, databaseName))
            );
        }
    }

    @Override
    protected Collection<String> getMappingBasePackages() {
        return Collections.singleton(AuditMongoRepository.class.getPackage().getName());
    }

    @Bean
    public AuditMongo auditMongo() {
        return new AuditMongo();
    }

    @Bean
    public AuditMongoWorker asyncAuditMongo() {
        return new AuditMongoWorker();
    }

    @Bean
    public AuditMongoRepository auditMongoRepository() {
        return new AuditMongoRepository();
    }

    @Bean
    public CollectionNameSupplier collectionNameSupplier() {
        if (collectionName.startsWith("$")) {
            Method readMethod = BeanUtils
                    .getPropertyDescriptor(MongoAuditEvent.class, collectionName.substring(1))
                    .getReadMethod();
            log.debug("Using method {} for collection name", readMethod);
            return mongoAuditEvent -> {
                try {
                    return (String) readMethod.invoke(mongoAuditEvent);
                } catch (Exception e) {
                    return "auditEvent";
                }
            };
        }
        return (mongoAuditEvent -> collectionName);
    }
}
