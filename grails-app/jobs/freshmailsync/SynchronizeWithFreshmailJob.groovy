package freshmailsync



class SynchronizeWithFreshmailJob {
     def freshmailService

    static triggers = {
      simple repeatInterval: 5000l // execute job once in 5 seconds
    }

    def execute() {
       println "Synchronize entry with freshmail"
       freshmailService.synchronizeWithFreshmail()
       log.info "End synchronize entry with freshmail"
    }
}
