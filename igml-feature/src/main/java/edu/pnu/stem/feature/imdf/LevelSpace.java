package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;

import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.LEVELCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.RESTRICTIONCATEGORY;

public class LevelSpace extends CellSpace {

	private FeatureInformation featureInformation;
	private LEVELCATEGORY category;
	private RESTRICTIONCATEGORY restriction;
	private String outdoor;
	private String ordinal;
	private String address_id;
	private String[] building_ids;
	private Labels feature_name;
	private Labels short_name;

	public LevelSpace(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}

	public void setFeatureInformation(FeatureInformation featureInformation) {

		this.featureInformation = featureInformation;
	}

	public FeatureInformation getFeatureInformation() {

		return featureInformation;
	}

	public void setCategory(LEVELCATEGORY category) {

		this.category = category;
	}

	public LEVELCATEGORY getCategory() {

		return category;
	}

	public void setRestrictionCategory(RESTRICTIONCATEGORY restriction) {

		this.restriction = restriction;
	}

	public RESTRICTIONCATEGORY getRestrictionCategory() {

		return restriction;
	}

	public void setOutdoor(String outdoor) {
		this.outdoor = outdoor;
	}

	public String getOutdoor() {
		return outdoor;
	}

	public void setOrdinal(String ordinal) {
		this.ordinal = ordinal;
	}

	public String getOrdinal() {
		return ordinal;
	}

	public void setAddressId(String address_id) {
		this.address_id = address_id;
	}

	public String getAddressId() {
		return address_id;
	}

	public void setBuildingIds(String[] building_ids2) {
		this.building_ids = building_ids2;
	}

	public String[] getBuildingIds() {
		return building_ids;
	}

	public void setFeatureName(Labels feature_name) {
		this.feature_name = feature_name;
	}

	public Labels getFeatureName() {
		return feature_name;
	}

	public void setShortName(Labels short_name) {
		this.short_name = short_name;
	}

	public Labels getShortName() {
		return short_name;
	}

}
