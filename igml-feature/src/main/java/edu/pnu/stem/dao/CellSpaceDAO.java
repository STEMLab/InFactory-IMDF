package edu.pnu.stem.dao;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;
import edu.pnu.stem.feature.core.CellSpaceBoundary;
import edu.pnu.stem.feature.core.PrimalSpaceFeatures;
import edu.pnu.stem.feature.core.State;
import edu.pnu.stem.geometry.jts.Solid;
import edu.pnu.stem.geometry.jts.WKTReader3D;
import edu.pnu.stem.util.GeometryUtil;

/**
 * @author jungh
 *
 */
public class CellSpaceDAO {

	public static void deleteCellSpace(IndoorGMLMap map, String id) {
		CellSpace target = (CellSpace)map.getFeature(id);
		PrimalSpaceFeatures parent = target.getParent();
		parent.deleteCellSpaceMember(id);
		
		if(target.hasDuality()) {
			State duality = target.getDuality();
			duality.resetDuality();
		}
		map.removeFeature(id);
	}
	public static CellSpace readCellSpace(IndoorGMLMap map, String id) {
		CellSpace target = null;
		try {
			target = (CellSpace)map.getFeature(id);
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		return target;
	}
	public static CellSpace updateCellSpace(IndoorGMLMap map, String parentId, String id, String name, String description, Geometry geometry, String duality, List<String>partialboundedBy) {
		CellSpace result = new CellSpace(map,id);
		CellSpace target = (CellSpace)map.getFeature(id);

		
		PrimalSpaceFeatures parent = target.getParent();

		
		if(!parent.getId().equals(parentId)) {
			parent.deleteCellSpaceMember(id);			
			PrimalSpaceFeatures newParent = new PrimalSpaceFeatures(map, parentId);
			result.setParent(newParent);
			result.getParent().addCellSpaceMember(result);
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
		
		if(geometry != null) {
			result.setGeometry(geometry);
		}
		
		if(duality == null) {
			State d = target.getDuality();
			if(d != null)
				d.resetDuality();
		}
		else{
			if(target.getDuality() != null) {
				if(!target.getDuality().getId().equals(duality)) {
					State oldDuality = target.getDuality();
					oldDuality.resetDuality();				
				}
			}
			
			State newDuality = new State(map,duality);
			result.setDuality(newDuality);
			
		}
		
		if(partialboundedBy != null) {
			List<CellSpaceBoundary>pbb = new ArrayList<CellSpaceBoundary>();
			for(String csbi : partialboundedBy) {
				CellSpaceBoundary temp = new CellSpaceBoundary(map, csbi);
				pbb.add(temp);
			}
			result.setPartialboundedBy(pbb);
		}
		
		map.removeFeature(id);
		map.setFeature(id, "CellSpace", result);
		
		return result;
	}
	public static CellSpace createCellSpace(IndoorGMLMap map, String parentId, String id, String name, String description, Geometry geometry, String duality, List<String> level, List<String> partialBoundedBy) {
		
		if(id == null) {
			id = UUID.randomUUID().toString();
		}
		
		
		CellSpace newFeature = new CellSpace(map, id);
		if(map.hasFutureID(id)){
			newFeature = (CellSpace)map.getFutureFeature(id);
			//map.removeFutureID(id);
		}
		else{
			map.setFutureFeature(id, newFeature);
		}
		map.setFeature(id, "CellSpace", newFeature);
		
		//setting parent(in attribute relationship)
		PrimalSpaceFeatures parent = (PrimalSpaceFeatures) map.getFeature(parentId);
		
		if(parent == null){
			if(map.hasFutureID(parentId)){
				parent = (PrimalSpaceFeatures)map.getFutureFeature(parentId);
			}
			else{
				parent = new PrimalSpaceFeatures(map,parentId);
			}			
		}
		
		if(name != null) {
			newFeature.setName(name);
		}
		
		if(description != null) {
			newFeature.setDescription(description);
		}
		
		//parent.addCellSpaceMember(newFeature);
		List<CellSpace>cellSpaceMember = parent.getCellSpaceMember();
		if(cellSpaceMember == null)
			cellSpaceMember = new ArrayList<CellSpace>();
		
		cellSpaceMember.add(newFeature);
		parent.setCellSpaceMember(cellSpaceMember);
		newFeature.setParent(parent);
		
		if(geometry != null) {
			newFeature.setGeometry(geometry);
		}
		
		if(duality != null){
			State dualityFeature = (State) map.getFeature(duality);
			
			if(dualityFeature == null){
				dualityFeature = new State(map, duality);
			}
			
			dualityFeature.setDuality(newFeature);
			newFeature.setDuality(dualityFeature);

		}
		
		if(partialBoundedBy != null){
			List<CellSpaceBoundary> realPartialBoundedBy = new ArrayList<CellSpaceBoundary>();
			for(String b : partialBoundedBy){
				CellSpaceBoundary pb = (CellSpaceBoundary) map.getFeature(b);
				if(pb == null){
					pb = new CellSpaceBoundary(map,b);
				}
				realPartialBoundedBy.add(pb);
			}
			newFeature.setPartialboundedBy(realPartialBoundedBy);
		}
		if(level != null) {
			List<String> lv = new ArrayList<String>();
			for(String l : level) {
				lv.add(l);
			}
			System.out.println(lv.get(0));
			newFeature.setLevel(lv);			
		}
		map.removeFutureID(id);

		return newFeature;
	}
	public static CellSpace createCellSpace(IndoorGMLMap map, String parentId, String id, String geometry, String duality) {
		if(id == null) {
			id = UUID.randomUUID().toString();
		}
		

		CellSpace newFeature = new CellSpace(map, id);
		if(map.hasFutureID(id)){
			newFeature = (CellSpace)map.getFutureFeature(id);
			//map.removeFutureID(id);
		}
		else{
			map.setFutureFeature(id, newFeature);
		}
		map.setFeature(id, "CellSpace", newFeature);
		
		
		PrimalSpaceFeatures parent = (PrimalSpaceFeatures) map.getFeature(parentId);
		
		if(parent == null){
			if(map.hasFutureID(parentId)){
				parent = (PrimalSpaceFeatures)map.getFutureFeature(parentId);
			}
			else{
				parent = new PrimalSpaceFeatures(map,parentId);
			}			
		}
		
		//parent.addCellSpaceMember(newFeature);
		ArrayList<CellSpace>cellSpaceMember = new ArrayList<CellSpace>();
		cellSpaceMember.add(newFeature);
		parent.setCellSpaceMember(cellSpaceMember);
		newFeature.setParent(parent);
		
		if (geometry != null) {
			WKTReader3D wkt = new WKTReader3D();
			try {
				
				Solid s = (Solid) wkt.read(geometry);
				map.setFeature4Geometry(GeometryUtil.getMetadata(s, "id"), s);
				
				//edu.pnu.stem.util.GeometryUtil.setMetadata(newFeature,"id", UUID.randomUUID().toString());
			
				newFeature.setGeometry(s);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(duality != null){
			State dualityFeature = (State) map.getFeature(duality);
			
			if(dualityFeature == null){
				dualityFeature = new State(map, duality);
			}
			
			dualityFeature.setDuality(newFeature);
			newFeature.setDuality(dualityFeature);

		}
		map.removeFutureID(id);

		return newFeature;
	}
	

	
}
