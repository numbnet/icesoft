package org.icemobile.jsp.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icemobile.jsp.util.Constants;
import org.icemobile.jsp.util.Util;


@WebServlet( name="ICEmobileResourceServlet", 
	urlPatterns = {Constants.RESOURCE_BASE_URL+"/*"}, 
	loadOnStartup=1)
public class ResourceServlet extends HttpServlet{
	
	private final Date lastModified = new Date();
	private final String STARTUP_TIME = Util.HTTP_DATE.format(lastModified);
	private ClassLoader loader;
	private ServletContext servletContext;
	
	private static final Logger log = Logger.getLogger(ResourceServlet.class.getName());
	
	public void init(final ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.loader = this.getClass().getClassLoader();
        this.servletContext = servletConfig.getServletContext();
    }

	
	public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String modifedHeader = httpServletRequest
                .getHeader("If-Modified-Since");
        if (null != modifedHeader) {
            try {
                Date modifiedSince = Util.HTTP_DATE.parse(modifedHeader);
                if (modifiedSince.getTime() + 1000 > lastModified.getTime()) {
                    //respond with a not-modifed
                    httpServletResponse.setStatus(304);
                    httpServletResponse.setDateHeader(
                            "Date", new Date().getTime());
                    httpServletResponse.setDateHeader(
                            "Last-Modified", lastModified.getTime());
                    return;
                }
            } catch (ParseException e) {
                //if the headers are corrupted, still just serve the resource
                log.log(Level.FINE, "failed to parse date: " + modifedHeader, e);
            } catch (NumberFormatException e) {
                //if the headers are corrupted, still just serve the resource
                log.log(Level.FINE, "failed to parse date: " + modifedHeader, e);
            }
        }

        String path = httpServletRequest.getPathInfo();
        String resourceAbsPath = Constants.JAR_RESOURCE_PATH + path;
        final InputStream in = loader.getResourceAsStream(resourceAbsPath);
        if (null == in) {
            httpServletResponse.setStatus(404, "Resource not found, :( " + resourceAbsPath + ", path="+path);
            return;
        }
        String mimeType = servletContext.getMimeType(path);
        httpServletResponse.setHeader("Content-Type", mimeType);
        httpServletResponse.setHeader("Last-Modified", STARTUP_TIME);

        OutputStream out = httpServletResponse.getOutputStream();

        Util.copyStream(in, out);
    }

}
