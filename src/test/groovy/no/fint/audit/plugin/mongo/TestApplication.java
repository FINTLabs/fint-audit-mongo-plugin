package no.fint.audit.plugin.mongo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = AuditMongoRepository.class)
public class TestApplication {
}
