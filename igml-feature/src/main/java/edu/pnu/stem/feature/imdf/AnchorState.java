package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.Nodes;
import edu.pnu.stem.feature.core.State;

public class AnchorState extends State {
	String unit_id;
	String address_id;

	public AnchorState(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}

	public void setParent(Nodes parent) {
		Nodes found = null;
		found = (Nodes) indoorGMLMap.getFeature(parent.getId());
		if (found == null) {

			indoorGMLMap.setFutureFeature(parent.getId(), parent);

		}
		this.parentId = parent.getId();
	}

	public Nodes getParent() {
		Nodes found = (Nodes) indoorGMLMap.getFeature(this.parentId);
		if (found == null) {
			if (indoorGMLMap.hasFutureID(parentId)) {
				found = (Nodes) indoorGMLMap.getFutureFeature(parentId);
			}
		}
		return found;
	}

	public void setUnitId(String unit_id) {
		this.unit_id = unit_id;
	}

	public String getUnitId() {
		return unit_id;
	}

	public void setAddressId(String address_id) {
		this.address_id = address_id;
	}

	public String getAddressId() {
		return address_id;
	}

}
