import pl.jednomandatowe.Signature
import pl.jednomandatowe.Province
class BootStrap {

    def init = { servletContext ->
    	environments {
    		development {
    			def signature = new Signature(firstName: "Mateusz", lastName: "Milian", city : "Wrocław", email:"mateusz.milian@gmail.com",dateCreated : new Date()-1,province : Province.DOLNOSLASKIE, allow : true, syncWithFreshMail : null, newSignature : null)
    			signature.save()
    		}
    	}
    }
    def destroy = {
    }
}
