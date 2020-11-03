package edu.pnu.stem.binder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import edu.pnu.stem.feature.core.CellSpace;
import edu.pnu.stem.feature.core.CellSpaceBoundary;
import edu.pnu.stem.feature.core.Edges;
import edu.pnu.stem.feature.core.Envelope;
import edu.pnu.stem.feature.core.ExternalObjectReference;
import edu.pnu.stem.feature.core.ExternalReference;
import edu.pnu.stem.feature.core.IndoorFeatures;
import edu.pnu.stem.feature.core.InterEdges;
import edu.pnu.stem.feature.core.InterLayerConnection;
import edu.pnu.stem.feature.core.MultiLayeredGraph;
import edu.pnu.stem.feature.core.Nodes;
import edu.pnu.stem.feature.core.PrimalSpaceFeatures;
import edu.pnu.stem.feature.core.SpaceLayer;
import edu.pnu.stem.feature.core.SpaceLayers;
import edu.pnu.stem.feature.core.State;
import edu.pnu.stem.feature.core.Transition;
import edu.pnu.stem.feature.imdf.AmenityState;
import edu.pnu.stem.feature.imdf.AnchorState;
import edu.pnu.stem.feature.imdf.BuildingState;
import edu.pnu.stem.feature.imdf.FixtureSpace;
import edu.pnu.stem.feature.imdf.FootPrintSpace;
import edu.pnu.stem.feature.imdf.GeofenceSpace;
import edu.pnu.stem.feature.imdf.IndoorFeaturesForIMDF;
import edu.pnu.stem.feature.imdf.KioskSpace;
import edu.pnu.stem.feature.imdf.LevelSpace;
import edu.pnu.stem.feature.imdf.Occupant;
import edu.pnu.stem.feature.imdf.OpeningCellSpaceBoundary;
import edu.pnu.stem.feature.imdf.Relationship;
import edu.pnu.stem.feature.imdf.SectionSpace;
import edu.pnu.stem.feature.imdf.UnitSpace;
import edu.pnu.stem.feature.imdf.VenueSpace;
import edu.pnu.stem.feature.navigation.AnchorBoundary;
import edu.pnu.stem.feature.navigation.AnchorSpace;
import edu.pnu.stem.feature.navigation.ConnectionBoundary;
import edu.pnu.stem.feature.navigation.ConnectionSpace;
import edu.pnu.stem.feature.navigation.GeneralSpace;
import edu.pnu.stem.feature.navigation.TransitionSpace;
import edu.pnu.stem.geometry.jts.Solid;
import net.opengis.gml.v_3_2_1.BoundingShapeType;
import net.opengis.gml.v_3_2_1.CodeType;
import net.opengis.gml.v_3_2_1.CurvePropertyType;
import net.opengis.gml.v_3_2_1.EnvelopeType;
import net.opengis.gml.v_3_2_1.LineStringType;
import net.opengis.gml.v_3_2_1.PointPropertyType;
import net.opengis.gml.v_3_2_1.PointType;
import net.opengis.gml.v_3_2_1.PolygonType;
import net.opengis.gml.v_3_2_1.SolidPropertyType;
import net.opengis.gml.v_3_2_1.SolidType;
import net.opengis.gml.v_3_2_1.StringOrRefType;
import net.opengis.gml.v_3_2_1.SurfacePropertyType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryGeometryType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryMemberType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryPropertyType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceGeometryType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceMemberType;
import net.opengis.indoorgml.core.v_1_0.CellSpacePropertyType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceType;
import net.opengis.indoorgml.core.v_1_0.EdgesType;
import net.opengis.indoorgml.core.v_1_0.ExternalObjectReferenceType;
import net.opengis.indoorgml.core.v_1_0.ExternalReferenceType;
import net.opengis.indoorgml.core.v_1_0.IndoorFeaturesType;
import net.opengis.indoorgml.core.v_1_0.InterEdgesType;
import net.opengis.indoorgml.core.v_1_0.InterLayerConnectionMemberType;
import net.opengis.indoorgml.core.v_1_0.InterLayerConnectionType;
import net.opengis.indoorgml.core.v_1_0.MultiLayeredGraphPropertyType;
import net.opengis.indoorgml.core.v_1_0.MultiLayeredGraphType;
import net.opengis.indoorgml.core.v_1_0.NodesType;
import net.opengis.indoorgml.core.v_1_0.PrimalSpaceFeaturesPropertyType;
import net.opengis.indoorgml.core.v_1_0.PrimalSpaceFeaturesType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayerMemberType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayerPropertyType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayerType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayersType;
import net.opengis.indoorgml.core.v_1_0.StateMemberType;
import net.opengis.indoorgml.core.v_1_0.StatePropertyType;
import net.opengis.indoorgml.core.v_1_0.StateType;
import net.opengis.indoorgml.core.v_1_0.TransitionMemberType;
import net.opengis.indoorgml.core.v_1_0.TransitionPropertyType;
import net.opengis.indoorgml.core.v_1_0.TransitionType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSCONTROLCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSIBILITYCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.AddressType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.AmenityStateType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.AnchorStateType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.BuildingStateType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.DoorType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FeatureInformationType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FixtureSpaceType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FootPrintSpaceType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.GeofenceSpaceType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.IndoorFeaturesForIMDFType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.KioskSpaceType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.LabelsType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.LevelSpaceType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ManifestType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.OccupantType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.OpeningCellSpaceBoundaryType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.RelationshipType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.SectionSpaceType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.TEMPORALITYType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.UnitSpaceType;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.VenueSpaceType;
import net.opengis.indoorgml.navigation.v_1_0.AnchorBoundaryType;
import net.opengis.indoorgml.navigation.v_1_0.AnchorSpaceType;
import net.opengis.indoorgml.navigation.v_1_0.ConnectionBoundaryType;
import net.opengis.indoorgml.navigation.v_1_0.ConnectionSpaceType;
import net.opengis.indoorgml.navigation.v_1_0.GeneralSpaceType;
import net.opengis.indoorgml.navigation.v_1_0.ObjectFactory;
import net.opengis.indoorgml.navigation.v_1_0.TransitionSpaceType;

public class Convert2JaxbClass {
	static net.opengis.indoorgml.core.v_1_0.ObjectFactory indoorgmlcoreOF = new net.opengis.indoorgml.core.v_1_0.ObjectFactory();

	static net.opengis.gml.v_3_2_1.ObjectFactory gmlOF = new net.opengis.gml.v_3_2_1.ObjectFactory();

	static ObjectFactory indoorgmlnaviOF = new net.opengis.indoorgml.navigation.v_1_0.ObjectFactory();

	static net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ObjectFactory imdfOF = new net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ObjectFactory();

	@SuppressWarnings("unchecked")
	public static CellSpaceType change2JaxbClass(IndoorGMLMap savedMap, CellSpace feature) throws JAXBException {

		CellSpaceType newFeature = indoorgmlcoreOF.createCellSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}
		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}
		List<String> lv = new ArrayList<String>();
		if (feature.getLevel() != null) {
			for (int i = 0; i < feature.getLevel().size(); i++) {
				lv.add(feature.getLevel().get(i));
			}
			newFeature.setLevel(lv);
		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static CellSpaceBoundaryType change2JaxbClass(IndoorGMLMap savedMap, CellSpaceBoundary feature) {
		CellSpaceBoundaryType newFeature = indoorgmlcoreOF.createCellSpaceBoundaryType();
		TransitionPropertyType duality = new TransitionPropertyType();
		newFeature.setId(feature.getId());

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
			newFeature.setId(feature.getId());
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF
						.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry3D(polygonProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			} else if (geom instanceof LineString) {
				LineString l = (LineString) geom;
				LineStringType linestring = Convert2JaxbGeometry.Convert2LineStringType(l);
				JAXBElement<LineStringType> jaxbLineString = gmlOF.createLineString(linestring);
				CurvePropertyType lineProp = gmlOF.createCurvePropertyType();
				lineProp.setAbstractCurve(jaxbLineString);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF
						.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry2D(lineProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			}
		}

		return newFeature;
	}

	public static EdgesType change2JaxbClass(IndoorGMLMap savedMap, Edges p) throws JAXBException {
		EdgesType newFeature = indoorgmlcoreOF.createEdgesType();

		newFeature.setId(p.getId());

		List<TransitionMemberType> transitionmember = new ArrayList<TransitionMemberType>();

		if (p.getTransitionMember() != null) {
			for (int j = 0; j < p.getTransitionMember().size(); j++) {

				String tempId = p.getTransitionMember().get(j).getId();
				Transition tempsl = (Transition) savedMap.getFeature(tempId);
				JAXBElement<TransitionType> temp = indoorgmlcoreOF.createTransition(change2JaxbClass(savedMap, tempsl));
				TransitionMemberType temptm = new TransitionMemberType();
				temptm.setTransition(temp);
				transitionmember.add(temptm);

			}
			newFeature.setTransitionMember(transitionmember);

		}

		// newFeature.setBoundedBy(feature.);

		return newFeature;
	}

	ExternalObjectReferenceType change2JaxbClass(ExternalObjectReference feature) {
		ExternalObjectReferenceType newFeature = new ExternalObjectReferenceType();
		newFeature.setUri(feature.getUri());

		return newFeature;

	}

	ExternalReferenceType change2JaxbClass(ExternalReference feature) {
		ExternalReferenceType newFeature = new ExternalReferenceType();

		newFeature.setExternalObject(change2JaxbClass(feature.externalObject));
		// TODO:change externalObjectReference
		return newFeature;
	}

	static public IndoorFeaturesType change2JaxbClass(IndoorGMLMap savedMap, IndoorFeatures feature)
			throws JAXBException {
		IndoorFeaturesType newFeature = new IndoorFeaturesType();
		newFeature.setId(feature.getId());

		if (feature.getBoundedBy() != null) {
			Envelope e = (Envelope) savedMap.getFeature(feature.getBoundedBy().getId());
			JAXBElement<EnvelopeType> jaxbEnvelope = gmlOF.createEnvelope(change2JaxbClass(savedMap, e));
			BoundingShapeType bs = gmlOF.createBoundingShapeType();
			bs.setEnvelope(jaxbEnvelope);
			newFeature.setBoundedBy(bs);

		}
		if (feature.getPrimalSpaceFeatures() != null) {
			// Convert2FeatureClass.docContainer.
			PrimalSpaceFeatures p = (PrimalSpaceFeatures) savedMap.getFeature(feature.getPrimalSpaceFeatures().getId());
			PrimalSpaceFeaturesPropertyType pp = indoorgmlcoreOF.createPrimalSpaceFeaturesPropertyType();
			pp.setPrimalSpaceFeatures(change2JaxbClass(savedMap, p));
			newFeature.setPrimalSpaceFeatures(pp);
		}
		if (feature.getMultiLayeredGraph() != null) {
			MultiLayeredGraph m = (MultiLayeredGraph) savedMap.getFeature(feature.getMultiLayeredGraph().getId());
			MultiLayeredGraphPropertyType mp = indoorgmlcoreOF.createMultiLayeredGraphPropertyType();
			mp.setMultiLayeredGraph(change2JaxbClass(savedMap, m));
			newFeature.setMultiLayeredGraph(mp);
		}

		return newFeature;
	}

	static public IndoorFeaturesForIMDFType change2JaxbClass(IndoorGMLMap savedMap, IndoorFeaturesForIMDF feature)
			throws JAXBException {
		IndoorFeaturesForIMDFType newFeature = new IndoorFeaturesForIMDFType();
		newFeature.setId(feature.getId());

		if (feature.getBoundedBy() != null) {
			Envelope e = (Envelope) savedMap.getFeature(feature.getBoundedBy().getId());
			JAXBElement<EnvelopeType> jaxbEnvelope = gmlOF.createEnvelope(change2JaxbClass(savedMap, e));
			BoundingShapeType bs = gmlOF.createBoundingShapeType();
			bs.setEnvelope(jaxbEnvelope);
			newFeature.setBoundedBy(bs);

		}
		if (feature.getPrimalSpaceFeatures() != null) {
			// Convert2FeatureClass.docContainer.
			PrimalSpaceFeatures p = (PrimalSpaceFeatures) savedMap.getFeature(feature.getPrimalSpaceFeatures().getId());
			PrimalSpaceFeaturesPropertyType pp = indoorgmlcoreOF.createPrimalSpaceFeaturesPropertyType();
			pp.setPrimalSpaceFeatures(change2JaxbClass(savedMap, p));
			newFeature.setPrimalSpaceFeatures(pp);
		}
		if (feature.getMultiLayeredGraph() != null) {
			MultiLayeredGraph m = (MultiLayeredGraph) savedMap.getFeature(feature.getMultiLayeredGraph().getId());
			MultiLayeredGraphPropertyType mp = indoorgmlcoreOF.createMultiLayeredGraphPropertyType();
			mp.setMultiLayeredGraph(change2JaxbClass(savedMap, m));
			newFeature.setMultiLayeredGraph(mp);
		}
		if (feature.getMultiLayeredGraph() != null) {
			MultiLayeredGraph m = (MultiLayeredGraph) savedMap.getFeature(feature.getMultiLayeredGraph().getId());
			MultiLayeredGraphPropertyType mp = indoorgmlcoreOF.createMultiLayeredGraphPropertyType();
			mp.setMultiLayeredGraph(change2JaxbClass(savedMap, m));
			newFeature.setMultiLayeredGraph(mp);
		}
		if (feature.getManifest() != null) {
			ManifestType manifestType = new ManifestType();

			manifestType.setVersion(feature.getManifest().getVersion());
			manifestType.setCreated(feature.getManifest().getCreated());
			manifestType.setGeneratedBy(feature.getManifest().getGeneratedBy());
			manifestType.setLanguage(feature.getManifest().getLanguage());
			manifestType.setExtensions(feature.getManifest().getExtension());

			newFeature.setManifest(manifestType);
		}
		if (feature.getAddress() != null) {

			AddressType addressType = new AddressType();

			FeatureInformationType featureInformationType = new FeatureInformationType();
			featureInformationType.setId(feature.getAddress().getFeatureInformation().getId());
			featureInformationType.setType(feature.getAddress().getFeatureInformation().getType());
			featureInformationType.setFeatureType(feature.getAddress().getFeatureInformation().getFeatureType());
			featureInformationType.setGmlGeometry(feature.getAddress().getFeatureInformation().getGmlGeometry());

			addressType.setFeatureInformation(featureInformationType);

			addressType.setAddress(feature.getAddress().getAddress());
			addressType.setUnit(feature.getAddress().getUnit());
			addressType.setLocality(feature.getAddress().getLocality());
			addressType.setProvince(feature.getAddress().getProvince());
			addressType.setCountry(feature.getAddress().getCountry());
			addressType.setPostalCode(feature.getAddress().getPostalCode());
			addressType.setPostalCodeExt(feature.getAddress().getPostalCodeExt());
			addressType.setPostalCodeVanity(feature.getAddress().getPostalCodeVanity());

			newFeature.setAddress(addressType);
		}
		if (feature.getOccupant() != null) {
			ArrayList<Occupant> occupantList = feature.getOccupant();
			ArrayList<OccupantType> occupantTypeList = new ArrayList<OccupantType>();

			for (int i = 0; i < feature.getOccupant().size(); i++) {

				OccupantType occupantType = new OccupantType();

				if (feature.getOccupant().get(i).getFeatureInformation() != null) {

					FeatureInformationType featureInformationType = new FeatureInformationType();

					if (occupantList.get(i).getFeatureInformation().getId() != null) {
						featureInformationType.setId(occupantList.get(i).getFeatureInformation().getId());
						System.out.print("featureID:" + occupantList.get(i).getFeatureInformation().getId());
					}
					if (occupantList.get(i).getFeatureInformation().getType() != null) {
						featureInformationType.setType(occupantList.get(i).getFeatureInformation().getType());
					}
					if (occupantList.get(i).getFeatureInformation().getFeatureType() != null) {
						featureInformationType
								.setFeatureType(occupantList.get(i).getFeatureInformation().getFeatureType());
					}
					if (occupantList.get(i).getFeatureInformation().getGmlGeometry() != null) {
						featureInformationType
								.setGmlGeometry(occupantList.get(i).getFeatureInformation().getGmlGeometry());
					}
					occupantType.setFeatureInformation(featureInformationType);
					
				}
				if (occupantList.get(i).getCategory() != null) {
					occupantType.setCategory(occupantList.get(i).getCategory());

				}
				if (occupantList.get(i).getAnchorId() != null) {
					occupantType.setAnchorId(occupantList.get(i).getAnchorId());

				}
				if (occupantList.get(i).getHours() != null) {
					occupantType.setHours(occupantList.get(i).getHours());

				}
				if (occupantList.get(i).getOrdinal() != null) {
					occupantType.setOrdinal(occupantList.get(i).getOrdinal());
				}
				if (occupantList.get(i).getPhone() != null) {

//					List<String> phones = new ArrayList<String>();
//
//					for (String phone : occupantList.get(i).getPhone()) {
//						phones.add(phone);
//
//					}
					occupantType.setPhone(occupantList.get(i).getPhone());
				}
				if (occupantList.get(i).getWebsite() != null) {

					occupantType.setWebsite(occupantList.get(i).getWebsite());
				}
				if (occupantList.get(i).getValidity() != null) {

					TEMPORALITYType t = new TEMPORALITYType();
					t.setStart(occupantList.get(i).getValidity().getStart());
					t.setModified(occupantList.get(i).getValidity().getModified());
					t.setEnd(occupantList.get(i).getValidity().getEnd());
					occupantType.setValidity(t);
				}
				if (occupantList.get(i).getCorrelationId() != null) {

//					LabelsType labelsType = new LabelsType();
//					labelsType.setLanguage(occupantList.get(i).getCorrelationId().getLanguage());
//					labelsType.setName(occupantList.get(i).getCorrelationId().getName());

					occupantType.setCorrelationId(occupantList.get(i).getCorrelationId());
				}

				occupantTypeList.add(occupantType);

			}
			newFeature.setOccupant(occupantTypeList);

		}
		if (feature.getRelationship() != null) {
			ArrayList<Relationship> relationshipList = feature.getRelationship();
			ArrayList<RelationshipType> relationshipTypeList = new ArrayList<RelationshipType>();

			for (int i = 0; i < relationshipList.size(); i++) {
				RelationshipType relationshipType = new RelationshipType();

				if (relationshipList.get(i).getFeatureInformation() != null) {

					FeatureInformationType featureInformationType = new FeatureInformationType();

					if (relationshipList.get(i).getFeatureInformation().getId() != null) {
						featureInformationType.setId(relationshipList.get(i).getFeatureInformation().getId());

					}
					if (relationshipList.get(i).getFeatureInformation().getType() != null) {
						featureInformationType.setType(relationshipList.get(i).getFeatureInformation().getType());
					}
					if (relationshipList.get(i).getFeatureInformation().getFeatureType() != null) {
						featureInformationType
								.setFeatureType(relationshipList.get(i).getFeatureInformation().getFeatureType());
					}
					if (relationshipList.get(i).getFeatureInformation().getGmlGeometry() != null) {
						featureInformationType
								.setGmlGeometry(relationshipList.get(i).getFeatureInformation().getGmlGeometry());
					}
					relationshipType.setFeatureInformation(featureInformationType);
				}
				if (relationshipList.get(i).getCategory() != null) {
					relationshipType.setCategory(relationshipList.get(i).getCategory());
				}
				if (relationshipList.get(i).getDirection() != null) {
					relationshipType.setDirection(relationshipList.get(i).getDirection());

				}
				if (relationshipList.get(i).getHours() != null) {
					relationshipType.setHours(relationshipList.get(i).getHours());

				}
				if (relationshipList.get(i).getDestination() != null) {
					relationshipType.setDestination(relationshipList.get(i).getDestination());

				}
				if (relationshipList.get(i).getOrigin() != null) {
					relationshipType.setOrigin(relationshipList.get(i).getOrigin());
				}
				if (relationshipList.get(i).getIntermediary() != null) {
					relationshipType.setIntermediary(relationshipList.get(i).getIntermediary());

				}
				relationshipTypeList.add(relationshipType);

			}
			newFeature.setRealationship(relationshipTypeList);

		}

		return newFeature;
	}

	static public EnvelopeType change2JaxbClass(IndoorGMLMap savedMap, Envelope feature) throws JAXBException {
		EnvelopeType newFeature = new EnvelopeType();

		Point low = (Point) feature.getLowerCorner();
		Point upper = (Point) feature.getUpperCorner();
		if (low != null) {
			PointType point = Convert2JaxbGeometry.Convert2PointType(low);
			newFeature.setLowerCorner(point.getPos());
		}
		if (upper != null) {
			PointType point = Convert2JaxbGeometry.Convert2PointType(upper);

			newFeature.setUpperCorner(point.getPos());
		}
		newFeature.setSrsName(feature.getSrsName());
		newFeature.setSrsDimension(feature.getSrsDimension());

		return newFeature;
	}

	private static MultiLayeredGraphType change2JaxbClass(IndoorGMLMap savedMap, MultiLayeredGraph feature)
			throws JAXBException {
		MultiLayeredGraphType newFeature = new MultiLayeredGraphType();
		newFeature.setId(feature.getId());

		List<SpaceLayersType> spaceLayers = new ArrayList<SpaceLayersType>();
		List<InterEdgesType> interEdges = new ArrayList<InterEdgesType>();
		if (feature.getSpaceLayers() != null) {
			for (int i = 0; i < feature.getSpaceLayers().size(); i++) {
				String tempId = feature.getSpaceLayers().get(i).getId();
				SpaceLayers tempsl = (SpaceLayers) savedMap.getFeature(tempId);
				SpaceLayersType temp = change2JaxbClass(savedMap, tempsl);
				spaceLayers.add(temp);
			}
			newFeature.setSpaceLayers(spaceLayers);
		}

		if (feature.getInterEdges() != null) {
			for (int i = 0; i < feature.getInterEdges().size(); i++) {
				InterEdges tempie = (InterEdges) savedMap.getFeature(feature.getInterEdges().get(i).getId());
				InterEdgesType temp = change2JaxbClass(savedMap, tempie);
				interEdges.add(temp);
			}
			newFeature.setInterEdges(interEdges);
		}

		return newFeature;
	}

	private static InterEdgesType change2JaxbClass(IndoorGMLMap savedMap, InterEdges feature) {
		InterEdgesType newFeature = indoorgmlcoreOF.createInterEdgesType();
		newFeature.setId(feature.getId());
		List<InterLayerConnectionMemberType> interlayerconnectionmember = new ArrayList<InterLayerConnectionMemberType>();

		if (feature.getInterLayerConnectionMember() != null) {
			for (int i = 0; i < feature.getInterLayerConnectionMember().size(); i++) {
				InterLayerConnection tempilc = (InterLayerConnection) savedMap
						.getFeature(feature.getInterLayerConnectionMember().get(i).getId());

				InterLayerConnectionType temp = change2JaxbClass(savedMap, tempilc);

				InterLayerConnectionMemberType tempmember = indoorgmlcoreOF.createInterLayerConnectionMemberType();

				tempmember.setInterLayerConnection(temp);

				interlayerconnectionmember.add(tempmember);
			}
			newFeature.setInterLayerConnectionMember(interlayerconnectionmember);

		}

		return newFeature;
	}

	private static InterLayerConnectionType change2JaxbClass(IndoorGMLMap savedMap, InterLayerConnection feature) {
		InterLayerConnectionType newFeature = indoorgmlcoreOF.createInterLayerConnectionType();

		newFeature.setId(feature.getId());
		List<StatePropertyType> interConnects = new ArrayList<StatePropertyType>();
		List<SpaceLayerPropertyType> connectedLayer = new ArrayList<SpaceLayerPropertyType>();

		if (feature.getInterConnects() != null) {
			StatePropertyType temp = indoorgmlcoreOF.createStatePropertyType();

			for (int i = 0; i < feature.getInterConnects().length; i++) {

				String href = feature.getInterConnects()[i].getId();
				href = "#" + href;
				temp.setHref(href);
				interConnects.add(temp);
			}
		}

		if (feature.getConnectedLayers() != null) {

			SpaceLayerPropertyType temp = indoorgmlcoreOF.createSpaceLayerPropertyType();

			for (int i = 0; i < feature.getConnectedLayers().length; i++) {
				if (feature.getConnectedLayers()[i] != null) {
					String str = feature.getConnectedLayers()[i].getId();
					temp.setHref("#" + str);
					connectedLayer.add(temp);

				} else {
					System.out.println("Creating" + feature.getId() + "is fail.");
				}
			}
		}
		if (feature.getTypeOfTopoExpression() != null) {

			newFeature.setTypeOfTopoExpression(feature.getTypeOfTopoExpression().type.toString());
		}

		newFeature.setConnectedLayers(connectedLayer);
		newFeature.setInterConnects(interConnects);

		return newFeature;
	}

	static SpaceLayersType change2JaxbClass(IndoorGMLMap savedMap, SpaceLayers feature) throws JAXBException {
		SpaceLayersType newFeature = new SpaceLayersType();

		newFeature.setId(feature.getId());
		List<SpaceLayerMemberType> spaceLayerMember = new ArrayList<SpaceLayerMemberType>();

		if (feature.getSpaceLayerMember() != null) {
			for (int i = 0; i < feature.getSpaceLayerMember().size(); i++) {
				String tempId = feature.getSpaceLayerMember().get(i).getId();
				SpaceLayer tempsl = (SpaceLayer) savedMap.getFeature(tempId);
				SpaceLayerType temp = change2JaxbClass(savedMap, tempsl);
				SpaceLayerMemberType tempsm = new SpaceLayerMemberType();
				tempsm.setSpaceLayer(temp);
				spaceLayerMember.add(tempsm);
			}
			newFeature.setSpaceLayerMember(spaceLayerMember);

		}

		return newFeature;
	}

	private static SpaceLayerType change2JaxbClass(IndoorGMLMap savedMap, SpaceLayer feature) throws JAXBException {
		SpaceLayerType newFeature = new SpaceLayerType();
		newFeature.setId(feature.getId());

		List<EdgesType> edgesTypeList = new ArrayList<EdgesType>();

		// node 들고와서
		// node 밑의 statemember 찾아서
		// 걔 까지만 association으로.

		List<NodesType> nodesTypeList = new ArrayList<NodesType>();

		for (int i = 0; i < feature.getNodes().size(); i++) {
			Nodes tempnodes = (Nodes) savedMap.getFeature(feature.getNodes().get(i).getId());
			NodesType tempnodestype = change2JaxbClass(savedMap, tempnodes);
			nodesTypeList.add(tempnodestype);
		}
		newFeature.setNodes(nodesTypeList);

		if (feature.getEdges() != null) {
			for (int i = 0; i < feature.getEdges().size(); i++) {
				Edges tempEdge = (Edges) savedMap.getFeature(feature.getEdges().get(i).getId());
				EdgesType tempEdgesType = change2JaxbClass(savedMap, tempEdge);
				edgesTypeList.add(tempEdgesType);
			}
			newFeature.setEdges(edgesTypeList);
		}

		return newFeature;
	}

	private static NodesType change2JaxbClass(IndoorGMLMap savedMap, Nodes feature) throws JAXBException {
		NodesType newFeature = new NodesType();

		newFeature.setId(feature.getId());

		List<StateMemberType> smTypeList = new ArrayList<StateMemberType>();

		if (feature.getStateMember() != null) {
			for (int i = 0; i < feature.getStateMember().size(); i++) {
				if (feature.getStateMember().get(i).getClass().getSimpleName().equals("AmenityState")) {
					AmenityState tempstate = (AmenityState) savedMap
							.getFeature(feature.getStateMember().get(i).getId());
					JAXBElement<AmenityStateType> jaxbState = imdfOF
							.createAmenityState(change2JaxbClass(savedMap, tempstate));
					StateMemberType tempstatemember = indoorgmlcoreOF.createStateMemberType();
					tempstatemember.setState(jaxbState);
					smTypeList.add(tempstatemember);
				} else if (feature.getStateMember().get(i).getClass().getSimpleName().equals("AnchorState")) {
					AnchorState tempstate = (AnchorState) savedMap.getFeature(feature.getStateMember().get(i).getId());
					JAXBElement<AnchorStateType> jaxbState = imdfOF
							.createAnchorState(change2JaxbClass(savedMap, tempstate));
					StateMemberType tempstatemember = indoorgmlcoreOF.createStateMemberType();
					tempstatemember.setState(jaxbState);
					smTypeList.add(tempstatemember);

				} else if (feature.getStateMember().get(i).getClass().getSimpleName().equals("BuildingState")) {
					BuildingState tempstate = (BuildingState) savedMap
							.getFeature(feature.getStateMember().get(i).getId());
					JAXBElement<BuildingStateType> jaxbState = imdfOF
							.createBuildingState(change2JaxbClass(savedMap, tempstate));
					StateMemberType tempstatemember = indoorgmlcoreOF.createStateMemberType();
					tempstatemember.setState(jaxbState);
					smTypeList.add(tempstatemember);

				} else {
					State tempstate = (State) savedMap.getFeature(feature.getStateMember().get(i).getId());
					StateType tempstatetype = change2JaxbClass(savedMap, tempstate);
					JAXBElement<StateType> jaxbState = indoorgmlcoreOF.createState(tempstatetype);
					StateMemberType tempstatemember = indoorgmlcoreOF.createStateMemberType();
					tempstatemember.setState(jaxbState);
					smTypeList.add(tempstatemember);

				}

			}

			newFeature.setStateMember(smTypeList);

		}

		return newFeature;
	}

	static PrimalSpaceFeaturesType change2JaxbClass(IndoorGMLMap savedMap, PrimalSpaceFeatures feature)
			throws JAXBException {
		PrimalSpaceFeaturesType newFeature = new PrimalSpaceFeaturesType();
		newFeature.setId(feature.getId());

		List<CellSpaceMemberType> cellspacemember = new ArrayList<CellSpaceMemberType>();
		List<CellSpaceBoundaryMemberType> cellspaceboundarymember = new ArrayList<CellSpaceBoundaryMemberType>();

		if (feature.getCellSpaceMember() != null) {
			for (int i = 0; i < feature.getCellSpaceMember().size(); i++) {

				if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("FixtureSpace")) {

					FixtureSpace tempcellspace = (FixtureSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember
							.setCellSpace(imdfOF.createFixtureSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);

				} else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("FootPrintSpace")) {

					FootPrintSpace tempcellspace = (FootPrintSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember
							.setCellSpace(imdfOF.createFootPrintSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);

				} else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("GeofenceSpace")) {

					GeofenceSpace tempcellspace = (GeofenceSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember
							.setCellSpace(imdfOF.createGeofenceSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);

				} else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("KioskSpace")) {

					KioskSpace tempcellspace = (KioskSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember
							.setCellSpace(imdfOF.createKioskSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);

				} else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("LevelSpace")) {

					LevelSpace tempcellspace = (LevelSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember
							.setCellSpace(imdfOF.createLevelSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);

				} else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("SectionSpace")) {

					SectionSpace tempcellspace = (SectionSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember
							.setCellSpace(imdfOF.createSectionSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);

				} else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("UnitSpace")) {

					UnitSpace tempcellspace = (UnitSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember.setCellSpace(imdfOF.createUnitSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);

				} else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("VenueSpace")) {

					VenueSpace tempcellspace = (VenueSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember
							.setCellSpace(imdfOF.createVenueSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);

				} else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("GeneralSpace")) {

					GeneralSpace tempcellspace = (GeneralSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember.setCellSpace(
							indoorgmlnaviOF.createGeneralSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);

				} else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("TransitionSpace")) {

					TransitionSpace tempcellspace = (TransitionSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember.setCellSpace(
							indoorgmlnaviOF.createTransitionSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);
				} else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("ConnectionSpace")) {

					ConnectionSpace tempcellspace = (ConnectionSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember.setCellSpace(
							indoorgmlnaviOF.createConnectionSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);

				} else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("AnchorSpace")) {

					AnchorSpace tempcellspace = (AnchorSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember
							.setCellSpace(indoorgmlnaviOF.createAnchorSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);

				} else {

					CellSpace tempcellspace = (CellSpace) savedMap
							.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempcellspacemember
							.setCellSpace(indoorgmlcoreOF.createCellSpace(change2JaxbClass(savedMap, tempcellspace)));
					cellspacemember.add(tempcellspacemember);
				}

			}
			newFeature.setCellSpaceMember(cellspacemember);
		}

		if (feature.getCellSpaceBoundaryMember() != null) {
			for (int i = 0; i < feature.getCellSpaceBoundaryMember().size(); i++) {
				if (feature.getCellSpaceBoundaryMember().get(i).getClass().getSimpleName()
						.equals("OpeningCellSpaceBoundary")) {

					OpeningCellSpaceBoundary tempcellspace = (OpeningCellSpaceBoundary) savedMap
							.getFeature(feature.getCellSpaceBoundaryMember().get(i).getId());
					CellSpaceBoundaryMemberType tempcellspacemember = indoorgmlcoreOF
							.createCellSpaceBoundaryMemberType();
					tempcellspacemember.setCellSpaceBoundary(
							imdfOF.createOpeningCellSpaceBoundary(change2JaxbClass(savedMap, tempcellspace)));
					cellspaceboundarymember.add(tempcellspacemember);
				}

				else if (feature.getCellSpaceBoundaryMember().get(i).getClass().getSimpleName()
						.equals("AnchorBoundary")) {

					AnchorBoundary tempcellspace = (AnchorBoundary) savedMap
							.getFeature(feature.getCellSpaceBoundaryMember().get(i).getId());
					CellSpaceBoundaryMemberType tempcellspacemember = indoorgmlcoreOF
							.createCellSpaceBoundaryMemberType();
					tempcellspacemember.setCellSpaceBoundary(
							indoorgmlnaviOF.createAnchorBoundary(change2JaxbClass(savedMap, tempcellspace)));
					cellspaceboundarymember.add(tempcellspacemember);
				} else if (feature.getCellSpaceBoundaryMember().get(i).getClass().getSimpleName()
						.equals("ConnectionBoundary")) {

					ConnectionBoundary tempcellspace = (ConnectionBoundary) savedMap
							.getFeature(feature.getCellSpaceBoundaryMember().get(i).getId());
					CellSpaceBoundaryMemberType tempcellspacemember = indoorgmlcoreOF
							.createCellSpaceBoundaryMemberType();
					tempcellspacemember.setCellSpaceBoundary(
							indoorgmlnaviOF.createConnectionBoundary(change2JaxbClass(savedMap, tempcellspace)));
					cellspaceboundarymember.add(tempcellspacemember);
				} else {

					CellSpaceBoundary tempcellspace = (CellSpaceBoundary) savedMap
							.getFeature(feature.getCellSpaceBoundaryMember().get(i).getId());
					CellSpaceBoundaryMemberType tempcellspacemember = indoorgmlcoreOF
							.createCellSpaceBoundaryMemberType();
					tempcellspacemember.setCellSpaceBoundary(
							indoorgmlcoreOF.createCellSpaceBoundary(change2JaxbClass(savedMap, tempcellspace)));
					cellspaceboundarymember.add(tempcellspacemember);
				}

			}
			newFeature.setCellSpaceBoundaryMember(cellspaceboundarymember);
		}

		// TODO Auto-generated method stub
		return newFeature;
	}

	static StateType change2JaxbClass(IndoorGMLMap savedMap, State feature) throws JAXBException {
		StateType newFeature = new StateType();

		List<TransitionPropertyType> connects = new ArrayList<TransitionPropertyType>();

		if (feature.getConnects() != null) {
			for (int i = 0; i < feature.getConnects().size(); i++) {
				TransitionPropertyType tempTransitionPropertyType = new TransitionPropertyType();
				String href = feature.getConnects().get(i).getId();
				href = "#" + href;
				tempTransitionPropertyType.setHref(href);
				connects.add(tempTransitionPropertyType);
			}
			newFeature.setConnects(connects);
		}

		Point geom = (Point) feature.getGeometry();
		if (geom != null) {
			PointType point = Convert2JaxbGeometry.Convert2PointType(geom);
			PointPropertyType pointProp = gmlOF.createPointPropertyType();
			pointProp.setPoint(point);
			newFeature.setGeometry(pointProp);
		}

		if (feature.getDuality() != null) {
			CellSpacePropertyType duality = indoorgmlcoreOF.createCellSpacePropertyType();
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}
		// feature.geometry

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		newFeature.setId(feature.getId());
		return newFeature;
	}

	static TransitionType change2JaxbClass(IndoorGMLMap savedMap, Transition feature) throws JAXBException {
		TransitionType newFeature = new TransitionType();
		// CurveType tempCurve = feature.geometry;
		newFeature.setId(feature.getId());

		List<StatePropertyType> connects = new ArrayList<StatePropertyType>();

		if (feature.getConnects() != null) {
			for (int i = 0; i < feature.getConnects().length; i++) {
				StatePropertyType temp = indoorgmlcoreOF.createStatePropertyType();
				String href = feature.getConnects()[i].getId();
				href = "#" + href;
				temp.setHref(href);
				connects.add(temp);
			}
			newFeature.setConnects(connects);
		}

		LineString geom = (LineString) feature.getGeometry();
		if (geom != null) {
			LineStringType linestring = Convert2JaxbGeometry.Convert2LineStringType(geom);
			CurvePropertyType curveProperty = gmlOF.createCurvePropertyType();
			curveProperty.setAbstractCurve(gmlOF.createLineString(linestring));
			newFeature.setGeometry(curveProperty);
		}

		if (feature.getDuality() != null) {
			CellSpaceBoundaryPropertyType duality = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		newFeature.setWeight(feature.getWeight());

		return newFeature;

	}

	public static GeneralSpaceType change2JaxbClass(IndoorGMLMap savedMap, GeneralSpace feature) throws JAXBException {

		GeneralSpaceType newFeature = indoorgmlnaviOF.createGeneralSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}
		if (feature.getClassType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getClassType());
			newFeature.setClazz(e);

		}
		if (feature.getFunctionType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getFunctionType());
			newFeature.setFunction(e);

		}
		if (feature.getUsageType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getUsageType());
			newFeature.setUsage(e);

		}
		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static TransitionSpaceType change2JaxbClass(IndoorGMLMap savedMap, TransitionSpace feature)
			throws JAXBException {

		TransitionSpaceType newFeature = indoorgmlnaviOF.createTransitionSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}
		if (feature.getClassType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getClassType());
			newFeature.setClazz(e);

		}
		if (feature.getFunctionType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getFunctionType());
			newFeature.setFunction(e);

		}
		if (feature.getUsageType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getUsageType());
			newFeature.setUsage(e);
		}
		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static ConnectionSpaceType change2JaxbClass(IndoorGMLMap savedMap, ConnectionSpace feature)
			throws JAXBException {

		ConnectionSpaceType newFeature = indoorgmlnaviOF.createConnectionSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}
		if (feature.getClassType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getClassType());
			newFeature.setClazz(e);

		}
		if (feature.getFunctionType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getFunctionType());
			newFeature.setFunction(e);

		}
		if (feature.getUsageType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getUsageType());
			newFeature.setUsage(e);

		}
		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static AnchorSpaceType change2JaxbClass(IndoorGMLMap savedMap, AnchorSpace feature) throws JAXBException {

		AnchorSpaceType newFeature = indoorgmlnaviOF.createAnchorSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}
		if (feature.getClassType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getClassType());
			newFeature.setClazz(e);

		}
		if (feature.getFunctionType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getFunctionType());
			newFeature.setFunction(e);

		}
		if (feature.getUsageType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getUsageType());
			newFeature.setUsage(e);

		}
		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static AnchorBoundaryType change2JaxbClass(IndoorGMLMap savedMap, AnchorBoundary feature) {
		AnchorBoundaryType newFeature = indoorgmlnaviOF.createAnchorBoundaryType();
		TransitionPropertyType duality = new TransitionPropertyType();
		newFeature.setId(feature.getId());

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
			newFeature.setId(feature.getId());
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF
						.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry3D(polygonProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			} else if (geom instanceof LineString) {
				LineString l = (LineString) geom;
				LineStringType linestring = Convert2JaxbGeometry.Convert2LineStringType(l);
				JAXBElement<LineStringType> jaxbLineString = gmlOF.createLineString(linestring);
				CurvePropertyType lineProp = gmlOF.createCurvePropertyType();
				lineProp.setAbstractCurve(jaxbLineString);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF
						.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry2D(lineProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			}
		}

		return newFeature;
	}

	public static ConnectionBoundaryType change2JaxbClass(IndoorGMLMap savedMap, ConnectionBoundary feature) {
		ConnectionBoundaryType newFeature = indoorgmlnaviOF.createConnectionBoundaryType();
		TransitionPropertyType duality = new TransitionPropertyType();
		newFeature.setId(feature.getId());

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
			newFeature.setId(feature.getId());
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF
						.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry3D(polygonProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			} else if (geom instanceof LineString) {
				LineString l = (LineString) geom;
				LineStringType linestring = Convert2JaxbGeometry.Convert2LineStringType(l);
				JAXBElement<LineStringType> jaxbLineString = gmlOF.createLineString(linestring);
				CurvePropertyType lineProp = gmlOF.createCurvePropertyType();
				lineProp.setAbstractCurve(jaxbLineString);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF
						.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry2D(lineProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			}
		}

		return newFeature;
	}

	public static OpeningCellSpaceBoundaryType change2JaxbClass(IndoorGMLMap savedMap,
			OpeningCellSpaceBoundary feature) {
		OpeningCellSpaceBoundaryType newFeature = imdfOF.createOpeningCellSpaceBoundaryType();
		TransitionPropertyType duality = new TransitionPropertyType();
		newFeature.setId(feature.getId());

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
			newFeature.setId(feature.getId());
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF
						.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry3D(polygonProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			} else if (geom instanceof LineString) {
				LineString l = (LineString) geom;
				LineStringType linestring = Convert2JaxbGeometry.Convert2LineStringType(l);
				JAXBElement<LineStringType> jaxbLineString = gmlOF.createLineString(linestring);
				CurvePropertyType lineProp = gmlOF.createCurvePropertyType();
				lineProp.setAbstractCurve(jaxbLineString);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF
						.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry2D(lineProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			}
		}
		if (feature.getFeatureInformation() != null) {
			FeatureInformationType featureInformationType = new FeatureInformationType();

			featureInformationType.setId(feature.getFeatureInformation().getId());
			featureInformationType.setType(feature.getFeatureInformation().getType());
			featureInformationType.setFeatureType(feature.getFeatureInformation().getFeatureType());
			featureInformationType.setGmlGeometry(feature.getFeatureInformation().getGmlGeometry());

			newFeature.setFeatureInformation(featureInformationType);

		}
		if (feature.getOpeningCategory() != null) {
			newFeature.setCategory(feature.getOpeningCategory());

		}

		if (feature.getAccessibilityCategory() != null) {

			List<ACCESSIBILITYCATEGORY> accessibilityList = new ArrayList<ACCESSIBILITYCATEGORY>();

			for (ACCESSIBILITYCATEGORY accessibility : feature.getAccessibilityCategory()) {

				accessibilityList.add(accessibility);
				System.out.println(accessibility);
			}
			newFeature.setAccessibility(accessibilityList);
		}
		if (feature.getAccessControlCategory() != null) {

			List<ACCESSCONTROLCATEGORY> accessControllList = new ArrayList<ACCESSCONTROLCATEGORY>();

			for (ACCESSCONTROLCATEGORY accessControll : feature.getAccessControlCategory()) {

				accessControllList.add(accessControll);
				System.out.println(accessControll);
			}
			newFeature.setAccessControl(accessControllList);
		}
		if (feature.getLevelId() != null) {

			newFeature.setLevelId(feature.getLevelId());
		}
		if (feature.getDoor() != null) {
			DoorType doorType = new DoorType();
			doorType.setType(feature.getDoor().getType());
			doorType.setAutomatic(feature.getDoor().getAutomatic());
			doorType.setMaterial(feature.getDoor().getmaterial());

			newFeature.setDoor(doorType);
		}

		if (feature.getFeatureName() != null) {
			LabelsType labelsType = new LabelsType();

			labelsType.setLanguage(feature.getFeatureName().getLanguage());
			labelsType.setName(feature.getFeatureName().getName());
			newFeature.setFeatureName(labelsType);

		}
		if (feature.getAltName() != null) {
			LabelsType labelsType = new LabelsType();
			labelsType.setLanguage(feature.getAltName().getLanguage());
			labelsType.setName(feature.getAltName().getName());
			newFeature.setAltName(labelsType);

		}

		return newFeature;
	}

	static AmenityStateType change2JaxbClass(IndoorGMLMap savedMap, AmenityState feature) throws JAXBException {
		AmenityStateType newFeature = new AmenityStateType();

		List<TransitionPropertyType> connects = new ArrayList<TransitionPropertyType>();

		if (feature.getConnects() != null) {
			for (int i = 0; i < feature.getConnects().size(); i++) {
				TransitionPropertyType tempTransitionPropertyType = new TransitionPropertyType();
				String href = feature.getConnects().get(i).getId();
				href = "#" + href;
				tempTransitionPropertyType.setHref(href);
				connects.add(tempTransitionPropertyType);
			}
			newFeature.setConnects(connects);
		}

		Point geom = (Point) feature.getGeometry();
		if (geom != null) {
			PointType point = Convert2JaxbGeometry.Convert2PointType(geom);
			PointPropertyType pointProp = gmlOF.createPointPropertyType();
			pointProp.setPoint(point);
			newFeature.setGeometry(pointProp);
		}

		if (feature.getDuality() != null) {
			CellSpacePropertyType duality = indoorgmlcoreOF.createCellSpacePropertyType();
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}
		// feature.geometry

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}
		if (feature.getAmenitycategory() != null) {
			newFeature.setCategory(feature.getAmenitycategory());

		}
		if (feature.getAccessibilitycategory() != null) {

			List<ACCESSIBILITYCATEGORY> accessibilityList = new ArrayList<ACCESSIBILITYCATEGORY>();

			for (ACCESSIBILITYCATEGORY accessibility : feature.getAccessibilitycategory()) {

				accessibilityList.add(accessibility);
			}

			newFeature.setAccessibility(accessibilityList);
		}
		if (feature.getFeatureName() != null) {
			LabelsType labelsType = new LabelsType();

			labelsType.setLanguage(feature.getFeatureName().getLanguage());
			labelsType.setName(feature.getFeatureName().getName());
			newFeature.setFeatureName(labelsType);
		

		}
		if (feature.getAltName() != null) {
			LabelsType labelsType = new LabelsType();
			labelsType.setLanguage(feature.getAltName().getLanguage());
			labelsType.setName(feature.getAltName().getName());
			newFeature.setAltName(labelsType);

		}

		if (feature.getHours() != null) {
			newFeature.setHours(feature.getHours());
		}
		if (feature.getPhone() != null) {
			newFeature.setPhone(feature.getPhone());
		}
		if (feature.getWebsite() != null) {
			newFeature.setWebsite(feature.getWebsite());
		}
		if (feature.getUnitIds() != null) {
			List<String> unitIDList = new ArrayList<String>();
			for (String unitID : feature.getUnitIds()) {

				unitIDList.add(unitID);
			}
			newFeature.setUnitIds(unitIDList);
		}
		if (feature.getAddressId() != null) {
			newFeature.setAddressId(feature.getAddressId());
		}
		if (feature.getCorrelationId() != null) {
			newFeature.setCorrelationId(feature.getCorrelationId());
		}

		newFeature.setId(feature.getId());
		return newFeature;
	}

	static AnchorStateType change2JaxbClass(IndoorGMLMap savedMap, AnchorState feature) throws JAXBException {
		AnchorStateType newFeature = new AnchorStateType();

		List<TransitionPropertyType> connects = new ArrayList<TransitionPropertyType>();

		if (feature.getConnects() != null) {
			for (int i = 0; i < feature.getConnects().size(); i++) {
				TransitionPropertyType tempTransitionPropertyType = new TransitionPropertyType();
				String href = feature.getConnects().get(i).getId();
				href = "#" + href;
				tempTransitionPropertyType.setHref(href);
				connects.add(tempTransitionPropertyType);
			}
			newFeature.setConnects(connects);
		}

		Point geom = (Point) feature.getGeometry();
		if (geom != null) {
			PointType point = Convert2JaxbGeometry.Convert2PointType(geom);
			PointPropertyType pointProp = gmlOF.createPointPropertyType();
			pointProp.setPoint(point);
			newFeature.setGeometry(pointProp);
		}

		if (feature.getDuality() != null) {
			CellSpacePropertyType duality = indoorgmlcoreOF.createCellSpacePropertyType();
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}
		// feature.geometry

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}
		if (feature.getAddressId() != null) {

			newFeature.setAddressId(feature.getAddressId());
		}
		if (feature.getUnitId() != null) {

			newFeature.setUnitId(feature.getUnitId());
		}

		newFeature.setId(feature.getId());
		return newFeature;
	}

	static BuildingStateType change2JaxbClass(IndoorGMLMap savedMap, BuildingState feature) throws JAXBException {
		BuildingStateType newFeature = new BuildingStateType();

		List<TransitionPropertyType> connects = new ArrayList<TransitionPropertyType>();

		if (feature.getConnects() != null) {
			for (int i = 0; i < feature.getConnects().size(); i++) {
				TransitionPropertyType tempTransitionPropertyType = new TransitionPropertyType();
				String href = feature.getConnects().get(i).getId();
				href = "#" + href;
				tempTransitionPropertyType.setHref(href);
				connects.add(tempTransitionPropertyType);
			}
			newFeature.setConnects(connects);
		}

		Point geom = (Point) feature.getGeometry();
		if (geom != null) {
			PointType point = Convert2JaxbGeometry.Convert2PointType(geom);
			PointPropertyType pointProp = gmlOF.createPointPropertyType();
			pointProp.setPoint(point);
			newFeature.setGeometry(pointProp);
		}

		if (feature.getDuality() != null) {
			CellSpacePropertyType duality = indoorgmlcoreOF.createCellSpacePropertyType();
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}
		// feature.geometry

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}
		if (feature.getBuildCategory() != null) {
			newFeature.setCategory(feature.getBuildCategory());

		}
		if (feature.getRestrictionCategory() != null) {
			newFeature.setRestriction(feature.getRestrictionCategory());

		}
		if (feature.getAddressId() != null) {
			newFeature.setAddressId(feature.getAddressId());
		}

		newFeature.setId(feature.getId());
		return newFeature;
	}

	public static GeofenceSpaceType change2JaxbClass(IndoorGMLMap savedMap, GeofenceSpace feature)
			throws JAXBException {

		GeofenceSpaceType newFeature = imdfOF.createGeofenceSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}
		if (feature.getFeatureInformation() != null) {
			FeatureInformationType featureInformationType = new FeatureInformationType();

			featureInformationType.setId(feature.getFeatureInformation().getId());
			featureInformationType.setType(feature.getFeatureInformation().getType());
			featureInformationType.setFeatureType(feature.getFeatureInformation().getFeatureType());
			featureInformationType.setGmlGeometry(feature.getFeatureInformation().getGmlGeometry());

			newFeature.setFeatureInformation(featureInformationType);

		}
		if (feature.getCategory() != null) {

			newFeature.setCategory(feature.getCategory());

		}
		if (feature.getRestrictionCategory() != null) {

			newFeature.setRestriction(feature.getRestrictionCategory());

		}
		if (feature.getCorrelationId() != null) {

			newFeature.setCorrelationId(feature.getCorrelationId());

		}
		if (feature.getBuildingIds() != null) {

			List<String> buildingList = new ArrayList<String>();

			for (String building_id : feature.getBuildingIds()) {

				buildingList.add(building_id);
			}
			newFeature.setBuildingIds(buildingList);
		}
		if (feature.getLevelIds() != null) {

			List<String> levelList = new ArrayList<String>();

			for (String level_id : feature.getLevelIds()) {
				levelList.add(level_id);
			}
			newFeature.setLevelIds(levelList);
		}
		if (feature.getParents() != null) {

			List<String> parentList = new ArrayList<String>();

			for (String parent : feature.getLevelIds()) {
				parentList.add(parent);
			}
			newFeature.setParents(parentList);

		}
		if (feature.getFeatureName() != null) {
			LabelsType labelsType = new LabelsType();

			labelsType.setLanguage(feature.getFeatureName().getLanguage());
			labelsType.setName(feature.getFeatureName().getName());
			newFeature.setFeatureName(labelsType);

		}
		if (feature.getAltName() != null) {
			LabelsType labelsType = new LabelsType();
			labelsType.setLanguage(feature.getAltName().getLanguage());
			labelsType.setName(feature.getAltName().getName());
			newFeature.setAltName(labelsType);

		}
		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static FixtureSpaceType change2JaxbClass(IndoorGMLMap savedMap, FixtureSpace feature) throws JAXBException {

		FixtureSpaceType newFeature = imdfOF.createFixtureSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		if (feature.getFixtureCategory() != null) {

			newFeature.setCategory(feature.getFixtureCategory());

		}
		if (feature.getAnchorId() != null) {
			newFeature.setAnchorId(feature.getAnchorId());

		}
		if (feature.getLevelId() != null) {
			newFeature.setLevelId(feature.getLevelId());

		}

		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static FootPrintSpaceType change2JaxbClass(IndoorGMLMap savedMap, FootPrintSpace feature)
			throws JAXBException {

		FootPrintSpaceType newFeature = imdfOF.createFootPrintSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		if (feature.getFeatureInformation() != null) {
			FeatureInformationType featureInformationType = new FeatureInformationType();

			featureInformationType.setId(feature.getFeatureInformation().getId());
			featureInformationType.setType(feature.getFeatureInformation().getType());
			featureInformationType.setFeatureType(feature.getFeatureInformation().getFeatureType());
			featureInformationType.setGmlGeometry(feature.getFeatureInformation().getGmlGeometry());

			newFeature.setFeatureInformation(featureInformationType);

		}
		if (feature.getFeatureCategory() != null) {

			newFeature.setCategory(feature.getFeatureCategory());

		}
		if (feature.getBuildingIds() != null) {

			List<String> buildingList = new ArrayList<String>();

			for (String building_id : feature.getBuildingIds()) {

				buildingList.add(building_id);
			}
			newFeature.setBuildingIds(buildingList);
		}

		if (feature.getFeatureName() != null) {
			LabelsType labelsType = new LabelsType();

			labelsType.setLanguage(feature.getFeatureName().getLanguage());
			labelsType.setName(feature.getFeatureName().getName());
			newFeature.setFeatureName(labelsType);

		}
//		if (feature.getSilingPolygonIds() != null) {
//
//			List<String> silingPolygonIds = new ArrayList<String>();
//
//			for (String silingPolygonId : feature.getSilingPolygonIds()) {
//
//				silingPolygonIds.add(silingPolygonId);
//			}
//
//			newFeature.setSiblingPolygonIds(silingPolygonIds);
//
//		}

		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static KioskSpaceType change2JaxbClass(IndoorGMLMap savedMap, KioskSpace feature) throws JAXBException {

		KioskSpaceType newFeature = imdfOF.createKioskSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		if (feature.getFeatureInformation() != null) {
			FeatureInformationType featureInformationType = new FeatureInformationType();

			featureInformationType.setId(feature.getFeatureInformation().getId());
			featureInformationType.setType(feature.getFeatureInformation().getType());
			featureInformationType.setFeatureType(feature.getFeatureInformation().getFeatureType());
			featureInformationType.setGmlGeometry(feature.getFeatureInformation().getGmlGeometry());

			newFeature.setFeatureInformation(featureInformationType);

		}
		if (feature.getAnchorId() != null) {

			newFeature.setAnchorId(feature.getAnchorId());

		}
		if (feature.getLevelId() != null) {

			newFeature.setLevelId(feature.getLevelId());
		}

		if (feature.getFeatureName() != null) {
			LabelsType labelsType = new LabelsType();

			labelsType.setLanguage(feature.getFeatureName().getLanguage());
			labelsType.setName(feature.getFeatureName().getName());
			newFeature.setFeatureName(labelsType);

		}
		if (feature.getAltName() != null) {
			LabelsType labelsType = new LabelsType();
			labelsType.setLanguage(feature.getAltName().getLanguage());
			labelsType.setName(feature.getAltName().getName());
			newFeature.setAltName(labelsType);

		}

		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static LevelSpaceType change2JaxbClass(IndoorGMLMap savedMap, LevelSpace feature) throws JAXBException {

		LevelSpaceType newFeature = imdfOF.createLevelSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		if (feature.getFeatureInformation() != null) {
			FeatureInformationType featureInformationType = new FeatureInformationType();

			featureInformationType.setId(feature.getFeatureInformation().getId());
			featureInformationType.setType(feature.getFeatureInformation().getType());
			featureInformationType.setFeatureType(feature.getFeatureInformation().getFeatureType());
			featureInformationType.setGmlGeometry(feature.getFeatureInformation().getGmlGeometry());

			newFeature.setFeatureInformation(featureInformationType);

		}
		if (feature.getCategory() != null) {
			newFeature.setCategory(feature.getCategory());

		}
		if (feature.getRestrictionCategory() != null) {

			newFeature.setRestriction(feature.getRestrictionCategory());

		}
		if (feature.getOutdoor() != null) {

			newFeature.setOutdoor(feature.getOutdoor());
		}
		if (feature.getOrdinal() >=0) {
			 BigInteger bigInt =  BigInteger.valueOf(feature.getOrdinal());	

			newFeature.setOrdinal(bigInt);
		}
		if (feature.getAddressId() != null) {

			newFeature.setAddressId(feature.getAddressId());
		}
		if (feature.getBuildingIds() != null) {

			List<String> buildingList = new ArrayList<String>();

			for (String building_id : feature.getBuildingIds()) {

				buildingList.add(building_id);
			}
			newFeature.setBuildingIds(buildingList);
		}

		if (feature.getFeatureName() != null) {
			LabelsType labelsType = new LabelsType();

			labelsType.setLanguage(feature.getFeatureName().getLanguage());
			labelsType.setName(feature.getFeatureName().getName());
			newFeature.setFeatureName(labelsType);

		}
		if (feature.getShortName() != null) {
			LabelsType labelsType = new LabelsType();
			labelsType.setLanguage(feature.getShortName().getLanguage());
			labelsType.setName(feature.getShortName().getName());
			newFeature.setShortName(labelsType);

		}

		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static UnitSpaceType change2JaxbClass(IndoorGMLMap savedMap, UnitSpace feature) throws JAXBException {

		UnitSpaceType newFeature = imdfOF.createUnitSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		if (feature.getFeatureInformation() != null) {
			FeatureInformationType featureInformationType = new FeatureInformationType();

			featureInformationType.setId(feature.getFeatureInformation().getId());
			featureInformationType.setType(feature.getFeatureInformation().getType());
			featureInformationType.setFeatureType(feature.getFeatureInformation().getFeatureType());
			featureInformationType.setGmlGeometry(feature.getFeatureInformation().getGmlGeometry());

			newFeature.setFeatureInformation(featureInformationType);

		}
		if (feature.getCategory() != null) {
			newFeature.setCategory(feature.getCategory());

		}
		if (feature.getRestrictionCategory() != null) {

			newFeature.setRestriction(feature.getRestrictionCategory());

		}
		if (feature.getAccessibilityCategory() != null) {

			List<ACCESSIBILITYCATEGORY> accessibilityList = new ArrayList<ACCESSIBILITYCATEGORY>();

			for (ACCESSIBILITYCATEGORY accessibility : feature.getAccessibilityCategory()) {

				accessibilityList.add(accessibility);
			}
			newFeature.setAccessibility(accessibilityList);
		}
		if (feature.getLevelId() != null) {

			newFeature.setLevelId(feature.getLevelId());
		}

		if (feature.getFeatureName() != null) {
			LabelsType labelsType = new LabelsType();

			labelsType.setLanguage(feature.getFeatureName().getLanguage());
			labelsType.setName(feature.getFeatureName().getName());
			newFeature.setFeatureName(labelsType);

		}
		if (feature.getAltName() != null) {
			LabelsType labelsType = new LabelsType();
			labelsType.setLanguage(feature.getAltName().getLanguage());
			labelsType.setName(feature.getAltName().getName());
			newFeature.setAltName(labelsType);

		}

		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static VenueSpaceType change2JaxbClass(IndoorGMLMap savedMap, VenueSpace feature) throws JAXBException {

		VenueSpaceType newFeature = imdfOF.createVenueSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		if (feature.getFeatureInformation() != null) {
			FeatureInformationType featureInformationType = new FeatureInformationType();

			featureInformationType.setId(feature.getFeatureInformation().getId());
			featureInformationType.setType(feature.getFeatureInformation().getType());
			featureInformationType.setFeatureType(feature.getFeatureInformation().getFeatureType());
			featureInformationType.setGmlGeometry(feature.getFeatureInformation().getGmlGeometry());

			newFeature.setFeatureInformation(featureInformationType);

		}
		if (feature.getCategory() != null) {
			newFeature.setCategory(feature.getCategory());

		}
		if (feature.getRestrictionCategory() != null) {

			newFeature.setRestriction(feature.getRestrictionCategory());

		}

		if (feature.getHours() != null) {

			newFeature.setHours(feature.getHours());
		}
		if (feature.getPhone() != null) {

			newFeature.setPhone(feature.getPhone());
		}
		if (feature.getWebsite() != null) {

			newFeature.setWebsite(feature.getWebsite());
		}

		if (feature.getFeatureName() != null) {
			LabelsType labelsType = new LabelsType();

			labelsType.setLanguage(feature.getFeatureName().getLanguage());
			labelsType.setName(feature.getFeatureName().getName());
			newFeature.setFeatureName(labelsType);

		}
		if (feature.getAltName() != null) {
			LabelsType labelsType = new LabelsType();
			labelsType.setLanguage(feature.getAltName().getLanguage());
			labelsType.setName(feature.getAltName().getName());
			newFeature.setAltName(labelsType);

		}

		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static SectionSpaceType change2JaxbClass(IndoorGMLMap savedMap, SectionSpace feature) throws JAXBException {

		SectionSpaceType newFeature = imdfOF.createSectionSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		if (feature.getFeatureInformation() != null) {
			FeatureInformationType featureInformationType = new FeatureInformationType();

			featureInformationType.setId(feature.getFeatureInformation().getId());
			featureInformationType.setType(feature.getFeatureInformation().getType());
			featureInformationType.setFeatureType(feature.getFeatureInformation().getFeatureType());
			featureInformationType.setGmlGeometry(feature.getFeatureInformation().getGmlGeometry());

			newFeature.setFeatureInformation(featureInformationType);

		}
		if (feature.getCategory() != null) {
			newFeature.setCategory(feature.getCategory());

		}
		if (feature.getRestrictionCategory() != null) {

			newFeature.setRestriction(feature.getRestrictionCategory());

		}
		if (feature.getAccessibilityCategory() != null) {

			List<ACCESSIBILITYCATEGORY> accessibilityList = new ArrayList<ACCESSIBILITYCATEGORY>();

			for (ACCESSIBILITYCATEGORY accessibility : feature.getAccessibilityCategory()) {

				accessibilityList.add(accessibility);
			}
			newFeature.setAccessibility(accessibilityList);
		}
		if (feature.getLevelId() != null) {

			newFeature.setLevelId(feature.getLevelId());
		}
		if (feature.getAddressId() != null) {

			newFeature.setAddressId(feature.getAddressId());
		}

		if (feature.getFeatureName() != null) {
			LabelsType labelsType = new LabelsType();

			labelsType.setLanguage(feature.getFeatureName().getLanguage());
			labelsType.setName(feature.getFeatureName().getName());
			newFeature.setFeatureName(labelsType);

		}
		if (feature.getAltName() != null) {
			LabelsType labelsType = new LabelsType();
			labelsType.setLanguage(feature.getAltName().getLanguage());
			labelsType.setName(feature.getAltName().getName());
			newFeature.setAltName(labelsType);

		}
		if (feature.getParents() != null) {

			List<String> parentList = new ArrayList<String>();

			for (String parent : feature.getParents()) {

				parentList.add(parent);
			}
			newFeature.setParents(parentList);

		}

		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialboundedBy() != null) {
			for (int i = 0; i < feature.getPartialboundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialboundedByHref = feature.getPartialboundedBy().get(i).getId();
				partialboundedByHref = "#" + partialboundedByHref;
				tempcsb.setHref(partialboundedByHref);
				partialboundedBy.add(tempcsb);
			}

			newFeature.setPartialboundedBy(partialboundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = (Geometry) feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

}