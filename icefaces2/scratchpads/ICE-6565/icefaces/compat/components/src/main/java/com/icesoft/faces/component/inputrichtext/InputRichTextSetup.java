package com.icesoft.faces.component.inputrichtext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import org.icefaces.impl.event.UIOutputWriter;
import org.icefaces.util.EnvUtils;
import com.icesoft.faces.context.JarResource;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.ResourceLinker;
import com.icesoft.faces.context.ResourceRegistry;
import com.icesoft.faces.context.ResourceRegistryLocator;


public class InputRichTextSetup implements SystemEventListener{
	public static final Resource ICE_CK_EDITOR_JS = new FCKJarResource("com/icesoft/faces/component/inputrichtext/ckeditor_ext.js");
	private static final String CK_EDITOR_ZIP = "com/icesoft/faces/component/inputrichtext/ckeditor.zip";
	private static final Date lastModified = new Date();
	private static final Map ZipEntryCacheCK = new HashMap();   
	public static URI ckBaseURI = null;


	public boolean isListenerForSource(Object source) {
		return true;
	}

	public void processEvent(SystemEvent event) throws AbortProcessingException {
		final FacesContext context = FacesContext.getCurrentInstance();
		if (EnvUtils.isICEfacesView(context)) {
			UIViewRoot root = context.getViewRoot();

			UIOutput inputTextJS = new UIOutputWriter() {
				public void encode(ResponseWriter writer, FacesContext context) throws IOException {
					String clientID = getClientId(context);
					writer.startElement("script", this);
					writer.writeAttribute("id", clientID+"JS", null);
					writer.writeAttribute("src",getURI()+ "/ckeditor.js", "src");
					writer.endElement("script");
					writer.startElement("script", this);
					writer.writeAttribute("id", clientID+"JSWrpr", null);		                
					writer.writeAttribute("src", getURI(), null);
					writer.endElement("script");	                    
				}
			};
			inputTextJS.setTransient(true);
			inputTextJS.setId("inpRichTxt");

			root.addComponentResource(context,  inputTextJS, "head");
		}
	}

	private String getURI() {

		if (ckBaseURI == null) {

			ResourceRegistry registry =
				ResourceRegistryLocator.locate(FacesContext.getCurrentInstance());

			if (registry != null) {
				ResourceLinker.Handler CK_LINKED_BASE = new ResourceLinker.Handler() {
					public void linkWith(ResourceLinker linker) {
						synchronized (ZipEntryCacheCK) {
							if (ZipEntryCacheCK.isEmpty()) {
								loadZipEntryCache();
							}
						}
						Iterator i = ZipEntryCacheCK.keySet().iterator();
						while (i.hasNext()) {
							final String entryName = (String) i.next();
							linker.registerRelativeResource(entryName, new Resource() {
								public String calculateDigest() {
									return String.valueOf(CK_EDITOR_ZIP + entryName);
								}

								public Date lastModified() {
									return lastModified;
								}

								public InputStream open() throws IOException {
									return new ByteArrayInputStream((byte[]) ZipEntryCacheCK.get(entryName));
								}

								public void withOptions(Resource.Options options) {
									options.setFileName(entryName);
									options.setLastModified(lastModified);
								}
							});
						}
					}
				};
				ckBaseURI = registry.loadJavascriptCode(ICE_CK_EDITOR_JS, CK_LINKED_BASE);
				registry.loadJavascriptCode(ICE_CK_EDITOR_JS);

			}
		} 
		return  ckBaseURI.toString();

	}



	private  void loadZipEntryCache() {
		try {
			InputStream inCK = InputRichText.class.getClassLoader().getResourceAsStream(CK_EDITOR_ZIP);
			ZipInputStream zipCK = new ZipInputStream(inCK);
			ZipEntry entryCK;
			while ((entryCK = zipCK.getNextEntry()) != null) {
				if (!entryCK.isDirectory()) {
					ZipEntryCacheCK.put(entryCK.getName(), toByteArray(zipCK));
				}
			}            
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	private static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buf = new byte[4096];
		int len = 0;
		while ((len = input.read(buf)) > -1) output.write(buf, 0, len);
		return output.toByteArray();
	}

}

class FCKJarResource extends JarResource {

	public FCKJarResource(String path) {
		super(path);
	}

	public void withOptions(Options options) throws IOException {

	}
}