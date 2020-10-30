package edu.pnu.stem.dao;

import java.util.UUID;

import org.locationtech.jts.geom.Geometry;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;
import edu.pnu.stem.feature.core.CellSpaceBoundary;
import edu.pnu.stem.feature.core.PrimalSpaceFeatures;
import edu.pnu.stem.feature.core.Transition;
import edu.pnu.stem.feature.imdf.Door;
import edu.pnu.stem.feature.imdf.FeatureInformation;
import edu.pnu.stem.feature.imdf.Labels;
import edu.pnu.stem.feature.imdf.OpeningCellSpaceBoundary;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSCONTROLCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSIBILITYCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.OPENINGCATEGORY;

public class OpeningCellSpaceBoundaryDAO {

	public static OpeningCellSpaceBoundary readOpeningCellSpaceBoundary(IndoorGMLMap map, String id) {
		OpeningCellSpaceBoundary target = null;
		try {
			target = (OpeningCellSpaceBoundary) map.getFeature(id);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return target;
	}

	public static OpeningCellSpaceBoundary createOpeningCellSpaceBoundary(IndoorGMLMap map, String parentId, String id,
			String name, String description, Geometry geometry, String duality, FeatureInformation featureInformation,
			OPENINGCATEGORY category, ACCESSIBILITYCATEGORY[] accessibility, ACCESSCONTROLCATEGORY[] access_control,
			String level_id, Door door, Labels feature_name, Labels alt_name) {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}

		OpeningCellSpaceBoundary newFeature = new OpeningCellSpaceBoundary(map, id);
		;

		if (map.hasFutureID(id)) {
			// newFeature = (OpeningCellSpaceBoundary) map.getFutureFeature(id);
			newFeature.copyCellBoundary((CellSpaceBoundary) map.getFutureFeature(id));
			// map.removeFutureID(id);
		} else {
			map.setFutureFeature(id, newFeature);
		}

		map.setFeature(id, "OpeningCellSpaceBoundary", newFeature);
		PrimalSpaceFeatures parent = (PrimalSpaceFeatures) map.getFeature(parentId);
		if (parent == null) {
			if (map.hasFutureID(parentId)) {
				parent = (PrimalSpaceFeatures) map.getFutureFeature(parentId);
				map.removeFutureID(parentId);
			} else {
				parent = new PrimalSpaceFeatures(map, parentId);
			}
		}

		parent.addCellSpaceBoundaryMember(newFeature);
		newFeature.setParent(parent);

		if (name != null) {
			newFeature.setName(name);
		}

		if (description != null) {
			newFeature.setDescription(description);
		}

		if (geometry != null) {
			newFeature.setGeometry(geometry);
		}

		if (duality != null) {
			Transition dualityFeature = (Transition) map.getFeature(duality);
			if (dualityFeature == null) {
				dualityFeature = new Transition(map, duality);
				dualityFeature.setDuality(newFeature);
				map.setFutureFeature(duality, dualityFeature);
			} else {
				dualityFeature.setDuality(newFeature);
			}
			newFeature.setDuality(dualityFeature);
		}
		if (featureInformation != null) {
			newFeature.setFeatureInformation(featureInformation);
		}
		if (category != null) {
			newFeature.setOpeningCategory(category);
		}
		if (accessibility != null) {
			newFeature.setAccessibilityCategory(accessibility);
		}
		if (access_control != null) {
			newFeature.setAccessControlCategory(access_control);
		}
		if (level_id != null) {
			newFeature.setLevelId(level_id);
		}
		if (door != null) {
			newFeature.setDoor(door); 
		}
		
		if (feature_name != null) {
			newFeature.setFeatureName(feature_name); 
		}
		if (alt_name != null) {
			newFeature.setAltName(alt_name); 
		}

		return newFeature;
	}

	public static OpeningCellSpaceBoundary updateOpeningCellSpaceBoundary(IndoorGMLMap map, String parentId, String id,
			String name, String description, Geometry geometry, String duality) {
		OpeningCellSpaceBoundary result = new OpeningCellSpaceBoundary(map, id);
		OpeningCellSpaceBoundary target = (OpeningCellSpaceBoundary) map.getFeature(id);

		PrimalSpaceFeatures parent = target.getParent();

		if (!parent.getId().equals(parentId)) {
			parent.deleteCellSpaceBoundaryMember(id);
			PrimalSpaceFeatures newParent = new PrimalSpaceFeatures(map, parentId);
			result.setParent(newParent);
			result.getParent().addCellSpaceBoundaryMember(result);
		} else {
			result.setParent(parent);
		}

		if (name != null) {
			result.setName(name);
		}

		if (description != null) {
			result.setDescription(description);
		}

		if (geometry != null) {
			result.setGeometry(geometry);
		}

		if (duality == null) {
			Transition d = (Transition) target.getDuality();
			if (d != null)
				d.resetDuality();
		} else {
			if (target.getDuality() != null) {
				if (!target.getDuality().getId().equals(duality)) {
					Transition oldDuality = target.getDuality();
					oldDuality.resetDuality();
				}
			}

			Transition newDuality = new Transition(map, duality);
			result.setDuality(newDuality);

		}
		map.removeFeature(id);
		map.setFeature(id, "OpeningCellSpaceBoundary", result);
		return result;

	}

	public static void deleteOpeningCellSpaceBoundary(IndoorGMLMap map, String id) {
		OpeningCellSpaceBoundary target = (OpeningCellSpaceBoundary) map.getFeature(id);
		PrimalSpaceFeatures parent = target.getParent();
		parent.deleteCellSpaceBoundaryMember(id);

		if (target.hasDuality()) {
			Transition duality = target.getDuality();
			duality.resetDuality();
		}

		// cellspace 찾아가서 partialboundedby 해제
		CellSpace cellspaceForPartialBoundedBy = target.getCellSpace();
		cellspaceForPartialBoundedBy.deletePartialBoundedBy(target);

		map.removeFeature(id);
	}
}
