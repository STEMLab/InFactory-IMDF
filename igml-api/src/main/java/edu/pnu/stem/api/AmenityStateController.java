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
import edu.pnu.stem.dao.AmenityStateDAO;
import edu.pnu.stem.feature.core.State;
import edu.pnu.stem.feature.imdf.AmenityState;
import edu.pnu.stem.feature.imdf.Labels;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.ACCESSIBILITYCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.AMENITYCATEGORY;

@RestController
@RequestMapping("/documents/{docId}/AmenityState")
public class AmenityStateController {

	@Autowired
	private ApplicationContext applicationContext;

	@PostMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createAmenityState(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {

		String parentId = json.get("parentId").asText().trim();
		String name = null;
		String description = null;
		Geometry geometry = null;
		AmenityState s;
		List<String> connected = null;
		String duality = null;

		AMENITYCATEGORY category = null;
		ACCESSIBILITYCATEGORY[] accessibility = null;
		String hours = null;
		String phone = null;
		String website = null;
		String[] unit_ids = null;
		String address_id = null;
		String correlation_id = null;
		Labels feature_name =null;
		Labels alt_name =null;

		if (id == null || id.isEmpty()) {
			id = UUID.randomUUID().toString();
		}
		if (json.has("geometry")) {
			geometry = Convert2Json.json2Geometry(json.get("geometry"));
		}

		if (json.has("properties")) {
			if (json.get("properties").has("duality")) {
				duality = json.get("properties").get("duality").asText().trim();
			}

			if (json.get("properties").has("connects")) {
				connected = new ArrayList<String>();
				JsonNode test = json.get("properties").get("connects");
				for (int i = 0; i < test.size(); i++) {
					connected.add(test.get(i).asText().trim());
				}
			}
			if (json.get("properties").has("name")) {
				name = json.get("properties").get("name").asText().trim();
			}
			if (json.get("properties").has("description")) {
				description = json.get("properties").get("description").asText().trim();
			}
		}
		if (json.has("imdf")) {
			if (json.get("imdf").has("hours")) {
				hours = json.get("imdf").get("hours").asText().trim();
			}
			if (json.get("imdf").has("phone")) {
				phone = json.get("imdf").get("phone").asText().trim();
			}
			if (json.get("imdf").has("name")) {
				website = json.get("imdf").get("name").asText().trim();
			}
			if (json.get("imdf").has("alt_name")) {
				website = json.get("imdf").get("alt_name").asText().trim();
			}
			if (json.get("imdf").has("website")) {
				website = json.get("imdf").get("website").asText().trim();
			}
			if (json.get("imdf").has("unit_ids")) {
				JsonNode unit_id = json.get("imdf").get("unit_ids");
				unit_ids = new String[unit_id.size()];

				for (int i = 0; i < unit_id.size(); i++) {
					unit_ids[i] = unit_id.get(i).asText().trim();
				}
			}

			if (json.get("imdf").has("address_id")) {
				address_id = json.get("imdf").get("address_id").asText().trim();
			}
			if (json.get("imdf").has("correlation_id")) {
				correlation_id = json.get("imdf").get("correlation_id").asText().trim();
			}
		}

		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			s = AmenityStateDAO.createAmenityState(map, parentId, id, name, description, geometry, duality, connected, category, accessibility, feature_name, alt_name ,hours,phone, website, unit_ids, address_id,correlation_id);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
		response.setHeader("Location", request.getRequestURL().append(s.getId()).toString());
	}

	@PutMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateAmenityState(@PathVariable("docId") String docId, @PathVariable("id") String id,
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
				if (json.get("properties").has("name")) {

					name = json.get("properties").get("name").asText().trim();
				}
				if (json.get("properties").has("description")) {

					description = json.get("properties").get("description").asText().trim();
				}

				if (json.get("properties").has("connects")) {
					connects = new ArrayList<String>();
					JsonNode partialBoundedByList = json.get("properties").get("connects");
					for (int i = 0; i < partialBoundedByList.size(); i++) {
						connects.add(partialBoundedByList.get(i).asText().trim());
					}
				}
			}

			if (json.has("geometry")) {
				geometry = json.get("geometry");
				geom = Convert2Json.json2Geometry(geometry);
			}

			// TODO : 나중에 고치기!!
			// String properties = json.get("properties").asText().trim();
			// String duality = null;

			AmenityStateDAO.updateAmenityState(map, parentId, id, name, description, geom, duality, connects);

		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.FOUND)
	public void getAmenityState(@PathVariable("docId") String docId, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);

			ObjectNode target = Convert2Json.convert2JSON(map, AmenityStateDAO.readAmenityState(map, id));
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
	public void deleteAmenityState(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			AmenityStateDAO.deleteAmenityState(map, id);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

}
