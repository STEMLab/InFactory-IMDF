package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSIBILITYCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.RESTRICTIONCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.SECTIONCATEGORY;

public class SectionSpace extends CellSpace {

	FeatureInformation featureInformation;
	SECTIONCATEGORY category;
	RESTRICTIONCATEGORY restriction;
	ACCESSIBILITYCATEGORY[] accessibility;
	String level_id;
	String address_id;
	Labels feature_name;
	Labels alt_name;
	String[] parents;

	public SectionSpace(IndoorGMLMap doc, String id) {

		super(doc, id);
		// TODO Auto-generated constructor stub
	}

	public void setFeatureInformation(FeatureInformation featureInformation) {

		this.featureInformation = featureInformation;
	}

	public FeatureInformation getFeatureInformation() {

		return featureInformation;
	}

	public void setCategory(SECTIONCATEGORY category) {

		this.category = category;
	}

	public SECTIONCATEGORY getCategory() {

		return category;
	}

	public void setRestrictionCategory(RESTRICTIONCATEGORY restriction) {

		this.restriction = restriction;
	}

	public RESTRICTIONCATEGORY getRestrictionCategory() {

		return restriction;
	}

	public void setAccessibilityCategory(ACCESSIBILITYCATEGORY[] accessibility) {

		this.accessibility = accessibility;
	}

	public ACCESSIBILITYCATEGORY[] getAccessibilityCategory() {
		return accessibility;
	}

	public void setLevelId(String level_id) {

		this.level_id = level_id;
	}

	public String getLevelId() {

		return level_id;
	}

	public void setAddressId(String address_id) {

		this.address_id = address_id;
	}

	public String getAddressId() {

		return address_id;
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

	public void setParents(String[] parents) {
		this.parents = parents;

	}

	public String[] getParents() {
		return parents;
	}

}
