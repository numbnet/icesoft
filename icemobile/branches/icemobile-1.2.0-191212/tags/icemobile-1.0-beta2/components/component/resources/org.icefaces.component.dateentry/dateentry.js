/**
 * Created by IntelliJ IDEA.
 * User: jguglielmin
 * Date: 11-10-07
 * Time: 2:11 AM
 * To change this template use File | Settings | File Templates.
 */
  calendarTemplate = function(csspref, yearscr, wkstrt, dtformat){
      if (window.A_TCALCONF)  {
          window.A_TCALCONF.cssprefix=csspref;
          window.A_TCALCONF.yearscroll=yearscr;
          window.A_TCALCONF.weekstart = wkstrt;
          window.A_TCALCONF.format=dtformat;
      };
      f_tcalInit();
  }

