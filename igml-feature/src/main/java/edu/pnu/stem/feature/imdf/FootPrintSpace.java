package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FEATURECATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.LabelsType;

public class FootPrintSpace extends CellSpace{
	FeatureInformation featureInformation;
	FEATURECATEGORY category;
	String[] building_ids;
	String parentId;
	Labels feature_name;
//	String[] sibling_polygon_ids;
	
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

	public FootPrintSpace(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}
	
	public void setFeatureInformation(FeatureInformation featureInformation) {
		this.featureInformation = featureInformation;
	}
	
	public FeatureInformation getFeatureInformation() {
		return featureInformation;
	}
	
	public void setFeatureCategory(FEATURECATEGORY category) {
		this.category = category;
	}
	
	public FEATURECATEGORY getFeatureCategory() {
		return category;
	}
	
	public void setBuildingIds(String[] building_ids) {
		this.building_ids = building_ids;
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
	
//	public void setSilingPolygonIds(String[] sibling_polygon_ids) {
//		this.sibling_polygon_ids = sibling_polygon_ids;
//	}
//	
//	public String[] getSilingPolygonIds() {
//		return sibling_polygon_ids;
//	}

}
