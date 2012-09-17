package org.icefaces.icemobile.tests;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.icefaces.mobi.utils.Utils;
import org.icefaces.mobi.utils.Utils.DeviceType;
import org.junit.Before;
import org.junit.Test;
import static org.icefaces.icemobile.tests.UserAgentReader.Device;;

public class UserAgentTests  {
	
	private static final int TEST_DEVICES_FROM_YEAR = 2010;
	
	List<Device> devices;
	Set<String> fallBacks = new TreeSet<String>();
	private Set<Device> foundAndroidPhoneUserAgents = new TreeSet<Device>();
	private Set<Device> foundAndroidTabletUserAgents = new TreeSet<Device>();
	private Set<Device> foundIPhoneUserAgents = new TreeSet<Device>();
	private Set<Device> foundIPadUserAgents = new TreeSet<Device>();
	private Set<Device> foundBlackBerryUserAgents = new TreeSet<Device>();
	private Set<Device> unfoundUserAgents = new TreeSet<Device>();
	
	
	@Before
	public void setUp() throws Exception {
		devices = new UserAgentReader().getDevices(TEST_DEVICES_FROM_YEAR);
		System.out.println("analyzing " + devices.size() + " devices");
		if( devices == null ){
			System.out.println("devices is null");
		}
		else{
			Iterator<Device> iter = devices.iterator();
			while( iter.hasNext()){
				UserAgentReader.Device d = iter.next();
				fallBacks.add(d.fallBack);
			}
			
			Iterator<String> iter2 = fallBacks.iterator();
			while( iter2.hasNext() ){
				System.out.println("fallback: " + iter2.next());
			}
			System.out.println("found " + fallBacks.size() + " fallbacks");
		}
	}
		
	@Test
	public void testUserAgents() throws Exception{
		System.out.println("testUserAgents");
		
		Iterator<Device> iter = devices.iterator();
		while( iter.hasNext()){
			Device d = iter.next();
			DeviceType dt = Utils.getDeviceTypeWithoutDefault(d.userAgent);
			if( dt == null ){
				unfoundUserAgents.add(d);
			}
			else if( dt == DeviceType.ANDROID_PHONE){
				foundAndroidPhoneUserAgents.add(d);
			}
			else if( dt == DeviceType.ANDROID_TABLET){
				foundAndroidTabletUserAgents.add(d);
			}
			else if( dt == DeviceType.BLACKBERRY){
				foundBlackBerryUserAgents.add(d);
			}
			else if( dt == DeviceType.IPAD ){
				foundIPadUserAgents.add(d);
			}
			else if( dt == DeviceType.IPHONE){
				foundIPhoneUserAgents.add(d);
			}
		}
		System.out.println("\n******************* android phones = " + foundAndroidPhoneUserAgents.size());
		printOutDeviceList(foundAndroidPhoneUserAgents);
		System.out.println("\n******************* android tablets = " + foundAndroidTabletUserAgents.size());
		printOutDeviceList(foundAndroidTabletUserAgents);
		System.out.println("\n******************* blackberries = " + foundBlackBerryUserAgents.size());
		printOutDeviceList(foundBlackBerryUserAgents);
		System.out.println("\n******************* iphones = " + foundIPhoneUserAgents.size());
		printOutDeviceList(foundIPhoneUserAgents);
		System.out.println("\n******************* ipads = " + foundIPadUserAgents.size());
		printOutDeviceList(foundIPadUserAgents);
		System.out.println("\n******************* unfound = " + unfoundUserAgents.size());
		printOutDeviceList(unfoundUserAgents);
		
	}
	
	private void printOutDeviceList(Set<Device> devices){
		Iterator<Device> iter = devices.iterator();
		while( iter.hasNext() ){
			System.out.println(iter.next());
		}
	}
	
	public void testDetectMobileBrowsers(){
		HttpServletRequest request = null;
		String ua=request.getHeader("User-Agent").toLowerCase();
		if(ua.matches("(?i).*(android.+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|meego.+mobile|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")
				||ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {
		  return;
		}
	}

}
