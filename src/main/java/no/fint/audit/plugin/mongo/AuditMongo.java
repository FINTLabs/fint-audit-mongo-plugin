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
    private AsyncAuditMongo asyncAuditMongo;

    @Override
    public void audit(Event event, Status... statuses) {
        Event copy = new Event();
        BeanUtils.copyProperties(event, copy);
        for (Status status : statuses) {
            copy.setStatus(status);
            asyncAuditMongo.audit(copy, true);
        }
        event.setStatus(statuses[statuses.length - 1]);
    }

    @Override
    public void audit(Event event, boolean clearData) {
        Event copy = new Event();
        BeanUtils.copyProperties(event, copy);
        asyncAuditMongo.audit(copy, clearData);
    }
}
