package ru.ineureka.patents.legacy.api.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import ru.ineureka.patents.http.client.DefaultHttpClientResult
import ru.ineureka.patents.http.client.HttpClient
import ru.ineureka.patents.http.client.exception.HttpClientException
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCaseDto
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCheckPayRequestDto
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCheckRecordDto
import ru.ineureka.patents.legacy.api.v1.response.PayResponse
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class RequestSpec extends Specification {

    @Shared
    def mapper = getJsonMapper()

    @Unroll
    def "if get paid documents endpoint is ok -> #response"() {
        given:
        def httpClient = Stub(HttpClient) {
            get(_ as String) >> new DefaultHttpClientResult(toBytes(response as String))
        }
        def result = mapper.readValue(
                response, mapper.getTypeFactory().constructCollectionType(List.class, PaymentCaseDto.class))

        when:
        def request = new Request('test', httpClient)

        then:
        request.getUnpaidDocuments().toString() == result.toString()

        where:
        response                      || _
        '[]'                          || _
        '[{"id": "123", "count": "10", ' +
                '"date_created": "2020-01-01", ' +
                '"doc": "a", ' +
                '"max_due_date": "2020-10-01", ' +
                '"min_due_date": "2020-01-01", ' +
                '"partner_id": "1"}]' || _
    }

    @Unroll
    def "if get paid documents endpoint handles exceptions"() {
        given:
        def httpClient = Stub(HttpClient) {
            get(_ as String) >> { throw new HttpClientException() }
        }

        expect:
        new Request('test', httpClient).getUnpaidDocuments() == []
    }

    @Unroll
    def "if get unpaid case endpoint is ok -> #id, #response"() {
        given:
        def httpClient = Stub(HttpClient) {
            get(_ as String) >> new DefaultHttpClientResult(toBytes(response as String))
        }
        def result = mapper.readValue(
                response, mapper.getTypeFactory().constructCollectionType(List.class, PaymentCheckRecordDto.class))

        when:
        def request = new Request('test', httpClient)

        then:
        request.getUnpaidCase(id).toString() == result.toString()

        where:
        id  | response                              || _
        ''  | '[]'                                  || _
        '1' | '[]'                                  || _
        '3' | '[{"id": "1", "annuity": "1", "annuity_end": "1", ' +
                '"application_no": "1", "doc": "1", ' +
                '"due_date": "2020-01-01",' +
                '"paid": "0", "registration_number": "1", ' +
                '"registry": "fips", "type": "p"}]' || _
    }

    def "if get unpaid case endpoint handles exceptions"() {
        given:
        def httpClient = Stub(HttpClient) {
            get(_ as String) >> { throw new HttpClientException() }
        }

        expect:
        new Request('test', httpClient).getUnpaidCase('1') == []
    }

    @Unroll
    def "if post pay case endpoint is ok -> #response"() {
        given:
        def httpClient = Stub(HttpClient) {
            post(_ as String, _ as String) >> new DefaultHttpClientResult(toBytes(response as String))
        }
        def result = mapper.readValue(response, mapper.getTypeFactory().constructType(PayResponse.class))

        when:
        def request = new Request('test', httpClient)

        then:
        request.payCase(request_ as PaymentCheckPayRequestDto).toString() == result.toString()

        where:
        request_                                         | response
        new PaymentCheckPayRequestDto(['1'] as String[]) | '{"paid": 1}'
    }

    def "if post pay case endpoint handles exceptions"() {
        given:
        def httpClient = Stub(HttpClient) {
            post(_ as String, _ as String) >> { throw new HttpClientException() }
        }

        expect:
        new Request('test', httpClient)
                .payCase(new PaymentCheckPayRequestDto(['1'] as String[])) == null
    }

    def getJsonMapper() {
        ObjectMapper objectMapper = new ObjectMapper()

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        objectMapper.registerModule(new AfterburnerModule())

        objectMapper
    }

    def toBytes(String data) { data.getBytes() }
}
