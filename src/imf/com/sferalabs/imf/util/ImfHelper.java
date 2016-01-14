/*
 * Copyright (c) 2015 Sfera Labs LLC. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 * display the following acknowledgement:
 * This product includes software developed by the SFERA LABS LLC.
 *
 * 4. Neither the name of SFERA LABS LLC nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY SFERA LABS LLC AND ITS CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SFERA LABS LLC OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.sferalabs.imf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import com.sferalabs.imf.exception.ImfXmlException;
import com.sferalabs.imf.validation.ValidationResult;
import com.sferalabs.imf.validation.ValidationResultList;

/**
 * ImfHelper singleton class
 */
public class ImfHelper {
	
	private static ImfHelper instance;
	private static Object mutex = new Object();
	
	private static class Messages{
		public static final String NOT_UNIQUE_ID = "UUID %s is not unique";
		public static final String UNIQUE_ID = "UUID %s is unique";
	}
	
	private final static List<String> PERMITTED_CONTENT_KIND_VALUES =
			Collections.unmodifiableList(Arrays.asList("advertisement", "feature", "psa", "rating", "short", "teaser",
					"test", "trailer", "transitional", "episode", "highlights", "event"));
	 
	private ImfHelper() {
		 
	}
	 
	public static ImfHelper getInstance() {
		if(instance == null) {
			synchronized (mutex) {
				if(instance == null) {
					instance = new ImfHelper();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Get base64 representation of selected hash algorithm of given file
	 * @param filepath
	 * @param hashAlgo
	 * @return calculated hash value
	 */
	public String getBase64FileHash(String filepath, ImfHashAlgorithm hashAlgo) {
		try (FileInputStream inputStream = new FileInputStream(filepath)) {
			MessageDigest digest;
			if (hashAlgo == ImfHashAlgorithm.SHA256)
				digest = MessageDigest.getInstance("SHA-256");
			else
				digest = MessageDigest.getInstance("SHA-1");
			
			byte[] bytesBuffer = new byte[1024];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
				digest.update(bytesBuffer, 0, bytesRead);
			}
			byte[] hashedBytes = digest.digest();
			return DatatypeConverter.printBase64Binary(hashedBytes);
		} catch (NoSuchAlgorithmException | IOException ex) {
			return null;
		}
	}
	 
	/**
	 * Get file size 
	 * @param filepath
	 * @return size of given file
	 */
	public long getFileSize(String filepath) {
		try {
			return Files.size(new File(filepath).toPath());
		} catch (IOException e) {
			return 0;
		}
	}
	
	/**
	 * Get file mime type 
	 * @param filepath
	 * @return mime filetype
	 */	
	public String getMimeFileType(String filepath) {
		try{
			String mimeType = Files.probeContentType(new File(filepath).toPath());
			if (mimeType == null) {
				//handle special case when OS cannot detect file type
				//get file extension
				int i = filepath.lastIndexOf('.');
				mimeType = filepath.substring(i+1);
				mimeType = "application/" + mimeType;
			}
			return mimeType;
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Get Id from Uuid
	 * @param uuid (for example: urn:uuid:157c47e5-1568-44f8-b801-4b4ddca9a62f)
	 * @return 157c47e5-1568-44f8-b801-4b4ddca9a62f
	 */
	public String getIdFromUuid(String uuid) {
		String[] elements = uuid.split(":");
		if (elements.length == 3) {
			if ((elements[0].equals("urn")) && (elements[1].equals("uuid"))) {
				return elements[2];
			}
		}
		return null;
	}
	
	
	/**
	 * Check unique of uuid in the given list
	 * @param uuids
	 * @return List with check results
	 */
	public ValidationResultList checkUuidsUnique(List<String> uuids) throws ImfXmlException {
		ValidationResultList result= new ValidationResultList();
		HashSet<String> uniqueLists = new HashSet<String>();
		for (int i=0; i<uuids.size(); i++) {
			String uuid = uuids.get(i);
			if (!uniqueLists.add(uuid)) {
				ValidationResult res = new ValidationResult(true, String.format(Messages.NOT_UNIQUE_ID, uuid));
				result.add(res);
				ImfLogger.getLogger().logError(String.format(Messages.NOT_UNIQUE_ID, uuid));
			} else {
				ImfLogger.getLogger().logDebug(String.format(Messages.UNIQUE_ID, uuid));
			}
		}
		return  result;
	}
	
	/**
	 * Check if given content kind is permitted
	 * @param contentKind
	 * @return true if contentKind is permitted, otherwise false
	 */
	public boolean isContentKindPermitted(String contentKind)
	{
		return PERMITTED_CONTENT_KIND_VALUES.contains(contentKind);
	}
}
