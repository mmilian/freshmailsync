package freshmailsync



class AddToFreshmailJob {
    
    def freshmailService

    static triggers = {
      simple repeatInterval: 5000l // execute job once in 5 seconds
    }

    def execute() {
       println "Add entry to freshmail"
       freshmailService.addTOFreshmail()
       log.info "End entry to freshmail"
    }
}
