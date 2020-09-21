package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.State;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.BUILDINGCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.RESTRICTIONCATEGORY;

public class BuildingState extends State  {
	
	BUILDINGCATEGORY category;
	RESTRICTIONCATEGORY restriction;
	String address_id;

	public BuildingState(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}
	public void setParent(State parent) {
		State found = null;
		found = (State) indoorGMLMap.getFeature(parent.getId());
		if (found == null) {

			indoorGMLMap.setFutureFeature(parent.getId(), parent);

		}
		this.parentId = parent.getId();
	}

	public State getState() {
		State found = (State) indoorGMLMap.getFeature(this.parentId);
		if (found == null) {
			if (indoorGMLMap.hasFutureID(parentId)) {
				found = (State) indoorGMLMap.getFutureFeature(parentId);
			}
		}

		return found;
	}
	
	public void setBuildingCategory(BUILDINGCATEGORY category) {
		this.category = category;	
	}
	public BUILDINGCATEGORY getBuildCategory() {
		return category;
	}
	
	public void setRestrictionCategory(RESTRICTIONCATEGORY restriction) {
		this.restriction = restriction;	
	}
	public RESTRICTIONCATEGORY getRestrictionCategory() {
		return restriction;
	}
	
	public void setAddressId(String address_id) {
		this.address_id = address_id;
		
	}
	public String getAddressId() {
		return address_id;
	}

}
