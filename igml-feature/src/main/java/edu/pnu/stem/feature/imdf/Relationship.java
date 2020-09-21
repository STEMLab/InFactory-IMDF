package edu.pnu.stem.feature.imdf;

import java.util.ArrayList;

import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.DIRECTION;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FEATUREREFERENCE;

import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.RELATIONSHIPCATEGORY;

public class Relationship {

	private RELATIONSHIPCATEGORY category;
	private DIRECTION direction;
	private String hours;
	private FEATUREREFERENCE destination;
	private FEATUREREFERENCE origin;
	private ArrayList<FEATUREREFERENCE> intermediary;
	private FeatureInformation featureInformation;

	public void setFeatureInformation(FeatureInformation featureInformation) {

		this.featureInformation = featureInformation;
	}

	public FeatureInformation getFeatureInformation() {

		return featureInformation;
	}

	public void setCategory(RELATIONSHIPCATEGORY category) {
		this.category = category;

	}

	public RELATIONSHIPCATEGORY getCategory() {
		return category;

	}

	public void setDirection(DIRECTION direction) {
		this.direction = direction;

	}

	public DIRECTION getDirection() {
		return direction;

	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getHours() {
		return hours;

	}

	public void setDestination(FEATUREREFERENCE destination) {
		this.destination = destination;
	}

	public FEATUREREFERENCE getDestination() {
		return destination;
	}

	public void setOrigin(FEATUREREFERENCE origin) {
		this.origin = origin;
	}

	public FEATUREREFERENCE getOrigin() {
		return origin;
	}

	public void setIntermediary(ArrayList<FEATUREREFERENCE> intermediary) {
		this.intermediary = intermediary;

	}

	public ArrayList<FEATUREREFERENCE> getIntermediary() {
		return intermediary;
	}

}
