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
import edu.pnu.stem.dao.AnchorStateDAO;
import edu.pnu.stem.feature.imdf.AnchorState;

@RestController
@RequestMapping("/documents/{docId}/AnchorState")
public class AnchorStateController {

	@Autowired
	private ApplicationContext applicationContext;

	@PostMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createAnchorState(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {

		String parentId = json.get("parentId").asText().trim();
		String name = null;
		String description = null;
		Geometry geometry = null;
		AnchorState s;
		List<String> connected = null;
		String duality = null;

		String unit_id = null;
		String address_id = null;

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

			if (json.get("imdf").has("unit_id")) {
				unit_id = json.get("imdf").get("unit_id").asText().trim();
			}

			if (json.get("imdf").has("address_id")) {
				address_id = json.get("imdf").get("address_id").asText().trim();
			}

		}

		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			s = AnchorStateDAO.createAnchorState(map, parentId, id, name, description, geometry, duality, connected, unit_id, address_id);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
		response.setHeader("Location", request.getRequestURL().append(s.getId()).toString());
	}

	@PutMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateAnchorState(@PathVariable("docId") String docId, @PathVariable("id") String id,
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

			AnchorStateDAO.updateAnchorState(map, parentId, id, name, description, geom, duality, connects);

		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.FOUND)
	public void getAnchorState(@PathVariable("docId") String docId, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);

			ObjectNode target = Convert2Json.convert2JSON(map, AnchorStateDAO.readAnchorState(map, id));
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
	public void deleteAnchorState(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			AnchorStateDAO.deleteAnchorState(map, id);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

}
