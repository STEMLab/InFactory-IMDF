package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.Transition;

public class Detail extends Transition {
	
	
	private FeatureInformation featureInformation;
	private String level_id;

	public Detail(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}

	public void setFeatureInformation(FeatureInformation featureInformation) {

		this.featureInformation = featureInformation;
	}

	public FeatureInformation getFeatureInformation() {

		return featureInformation;
	}
	public void setLevelId(String level_id) {

		this.level_id = level_id;
	}
	
	public String getLevelId() {

		return level_id;
	}

}
