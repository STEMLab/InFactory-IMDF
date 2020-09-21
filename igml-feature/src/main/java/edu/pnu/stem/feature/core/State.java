package edu.pnu.stem.feature.core;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Geometry;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.navigation.GeneralSpace;
import edu.pnu.stem.util.GeometryUtil;

/**
 * @author jungh
 *
 */
public class State extends AbstractFeature {
	
	/**
	 * geometry of this feature
	 */
	private String geometry;
	
	private String duality;
	/**
	 * value of Transition feature which has this feature as boundary
	 */
	private List<String> connects;

	private String externalReference;
	
	public State(IndoorGMLMap doc, String id){
		super(doc, id);
		connects = new ArrayList<String>();
	}
	
	public Geometry getGeometry() {
		Geometry feature = null;
		if(this.geometry != null) {
			feature = (Geometry) indoorGMLMap.getFeature4Geometry(this.geometry);
		}
		return feature;
	}
	
	public void setGeometry(Geometry geom) {
		String gId = GeometryUtil.getMetadata(geom, "id");
		Geometry found = (Geometry) indoorGMLMap.getFeature4Geometry(gId);
		if(found == null) {
			indoorGMLMap.setFeature4Geometry(gId, geom);
		}
		this.geometry = gId;
	}
	
	public void setExternalReference(String e) {
		this.externalReference = e;
	}

	public String getExternalReference() {
		return this.externalReference;
	}

	public String parentId;

	public void setParent(Nodes parent) {
		Nodes found = null;
		found = (Nodes)indoorGMLMap.getFeature(parent.getId());
		if(found == null){
			
			indoorGMLMap.setFutureFeature(parent.getId(), parent);
			
		}		
		this.parentId = parent.getId();
	}
	
	public void resetDuality() {
		this.duality = null;
	}
	public Nodes getParent() {
		Nodes found = (Nodes)indoorGMLMap.getFeature(this.parentId);
		if(found == null){
			if(indoorGMLMap.hasFutureID(parentId)){
				found = (Nodes)indoorGMLMap.getFutureFeature(parentId);
			}
		}
		
		return found;
	}
	
	public boolean hasDuality() {
		if(this.duality != null)
			return true;
		return false;
	}

	public CellSpace getDuality() {
		CellSpace found = null;
		if(this.duality != null) {
			found = (CellSpace)indoorGMLMap.getFeature(this.duality);
			if(found == null) {
				found = (CellSpace)indoorGMLMap.getFutureFeature(this.duality);
			}
		}
		return found;
	}

	public void setDuality(CellSpace duality) {
		CellSpace found = null;
		found = (CellSpace)indoorGMLMap.getFeature(duality.getId());
		if(found == null){
			if(!indoorGMLMap.hasFutureID(duality.getId())){
				indoorGMLMap.setFutureFeature(duality.getId(), duality);
			}
		}
		this.duality = duality.getId();
	}
	public void setDuality(GeneralSpace duality) {
		GeneralSpace found = null;
		found = (GeneralSpace)indoorGMLMap.getFeature(duality.getId());
		if(found == null){
			if(!indoorGMLMap.hasFutureID(duality.getId())){
				indoorGMLMap.setFutureFeature(duality.getId(), duality);
			}
		}
		this.duality = duality.getId();
	}
	

	public void setConnects(List<Transition> connects) {
		this.connects = new ArrayList<String>();
		for (int i = 0; i < connects.size(); i++) {
			Transition found = null;
			found = (Transition)indoorGMLMap.getFeature(connects.get(i).getId());
			if(found == null){
				indoorGMLMap.setFutureFeature(connects.get(i).getId(), connects.get(i));
			}
			if(!this.connects.contains(connects.get(i).getId())){
				this.connects.add(connects.get(i).getId());
			}
		}
	}
	
	public void addConnects(Transition t) {
		if(this.connects == null)
			this.connects = new ArrayList<String>();
		if(!this.connects.contains(t.getId())){
			Transition found = null;
			found = (Transition)indoorGMLMap.getFeature(t.getId());
			if(found == null){
				indoorGMLMap.setFutureFeature(t.getId(), t);
			}
			if(!this.connects.contains(t.getId())){
				this.connects.add(t.getId());
			}
			
		}
	}

	public List<Transition> getConnects() {
		List<Transition> connects = null;
		if(this.connects.size() != 0){
			connects = new ArrayList<Transition>();
			for(int i = 0 ; i < this.connects.size() ; i++){
				Transition found = (Transition)indoorGMLMap.getFeature(this.connects.get(i));
				if(found == null)
					found = (Transition)indoorGMLMap.getFutureFeature(this.connects.get(i));
				connects.add(found);
			}
		}
		return connects;
	}
	
	public void deleteConnects(Transition t) {
		if(connects.contains(t.getId()))
			connects.remove(t.getId());
	}
	public void clearConnects(){
		this.connects.clear();
	}

	public void resetParent() {
		this.parentId = null;
		
	}


}
