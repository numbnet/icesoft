package icepushplace.grails

import org.icepush.place.grails.view.model.Person

class WorldController {

  def index = {
      
  }

  private void noCache(response) {
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
    response.setHeader("Pragma", "no-cache");//HTTP 1.0
    response.setHeader("Expires", "0");//prevents proxy caching
  }

  def updateSettings = {
    noCache(response)
    def thisPerson = session["person"]
    def submittedNickname = params["submittedNickname"];
    def mood = params["mood"];
    def comment = params["comment"];
    def region = params["region"];
    def changed;
    if(thisPerson.nickname != submittedNickname){
        thisPerson.nickname = submittedNickname;
        changed = true;
    }
    if(thisPerson.mood != mood){
        thisPerson.mood = mood;
        changed = true;
    }
    if(thisPerson.comment != comment){
        thisPerson.comment = comment;
        changed = true;
    }
    if(thisPerson.region != region){
        def regions = servletContext['regions']
        // Remove from previous region
        switch(thisPerson.region){
            case '1': regions.northAmerica.remove(thisPerson);break;
            case '2': regions.europe.remove(thisPerson);break;
            case '3': regions.southAmerica.remove(thisPerson);break;
            case '4': regions.asia.remove(thisPerson);break;
            case '5': regions.africa.remove(thisPerson);break;
            case '6': regions.antarctica.remove(thisPerson);break;
            default: println "Problem Removing Person from Region";
        }
        // Add to new region
        switch(region){
            case '1': regions.northAmerica.add(thisPerson);break;
            case '2': regions.europe.add(thisPerson);break;
            case '3': regions.southAmerica.add(thisPerson);break;
            case '4': regions.asia.add(thisPerson);break;
            case '5': regions.africa.add(thisPerson);break;
            case '6': regions.antarctica.add(thisPerson);break;
            default: println "Problem Adding Person to Region";
        }
        // Push to remove from old region
        push thisPerson.region
        // Set person in new region
        thisPerson.region = region;
        changed = true;
    }
    if(changed){
        // Push to update region
        push thisPerson.region
        // TODO: WILL BE REPLACED WITH SOMETHING LIKE:
        // Service call to display message in all applications
        // service.requestUpdate(person);
        // THE SERVICE CALL WILL HAVE TO CHECK THE PERSON'S REGION.
        // IF IT HAS CHANGED, A PUSH WILL HAVE TO BE CALLED ON THE OLD REGION AS WELL.
    }
    render ""
  }

  def messageOut = {
    def messageOut = params["msgOut"];
    def region = params["region"];
    def row = params["row"];
    def from = params["from"];
    def receiver
    def regions = servletContext['regions']
    switch(region){
        case '1': receiver = regions.northAmerica.get(Integer.parseInt(row));break;
        case '2': receiver = regions.europe.get(Integer.parseInt(row));break;
        case '3': receiver = regions.southAmerica.get(Integer.parseInt(row));break;
        case '4': receiver = regions.asia.get(Integer.parseInt(row));break;
        case '5': receiver = regions.africa.get(Integer.parseInt(row));break;
        case '6': receiver = regions.antarctica.get(Integer.parseInt(row));break;
        default: println("Receiver of Message Not Found");

    }
    // Set receiver message
    receiver.messageIn = from + " says: " + messageOut;
    // Push update out to receiver's region
    push region;
    // TODO: WILL BE REPLACED WITH SOMETHING LIKE:
    // Service call to display message in all applications
    //service.requestUpdate(person);
    render ""
  }

  def northAmerica = {
    noCache(response)
    def regions = servletContext['regions']
    [northAmerica: regions.northAmerica]
  }

  def europe = {
    noCache(response)
    def regions = servletContext['regions']
    [europe: regions.europe]
  }

  def southAmerica = {
    noCache(response)
    def regions = servletContext['regions']
    [southAmerica: regions.southAmerica]
  }

  def asia = {
    noCache(response)
    def regions = servletContext['regions']
    [asia: regions.asia]
  }

  def africa = {
    noCache(response)
    def regions = servletContext['regions']
    [africa: regions.africa]
  }

  def antarctica = {
    noCache(response)
    def regions = servletContext['regions']
    [antarctica: regions.antarctica]
  }

}
