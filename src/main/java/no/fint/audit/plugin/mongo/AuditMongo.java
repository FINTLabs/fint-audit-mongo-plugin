package no.fint.audit.plugin.mongo;

import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.event.model.Event;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class AuditMongo implements FintAuditService {

    @Autowired
    private AsyncAuditMongo asyncAuditMongo;

    @Override
    public void audit(Event event, boolean clearData) {
        Event copy = new Event();
        BeanUtils.copyProperties(event, copy);
        asyncAuditMongo.audit(copy, clearData);
    }
}
