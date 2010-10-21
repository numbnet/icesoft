import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWorld

class Filters {
  def filters = {
    initialization(controller: 'world', action: '*') {
      before = {
       if (!servletContext['world']){
            def world = new ICEpushPlaceWorld()
            world.setWebServiceURL("http://localhost:8080/icePushPlaceService")
            world.setApplicationURL("http://localhost:8080/icepushplace-grails")
            servletContext['world'] = world
       }
       if (request.getSession(false) == null ||
                !session['person']) {
          log.info("unauthorized request '$request.requestURI', directing to $controllerName")
          redirect controller:"register"
        }
      }
    }
  }
}
