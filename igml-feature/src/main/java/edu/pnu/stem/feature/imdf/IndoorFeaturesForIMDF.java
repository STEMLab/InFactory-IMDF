package edu.pnu.stem.feature.imdf;

import java.util.ArrayList;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.IndoorFeatures;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.AddressType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FeatureInformationType;

import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ManifestType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.OccupantType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.RelationshipType;

public class IndoorFeaturesForIMDF extends IndoorFeatures {

	Manifest manifest;
	Address address;
	ArrayList<Occupant> occupant;
	FeatureInformation featureInformation;
	ArrayList<Relationship> relationship;

	public IndoorFeaturesForIMDF(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}

	public void setManifest(Manifest manifest) {
		
		this.manifest = manifest;
	}

	public Manifest getManifest() {

		return manifest;
	}

	public void setAddress(Address address) {

		this.address = address;
	}

	public Address getAddress() {

		return address;
	}

	public void setOccupant(ArrayList<Occupant> occupant) {

		this.occupant = occupant;
	}

	public ArrayList<Occupant> getOccupant() {
		return occupant;
	}

	public void setRelationship(ArrayList<Relationship> relationship) {

		this.relationship = relationship;
	}

	public ArrayList<Relationship> getRelationship() {

		return relationship;
	}

}
