package no.fint.audit;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import no.fint.audit.plugin.mongo.AsyncAuditMongo;
import no.fint.audit.plugin.mongo.AuditMongo;
import no.fint.audit.plugin.mongo.AuditMongoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@Import(AsyncConfig.class)
@EnableMongoRepositories(basePackageClasses = AuditMongoRepository.class)
public class FintAuditConfig extends AbstractMongoConfiguration {


    @Value("${fint.audit.mongo.databasename:fint-audit}")
    private String databaseName;

    @Value("${fint.audit.mongo.connection-string}")
    private String connectionString;

    @Value("${fint.audit.test-mode:false}")
    private String testMode;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public Mongo mongo() throws Exception {
        if (Boolean.valueOf(testMode)) {
            return new Fongo(databaseName).getMongo();
        } else {
            return new MongoClient(
                    new MongoClientURI(String.format(connectionString, databaseName))
            );

        }
    }

    @Override
    protected Collection<String> getMappingBasePackages() {
        List<String> packages = new ArrayList<>();
        packages.add(AuditMongoRepository.class.getPackage().getName());
        return packages;
    }

    @Bean
    public AuditMongo auditMongo() {
        return new AuditMongo();
    }

    @Bean
    public AsyncAuditMongo asyncAuditMongo() {
        return new AsyncAuditMongo();
    }

    @Bean
    public AuditMongoRepository auditMongoRepository() {
        return new AuditMongoRepository();
    }
}
