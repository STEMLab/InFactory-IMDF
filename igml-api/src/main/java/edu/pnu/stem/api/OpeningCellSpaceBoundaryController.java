package edu.pnu.stem.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.pnu.stem.api.exception.UndefinedDocumentException;
import edu.pnu.stem.binder.Convert2Json;
import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.dao.OpeningCellSpaceBoundaryDAO;
import edu.pnu.stem.feature.imdf.Door;
import edu.pnu.stem.feature.imdf.FeatureInformation;
import edu.pnu.stem.feature.imdf.Labels;
import edu.pnu.stem.feature.imdf.OpeningCellSpaceBoundary;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSCONTROLCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSIBILITYCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FEATURECATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.GEOFENCECATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.LANGUAGETAG;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.OPENINGCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.RESTRICTIONCATEGORY;

@RestController
@RequestMapping("documents/{docId}/OpeningCellSpaceBoundary")
public class OpeningCellSpaceBoundaryController {

	@Autowired
	private ApplicationContext applicationContext;

	@PostMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createOpeningCellSpaceBoundary(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {

		final ObjectMapper mapper = new ObjectMapper();
		String parentId = json.get("parentId").asText().trim();
		String duality = null;
		String name = null;
		String description = null;

		String geomFormatType = "GEOJSON";
		String geom = json.get("geometry").asText().trim();
		Geometry geometry = null;

		FeatureInformation featureInformation = null;
		OPENINGCATEGORY category = null;
		ACCESSIBILITYCATEGORY[] accessibility = null;
		ACCESSCONTROLCATEGORY[] access_control = null;
		String level_id = null;
		Door door = null;
		Labels feature_name = null;
		Labels alt_name = null;

		if (id == null || id.isEmpty()) {
			id = UUID.randomUUID().toString();
		}

		try {
			mapper.readTree(geom);
			geometry = Convert2Json.json2Geometry(json.get("geometry"));
		} catch (IOException e) {
			geomFormatType = "WKT";
		}

		if (json.has("geometry")) {
			geometry = Convert2Json.json2Geometry(json.get("geometry"));
		}

		// TODO : 나중에 고칠 것. 임시로.
		if (json.has("duality")) {
			duality = json.get("duality").asText().trim();
		}
		if (json.has("properties")) {
			if (json.get("properties").has("duality")) {
				duality = json.get("properties").get("duality").asText().trim();
			}
			if (json.get("properties").has("name")) {
				name = json.get("properties").get("name").asText().trim();
			}
			if (json.get("properties").has("description")) {
				description = json.get("properties").get("description").asText().trim();
			}
		}
		if (json.has("imdf")) {

			if (json.get("imdf").has("featureInformation")) {
				featureInformation = new FeatureInformation();
				if (json.get("imdf").get("featureInformation").has("id")) {
					String str = json.get("imdf").get("featureInformation").get("id").asText().trim();

					featureInformation.setId(str);
				}
				if (json.get("imdf").get("featureInformation").has("type")) {
					String str = json.get("imdf").get("featureInformation").get("type").asText().trim();

					featureInformation.setType(str);
				}
				if (json.get("imdf").get("featureInformation").has("feature_type")) {

					String str = json.get("imdf").get("featureInformation").get("feature_type").asText().trim();

					for (FEATURECATEGORY value : FEATURECATEGORY.values()) {
						if (str.toUpperCase().equals(value.toString())) {
							featureInformation.setFeatureType(value);
						}
					}
				}
				if (json.get("imdf").get("featureInformation").has("gml_geometry")) {
					String str = json.get("imdf").get("featureInformation").get("gml_geometry").asText().trim();

					featureInformation.setGmlGeometry(str);
				}
			}
			if (json.get("imdf").has("category")) {
				String str = json.get("imdf").get("category").asText().trim();

				for (OPENINGCATEGORY value : OPENINGCATEGORY.values()) {
					if (str.toUpperCase().equals(value.toString())) {
						category = value;

					}
				}
			}
			if (json.get("imdf").has("accessibility")) {

				JsonNode accessibilityList = json.get("imdf").get("accessibility");

				for (ACCESSIBILITYCATEGORY value : ACCESSIBILITYCATEGORY.values()) {

					for (int i = 0; i < accessibilityList.size(); i++) {
						String str = json.get("imdf").get("accessibility").asText().trim();

						if (str.toUpperCase().equals(value.toString())) {

							accessibility[i] = value;
						}

					}

				}

			}
			if (json.get("imdf").has("access_control")) {

				JsonNode access_controlList = json.get("imdf").get("access_control");

				for (ACCESSCONTROLCATEGORY value : ACCESSCONTROLCATEGORY.values()) {

					for (int i = 0; i < access_controlList.size(); i++) {
						String str = json.get("imdf").get("access_control").asText().trim();

						if (str.toUpperCase().equals(value.toString())) {

							access_control[i] = value;
						}

					}

				}

			}
			if (json.get("imdf").has("level_id")) {

				level_id = json.get("imdf").get("level_id").asText().trim();

			}
			if (json.get("imdf").has("door")) {
				door = new Door();

				if (json.get("imdf").get("door").has("type")) {
					String type = json.get("imdf").get("door").get("type").asText().trim();
					door.setType(type);
				}
				if (json.get("imdf").get("door").has("automatic")) {
					String automatic = json.get("imdf").get("door").get("automatic").asText().trim();
					if (automatic.toUpperCase().equals("TRUE")) {
						door.setAutomatic(Boolean.TRUE);

					} else {
						door.setAutomatic(Boolean.FALSE);
					}

				}

			}

			if (json.get("imdf").has("feature_name")) {
				feature_name = new Labels();

				if (json.get("imdf").get("feature_name").has("language")) {
					String str = json.get("imdf").get("feature_name").get("language").asText().trim();

					for (LANGUAGETAG value : LANGUAGETAG.values()) {
						if (str.toUpperCase().equals(value.toString())) {
							feature_name.setLanguage(value);
						}
					}
				}
				if (json.get("imdf").get("feature_name").has("name")) {
					String str = json.get("imdf").get("feature_name").get("name").asText().trim();
					feature_name.setName(str);
				}

			}
			if (json.get("imdf").has("alt_name")) {
				alt_name = new Labels();
				if (json.get("imdf").get("alt_name").has("language")) {
					String str = json.get("imdf").get("alt_name").get("language").asText().trim();
					for (LANGUAGETAG value : LANGUAGETAG.values()) {
						if (str.toUpperCase().equals(value.toString())) {

							alt_name.setLanguage(value);
						}
					}

				}
				if (json.get("imdf").get("alt_name").has("name")) {
					String str = json.get("imdf").get("alt_name").get("name").asText().trim();

					alt_name.setName(str);
				}
			}

		}

		OpeningCellSpaceBoundary c = null;
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			/*
			 * 
			 * if(geomFormatType.equals("GEOJSON")){ c =
			 * OpeningCellSpaceBoundaryDAO.createOpeningCellSpaceBoundary(map, parentId, id,
			 * geometry, duality); } else if(geomFormatType.equals("WKT")){ c =
			 * OpeningCellSpaceBoundaryDAO.createOpeningCellSpaceBoundary(map, parentId, id,
			 * geom, duality); }
			 */

			c = OpeningCellSpaceBoundaryDAO.createOpeningCellSpaceBoundary(map, parentId, id, name, description,
					geometry, duality, featureInformation, category, accessibility, access_control, level_id, door,
					feature_name, alt_name);

		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
		response.setHeader("Location", request.getRequestURL().append(c.getId()).toString());
	}

	@PutMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateOpeningCellSpaceBoundary(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			String duality = null;
			JsonNode geometry = null;
			Geometry geom = null;
			String parentId = null;
			String name = null;
			String description = null;

			if (json.has("parentId")) {
				parentId = json.get("parentId").asText().trim();
			}

			if (json.has("duality")) {

				duality = json.get("duality").asText().trim();

			}
			if (json.has("properties")) {
				if (json.get("properties").has("duality")) {
					duality = json.get("properties").get("duality").asText().trim();

				}

			}
			if (json.has("geometry")) {
				geometry = json.get("geometry");
				geom = Convert2Json.json2Geometry(geometry);

			}

			if (json.has("properties")) {
				if (json.get("properties").has("name")) {
					name = json.get("properties").get("name").asText().trim();
				}
				if (json.get("properties").has("description")) {
					description = json.get("properties").get("description").asText().trim();
				}
			}

			OpeningCellSpaceBoundaryDAO.updateOpeningCellSpaceBoundary(map, parentId, id, name, description, geom,
					duality);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.FOUND)
	public void getOpeningCellSpaceBoundary(@PathVariable("docId") String docId, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);

			ObjectNode target = Convert2Json.convert2JSON(map,
					OpeningCellSpaceBoundaryDAO.readOpeningCellSpaceBoundary(map, id));
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(target);
			out.flush();

		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteOpeningCellSpaceBoundary(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			OpeningCellSpaceBoundaryDAO.deleteOpeningCellSpaceBoundary(map, id);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

}
