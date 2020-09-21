package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.RESTRICTIONCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.VENUECATEGORY;

public class VenueSpace extends CellSpace {

	FeatureInformation featureInformation;
	VENUECATEGORY category;
	RESTRICTIONCATEGORY restriction;
	String hours;
	String phone;
	String website;
	Labels feature_name;
	Labels alt_name;

	public VenueSpace(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}

	public void setFeatureInformation(FeatureInformation featureInformation) {

		this.featureInformation = featureInformation;
	}

	public FeatureInformation getFeatureInformation() {

		return featureInformation;
	}

	public void setCategory(VENUECATEGORY category) {

		this.category = category;
	}

	public VENUECATEGORY getCategory() {

		return category;
	}

	public void setRestrictionCategory(RESTRICTIONCATEGORY restriction) {

		this.restriction = restriction;
	}

	public RESTRICTIONCATEGORY getRestrictionCategory() {

		return restriction;
	}

	public void setHours(String hours) {

		this.hours = hours;
	}

	public String getHours() {

		return hours;
	}

	public void setPhone(String phone) {

		this.phone = phone;
	}

	public String getPhone() {

		return phone;
	}

	public void setWebsite(String website) {

		this.website = website;
	}

	public String getWebsite() {

		return website;
	}

	public void setFeatureName(Labels feature_name) {

		this.feature_name = feature_name;
	}

	public Labels getFeatureName() {

		return feature_name;
	}

	public void setAltName(Labels alt_name) {

		this.alt_name = alt_name;
	}

	public Labels getAltName() {

		return alt_name;
	}

}
