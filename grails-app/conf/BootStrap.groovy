import pl.jednomandatowe.Signature
import pl.jednomandatowe.Province
class BootStrap {

    def init = { servletContext ->
    	def signature = new Signature(firstName: "test1", lastName: "test1", city : "Warszawa", email:"test1@test1.pl",dateCreated : new Date()-1,province : Province.DOLNOSLASKIE, allow : true, syncWithFreshMail : null)
    	signature.save()
    }
    def destroy = {
    }
}
