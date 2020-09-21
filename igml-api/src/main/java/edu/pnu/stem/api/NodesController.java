/**
 * 
 */
package edu.pnu.stem.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import edu.pnu.stem.dao.NodesDAO;
import edu.pnu.stem.feature.core.Nodes;

/**
 * @author Hyung-Gyu Ryoo (hyunggyu.ryoo@gmail.com, Pusan National University)
 *
 */
@RestController
@RequestMapping("/documents/{docId}/nodes")
public class NodesController {
	
	@Autowired
    private ApplicationContext applicationContext;
	
	@PostMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createSpaceLayer(@PathVariable("docId") String docId,@PathVariable("id") String id, @RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {

		String parentId = json.get("parentId").asText().trim();
		String name = null;
		String description = null;
		List<String> stateMember = null;
		
		if(id == null || id.isEmpty()) {
			id = UUID.randomUUID().toString();
		}
		
		
		if(json.has("properties")) {
			if(json.get("properties").has("name")) {
				name = json.get("properties").get("name").asText().trim();
			}
			if(json.get("properties").has("description")) {
				description = json.get("properties").get("description").asText().trim();
			}
			if(json.get("properties").has("stateMember")){
				stateMember = new ArrayList<String>();
				JsonNode partialBoundedByList = json.get("properties").get("stateMember");
				for(int i = 0 ; i < partialBoundedByList.size() ; i++){
					stateMember.add(partialBoundedByList.get(i).asText().trim());
				}
			}
		}
		
		Nodes ns;
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			ns = NodesDAO.createNodes(map, parentId, id, name, description, stateMember);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
		response.setHeader("Location", request.getRequestURL().append(ns.getId()).toString());
	}
	
	@PutMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateNodes(@PathVariable("docId") String docId,@PathVariable("id") String id, @RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);

			String parentId = null;
			String name = null;
			String description = null;
			List<String> stateMember = null;
			
			if(json.has("parentId")) {
				parentId = json.get("parentId").asText().trim();
			}
						
			if(json.has("properties")){
				if(json.get("properties").has("name")) {
					name = json.get("properties").get("name").asText().trim();
				}
				if(json.get("properties").has("description")) {
					description = json.get("properties").get("description").asText().trim();
				}
				if(json.get("properties").has("stateMember")){
					stateMember = new ArrayList<String>();
					JsonNode partialBoundedByList = json.get("properties").get("stateMember");
					for(int i = 0 ; i < partialBoundedByList.size() ; i++){
						stateMember.add(partialBoundedByList.get(i).asText().trim());
					}
				}
				
			}
						
			NodesDAO.updateNodes(map, parentId, id, name, description, stateMember);
		}
		catch(NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}
	@GetMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.FOUND)
	public void getNodes(@PathVariable("docId") String docId,@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			
			ObjectNode target = Convert2Json.convert2JSON(map, NodesDAO.readNodes(map, id));
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(target);
			out.flush();			
			
		}catch(NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}
	@DeleteMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteNodes(@PathVariable("docId") String docId,@PathVariable("id") String id, @RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);			
			NodesDAO.deleteNodes(map, id);
		}
		catch(NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}
}
