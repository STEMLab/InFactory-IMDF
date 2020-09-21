package edu.pnu.stem.dao;
import java.util.ArrayList;
import java.util.List;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;
import edu.pnu.stem.feature.core.InterEdges;
import edu.pnu.stem.feature.core.InterLayerConnection;
import edu.pnu.stem.feature.core.MultiLayeredGraph;
import edu.pnu.stem.feature.core.SpaceLayer;

public class InterEdgesDAO {
	public static InterEdges createInterEdges(IndoorGMLMap map, String parentId, String id, String name, String description, List<String>interLayerConnectionMember){
		InterEdges newFeature = new InterEdges(map, id);
		
		MultiLayeredGraph parent = (MultiLayeredGraph) map.getFeature(parentId);
		
		if(parent == null){
			if(map.hasFutureID(parentId)){
				parent = (MultiLayeredGraph)map.getFutureFeature(parentId);
			}
			else{
				parent = new MultiLayeredGraph(map,parentId);
			}
		}
		map.setFutureFeature(id, newFeature);
		parent.addInterEdges(newFeature);
		newFeature.setParent(parent);
		
		if(name != null) {
			newFeature.setName(name);
		}
		
		if(description != null) {
			newFeature.setDescription(description);
		}
		
		if(interLayerConnectionMember != null){
			List<InterLayerConnection>tempList = new ArrayList<InterLayerConnection>();
			for(int i = 0 ; i < interLayerConnectionMember.size() ; i++){
				InterLayerConnection temp = new InterLayerConnection(map, interLayerConnectionMember.get(i));
				tempList.add(temp);
			}
			newFeature.setInterLayerConnectionMember(tempList);
		}

		map.removeFutureID(id);
		map.setFeature(id, "InterEdges", newFeature);
	
		return newFeature;
	}
	public static InterEdges readInterEdges(IndoorGMLMap map, String id) {
		InterEdges target = null;
		try {
			target = (InterEdges)map.getFeature(id);
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		return target;
	}
	public static InterEdges updateInterEdges(IndoorGMLMap map, String parentId, String id, String name, String description,List<String>interLayerConnectionMember) {
		InterEdges result = new InterEdges(map, id);
		InterEdges target = (InterEdges)map.getFeature(id);
		
		MultiLayeredGraph parent = target.getParent();
		if(!parent.getId().equals(parentId)) {
			MultiLayeredGraph newParent = (MultiLayeredGraph)map.getFeature(parentId);
			if(newParent == null)
				newParent = new MultiLayeredGraph(map, parentId);
			
			parent.deleteInterEdges(target);
			result.setParent(newParent);
		}
		
		result.setParent(parent);
		if(name != null) {
			result.setName(name);
		}
	
		
		if(description != null) {
			result.setDescription(description);
		}
		
		if(interLayerConnectionMember != null) {
			List<InterLayerConnection> oldChild = target.getInterLayerConnectionMember();
			List<InterLayerConnection> newChild = new ArrayList<InterLayerConnection>();
			
			for(String ni : interLayerConnectionMember) {
				newChild.add(new InterLayerConnection(map,ni));
			}
			
			for(InterLayerConnection n : oldChild) {
				if(!newChild.contains(n)) {
					oldChild.remove(n);
				}
			}
			
			for(InterLayerConnection n : newChild) {
				if(!oldChild.contains(n)) {
					oldChild.add(n);
				}
			}
			
			result.setInterLayerConnectionMember(oldChild);;
		}
		else {
			if(target.getInterLayerConnectionMember().size() != 0) {
				for(InterLayerConnection s : target.getInterLayerConnectionMember()) {
					s.resetParent();
				}
			}
		}
		map.removeFeature(id);
		map.setFeature(id, "InterEdges", result);
		
		return result;
	}
	
	public static void deleteInterEdges(IndoorGMLMap map, String id) {
		InterEdges target = (InterEdges)map.getFeature(id);
		MultiLayeredGraph parent = target.getParent();
		
		parent.deleteInterEdges(target);
		
		for(InterLayerConnection i : target.getInterLayerConnectionMember())
			i.resetParent();
		
		map.removeFeature(id);
	}
}
