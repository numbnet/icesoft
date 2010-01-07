package org.icepush.samples.icechat.gwt.client;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
public class GWTPushContextTests{
		
	private Mockery context = new Mockery();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	
	@Test
	public void testAddPushListener(){
		try{
		final IcePushClientLibrary mockNativeLibrary = context.mock(IcePushClientLibrary.class);
		
		final PushEventListener listener = new PushEventListener() {
			@Override
			public void onPushEvent() {
				//do nothing - not possible to be called in this mock test anyway..
			}
		};
		
		context.checking(new Expectations(){{
			//should start be creating a new push id.
			one(mockNativeLibrary).createPushId(); will(returnValue("FAKEPUSHID"));
			
			//then should add the push id to each of the two groups.
			one(mockNativeLibrary).addGroupMember("group1", "FAKEPUSHID");
			one(mockNativeLibrary).addGroupMember("group2", "FAKEPUSHID");
			
			//then should attempt to register the listener as a callback for this push id.
			one(mockNativeLibrary).register("FAKEPUSHID", listener);
			
			
		}});
		GWTPushContext underTest = new GWTPushContext(mockNativeLibrary);
		
		

		String[] groupNames = new String[]{"group1","group2"};
		underTest.addPushEventListener(listener,groupNames);
		context.assertIsSatisfied();
		
		}catch(Throwable e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testRemovePushListener(){

		try{
			final IcePushClientLibrary mockNativeLibrary = context.mock(IcePushClientLibrary.class);
			
			final PushEventListener listener = new PushEventListener() {
				@Override
				public void onPushEvent() {
					//do nothing - not possible to be called in this mock test anyway..
				}
			};
			
			context.checking(new Expectations(){{
				//should start be creating a new push id.
				one(mockNativeLibrary).createPushId(); will(returnValue("FAKEPUSHID"));
				
				//then should add the push id to each of the two groups.
				one(mockNativeLibrary).addGroupMember("group1", "FAKEPUSHID");
				one(mockNativeLibrary).addGroupMember("group2", "FAKEPUSHID");
				
				//then should attempt to register the listener as a callback for this push id.
				one(mockNativeLibrary).register("FAKEPUSHID", listener);
				
				//now verify this listener is properly removed.
				
				//first the managed callback state needs to be cleanup.
				one(mockNativeLibrary).unregisterUserCallbacks(listener);
				
				//then the push id needs to be delisted with the window - [ice.push.deregister(pushId)]
				one(mockNativeLibrary).deregister("FAKEPUSHID");
				
				//then the groupmemberships need to be removed.
				one(mockNativeLibrary).removeGroupMember("group1", "FAKEPUSHID");
				one(mockNativeLibrary).removeGroupMember("group2", "FAKEPUSHID");
				
			}});
			GWTPushContext underTest = new GWTPushContext(mockNativeLibrary);
			
			

			String[] groupNames = new String[]{"group1","group2"};
			underTest.addPushEventListener(listener,groupNames);
			underTest.removePushEventListener(listener);
			context.assertIsSatisfied();
			
			}catch(Throwable e){
				e.printStackTrace();
				fail();
			}
			
			
	}

}
