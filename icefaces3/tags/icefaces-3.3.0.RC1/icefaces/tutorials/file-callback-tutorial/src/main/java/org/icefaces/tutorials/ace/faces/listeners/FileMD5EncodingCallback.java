/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.tutorials.ace.faces.listeners;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FilenameUtils;
import org.icefaces.ace.component.fileentry.FileEntryCallback;
import org.icefaces.ace.component.fileentry.FileEntryResults;
import org.icefaces.ace.component.fileentry.FileEntryStatus;
import org.icefaces.tutorials.util.TutorialMessageUtils;


/**
 * Created by IntelliJ IDEA. User: Nils Date: 11-04-05 Time: 12:38 PM
 */
@RequestScoped
@ManagedBean
public class FileMD5EncodingCallback implements FileEntryCallback, Serializable {
	private transient MessageDigest digest;
	private boolean md5NotFound = false;
	private List<String> extensionList = Arrays.asList("pptx","ppt","zip","etc");

	// Set up a instance of a MD5 block-encoder
	public void begin(FileEntryResults.FileInfo fileInfo) {
		try {
			digest = MessageDigest.getInstance("MD5");

		} catch (NoSuchAlgorithmException e) {
			md5NotFound = true;
		}
	}

	// Hash a block of bytes
	public void write(byte[] bytes, int offset, int length) {
		if (!md5NotFound) {
			digest.update(bytes, offset, length);
		}
	}

	// Hash a single byte
	public void write(int i) {
		if (!md5NotFound) {
			digest.update((byte) i);
		}
	}

	// When FileEntryCallback ends for a file:
	public void end(FileEntryResults.FileInfo fileEntryInfo) {
		// We can fail the file here for invalid file type, or some other reason
		if (extensionList.contains(FilenameUtils.getExtension(fileEntryInfo.getFileName()))) {
			fileEntryInfo.updateStatus(new InvalidFileStatus(), false);
		}
		// If the file upload was completed properly
		else if (md5NotFound) {
			// Work-around for ICEfaces 3.0.0 issue ICE-7712, where setting
			// invalidate=true will mess up the lifecycle. Instead, manually
			// invalidate the form ourselves
			fileEntryInfo.updateStatus(new EncodingNotFoundUploadStatus(),
					false);
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
		} else if (fileEntryInfo.getStatus().isSuccess()) {
			fileEntryInfo.updateStatus(new EncodingSuccessStatus(getHash()),
					false);
		}
		digest = null;
	}

	// Assistance method to convert digested bytes to hex string
	protected String getHash() {
		return String.valueOf(Hex.encodeHex(digest.digest()));
	}

	private static class EncodingNotFoundUploadStatus implements
			FileEntryStatus {
		public boolean isSuccess() {
			return false;
		}

		public FacesMessage getFacesMessage(FacesContext facesContext,
				UIComponent uiComponent, FileEntryResults.FileInfo fileInfo) {
			return new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					TutorialMessageUtils
							.getMessage("content.callback.encode.fail.message"),
					TutorialMessageUtils
							.getMessage("content.callback.encode.fail.detail"));
		}
	}

	private static class EncodingSuccessStatus implements FileEntryStatus {
		private String hash;

		EncodingSuccessStatus(String hash) {
			this.hash = hash;
		}

		public boolean isSuccess() {
			return true;
		}

		public FacesMessage getFacesMessage(FacesContext facesContext,
				UIComponent uiComponent, FileEntryResults.FileInfo fileInfo) {
			return new FacesMessage(FacesMessage.SEVERITY_INFO,
					TutorialMessageUtils
							.getMessage("content.callback.result.message"),
					hash);
		}
	}

	private static class InvalidFileStatus implements FileEntryStatus {

		public boolean isSuccess() {
			return false;
		}

		public FacesMessage getFacesMessage(FacesContext facesContext,
				UIComponent uiComponent, FileEntryResults.FileInfo fileInfo) {
			
			return new FacesMessage(FacesMessage.SEVERITY_INFO,
					TutorialMessageUtils
							.getMessage("content.callback.result.message.failed"),
					FilenameUtils.getExtension(fileInfo.getFileName()));
					
					
		}
	}
}