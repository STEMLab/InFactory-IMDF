package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSIBILITYCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.RESTRICTIONCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.UNITCATEGORY;

public class UnitSpace extends CellSpace {

	String parents;
	FeatureInformation featureInformation;
	UNITCATEGORY category;
	RESTRICTIONCATEGORY restriction;
	ACCESSIBILITYCATEGORY[] accessibility;
	String level_id;
	Labels feature_name;
	Labels alt_name;

	public UnitSpace(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}

	public void setFeatureInformation(FeatureInformation featureInformation) {

		this.featureInformation = featureInformation;
	}

	public FeatureInformation getFeatureInformation() {

		return featureInformation;
	}

	public void setCategory(UNITCATEGORY category) {

		this.category = category;
	}

	public UNITCATEGORY getCategory() {

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
