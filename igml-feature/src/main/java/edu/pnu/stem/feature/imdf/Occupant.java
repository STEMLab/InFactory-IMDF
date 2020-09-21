package edu.pnu.stem.feature.imdf;

import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FeatureInformationType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.LEVELCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.LabelsType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.OCCUPANTCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.TEMPORALITYType;

public class Occupant {
	
	private FeatureInformation featureInformation;
	private OCCUPANTCATEGORY category;
	private String anchor_id;
	private String hours;
	private String ordinal;
	String[] phone;
	String website;
	Temporality validity;
	Labels correlation_id;
	
	public void setFeatureInformation(FeatureInformation featureInformation) {

		this.featureInformation = featureInformation;
	}

	public FeatureInformation getFeatureInformation() {

		return featureInformation;
	}

	public void setCategory(OCCUPANTCATEGORY category) {

		this.category = category;
	}

	public OCCUPANTCATEGORY getCategory() {

		return category;
	}
	public void setAnchorId(String anchor_id) {

		this.anchor_id = anchor_id;
	}

	public String getAnchorId() {

		return anchor_id;
	}
	public void setHours(String hours) {

		this.hours = hours;
	}

	public String getHours() {

		return hours;
	}
	public void setOrdinal(String ordinal) {

		this.ordinal = ordinal;
	}

	public String getOrdinal() {

		return ordinal;
	}
	public void setPhone(String[] phones) {

		this.phone = phones;
	}

	public String[] getPhone() {

		return phone;
	}
	public void setWebsite(String website) {

		this.website = website;
	}

	public String getWebsite() {

		return website;
	}
	public void setValidity(Temporality validity) {

		this.validity = validity;
	}

	public Temporality getValidity() {

		return validity;
	}
	public void setCorrelationId(Labels correlation_id) {

		this.correlation_id = correlation_id;
	}

	public Labels getCorrelationId() {

		return correlation_id;
	}
	
	
	

}
