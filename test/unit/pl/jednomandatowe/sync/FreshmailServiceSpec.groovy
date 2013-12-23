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
class FreshmailServiceSpec extends spock.lang.Specification {
    
	def freshmailService
		
	def "synchronize the latest contact "() {
        setup:							
		mockDomain(Signature,[
			[firstName: "test1", lastName: "test1", city : "Warszawa", email:"test1@test1.pl",dateCreated : new Date()-1,province : Province.DOLNOSLASKIE, allow : true, syncWithFreshMail : null],
			[firstName: "test2", lastName: "test2", city : "Warszawa", email:"test2@test2.pl",dateCreated : new Date(),province : Province.DOLNOSLASKIE, allow : true, syncWithFreshMail : null],
			[firstName: "test3", lastName: "test2", city : "Warszawa", email:"test3@test2.pl",dateCreated : new Date()+1,province : null, allow : true, syncWithFreshMail : null]		
			])		
		when:
        freshmailService.synchronizeContact()       
        then:
        Signature.getByEmail("test2@test2.pl")[0].syncWithFreshMail == true 
	}

	def "add latest contact"() {
		setup:
		mockDomain(Signature,[
			[firstName: "test1", lastName: "test1", city : "Warszawa", email:"test1@test1.pl",dateCreated : new Date()-1,province : Province.DOLNOSLASKIE, allow : true, syncWithFreshMail : null, newSignature : true],
			[firstName: "test2", lastName: "test2", city : "Warszawa", email:"test2@test2.pl",dateCreated : new Date(),province : Province.DOLNOSLASKIE, allow : true, syncWithFreshMail : null, newSignature : true],
			[firstName: "test3", lastName: "test2", city : "Warszawa", email:"test3@test2.pl",dateCreated : new Date()+1,province : null, allow : true, syncWithFreshMail : null, newSignature : true]
			])
		when:
		freshmailService.addContact()
		then:
		Signature.getByEmail("test2@test2.pl")[0].syncWithFreshMail == true
		Signature.getByEmail("test2@test2.pl")[0].newSignature == false
	}

	
}
