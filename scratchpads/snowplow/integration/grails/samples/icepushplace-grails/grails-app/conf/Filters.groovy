import org.icepush.place.grails.view.model.Regions
import org.icepush.place.grails.services.IcepushPlaceService

class Filters {
  def filters = {
    initialization(controller: 'world', action: '*') {
      before = {
       if (!servletContext['regions']){
            def regions = new Regions();
            servletContext['regions'] = regions
       }
       if (!servletContext['service']){
           def icepushPlaceService = new IcepushPlaceService();
           icepushPlaceService.register()
           servletContext['service'] = icepushPlaceService
       }
       if (request.getSession(false) == null ||
                !session['person']) {
          log.info("unauthorized request '$request.requestURI', directing to $controllerName");
          redirect controller:"register"
        }
      }
    }
  }
}
