package no.fint.audit.plugin.mongo;

import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import no.twingine.CircularBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.nio.BufferOverflowException;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class AuditMongoWorker {

    @Autowired
    private AuditMongoRepository auditMongoRepository;

    @Value("${fint.audit.mongo.buffer-size:100000}")
    private int bufferSize;

    private CircularBuffer<MongoAuditEvent> buffer;

    private AtomicLong index;

    @PostConstruct
    public void init() {
        buffer = new CircularBuffer<>(bufferSize);
        index = buffer.index();
    }

    @Scheduled(initialDelay = 5000, fixedRateString = "${fint.audit.mongo.rate:10000}")
    public void save() {
        try {
            while (true) {
                MongoAuditEvent mongoAuditEvent = buffer.take(index);
                if (mongoAuditEvent == null) {
                    break;
                }
                auditMongoRepository.insert(mongoAuditEvent);
            }
        } catch (UncategorizedMongoDbException e) {
            if (e.getCause() instanceof MongoException
                    && ((MongoException)e.getCause()).getCode() == 16500) {
                log.info("Request rate is large - backing off..");
            }
        } catch (BufferOverflowException e) {
            log.warn("Audit event buffer overflow, losing at least {} events!", bufferSize);
        }
    }

    public void audit(Event event, boolean clearData) {
        buffer.add(new MongoAuditEvent(event, clearData));
    }
}
