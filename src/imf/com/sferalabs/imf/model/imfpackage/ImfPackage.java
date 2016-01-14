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
package com.sferalabs.imf.model.imfpackage;

import java.util.List;

/**
 * Package class
 */
public class ImfPackage {

	private boolean isFinalPackage;
	private List<String> dependentPackagePaths;
	private String packagePath;
	
	public ImfPackage(String packagePath) {
		this.packagePath = packagePath;
		isFinalPackage = true;
	}
	
	/**
	 * @return the packagePath
	 */
	public final String getPackagePath() {
		return packagePath;
	}

	/**
	 * @param packagePath the packagePath to set
	 */
	public final void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}

	/**
	 * @return the isFinalPackage
	 */
	public final boolean isFinalPackage() {
		return isFinalPackage;
	}

	/**
	 * @return the dependentPackagePaths
	 */
	public final List<String> getDependentPackageList() {
		return dependentPackagePaths;
	}

	/**
	 * @param dependentPackagePaths the dependentPackagePaths to set
	 */
	public final void setDependantPackageList(List<String> dependentPackagePaths) {
		this.dependentPackagePaths = dependentPackagePaths;
		if (dependentPackagePaths.size() > 0) {
			this.isFinalPackage = false;
		}
	}
}
