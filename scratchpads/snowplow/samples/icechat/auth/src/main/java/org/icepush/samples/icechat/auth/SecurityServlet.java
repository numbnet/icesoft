package org.icepush.samples.icechat.auth;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.service.exception.LoginFailedException;

public class SecurityServlet extends HttpServlet {
	
	public static final String OP = "op";
	public static final String USER_NAME = "userName";
	public static final String PASSWORD = "password";
	public static final String NICKNAME = "nickName";
	public static final String LOGIN = "login";
	public static final String LOGOUT = "logout";
	public static final String REGISTER = "register";
	static final String USER_KEY = "user";
	private String homePageURL;
	private static final String HOME_PAGE_URL_KEY = "homePageURL";
	public static final String RESOURCE_PARAM_KEY = "res";
	
	private static Logger LOG = Logger.getLogger(SecurityServlet.class.getName());
	
	private IChatService getChatService(){
		return (IChatService)this.getServletContext().getAttribute("chatService");
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String op = req.getParameter(OP);
		
		if( LOGOUT.equals(op)){
			User user = (User)req.getSession().getAttribute(USER_KEY);
			if( user != null ){
				req.setAttribute(USER_KEY, null);				
				LOG.info(user.getUserName() + " logged out ");
			}
			req.getSession().invalidate();
			return;
		}
		else if( ( LOGIN.equals(op) || REGISTER.equals(op) ) && req.getAttribute(USER_KEY) != null ){
			dispatch(homePageURL,req,resp);
			return;
		}
		else if( LOGIN.equals(op) || REGISTER.equals(op)){
			
			String userName = req.getParameter(USER_NAME);
			String password = req.getParameter(PASSWORD);
			String nickName = req.getParameter(NICKNAME);
			
			if( userName != null && password != null ){				
				
				HttpSession session = req.getSession(true);
				User user = null;
				if( LOGIN.equals(op) ){
					LOG.info("logging in " + userName);
					try{
						user = getChatService().login(userName, password);
					}
					catch(LoginFailedException lfe){
						lfe.printStackTrace();
					}
				}
				else{
					LOG.info("registering " + userName);
					user = getChatService().register(userName, nickName, password);
				}
				session.setAttribute(USER_KEY,user);
				LOG.info("session: " + session.getId() + " user=" + user);
				resp.setStatus(200);
				return;
			}
			
		}
		resp.setStatus(401);
		
	}
	
	private void dispatch(String url, HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {		
		getServletConfig().getServletContext().getRequestDispatcher(url).forward(req,resp);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		homePageURL = getServletConfig().getServletContext().getInitParameter(HOME_PAGE_URL_KEY);
	}
	

}
