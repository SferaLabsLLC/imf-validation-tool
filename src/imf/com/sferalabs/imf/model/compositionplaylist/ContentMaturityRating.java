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
package com.sferalabs.imf.model.compositionplaylist;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * ContentMaturityRating Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContentMaturityRatingType", propOrder = {
    "agency",
    "rating",
    "audience",
    "any"
})
public class ContentMaturityRating {

	@XmlElement(name = "Agency", required = true)
	private String agency;
	@XmlElement(name = "Rating", required = true)
	private String rating;
	@XmlElement(name = "Audience")
	private String audience;
	/**
	 * @return the agency
	 */
	public final String getAgency() {
		return agency;
	}
	/**
	 * @param agency the agency to set
	 */
	public final void setAgency(String agency) {
		this.agency = agency;
	}
	/**
	 * @return the rating
	 */
	public final String getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public final void setRating(String rating) {
		this.rating = rating;
	}
	/**
	 * @return the audience
	 */
	public final String getAudience() {
		return audience;
	}
	/**
	 * @param audience the audience to set
	 */
	public final void setAudience(String audience) {
		this.audience = audience;
	}
	
}
