package pl.jednomandatowe.sync

import java.util.Date;

import grails.test.mixin.*
import pl.jednomandatowe.*

import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(FreshmailService)
class FreshmailServiceSpec  extends spock.lang.Specification {
    def "Index action should redirect to list page"() {
        setup:
		mockDomain(Signature, [
			[firstName: "The Stand", lastName: 1000, city : "Warszawa", email:"test@test.pl",dataCreated : new Date(), province : Province.DOLNOSLASKIE, allow : true, syncWithFreshMail : null]])
		when:
        def test = service.synchronizeProvince()
        then:
        true == true
    }
}
