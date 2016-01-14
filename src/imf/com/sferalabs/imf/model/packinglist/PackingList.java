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
package com.sferalabs.imf.model.packinglist;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * PackingList class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "annotationText",
    "iconId",
    "issueDate",
    "issuer",
    "creator",
    "groupId",
    "assetList",
    "signer",
    "signature"
})
@XmlRootElement(name = "PackingList")
public class PackingList {

	@XmlElement(name = "Id", required = true)
	private String uuid;
	@XmlElement(name = "AnnotationText")
	private String annotation;
	@XmlElement(name = "IconId")
	private String iconId;
	@XmlElement(name = "IssueDate", required = true)
	private String issueDate;
	@XmlElement(name = "Issuer", required = true)
	private String issuer;
	@XmlElement(name = "Creator", required = true)
	private String creator;
	@XmlElement(name = "GroupId")
	private String groupId;
	@XmlElement(name = "AssetList", required = true)
	private List<PackingAsset> assetList;

	/**
	 * @return the uuid
	 */
	public final String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid
	 *            the uuid to set
	 */
	public final void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the annotation
	 */
	public final String getAnnotation() {
		return annotation;
	}

	/**
	 * @param annotation
	 *            the annotation to set
	 */
	public final void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	/**
	 * @return the iconId
	 */
	public final String getIconId() {
		return iconId;
	}

	/**
	 * @param iconId
	 *            the iconId to set
	 */
	public final void setIconId(String iconId) {
		this.iconId = iconId;
	}

	/**
	 * @return the issueDate
	 */
	public final String getIssueDate() {
		return issueDate;
	}

	/**
	 * @param issueDate
	 *            the issueDate to set
	 */
	public final void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * @return the issuer
	 */
	public final String getIssuer() {
		return issuer;
	}

	/**
	 * @param issuer
	 *            the issuer to set
	 */
	public final void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	/**
	 * @return the creator
	 */
	public final String getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public final void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the groupId
	 */
	public final String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public final void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the assetList
	 */
	public final List<PackingAsset> getAssetList() {
		return assetList;
	}

	/**
	 * @param assetList
	 *            the assetList to set
	 */
	public final void setAssetList(List<PackingAsset> assetList) {
		this.assetList = assetList;
	}

	/**
	 * Get all uuids in PackingList file
	 * 
	 * @return list of uuids
	 */
	public final List<String> getAllUuids() {
		List<String> uuids = new ArrayList<String>();

		if (uuid != null) {
			uuids.add(uuid);
		}

		if (assetList != null) {
			for (PackingAsset asset : assetList) {
				if (asset.getUuid() != null) {
					uuids.add(asset.getUuid());
				}
			}
		}
		return uuids;
	}
	/**
	 * Get packing asset by uuid
	 * 
	 * @param uuid
	 * @return packing asset, null if packing asset is not found
	 */
	public final PackingAsset getPackingAssetByUuid(String uuid) {
		if (assetList != null) {
			for (PackingAsset asset : assetList) {
				if (asset.getUuid() != null) {
					if (uuid.equals(asset.getUuid()))
						return asset;
				}
			}
		}
		return null;
	}
}
