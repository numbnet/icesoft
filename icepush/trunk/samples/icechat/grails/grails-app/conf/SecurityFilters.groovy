/**
 *
 *
 * Date: 14 avr. 2010
 * Time: 00:56:29
 *
 * @author Stephane MALDINI
 */
class SecurityFilters {
  def filters = {
    chatSecurity(controller: 'chat', action: '*') {
      before = {
       if (request.getSession(false) == null ||
                !session[org.icepush.samples.icechat.LoginController.USER_KEY]) {
          log.info("unauthorized request '$request.requestURI', directing to $controllerName");
          redirect controller:"login"
        }
      }
    }
  }
}