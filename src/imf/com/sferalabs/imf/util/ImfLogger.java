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

/**
 * ImfLogger class
 */
public class ImfLogger {
	
	private static ImfLogger instance;
	private static boolean enableDebugLog;
	private static boolean errorOccured;
	private static boolean warningOccured;
	private static Object mutex = new Object();
	
	private ImfLogger() {
		
	}
	
	public static ImfLogger getLogger() {
		if(instance == null) {
			synchronized (mutex) {
				if(instance == null) {
					instance = new ImfLogger();
					enableDebugLog = false;
				}
			}
		}
		return instance;
	}
	
	/**
	 * Log debug message
	 * @param msg
	 */
	public void logDebug(String msg) {
		if (enableDebugLog)
			System.out.println("[D]: " + msg);
	}
	
	/**
	 * Log info message
	 * @param msg
	 */
	public void logInfo(String msg) {
		System.out.println("[I]: " + msg);
	}
	
	/**
	 * Log error message
	 * @param msg
	 */
	public void logError(String msg) {
		errorOccured = true;
		System.out.println("[E]: " + msg);
	}
	
	/**
	 * Log warning message
	 * @param msg
	 */
	public void logWarning(String msg) {
		System.out.println("[W]: " + msg);
	}
	
	/**
	 * Enable to display all logs including debug log
	 */
	public void enableDebugLog() {
		enableDebugLog = true;
	}
	
	/**
	 * Disable debug log, only show error and warning log
	 */
	public void disableDebugLog() {
		enableDebugLog = false;
	}
	
	/**
	 * Check if any error occurs
	 * @return errorOccured
	 */
	public boolean hasAnyErrors() {
		return errorOccured;
	}
	
	/**
	 * Check if any error occurs
	 * @return errorOccured
	 */
	public boolean hasAnyWarnings() {
		return warningOccured;
	}
	
	/**
	 * Clear errors
	 */
	public void clearErrors() {
		errorOccured = false;
		warningOccured = false;
	}
}
