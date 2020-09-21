package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.SpaceLayer;
import edu.pnu.stem.feature.core.SpaceLayers;
import net.opengis.indoorgml.core.v_1_0.NodesType;

public class AmenitySpaceLayer extends SpaceLayer {
	private String parentId;
	private NodesType nodes;

	public AmenitySpaceLayer(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}

	public void setParent(SpaceLayers parent) {
		SpaceLayers found = null;
		found = (SpaceLayers) indoorGMLMap.getFeature(parent.getId());
		if (found == null) {
			indoorGMLMap.setFutureFeature(parent.getId(), parent);
		}
		this.parentId = parent.getId();
	}

	public SpaceLayers getParent() {
		SpaceLayers found = null;
		found = (SpaceLayers) indoorGMLMap.getFeature(this.parentId);
		if (found == null) {
			if (indoorGMLMap.hasFutureID(parentId))
				found = (SpaceLayers) indoorGMLMap.getFutureFeature(parentId);
		}
		return found;
	}
}
