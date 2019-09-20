package no.fint.audit.plugin.mongo;

import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class AuditMongo implements FintAuditService {

    @Autowired
    private AuditMongoWorker auditMongoWorker;

    @Override
    public void audit(Event event, Status... statuses) {
        for (Status status : statuses) {
            Event copy = new Event();
            BeanUtils.copyProperties(event, copy);
            copy.setStatus(status);
            auditMongoWorker.audit(copy, true);
        }
        event.setStatus(statuses[statuses.length - 1]);
    }

    @Override
    public void audit(Event event, boolean clearData) {
        Event copy = new Event();
        BeanUtils.copyProperties(event, copy);
        auditMongoWorker.audit(copy, clearData);
    }
}
