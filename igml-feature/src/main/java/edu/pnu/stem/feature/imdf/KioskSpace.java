package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FeatureInformationType;

public class KioskSpace extends CellSpace {

	FeatureInformation featureInformation;
	String anchor_id;
	String level_id;
	Labels feature_name;
	Labels alt_name;
	String parentId;
	
	public void setParent(CellSpace parent) {
		CellSpace found = null;
		found = (CellSpace) indoorGMLMap.getFeature(parent.getId());
		if (found == null) {
			indoorGMLMap.setFutureFeature(parent.getId(), parent);
		}
		this.parentId = parent.getId();
	}

	public CellSpace getCellSpaceFeature() {
		CellSpace feature = null;
		feature = (CellSpace) indoorGMLMap.getFeature(this.parentId);
		if (feature == null) {
			feature = (CellSpace) indoorGMLMap.getFutureFeature(this.parentId);
		}
		return feature;
	}

	public KioskSpace(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}

	public void setFeatureInformation(FeatureInformation featureInformation) {

		this.featureInformation = featureInformation;
	}

	public FeatureInformation getFeatureInformation() {

		return featureInformation;
	}

	public void setAnchorId(String anchor_id) {
		this.anchor_id = anchor_id;
	}

	public String getAnchorId() {
		return anchor_id;
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
