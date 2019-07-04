package no.fint.audit.plugin.mongo;

import java.util.function.Function;

@FunctionalInterface
public interface CollectionNameSupplier extends Function<MongoAuditEvent, String> {
}
