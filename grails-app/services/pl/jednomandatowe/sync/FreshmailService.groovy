package pl.jednomandatowe.sync
import pl.jednomandatowe.Signature
import groovy.json.JsonBuilder
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.JSON
import static com.lazythought.easyhash.HashGenerator.*
import groovyx.net.http.HTTPBuilder

class FreshmailService {

    def getLatestSignatureToSynchronize() {
    	def result =  Signature.findAll([max: 1, sort: "dateCreated", order: "desc"]){
   			syncWithFreshMail == null
   			province != null
		}
		result[0]
	}

	def getRequestContentInJSON(signature,listHash) {
		def json = new groovy.json.JsonBuilder()					                            
		json {
			email  signature.email
			list   listHash
			custom_fields {
				wojewodztwo signature.province
				miejscowosc signature.city
			}
		}
	 	json
	}
	
	//def callExternalServiceWithHttpJSON(url,path,apiKey,apiSecret,content)	{
	//	withHttp(uri : url, contentType : JSON) {
   	//					def result = post(path:path,
	//									  headers:['User-Agent':'Mozilla/5.0','X-Rest-ApiKey' : apiKey,'X-Rest-ApiSign' : sha1(apiKey + path + content.toString() + apiSecret)],
   	//						              body : content.toString()) { resp, json ->
    //								      println 'got response!'
    //  									  return json
   	//									}
    //					}
    //}

    def callExternalServiceWithHttpJSON(url,path,apiKey,apiSecret,content) {
    	def json = new groovy.json.JsonBuilder()		
    	//json
		//{
  		//	email '111mateusz.milian@gmail.com'
  		//	list  'wa40b9beep'
  		//}
  		//println json
  		//def c = json.toString()
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
   	 			return json2
  				}
 	 		// called only for a 404 (not found) status code:
  			response.'404' = { resp ->
   	 			println 'Not found'
  			}
  			response.'500' = { resp,js ->
   	 			println 'Problem'
   	 			println js
  			}
  		}
	}
}
