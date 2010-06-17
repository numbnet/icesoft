import org.icepush.place.grails.view.model.Person
import org.icepush.place.grails.view.model.Regions

class Filters {
  def filters = {
    initialization(controller: '*', action: '*') {
      before = {
       if (!servletContext['regions']){
            println 'FILTER CREATING Application BEAN'
            def regions = new Regions();
            servletContext['regions'] = regions
       }
       if (request.getSession(false) == null || !session['person']) {
            println 'FILTER CREATING Session BEAN'
            def thisPerson = new Person();
            thisPerson.region = '1'
            def regions = servletContext['regions']
            regions.northAmerica.add(thisPerson);
            //push thisPerson.region
            session['person'] = thisPerson
       }
      }
    }
  }
}
