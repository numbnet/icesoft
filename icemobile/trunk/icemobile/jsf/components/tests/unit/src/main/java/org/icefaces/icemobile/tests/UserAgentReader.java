package org.icefaces.icemobile.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UserAgentReader {
	
	public static final String DO_NOT_MATCH = "DO_NOT_MATCH";

	public List<UserAgentReader.Device> getDevices(int fromYear) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docBuilderFactory.newDocumentBuilder();
		InputStream in = this.getClass().getResourceAsStream("wurfl.xml");
		if( in == null ){
			System.out.println("*********************** Missing wurfl.xml. Please download from "
					+ "http://wurfl.sourceforge.net and place beside test classs.");
			return Collections.EMPTY_LIST;
		}
		Document doc = docBuilder.parse(in);

		doc.getDocumentElement().normalize();
		
		NodeList deviceList = doc.getElementsByTagName("device");
		int totalDevices = deviceList.getLength();
		System.out.println("found " + totalDevices + " devices");

		List<Device> devices = new ArrayList<Device>();
		for(int s=0; s<deviceList.getLength() ; s++){
			Element elem = (Element)deviceList.item(s);
			int deviceYear = getYearOfDevice(elem);
			String ua = getUserAgentString(elem);
			if( ua != null && deviceYear >= fromYear){
				Device device = new Device();
				device.id = elem.getAttribute("id");
	            device.fallBack = elem.getAttribute("fall_back");
	            device.userAgent = elem.getAttribute("user_agent");
	            devices.add(device);
			}
		}
		return devices;

	}
	
	private static String getUserAgentString(Element elem){
		String ua = elem.getAttribute("user_agent");
		if( ua != null && ua.startsWith(DO_NOT_MATCH)){
			ua = null;
		}
		return ua;
	}
	
	private static int getYearOfDevice(Element elem){
		int year = 0;
		NodeList groupList = elem.getElementsByTagName("group");
		for( int i=0 ; i<groupList.getLength() ; i++ ){
			Element productInfo = (Element)groupList.item(i);
			if( productInfo.getAttribute("id").equals("product_info")){
				NodeList capabilities = productInfo.getElementsByTagName("capability");
				for( int j=0 ; j < capabilities.getLength() ; j++){
					Element capability = (Element)capabilities.item(j);
					if( capability.getAttribute("name").equals("release_date")){
						String release = capability.getAttribute("value");
						if( release != null && release.length() > 0 ){
							release = release.substring(0,4);
							year = Integer.parseInt(release);
						}
						
					}
				}
			}
		}
		return year;
	}
	
	public static class Device implements Comparable{
		public String id;
		public String userAgent;
		public String fallBack;
		
		public String toString(){
			return id + " user-agent=" + userAgent + " fallback=" + fallBack;
		}

		public int compareTo(Object obj) {
			Device other = (Device)obj;
			if( other != null ){
				return this.id.compareTo(other.id);
			}
			else{
				return 1;
			}
			
		}
	}

}
