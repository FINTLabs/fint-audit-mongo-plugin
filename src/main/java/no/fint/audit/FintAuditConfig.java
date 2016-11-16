package no.fint.audit;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import no.fint.audit.plugin.mongo.AuditMongo;
import no.fint.audit.plugin.mongo.AuditMongoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = AuditMongoRepository.class)
public class FintAuditConfig extends AbstractMongoConfiguration {

    @Value("${fint.audit.mongo.databasename:fint-audit}")
    private String databaseName;

    @Value("${fint.audit.mongo.hostname:localhost}")
    private String hostname;

    @Value("${fint.audit.mongo.port:27017}")
    private int port;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(hostname, port);
    }

    @Override
    protected String getMappingBasePackage() {
        return AuditMongoRepository.class.getPackage().getName();
    }

    @Bean
    public AuditMongo auditMongo() {
        return new AuditMongo();
    }

    @Bean
    public AuditMongoRepository auditMongoRepository() {
        return new AuditMongoRepository();
    }
}
