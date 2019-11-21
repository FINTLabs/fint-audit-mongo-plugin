package no.fint.audit.plugin.mongo;

import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import no.twingine.CircularBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.UncategorizedMongoDbException;

import javax.annotation.PostConstruct;
import java.nio.BufferOverflowException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class AuditMongoWorker {

    @Autowired
    private AuditMongoRepository auditMongoRepository;

    @Value("${fint.audit.mongo.buffer-size:200000}")
    private int bufferSize;

    @Value("${fint.audit.mongo.rate:2500}")
    private long rate;

    private CircularBuffer<MongoAuditEvent> buffer;

    private AtomicLong index;

    private ScheduledExecutorService executorService;

    @PostConstruct
    public void init() {
        buffer = new CircularBuffer<>(bufferSize);
        index = buffer.index();
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(this::save, rate, rate, TimeUnit.MILLISECONDS);
    }

    public void save() {
        MongoAuditEvent mongoAuditEvent = buffer.take(index);
        try {
            while (mongoAuditEvent != null) {
                auditMongoRepository.insert(mongoAuditEvent);
                mongoAuditEvent = buffer.take(index);
            }
        } catch (UncategorizedMongoDbException e) {
            if (e.getCause() instanceof MongoException
                    && ((MongoException) e.getCause()).getCode() == 16500) {
                log.info("Request rate is large - backing off..");
                index.decrementAndGet();
            }
        } catch (BufferOverflowException e) {
            log.warn("Audit event buffer overflow, losing at least {} events!", bufferSize);
        } catch (Exception e) {
            log.trace("Stopping due to unknown error", e);
        }
    }

    public void audit(Event event, boolean clearData) {
        buffer.add(new MongoAuditEvent(event, clearData));
    }
}
