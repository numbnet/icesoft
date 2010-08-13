package icepushplace.grails

import org.icepush.ws.samples.icepushplace.PersonType

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
    def world = servletContext['world']
    def thisPerson = session["person"]
    def submittedNickname = params["submittedNickname"]
    def mood = params["mood"]
    def comment = params["comment"]
    def region = Integer.parseInt(params["region"])
    def changed
    def moving
    def oldRegion = -1
    if(thisPerson.name != submittedNickname){
        thisPerson.name = submittedNickname
        changed = true
    }
    if(thisPerson.mood != mood){
        thisPerson.mood = mood
        changed = true
    }
    if(thisPerson.comment != comment){
        thisPerson.comment = comment
        changed = true
    }
    if(thisPerson.region != region){
        moving = true
    }
    if(changed){
        world.updatePerson(thisPerson.region, thisPerson);
    }
    if(moving) {
        session["person"] = world.movePerson(thisPerson.region,region,thisPerson)
    }
    render ""
  }

  def messageOut = {
    def messageOut = params["msgOut"]
    def region = Integer.parseInt(params["region"])
    def row = Integer.parseInt(params["row"])
    def from = params["from"]
    def receiver
    def world = servletContext['world']
    receiver = world.getContinent(region).get(row)
    receiver.messageIn = from + " says: " + messageOut
    world.updatePerson(receiver.region, receiver)
    render ""
  }

  def northAmerica = {
    noCache(response)
    def world = servletContext['world']
    [northAmerica: world.northAmerica]
  }

  def europe = {
    noCache(response)
    def world = servletContext['world']
    [europe: world.europe]
  }

  def southAmerica = {
    noCache(response)
    def world = servletContext['world']
    [southAmerica: world.southAmerica]
  }

  def asia = {
    noCache(response)
    def world = servletContext['world']
    [asia: world.asia]
  }

  def africa = {
    noCache(response)
    def world = servletContext['world']
    [africa: world.africa]
  }

  def antarctica = {
    noCache(response)
    def world = servletContext['world']
    [antarctica: world.antarctica]
  }

}
