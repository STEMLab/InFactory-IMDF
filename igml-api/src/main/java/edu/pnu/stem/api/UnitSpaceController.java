package edu.pnu.stem.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
import edu.pnu.stem.dao.UnitSpaceDAO;
import edu.pnu.stem.feature.imdf.FeatureInformation;
import edu.pnu.stem.feature.imdf.Labels;
import edu.pnu.stem.feature.imdf.UnitSpace;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSIBILITYCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FEATURECATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.LANGUAGETAG;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.RESTRICTIONCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.UNITCATEGORY;

@RestController
@RequestMapping("/documents/{docId}/UnitSpace")
public class UnitSpaceController {

	@Autowired
	private ApplicationContext applicationContext;

	@PostMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createUnitSpace(@PathVariable("id") String id, @RequestBody ObjectNode json, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String docId = json.get("docId").asText().trim();
		String parentId = json.get("parentId").asText().trim();
		String name = null;
		String description = null;

		String geomFormatType = "GEOJSON";
		final ObjectMapper mapper = new ObjectMapper();

		String geom = json.get("geometry").asText().trim();
		String duality = null;
		Geometry geofenceGeometry = null;
		JsonNode geometry = null;

		List<String> partialBoundedBy = null;
		List<String> level = null;

		FeatureInformation featureInformation = null;
		UNITCATEGORY category = null;
		RESTRICTIONCATEGORY restriction = null;
		ACCESSIBILITYCATEGORY[] accessibility = null;
		String level_id = null;
		Labels feature_name = null;
		Labels alt_name = null;

		if (id == null || id.isEmpty()) {
			id = UUID.randomUUID().toString();
		}

		try {
			mapper.readTree(geom);
		} catch (IOException e) {
			geomFormatType = "WKT";
		}
		if (json.has("geometry")) {
			geometry = json.get("geometry");
			geofenceGeometry = Convert2Json.json2Geometry(geometry);
		}
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
			if (json.get("properties").has("level")) {
				level = new ArrayList<String>();
				JsonNode levelList = json.get("properties").get("level");
				for (int i = 0; i < levelList.size(); i++) {
					level.add(levelList.get(i).asText().trim());

				}
			}
			if (json.get("properties").has("partialboundedBy")) {
				partialBoundedBy = new ArrayList<String>();
				JsonNode partialBoundedByList = json.get("properties").get("partialboundedBy");
				for (int i = 0; i < partialBoundedByList.size(); i++) {
					partialBoundedBy.add(partialBoundedByList.get(i).asText().trim());
				}
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

				for (UNITCATEGORY value : UNITCATEGORY.values()) {
					if (str.toUpperCase().equals(value.toString())) {
						category = value;

					}
				}
			}
			if (json.get("imdf").has("restriction")) {

				String str = json.get("imdf").get("restriction").asText().trim();

				for (RESTRICTIONCATEGORY value : RESTRICTIONCATEGORY.values()) {
					if (str.toUpperCase().equals(value.toString())) {

						restriction = value;
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
			if (json.get("imdf").has("level_id")) {

				level_id = json.get("imdf").get("level_id").asText().trim();

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

		// TODO : 나중에 고치기!!
		// String properties = json.get("properties").asText().trim();
		// String duality = null;

		UnitSpace c = null;
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);

			c = UnitSpaceDAO.createUnitSpace(map, parentId, id, name, description, geofenceGeometry, duality, level,
					partialBoundedBy, featureInformation, category, restriction, accessibility, level_id, feature_name,
					alt_name);

		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
		response.setHeader("Location", request.getRequestURL().append(c.getId()).toString());
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.FOUND)
	public void getUnitSpace(@PathVariable("docId") String docId, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);

			ObjectNode target = Convert2Json.convert2JSON(map, UnitSpaceDAO.readUnitSpace(map, id));
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(target);

			out.flush();

		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

	@PutMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateUnitSpace(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			String duality = null;
			JsonNode geometry = null;
			List<String> partialBoundedBy = null;
			Geometry geom = null;
			String parentId = null;

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

			// TODO : 나중에 고치기!!
			// String properties = json.get("properties").asText().trim();
			// String duality = null;

			if (json.has("properties")) {
				if (json.get("properties").has("partialboundedBy")) {
					partialBoundedBy = new ArrayList<String>();
					JsonNode partialBoundedByList = json.get("properties").get("partialboundedBy");
					for (int i = 0; i < partialBoundedByList.size(); i++) {
						partialBoundedBy.add(partialBoundedByList.get(i).asText().trim());
					}
				}
			}

			UnitSpaceDAO.updateUnitSpace(map, parentId, id, null, null, geom, duality, partialBoundedBy);

		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUnitSpace(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			UnitSpaceDAO.deleteUnitSpace(map, id);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}
}
