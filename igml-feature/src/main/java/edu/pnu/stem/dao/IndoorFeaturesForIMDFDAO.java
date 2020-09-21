package edu.pnu.stem.dao;

import java.util.ArrayList;
import java.util.UUID;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.Envelope;
import edu.pnu.stem.feature.core.MultiLayeredGraph;
import edu.pnu.stem.feature.core.PrimalSpaceFeatures;
import edu.pnu.stem.feature.imdf.Address;
import edu.pnu.stem.feature.imdf.IndoorFeaturesForIMDF;
import edu.pnu.stem.feature.imdf.Manifest;
import edu.pnu.stem.feature.imdf.Occupant;
import edu.pnu.stem.feature.imdf.Relationship;

public class IndoorFeaturesForIMDFDAO {

	public static IndoorFeaturesForIMDF createIndoorFeaturesForIMDF(IndoorGMLMap map, String id, String name, String description, String envelope,
			String multiLayeredGraph, String primalSpaceFeatures, Manifest manifest, Address address, ArrayList<Occupant> occupantList, ArrayList<Relationship> relationshipList ) {
		IndoorFeaturesForIMDF newFeature = new IndoorFeaturesForIMDF(map, id);
		
		if(map.hasFutureID(id)){
			newFeature = (IndoorFeaturesForIMDF)map.getFutureFeature(id);
			//map.removeFutureID(id);
		}
		else{
			newFeature = new IndoorFeaturesForIMDF(map, id);
		}
		map.setFutureFeature(id, newFeature);
		if(name != null) {
			newFeature.setName(name);
		}
		
		if(description != null) {
			newFeature.setDescription(description);
		}
		if (envelope!= null) {
			Envelope newEnvelope = new Envelope(map, envelope);
			newFeature.setBoundedBy(newEnvelope);
		}		

		if (primalSpaceFeatures!= null) {
			PrimalSpaceFeatures newPrimalSpaceFeatures = new PrimalSpaceFeatures(map, primalSpaceFeatures);
			newFeature.setPrimalSpaceFeatures(newPrimalSpaceFeatures);
		}
		if (multiLayeredGraph != null) {
			MultiLayeredGraph newMultiLayeredGraph = new MultiLayeredGraph(map, multiLayeredGraph);
			newFeature.setMultiLayeredGraph(newMultiLayeredGraph);
		}
		if(manifest!=null) {
			
			newFeature.setManifest(manifest);			
		}
		if(address!=null) {
			newFeature.setAddress(address);
			
		}
		if(occupantList!=null) {
			newFeature.setOccupant(occupantList);
			
		}
		if(relationshipList!=null) {
			newFeature.setRelationship(relationshipList);
			
		}
		
		map.removeFutureID(id);
		map.setFeature(id, "IndoorFeaturesForIMDF", newFeature);
		return newFeature;
	}
	
	public static IndoorFeaturesForIMDF createIndoorFeaturesForIMDF(IndoorGMLMap map, String id) {
		if(id == null) {
			id = UUID.randomUUID().toString();
		}
		
		IndoorFeaturesForIMDF newFeature = null;
		
		if(map.hasFutureID(id)){
			newFeature = (IndoorFeaturesForIMDF)map.getFutureFeature(id);
			//map.removeFutureID(id);
		}
		else{
			newFeature = new IndoorFeaturesForIMDF(map, id);
		}
		
		map.setFeature(id, "IndoorFeaturesForIMDF", newFeature);
		return newFeature;
	}
	
	public static IndoorFeaturesForIMDF readIndoorFeaturesForIMDF(IndoorGMLMap map, String id) {
		IndoorFeaturesForIMDF target = null;
		try {
			target = (IndoorFeaturesForIMDF)map.getFeature(id);
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		return target;
	}

	public static IndoorFeaturesForIMDF updateIndoorFeaturesForIMDF(IndoorGMLMap map, String id, String name, String description, String multilayeredgraph, String primalspacefeatures) {
		IndoorFeaturesForIMDF result = new IndoorFeaturesForIMDF(map, id);
		IndoorFeaturesForIMDF target = (IndoorFeaturesForIMDF)map.getFeature(id);
		
		
		if(name != null) {
			result.setName(name);
		}
	
		
		if(description != null) {
			result.setDescription(description);
		}
		
		if(multilayeredgraph != null) {
			if(target.getMultiLayeredGraph() != null && !target.getMultiLayeredGraph().getId().equals(multilayeredgraph)) {
				result.setMultiLayeredGraph(new MultiLayeredGraph(map,multilayeredgraph));
			}
		}
		else {
			if(target.getMultiLayeredGraph() != null) {
				target.getMultiLayeredGraph().resetParent();
			}
		}
		
		if(primalspacefeatures != null) {
			if(target.getPrimalSpaceFeatures() != null && !target.getPrimalSpaceFeatures().getId().equals(primalspacefeatures)) {
				result.setPrimalSpaceFeatures(new PrimalSpaceFeatures(map,primalspacefeatures));
			}
		}
		else {
			if(target.getPrimalSpaceFeatures() != null) {
				target.getMultiLayeredGraph().resetParent();
			}
		}
		map.removeFeature(id);
		map.setFeature(id, "IndoorFeaturesForIMDF", result);
		return result;
		
	}
	
	public static void deleteIndoorFeaturesForIMDF(IndoorGMLMap map, String id) {
		IndoorFeaturesForIMDF target = (IndoorFeaturesForIMDF) map.getFeature(id);
		
		if(target.getMultiLayeredGraph() != null)
			target.getMultiLayeredGraph().resetParent();
		if(target.getPrimalSpaceFeatures() != null)
			target.getPrimalSpaceFeatures().resetParent();
		
		map.removeFeature(id);
	}

}
