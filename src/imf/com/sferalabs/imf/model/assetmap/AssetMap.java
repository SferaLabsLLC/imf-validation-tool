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
package com.sferalabs.imf.model.assetmap;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.sferalabs.imf.model.imfpackage.ImfPackage;
import com.sferalabs.imf.util.ImfHelper;

/**
 * AssetMap data structure class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssetMapType", propOrder = {
    "id",
    "annotationText",
    "creator",
    "volumeCount",
    "issueDate",
    "issuer",
    "assetList"
})
public class AssetMap {

	@XmlElement(name = "Id", required = true)
	private String uuid;
	@XmlElement(name = "AnnotationText")
	private String annotation;
	@XmlElement(name = "Creator", required = true)
	private String creator;
	@XmlElement(name = "VolumeCount", required = true)
	private long volumnCount;
	@XmlElement(name = "IssueDate", required = true)
	private String issueDate;
	@XmlElement(name = "Issuer", required = true)
	private String issuer;
	@XmlElement(name = "AssetList", required = true)
	private List<Asset> assetList;
	
	private ImfPackage imfPackage;

	/**
	 * @return the imfPackage
	 */
	public final ImfPackage getImfPackage() {
		return imfPackage;
	}

	/**
	 * @param imfPackage the imfPackage to set
	 */
	public final void setImfPackage(ImfPackage imfPackage) {
		this.imfPackage = imfPackage;
	}

	/**
	 * Set asset uuid
	 * 
	 * @param uuid
	 */
	public final void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Get asset uuid
	 * 
	 * @return uuid
	 */
	public final String getUuid() {
		return uuid;
	}

	/**
	 * Set asset annotation
	 * 
	 * @param annotation
	 */
	public final void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	/**
	 * Get asset annotation
	 * 
	 * @return annotation
	 */
	public final String getAnnotation() {
		return annotation;
	}

	/**
	 * Set asset creator
	 * 
	 * @param creator
	 */
	public final void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * Get asset creator
	 * 
	 * @return creator
	 */
	public final String getCreator() {
		return creator;
	}

	/**
	 * Set asset volumn count
	 * 
	 * @param volumnCount
	 */
	public final void setVolumnCount(long volumnCount) {
		this.volumnCount = volumnCount;
	}

	/**
	 * Get asset volumn count
	 * 
	 * @return volumnCount
	 */
	public long getVolumnCount() {
		return volumnCount;
	}

	/**
	 * Set asset issueDate
	 * 
	 * @param issueDate
	 */
	public final void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * Get asset issueDate
	 * 
	 * @return issueDate
	 */
	public final String getIssueDate() {
		return issueDate;
	}

	/**
	 * Set asset issuer
	 * 
	 * @param issuer
	 */
	public final void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	/**
	 * Get asset issuer
	 * 
	 * @return issuer
	 */
	public final String getIssuer() {
		return issuer;
	}

	/**
	 * Set asset list
	 * 
	 * @param assetList
	 */
	public final void setAssetList(List<Asset> assetList) {
		this.assetList = assetList;
	}

	/**
	 * Get asset list
	 * 
	 * @return assetList
	 */
	public final List<Asset> getAssetList() {
		return assetList;
	}

	/**
	 * Get all uuids in ASSETMAP file
	 * 
	 * @return list of uuids
	 */
	public final List<String> getAllUuids() {
		List<String> uuids = new ArrayList<String>();

		if (uuid != null) {
			uuids.add(uuid);
		}

		if (assetList != null) {
			for (Asset asset : assetList) {
				if (asset.getUuid() != null) {
					uuids.add(asset.getUuid());
				}
			}
		}

		return uuids;
	}

	/**
	 * Get all chunk items
	 * 
	 * @return path to all chunk items
	 */
	public final List<String> getAllChunkPaths() {
		List<String> paths = new ArrayList<String>();

		if (assetList != null) {
			for (Asset asset : assetList) {
				if (asset.getChunkList() != null) {
					for (Chunk chunk : asset.getChunkList()) {
						if (chunk.getPath() != null) {
							paths.add(chunk.getPath());
						}
					}
				}
			}
		}
		return paths;
	}

	/**
	 * Get asset by uuid
	 * 
	 * @param uuid
	 * @return asset, null if asset is not found
	 */
	public final Asset getAssetByUuid(String uuid) {
		if (assetList != null) {
			for (Asset asset : assetList) {
				if (asset.getUuid() != null) {
					if (uuid.equals(asset.getUuid()))
						return asset;
				}
			}
		}
		return null;
	}

	/**
	 * Get PackingList assets
	 * 
	 * @return assets
	 */
	public final List<Asset> getPackingListAssets() {
		List<Asset> assets = new ArrayList<Asset>();
		if (assetList != null) {
			for (Asset asset : assetList) {
				if (asset.getPackingList()) {
					assets.add(asset);
				}
			}
		}
		return assets;
	}

	/**
	 * Get CPL Assets
	 * 
	 * @return cpl assets
	 */
	public final List<Asset> getCPLAssets() {
		List<Asset> assets = new ArrayList<Asset>();
		if (assetList != null) {
			for (Asset asset : assetList) {
				List<Chunk> chunks = asset.getChunkList();
				if (chunks != null && chunks.size() > 0) {
					Chunk chunk = chunks.get(0);
					String cplAssetPath = chunk.getPath().toLowerCase();
					if (asset.getUuid() != null) {
						String id = ImfHelper.getInstance().getIdFromUuid(
								asset.getUuid()).toLowerCase();
						if (id != null) {
							if (cplAssetPath.endsWith(".xml")) {
								if (cplAssetPath.contains("_cpl") || cplAssetPath.contains("cpl_")) {
									assets.add(asset);
								}
							}
						}
					}
				}
			}
		}
		return assets;
	}
	/**
	 * Get asset resource path based on given uuid
	 * @param uuid
	 * @return asset resource path
	 */
	public final String getAssetResourcePath(String uuid) {
		
		if (imfPackage == null)
			return null;
		
		Asset asset = getAssetByUuid(uuid);
		//looking for asset in package
		String assetPath = asset.getChunkList().get(0).getPath();
		String packagePath = imfPackage.getPackagePath();
		
		Path itempath = new File(packagePath).toPath().resolve(assetPath);
		
		if (Files.exists(itempath)) {
			return itempath.toAbsolutePath().toString();
		}
		
		//looking into supplemental packages
		if (!imfPackage.isFinalPackage()) {
			if (imfPackage.getDependentPackageList() != null) {
				for (String supplementalPackagePath : imfPackage.getDependentPackageList()) {
					itempath = new File(supplementalPackagePath).toPath().resolve(assetPath);
					if (Files.exists(itempath)) {
						return itempath.toAbsolutePath().toString();
					}
				}
			}
		}
		
		return null;
	}
}
