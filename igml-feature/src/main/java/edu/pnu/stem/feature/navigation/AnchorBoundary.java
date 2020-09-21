package edu.pnu.stem.feature.navigation;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpaceBoundary;
import edu.pnu.stem.feature.core.PrimalSpaceFeatures;

public class AnchorBoundary extends TransferBoundary{
	private String parentId;
	public AnchorBoundary(IndoorGMLMap doc, String id) {
		
		super(doc, id);
		// TODO Auto-generated constructor stub
		
	}
	public void setParent(TransferBoundary parent) {
		TransferBoundary found = null;
		found = (TransferBoundary)indoorGMLMap.getFeature(parent.getId());
		if(found == null){
			indoorGMLMap.setFutureFeature(parent.getId(), parent);
		}
		this.parentId = parent.getId();
	}


	public TransferBoundary getAnchorBoundaryParent() {
		TransferBoundary feature = null;
		feature = (TransferBoundary) indoorGMLMap.getFeature(this.parentId);
		if(feature == null) {
			feature = (TransferBoundary) indoorGMLMap.getFutureFeature(this.parentId);
		}
		return feature;
	}
	
	public void copyCellBoundary(CellSpaceBoundary cb) {
		this.setCellSpace(cb.getCellSpace());
	}
}
