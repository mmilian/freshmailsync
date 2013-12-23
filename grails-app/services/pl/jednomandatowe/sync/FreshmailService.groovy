package pl.jednomandatowe.sync
import pl.jednomandatowe.Signature
import pl.jednomandatowe.Province
import groovy.json.JsonBuilder
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.JSON
import static com.lazythought.easyhash.HashGenerator.*
import groovyx.net.http.HTTPBuilder
import groovy.json.JsonSlurper

class FreshmailService {

	def grailsApplication
	def freshmailProxyService

	def synchronizeContact() {
		def contact = getLatestContactToSynchronize()
		if (contact) {
			def response =  freshmailProxyService.synchronizeContact(getContentInJSON(contact))
			if (isResponseOk(response)) {
				contact.with {
					newSignature = false
					syncWithFreshMail = true
				}
			} else {
				logError(response)
				contact.with {
					newSignature = false
					syncWithFreshMail = false
				}
			}
			contact.save()
		} else {
			log.info "Nothing to synchronize"
		}
	}


	def addContact() {
		def contact = getAddedContact()
		if (contact) {
			def response = freshmailProxyService.addNewContact(getContentInJSON(contact))
			if (isResponseOk(response) || (contactAlreadyExist(response))) {
				contact.with {
					newSignature = false
					syncWithFreshMail = true
				}
			} else {
				logError(response)
				contact.with {
					newSignature = true
					syncWithFreshMail = false
				}
			}
			contact.save()
		} else {
			log.info "No more new adresses to synchronize"
		}
	}

	private contactAlreadyExist(response) {		
		response = new JsonSlurper().parseText response
		return response.errors.code == '1304'
	}
	
	private logError(response) {
		response = new JsonSlurper().parseText response		
		response.status ? log.error(response.errors) : log.error("Non know problem")
	}

	private boolean isResponseOk(response) {
		def slurper = new JsonSlurper()
		response = slurper.parseText response
		return response.status != null && response.status == "OK"
	}


	private def getLatestContactToSynchronize() {
		def result =  Signature.findAll([max: 1, sort: "dateCreated", order: "desc"]){
			syncWithFreshMail == null
			province != null
			newSignature == null
		}
		result[0]
	}

	private def getAddedContact() {
		def result =  Signature.findAll([max: 1, sort: "dateCreated", order: "desc"]){
			syncWithFreshMail == null
			province != null
			newSignature == true
		}
		result[0]
	}

	private def getContentInJSON(signature) {
		def json = new groovy.json.JsonBuilder()
		json {
			email  signature.email
			list   grailsApplication.config.freshmail.hashList
			state 1
			custom_fields {
				"Imie" signature.firstName
				"Nazwisko" signature.lastName
				"Wojewodztwo" signature.province
				"Miejscowosc" signature.city
			}
		}
		json.toString()
	}
}
