package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.State;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSIBILITYCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.AMENITYCATEGORY;

public class AmenityState extends State {

	AMENITYCATEGORY category;
	ACCESSIBILITYCATEGORY[] accessibility;

	String hours;
	String phone;
	String website;
	String[] unit_ids;
	String address_id;
	String correlation_id;

	public AmenityState(IndoorGMLMap doc, String id) {
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

	public void setAmenitycategory(AMENITYCATEGORY category) {

		this.category = category;
	}

	public AMENITYCATEGORY getAmenitycategory() {

		return category;
	}

	public void setAccessibilitycategory(ACCESSIBILITYCATEGORY[] accessibility) {

		this.accessibility = accessibility;
	}

	public ACCESSIBILITYCATEGORY[] getAccessibilitycategory() {

		return accessibility;
	}

	public void setHours(String hours) {

		this.hours = hours;
	}

	public String getHours() {

		return hours;
	}

	public void setPhone(String phone) {

		this.phone = phone;
	}

	public String getPhone() {

		return phone;
	}

	public void setWebsite(String website) {

		this.website = website;
	}

	public String getWebsite() {

		return website;
	}

	public void setUnitIds(String[] unit_ids2) {

		this.unit_ids = unit_ids2;
	}

	public String[] getUnitIds() {

		return unit_ids;
	}

	public void setAddressId(String address_id) {

		this.address_id = address_id;
	}

	public String getAddressId() {
		return address_id;
	}

	public void setCorrelationId(String correlation_id) {

		this.correlation_id = correlation_id;
	}

	public String getCorrelationId() {

		return correlation_id;
	}

}
