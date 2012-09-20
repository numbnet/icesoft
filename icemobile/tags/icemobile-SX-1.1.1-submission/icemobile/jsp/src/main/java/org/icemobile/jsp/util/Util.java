package org.icemobile.jsp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class Util {
	
	public static final DateFormat HTTP_DATE =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
	
	public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1000];
        int l = 1;
        while (l > 0) {
            l = in.read(buf);
            if (l > 0) {
                out.write(buf, 0, l);
            }
        }
    }

    public static int copyStream(InputStream in, OutputStream out,
            int start, int end) throws IOException {
        long skipped = in.skip((long) start);
        if (start != skipped)  {
            throw new IOException("copyStream failed range start " + start);
        }
        byte[] buf = new byte[1000];
        int pos = start - 1;
        int count = 0;
        int l = 1;
        while (l > 0) {
            l = in.read(buf);
            if (l > 0) {
                pos = pos + l;
                if (pos > end)  {
                    l = l - (pos - end);
                    out.write(buf, 0, l);
                    count += l;
                    break;
                }
                out.write(buf, 0, l);
                count += l;
            }
        }
        return count;
    }
    
    public static String getContextRoot(ServletRequest request){
    	return ((HttpServletRequest)request).getContextPath();
    }


}
