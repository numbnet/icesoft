package icepushplace.grails
import org.icepush.ws.samples.icepushplace.PersonType

class RegisterController {

  def index = {
    
  }

  def register = {
    def thisPerson = new PersonType();
    thisPerson.name = params["submittedNickname"]
    thisPerson.mood = params["mood"]
    thisPerson.comment = params["comment"]
    thisPerson.key = Integer.parseInt(params["region"])
    session['person'] = thisPerson
    def regions = servletContext['regions']
    // Add to region
    switch(params["region"]){
        case '1': regions.northAmerica.add(thisPerson);break;
        case '2': regions.europe.add(thisPerson);break;
        case '3': regions.southAmerica.add(thisPerson);break;
        case '4': regions.asia.add(thisPerson);break;
        case '5': regions.africa.add(thisPerson);break;
        case '6': regions.antarctica.add(thisPerson);break;
        default: println "Problem Initializing Person";
    }
    push thisPerson.key.toString()
    response.status = 200;
    render " "
  }

}
