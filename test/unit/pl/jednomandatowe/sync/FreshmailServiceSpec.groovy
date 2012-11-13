package pl.jednomandatowe.sync

import java.util.Date;

import grails.test.mixin.*
import pl.jednomandatowe.*

import org.junit.*
import groovy.json.JsonBuilder

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@Ignore
@Mock([Signature])
class FreshmailServiceSpec  extends spock.lang.Specification {
    def "get the most fresh signature "() {
        setup:							
		mockDomain(Signature,[
			[firstName: "test1", lastName: "test1", city : "Warszawa", email:"test1@test1.pl",dateCreated : new Date()-1,province : Province.DOLNOSLASKIE, allow : true, syncWithFreshMail : null],
			[firstName: "test2", lastName: "test2", city : "Warszawa", email:"test2@test2.pl",dateCreated : new Date(),province : Province.DOLNOSLASKIE, allow : true, syncWithFreshMail : null],
			[firstName: "test3", lastName: "test2", city : "Warszawa", email:"test3@test2.pl",dateCreated : new Date()+1,province : null, allow : true, syncWithFreshMail : null]		
			])		
		when:
        def result = service.getLatestSignatureToSynchronize()        
        then:
        result.email == "test2@test2.pl"
    }

    def "return JSON data for content of request to Freshmail"() {
    	setup:									
		def signature = new Signature(firstName: "test1", lastName: "test1", city : "Warszawa", email:"test1@test1.pl",dateCreated : new Date()-1,province : Province.DOLNOSLASKIE, allow : true, syncWithFreshMail : null)
		def json = new groovy.json.JsonBuilder()
		json {
			email "test1@test1.pl"
			list  '6z51konvod'
			//custom_fields {
			//	wojewodztwo 'DOLNOSLASKIE'
			//	miejscowosc 'Warszawa'
			//}
		}
		when:
        def result = service.getRequestContentInJSON(signature,'6z51konvod')        
        then:
        result.toString() == json.toString()			
    }
}
