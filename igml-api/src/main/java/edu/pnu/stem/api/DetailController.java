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
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.pnu.stem.api.exception.UndefinedDocumentException;
import edu.pnu.stem.binder.Convert2Json;
import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.dao.DetailDAO;
import edu.pnu.stem.feature.imdf.Detail;
import edu.pnu.stem.feature.imdf.FeatureInformation;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FEATURECATEGORY;

@RestController
@RequestMapping("/documents/{docId}/Detail")
public class DetailController {

	@Autowired
	private ApplicationContext applicationContext;

	@PostMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createSpaceLayer(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {

		String parentId = json.get("parentId").asText().trim();
		String name = null;
		String description = null;
		double weight = 0;
		String[] connects = null;

		String duality = null;
		String geom = json.get("geometry").asText().trim();

		Detail t;
		Geometry geometry = null;
		FeatureInformation featureInformation = null;
		String level_id = null;

		if (id == null || id.isEmpty()) {
			id = UUID.randomUUID().toString();
		}

		if (json.has("properties")) {
			if (json.get("properties").has("connects")) {
				connects = new String[2];
				connects[0] = json.get("properties").get("connects").get(0).asText().trim();
				connects[1] = json.get("properties").get("connects").get(1).asText().trim();
			}
			if (json.get("properties").has("duality")) {
				duality = json.get("properties").get("duality").asText().trim();
			}

			if (json.get("properties").has("name")) {
				name = json.get("properties").get("name").asText().trim();
			}

			if (json.get("properties").has("description")) {
				description = json.get("properties").get("description").asText().trim();
			}

			if (json.get("properties").has("weight")) {
				weight = json.get("properties").get("weight").asDouble();
			}
		}
		if (json.has("connects")) {
			JsonNode connectsNode = json.get("connects");
			if (connectsNode.isArray()) {
				connects[0] = connectsNode.get(0).asText().trim();
				connects[1] = connectsNode.get(1).asText().trim();
			}
		}

		if (json.has("geometry")) {
			geometry = Convert2Json.json2Geometry(json.get("geometry"));
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
			if (json.get("imdf").has("level_id")) {

				level_id = json.get("imdf").get("level_id").asText().trim();

			}

		}

		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			t = DetailDAO.createDetail(map, parentId, id, name, description, geometry, duality, connects, weight,
					featureInformation, level_id);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
		response.setHeader("Location", request.getRequestURL().append(t.getId()).toString());
	}

	@PutMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateDetail(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			String duality = null;
			JsonNode geometry = null;
			List<String> connects = null;
			Geometry geom = null;
			String parentId = null;
			String name = null;
			String description = null;
			String[] arrConnects = null;
			double weight = 0;

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

				if (json.get("properties").has("weight")) {
					weight = json.get("properties").get("weight").asDouble();
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
				if (json.get("properties").has("connects")) {
					connects = new ArrayList<String>();
					JsonNode partialBoundedByList = json.get("properties").get("connects");
					for (int i = 0; i < partialBoundedByList.size(); i++) {
						connects.add(partialBoundedByList.get(i).asText().trim());
					}
					arrConnects = new String[2];
					connects.toArray(arrConnects);
				}

			}

			DetailDAO.updateDetail(map, parentId, id, name, description, geom, duality, arrConnects);

		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.FOUND)
	public void getDetail(@PathVariable("docId") String docId, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);

			ObjectNode target = Convert2Json.convert2JSON(map, DetailDAO.readDetail(map, id));
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
	public void deleteDetail(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			DetailDAO.deleteDetail(map, id);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

}
