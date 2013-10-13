package freshmailsync



class SynchronizeWithFreshmailJob {
     def freshmailService

    static triggers = {
      simple repeatInterval: 2000l // execute job once in 5 seconds
    }

    def execute() {
       println "Synchronize entry with freshmail"
       freshmailService.synchronizeWithFreshmail()
       log.info "End synchronize entry with freshmail"
    }
}
