package pl.jednomandatowe



class Signature {
	
	String firstName
	String lastName
	String city
	String email
	Date dateCreated
	Province province
	boolean allow
	Boolean syncWithFreshMail
	Boolean newSignature		
	
	
	def beforeInsert() {
		dateCreated = new Date()
	}
	
    static constraints = {
		firstName(blank:false)
		lastName(blank:false)
		syncWithFreshMail(nullable:true)
		newSignature(nullable:true)
		email(blank:false)
		email(nullable:false)
		allow validator: {
			if (!it) return ['must.be.checked']
		}
	}
	
	String toString() {
		firstName
	}
	
	static {
		grails.converters.JSON.registerObjectMarshaller(Signature) {
		   // you can filter here the key-value pairs to output:
		   return it.properties.findAll {k,v -> (k != 'email' && k!= 'allow' && k!= 'dateCreated')}
		  }
    }
	
	static {
		grails.converters.XML.registerObjectMarshaller(Signature) {
		   // you can filter here the key-value pairs to output:
		   it.properties.findAll {k,v -> (k != 'email' && k!= 'allow' && k!= 'dateCreated')}
		  }
	}
	
	
}

