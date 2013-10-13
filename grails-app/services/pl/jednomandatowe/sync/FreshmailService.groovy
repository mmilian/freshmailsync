package pl.jednomandatowe.sync
import pl.jednomandatowe.Signature
import groovy.json.JsonBuilder
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.JSON
import static com.lazythought.easyhash.HashGenerator.*
import groovyx.net.http.HTTPBuilder
import groovy.json.JsonSlurper

class FreshmailService {

	def grailsApplication

	def synchronizeWithFreshmail() {
		def entryToSynchronize = getLatestSignatureToSynchronize()
		if (entryToSynchronize) {
			def content = getRequestContentInJSON(entryToSynchronize,grailsApplication.config.freshmail.hashList)
			content=content.toString()
			def response = callExternalServiceWithHttpJSON(grailsApplication.config.freshmail.url,
					"/rest/subscriber/edit",
					grailsApplication.config.freshmail.apiKey,
					grailsApplication.config.freshmail.apiSecret,
					content)
			log.info response
			def slurper = new JsonSlurper()
			response = slurper.parseText response
			if (response.status != null && response.status == "OK") {
				entryToSynchronize.newSignature = false
				entryToSynchronize.syncWithFreshMail = true
			} else {
				response.status ? log.error(response.status) : log.error("Non know problem")
				entryToSynchronize.newSignature = false
				entryToSynchronize.syncWithFreshMail = false
			}
			entryToSynchronize.save()
		} else {
			println "Nothing to synchronize"
		}
	}


	def addTOFreshmail() {
		def entryToSynchronize = getAddedSignature()
		println "grailsApplication.config.freshmail.url ${grailsApplication.config.freshmail.url}"
		if (entryToSynchronize) {
			def content = getRequestContentInJSON(entryToSynchronize,grailsApplication.config.freshmail.hashList)
			content=content.toString()
			def response = callExternalServiceWithHttpJSON(grailsApplication.config.freshmail.url,
					"/rest/subscriber/add",
					grailsApplication.config.freshmail.apiKey,
					grailsApplication.config.freshmail.apiSecret,
					content)
			def slurper = new JsonSlurper()
			response = slurper.parseText response
			if (response.status != null && response.status == "OK") {
				entryToSynchronize.newSignature = false
				entryToSynchronize.syncWithFreshMail = true
			} else {
				response.status ? log.error(response.status) : log.error("Non know problem")
				entryToSynchronize.newSignature = true
				entryToSynchronize.syncWithFreshMail = false
			}
			entryToSynchronize.save()
		} else {
			println "Nothing to synchronize"
		}
	}



	private def getLatestSignatureToSynchronize() {
		def result =  Signature.findAll([max: 1, sort: "dateCreated", order: "desc"]){
			syncWithFreshMail == null
			province != null
			newSignature == null
		}
		result[0]
	}

	private def getAddedSignature() {
		def result =  Signature.findAll([max: 1, sort: "dateCreated", order: "desc"]){
			syncWithFreshMail == null
			province != null
			newSignature == true
		}
		println "result[0] ${result[0]}"
		result[0]
	}

	private def getRequestContentInJSON(signature,listHash) {
		def json = new groovy.json.JsonBuilder()
		json {
			email  signature.email
			list   listHash
			state 1
			custom_fields {
				"Imie" signature.firstName
				"Nazwisko" signature.lastName
				"Wojewodztwo" signature.province
				"Miejscowosc" signature.city
			}
		}
		json
	}


	private def callExternalServiceWithHttpJSON(url,path,apiKey,apiSecret,content) {
		def json = new groovy.json.JsonBuilder()
		def http = new HTTPBuilder( 'https://app.freshmail.pl' )
		http.request(POST,JSON) { req ->
			uri.path = path
			// overrides any path in the default URL
			headers.'User-Agent' = 'Mozilla/5.0'
			headers.'X-Rest-ApiKey' = apiKey
			headers.'X-Rest-ApiSign' = sha1(apiKey + path + content + apiSecret)
			body = content
			response.success = { resp, json2 ->
				assert resp.status == 200
				println "My response handler got response: ${resp.statusLine}"
				println "Response length: ${resp.headers.'Content-Length'}"
				println json2
				return json2.toString()
			}
			// called only for a 404 (not found) status code:
			response.'404' = { resp->
				println 'Not found'
				def json3 = new groovy.json.JsonBuilder()
				json3 { "status"  "error 404" }
				return json3.toString()
			}
			response.'500' = { resp ->
				println 'Problem'
				def json4 = new groovy.json.JsonBuilder()
				json4 { "status"  "error 500" }
				return json4.toString()
			}
		}
	}
}
