package com.icesoft.faces.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.icesoft.faces.context.Resource;

public class ImageResource implements Resource{
	ByteArrayOutputStream bos;
	public ImageResource(ByteArrayOutputStream bos) {
		this.bos = bos;
	}

	public String calculateDigest() {
		return String.valueOf(System.currentTimeMillis());
	}

	public Date lastModified() {
		return new Date();
	}

	public InputStream open() throws IOException {
		return new ByteArrayInputStream(bos.toByteArray());
	}

}
