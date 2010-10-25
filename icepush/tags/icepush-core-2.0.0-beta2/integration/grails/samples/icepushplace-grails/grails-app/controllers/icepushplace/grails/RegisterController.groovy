package icepushplace.grails
import org.icepush.ws.samples.icepushplace.PersonType

class RegisterController {

  def index = {
    
  }

  def register = {
    def thisPerson = new PersonType()
    thisPerson.name = params["submittedNickname"]
    thisPerson.mood = params["mood"]
    thisPerson.comment = params["comment"]
    thisPerson.region = Integer.parseInt(params["region"])
    thisPerson.technology = "Grails"
    def world = servletContext['world']
    thisPerson = world.loginPerson(thisPerson.region, thisPerson)
    session['person'] = thisPerson
    session['world'] = world
    response.status = 200
    render " "
  }

}
