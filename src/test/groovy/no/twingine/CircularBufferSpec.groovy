package no.twingine

import spock.lang.Specification

import java.nio.BufferOverflowException
import java.util.concurrent.atomic.AtomicLong

class CircularBufferSpec extends Specification {
    def "Empty should return null"() {
        given:
        CircularBuffer<Integer> buf = new CircularBuffer<Integer>(10)
        AtomicLong idx = buf.index()

        when:
        def result = buf.take(idx)

        then:
        result == null
    }

    def "Empty should drain empty list"() throws Exception {
        given:
        CircularBuffer<Integer> buf = new CircularBuffer<Integer>(10)
        AtomicLong idx = buf.index()

        when:
        List<Integer> result = buf.drain(idx)

        then:
        result.size() == 0
    }

    def "Two drains yield partial results"() throws Exception {
        given:
        CircularBuffer<Integer> buf = new CircularBuffer<Integer>(10)
        AtomicLong idx = buf.index()

        when:
        for (int i = 0; i < 10; i++)
            buf.add(i)
        List<Integer> first = buf.drain(idx)

        then:
        first == [0,1,2,3,4,5,6,7,8,9]

        when:
        for (int i = 10; i < 20; i++)
            buf.add(i)
        List<Integer> second = buf.drain(idx)

        then:
        second == [10,11,12,13,14,15,16,17,18,19]
    }

    def "What happens at wraparound?"() throws Exception {
        given:
        CircularBuffer<Integer> buf = new CircularBuffer<Integer>(10)
        AtomicLong idx = buf.index()

        when:
        buf.add(1)
        def result = buf.take(idx)

        then:
        result == 1

        when:
        for (int i = 2; i < buf.size()+3; i++)
            buf.add(i)
        buf.drain(idx)

        then:
        thrown(BufferOverflowException)
    }

    def 'Buffer overflow'() throws Exception {
        given:
        final CircularBuffer<Integer> buffer = new CircularBuffer<Integer>(8)
        AtomicLong r = buffer.index()

        when:
        for (int i = 0; i < 10; ++i)
            buffer.add(i)
        buffer.take(r)

        then:
        thrown(BufferOverflowException)
    }

}
