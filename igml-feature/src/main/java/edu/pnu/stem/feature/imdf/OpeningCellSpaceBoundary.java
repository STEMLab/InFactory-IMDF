package edu.pnu.stem.feature.imdf;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpaceBoundary;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSCONTROLCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSIBILITYCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.OPENINGCATEGORY;

public class OpeningCellSpaceBoundary extends CellSpaceBoundary {

	FeatureInformation featureInformation;
	OPENINGCATEGORY openingCategory;
	ACCESSIBILITYCATEGORY[] accessibilityCategory;
	ACCESSCONTROLCATEGORY[] accessControlCategory;
	String levelId;
	Door door;
	Labels featureName;
	Labels altName;
	String parentId;

	public OpeningCellSpaceBoundary(IndoorGMLMap doc, String id) {
		super(doc, id);
		// TODO Auto-generated constructor stub
	}

	public void setParent(CellSpaceBoundary parent) {
		CellSpaceBoundary found = null;
		found = (CellSpaceBoundary) indoorGMLMap.getFeature(parent.getId());
		if (found == null) {
			indoorGMLMap.setFutureFeature(parent.getId(), parent);
		}
		this.parentId = parent.getId();
	}

	public CellSpaceBoundary getOpeningCellSpaceBoundaryParent() {
		CellSpaceBoundary feature = null;
		feature = (CellSpaceBoundary) indoorGMLMap.getFeature(this.parentId);
		if (feature == null) {
			feature = (CellSpaceBoundary) indoorGMLMap.getFutureFeature(this.parentId);
		}
		return feature;
	}

	public void setFeatureInformation(FeatureInformation featureInformation) {
		this.featureInformation = featureInformation;
	}

	public FeatureInformation getFeatureInformation() {
		return featureInformation;
	}

	public void setOpeningCategory(OPENINGCATEGORY openingCategory) {
		this.openingCategory = openingCategory;
	}

	public OPENINGCATEGORY getOpeningCategory() {
		return openingCategory;
	}

	public void setAccessibilityCategory(ACCESSIBILITYCATEGORY[] accessibilityCategory) {

		this.accessibilityCategory = accessibilityCategory;
	}

	public ACCESSIBILITYCATEGORY[] getAccessibilityCategory() {
		return accessibilityCategory;
	}

	public void setAccessControlCategory(ACCESSCONTROLCATEGORY[] accessControlCategory) {

		this.accessControlCategory = accessControlCategory;
	}

	public ACCESSCONTROLCATEGORY[] getAccessControlCategory() {
		return accessControlCategory;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setDoor(Door door) {
		this.door = door;
	}

	public Door getDoor() {
		return door;
	}

	public void setFeatureName(Labels featureName) {
		this.featureName = featureName;
	}

	public Labels getFeatureName() {
		return featureName;
	}

	public void setAltName(Labels altName) {
		this.altName = altName;
	}

	public Labels getAltName() {
		return altName;
	}

	public void copyCellBoundary(CellSpaceBoundary cb) {
		this.setCellSpace(cb.getCellSpace());

	}

}
