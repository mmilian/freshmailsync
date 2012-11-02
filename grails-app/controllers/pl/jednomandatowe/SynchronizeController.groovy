package pl.jednomandatowe

import grails.converters.JSON

class SynchronizeController {

    def index() { }

	def freshmailService

	def synchronizeWithFreshmail() {
		def entryToSynchronize = freshmailService.getLatestSignatureToSynchronize()
		println entryToSynchronize
		def content = freshmailService.getRequestContentInJSON(entryToSynchronize,grailsApplication.config.freshmail.hashList)
		content=content.toString()
		def response = freshmailService.callExternalServiceWithHttpJSON(grailsApplication.config.freshmail.url,
			"/rest/subscriber/add",
			grailsApplication.config.freshmail.apiKey,
			grailsApplication.config.freshmail.apiSecret,
			content)
		println response
	}

		def pingFreshmail() {
			def map = [email:'mateusz.milian2@gmail.com',list :'wa40b9beep']
			def response = freshmailService.callExternalServiceWithHttpJSON(grailsApplication.config.freshmail.url,
			"/rest/ping",
			grailsApplication.config.freshmail.apiKey,
			grailsApplication.config.freshmail.apiSecret,
			"")

		println response
	}

	def test() {
		println "wat"
		println request.getHeader("User-Agent")
		println params
		return "wow"
	}
}
