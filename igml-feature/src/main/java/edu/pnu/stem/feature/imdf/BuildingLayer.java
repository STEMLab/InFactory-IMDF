package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.SpaceLayer;
import net.opengis.indoorgml.core.v_1_0.NodesType;

public class BuildingLayer extends SpaceLayer{
	NodesType nodes;

	public BuildingLayer(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}
}
