import org.icepush.place.grails.view.model.Regions

class Filters {
  def filters = {
    initialization(controller: 'world', action: '*') {
      before = {
       if (!servletContext['regions']){
            def regions = new Regions();
            servletContext['regions'] = regions
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
