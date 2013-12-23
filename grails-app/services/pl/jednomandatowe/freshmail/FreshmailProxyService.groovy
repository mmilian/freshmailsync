package pl.jednomandatowe.freshmail
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.JSON
import groovyx.net.http.HTTPBuilder
import grails.util.Holders
import static com.lazythought.easyhash.HashGenerator.*

class FreshmailProxyService {
    
   //def grailsApplication

   static private HTTPBuilder _http 

   def getHttp() {
        if (!_http) {
                _http = new HTTPBuilder(Holders.config.freshmail.url)
            }
        _http
    } 
 
    def addContact(data){
		request(Holders.config.freshmail.addSubscriber,data)
    }

    def synchronizeContact(data) {
        request(Holders.config.freshmail.synchronizeSubscriber, data)
    }

    def private request(adress,data){
        http.request(POST,JSON) { req ->
            uri.path = adress
            // overrides any path in the default URL
            headers.'User-Agent' = 'Mozilla/5.0'
            headers.'X-Rest-ApiKey' = Holders.config.freshmail.apiKey
            headers.'X-Rest-ApiSign' = sha1(Holders.config.freshmail.apiKey + adress + data.toString() + Holders.config.freshmail.apiSecret)
            body = data.toString()
            response.success = { resp, json ->
                return json
            }
			response.failure = { resp, json ->
				return json
			}
        }
    }
}