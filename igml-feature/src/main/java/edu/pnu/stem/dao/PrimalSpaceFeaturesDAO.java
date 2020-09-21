package edu.pnu.stem.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;
import edu.pnu.stem.feature.core.CellSpaceBoundary;
import edu.pnu.stem.feature.core.IndoorFeatures;
import edu.pnu.stem.feature.core.InterEdges;
import edu.pnu.stem.feature.core.PrimalSpaceFeatures;
import edu.pnu.stem.feature.core.SpaceLayers;

/**
 * 
 * @author jungh
 *	This class implements PrimalSpaceFeaturestype of IndoorGML-1.0.3
 */
public class PrimalSpaceFeaturesDAO {
	
	public static PrimalSpaceFeatures createPrimalSpaceFeatures(IndoorGMLMap map, String parentId, String id, String name, String description, List<String>cellSpaceMember,List<String>cellSpaceBoundaryMember) {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		PrimalSpaceFeatures newFeature = new PrimalSpaceFeatures(map, id);
		
		if(map.hasFutureID(id)){
			newFeature = (PrimalSpaceFeatures)map.getFutureFeature(id);
			//map.removeFutureID(id);
		}
		else {
			map.setFutureFeature(id, newFeature);
		}
		
		List<CellSpace>cm = newFeature.getCellSpaceMember();
		if(cm == null)
			cm = new ArrayList<CellSpace>();
		
		List<CellSpaceBoundary> cbm = newFeature.getCellSpaceBoundaryMember();
		if(cbm == null)
			cbm = new ArrayList<CellSpaceBoundary>();

		
		IndoorFeatures parent = (IndoorFeatures) map.getFeature(parentId);
		
		if(parent == null){
			if(map.hasFutureID(parentId)){
				map.getFutureFeature(parentId);
				//map.removeFutureID(parentId);
			}
			else{
				parent = new IndoorFeatures(map,parentId);
			}
		}
		
		if(name != null) {
			newFeature.setName(name);
		}
		
		if(description != null) {
			newFeature.setDescription(description);
		}
		
		if(cellSpaceMember != null) {
			for(String c : cellSpaceMember)
				cm.add(new CellSpace(map,c));
			newFeature.setCellSpaceMember(cm);
		}
		
		if(cellSpaceBoundaryMember != null) {
			for(String cb : cellSpaceBoundaryMember)
				cbm.add(new CellSpaceBoundary(map,cb));
			newFeature.setCellSpaceBoundaryMember(cbm);
		}
		
		parent.setPrimalSpaceFeatures(newFeature);
		newFeature.setParent(parent);
		map.removeFutureID(id);
		map.setFeature(id, "PrimalSpaceFeatures", newFeature);
		return newFeature;
	}
	public static PrimalSpaceFeatures readPrimalSpaceFeatures(IndoorGMLMap map, String id) {
		PrimalSpaceFeatures target = null;
		try {
			target = (PrimalSpaceFeatures)map.getFeature(id);
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		return target;
	}
	public static PrimalSpaceFeatures updatePrimalSpaceFeatures(IndoorGMLMap map, String parentId, String id, String name, String description, List<String>cellspacemembers, List<String>cellspaceboundarymembers) {
		PrimalSpaceFeatures result = new PrimalSpaceFeatures(map, id);
		PrimalSpaceFeatures target = (PrimalSpaceFeatures)map.getFeature(id);
		
		IndoorFeatures parent = target.getParent();
		if(!parent.getId().equals(parentId)) {
			IndoorFeatures newParent = (IndoorFeatures)map.getFeature(parentId);
			if(newParent == null)
				newParent = new IndoorFeatures(map, parentId);
			
			parent.resetMultiLayerdGraph();
			result.setParent(newParent);
		}
		else {
			result.setParent(parent);
		}
		
		
		if(name != null) {
			result.setName(name);
		}
	
		
		if(description != null) {
			result.setDescription(description);
		}
		
		if(cellspacemembers != null) {
			List<CellSpace> oldChild = target.getCellSpaceMember();
			List<CellSpace> newChild = new ArrayList<CellSpace>();
			
			for(String ni : cellspacemembers) {
				newChild.add(new CellSpace(map,ni));
			}
			if(oldChild != null) {
				for(CellSpace n : oldChild) {
					if(!newChild.contains(n)) {
						oldChild.remove(n);
					}
				}
			}
			else {
				oldChild = new ArrayList<CellSpace>();
			}
			
			for(CellSpace n : newChild) {
				if(!oldChild.contains(n)) {
					oldChild.add(n);
				}
			}
			
			result.setCellSpaceMember(oldChild);
			
		}
		else {
			if(target.getCellSpaceMember() != null && target.getCellSpaceMember().size() != 0) {
				List<CellSpace> oldChild = target.getCellSpaceMember();
				
				for(CellSpace child : oldChild) {
					child.resetParent();
				}
			}
		}
		
		if(cellspaceboundarymembers != null) {
			List<CellSpaceBoundary> oldChild = target.getCellSpaceBoundaryMember();
			List<CellSpaceBoundary> newChild = new ArrayList<CellSpaceBoundary>();
			
			
			for(String ei :	cellspaceboundarymembers) {
				newChild.add(new CellSpaceBoundary(map,ei));
			}
			
			if(oldChild != null) {
				for(CellSpaceBoundary n : oldChild) {
					if(!newChild.contains(n)) {
						oldChild.remove(n);
					}
				}
			}
			else {
				oldChild = new ArrayList<CellSpaceBoundary>();
			}
			
			
			for(CellSpaceBoundary n : newChild) {
				if(!oldChild.contains(n)) {
					oldChild.add(n);
				}
			}

			
			result.setCellSpaceBoundaryMember(oldChild);
		}
		else {
			if(target.getCellSpaceBoundaryMember() != null && target.getCellSpaceBoundaryMember().size() != 0) {
				List<CellSpaceBoundary> oldChild = target.getCellSpaceBoundaryMember();
				
				for(CellSpaceBoundary child : oldChild) {
					child.resetParent();
				}
			}
		}
		
		map.removeFeature(id);
		map.setFeature(id, "PrimalSpaceFeatures", result);
		return result;
	}
	
	public static void deletePrimalSpaceFeatures(IndoorGMLMap map, String id) {
		PrimalSpaceFeatures target = (PrimalSpaceFeatures) map.getFeature(id);
		IndoorFeatures parent = target.getParent();
		
		parent.deletePrimalSpaceFeatures(target);
		
		for(CellSpace c : target.getCellSpaceMember())
			c.resetParent();
		
		for(CellSpaceBoundary cb : target.getCellSpaceBoundaryMember())
			cb.resetParent();
		
		map.removeFeature(id);
	}
}
