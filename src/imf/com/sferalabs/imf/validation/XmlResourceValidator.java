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
package com.sferalabs.imf.validation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sferalabs.imf.exception.ImfXmlException;
import com.sferalabs.imf.util.ImfLogger;
import com.sferalabs.imf.xsd.ImfXsds;

/**
 * Abstract XmlResouce validation class
 */
public abstract class XmlResourceValidator {

	private static class Messages {
		public static final String XSD_NOT_PROVIDED_MSG = "Xsd file is not provided";
		public static final String VERIFYING_XSD_FMT = "Verifying %s against xsd schema";
		public static final String FOLLOW_XSD_FMT = "%s follows xsd schema";
		public static final String NOT_FOLLOW_XSD_FMT = "%s does not follow xsd schema";
		public static final String FILE_NOT_EXIST_FMT = "%s does not exist";
	}

	protected String filePath;
	protected String resourcePath; 
	
	public XmlResourceValidator(String path) {
		filePath = path;
	}
	
	/**
	 * Test whether the given file path exists
	 * @return false if the file path  is invalid, otherwise true
	 */
	protected boolean exists() {
		Path path = new File(filePath).toPath();
		return Files.exists(path);
	}
	
	/**
	 * Set Xsd Path for validation
	 * @param filePath given xsd filepath
	 */
	public void setXsdPath(String filePath) {
		resourcePath = filePath;
	}
	
	/**
	 * Validate xml file with given xsd file
	 * @throws SAXException
	 * @throws IOException
	 */
	public ValidationResult validateXsd() throws IOException {
		ValidationResult result = new ValidationResult();
		
		if (resourcePath == null) {
			ImfLogger.getLogger().logError(Messages.XSD_NOT_PROVIDED_MSG);
			throw new IOException(Messages.XSD_NOT_PROVIDED_MSG);
		}
		
		if (exists()) {
			ImfLogger.getLogger().logDebug(String.format(Messages.VERIFYING_XSD_FMT, filePath));
			try {
				ImfXsds.getInstance().validateXmlAgainsXsdResource(filePath, resourcePath);
			} catch (SAXException e) {
				ImfLogger.getLogger().logError(String.format(Messages.NOT_FOLLOW_XSD_FMT, filePath));
				result.setHasError(true);
				result.setMessage(String.format(Messages.VERIFYING_XSD_FMT, filePath));
				SAXParseException spe = (SAXParseException) e;
				if (spe != null) {
					String message = "Line: " + spe.getLineNumber() + " Col: " + spe.getColumnNumber() + " " + spe.getMessage();
					ImfLogger.getLogger().logError(message);
				}
				return result;
			}
			ImfLogger.getLogger().logDebug(String.format(Messages.FOLLOW_XSD_FMT, filePath));
		} else {
			ImfLogger.getLogger().logError(String.format(Messages.FILE_NOT_EXIST_FMT, filePath));
			throw new IOException(String.format(Messages.FILE_NOT_EXIST_FMT, filePath));
		}
		return result;
	}
	
	/**
	 * Parse the given xml file
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public abstract ValidationResult parse() throws IOException, ParserConfigurationException, SAXException;
	
	/**
	 * Do all validation on IMF resource
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws ImfXmlException
	 * @return ValidationResultList list of performed validations list
	 */
	public abstract ValidationResultList validate() throws ImfXmlException;
}
