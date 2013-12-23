
package pl.jednomandatowe.freshmail

import pl.jednomandatowe.*

import org.junit.*

import com.sun.net.httpserver.Authenticator.Success;

import grails.test.mixin.*
import grails.test.mixin.support.*
import groovyx.net.http.Method
import groovyx.net.http.ContentType
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.JSON
import groovyx.net.http.HTTPBuilder
import groovy.mock.interceptor.MockFor
import grails.util.Holders
import static com.lazythought.easyhash.HashGenerator.*

@TestMixin(GrailsUnitTestMixin)
//@TestFor(FreshmailProxyService)
class FreshmailProxyServiceSpec extends spock.lang.Specification {

	def freshmailProxyService

	def "test add method"() {
		setup:
		freshmailProxyService = new FreshmailProxyService()
		def httpBuildMock = new MockFor(HTTPBuilder.class)
		def reqPar = []
		def result
		def success
		def requestDelegate = [
			response:  [
				'statusLine': [ 'protocol': 'HTTP/1.1',
					'statusCode': status_code,
					'status': status
				]
			],
			uri: [:],
			headers: [:],
			body : [:]
		]
		httpBuildMock.demand.request(1){ Method met, ContentType type, Closure b ->
			b.delegate = requestDelegate
			b.call()
			if(success) {
				result = requestDelegate.response.success(requestDelegate.response,['status':'ok'])
			}else{
				result = requestDelegate.response.failure(requestDelegate.response,["errors":["message":"Brak tematu emaila", "code": "1202"],"status":"ERROR"])
			}
			reqPar << [method: met, type: type, req: b]
		}

		httpBuildMock.use{
			success = state
			freshmailProxyService.addNewContact("data:1")
		}

		expect:
		reqPar[0].method == POST
		reqPar[0].type == JSON
		reqPar[0].req.body == "data:1"
		reqPar[0].req.headers.'User-Agent' == 'Mozilla/5.0'
		reqPar[0].req.headers.'X-Rest-ApiKey' == Holders.config.freshmail.apiKey
		reqPar[0].req.headers.'X-Rest-ApiSign' == sha1(Holders.config.freshmail.apiKey + Holders.config.freshmail.addSubscriber + "data:1".toString() + Holders.config.freshmail.apiSecret)
		reqPar[0].req.uri.path == Holders.config.freshmail.addSubscriber
		result == restResp

		where:
		state | status  | status_code | restResp
		//false   | "Error" | 401			| ["errors":["message":"Brak tematu emaila", "code": "1202"],"status":"ERROR"]
		true 	| "Ok"    | 200			| ['status':'ok']
	}
}