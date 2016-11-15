package no.fint.audit.plugin.mongo;

import no.fint.audit.EnableFintAudit;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableFintAudit
@SpringBootApplication(scanBasePackageClasses = AuditMongoRepository.class)
public class TestApplication {
}
