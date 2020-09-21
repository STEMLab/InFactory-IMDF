package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.LabelsType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FeatureInformationType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.GEOFENCECATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.RESTRICTIONCATEGORY;
public class GeofenceSpace extends CellSpace {
	
	private String parentId;
	private String correlation_id;
	private String[] building_ids;
	private String[] level_ids;
	private String[] parents;
	private Labels feature_name;
	private Labels alt_name;
	private FeatureInformation featureInformation;
	
	private GEOFENCECATEGORY category;	
	private RESTRICTIONCATEGORY restriction;
	
	public GeofenceSpace(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}
	
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
	
	public void setFeatureInformation(FeatureInformation featureInformation) {
		
		this.featureInformation = featureInformation;
	}
	
	public FeatureInformation getFeatureInformation() {
		
		return featureInformation;
	}
	
	public void setCategory(GEOFENCECATEGORY category) {
		
		this.category = category;
	}
	
	public GEOFENCECATEGORY getCategory() {
		
		return category;
	}
	
	public void setRestrictionCategory(RESTRICTIONCATEGORY restriction) {
		
		this.restriction = restriction;
	}
	
	public RESTRICTIONCATEGORY getRestrictionCategory() {
		
		return restriction;
	}
	
	public void setCorrelationId(String correlation_id) {
		
		this.correlation_id = correlation_id;
	}
	
	public String getCorrelationId() {
		
		return correlation_id;
	}
	
	public void setBuildingIds(String[] building_ids) {
		
		this.building_ids = building_ids;
	}
	
	public String[] getBuildingIds() {
		
		return building_ids;
	}

	public void setLevelIds(String[] level_ids) {
		
		this.level_ids = level_ids;
	}
	
	public String[] getLevelIds() {
		
		return level_ids;
	}

	public void setParents(String[] parents) {
		
		this.parents = parents;
	}
	
	public String[] getParents() {
		
		return parents;
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
