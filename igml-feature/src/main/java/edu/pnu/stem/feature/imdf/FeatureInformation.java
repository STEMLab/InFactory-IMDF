package edu.pnu.stem.feature.imdf;

import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FEATURECATEGORY;


public class FeatureInformation {
	
	String id;
	String type;
	FEATURECATEGORY feature_type;
	String gml_geometry;
	
	
	public void setId(String id) {
		this.id = id;	
	}
	public String getId() {
		return id;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setFeatureType(FEATURECATEGORY feature_type) {
		this.feature_type = feature_type;
	}
	public FEATURECATEGORY getFeatureType() {
		return feature_type;
	}
	public void setGmlGeometry(String gml_geometry) {
		this.gml_geometry = gml_geometry;
	}
	public String getGmlGeometry() {
		return gml_geometry;
	}
	

}
