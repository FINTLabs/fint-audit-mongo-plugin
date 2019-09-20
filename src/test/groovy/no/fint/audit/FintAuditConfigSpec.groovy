package no.fint.audit


import com.mongodb.MockMongoClient
import spock.lang.Specification

class FintAuditConfigSpec extends Specification {

    def "Use test mongo instance when test-mode is enabled"() {
        given:
        def config = new FintAuditConfig(testMode: 'true')

        when:
        def mongo = config.mongo()

        then:
        mongo != null
        mongo instanceof MockMongoClient
    }
}
