/**
 * 
 */
package edu.pnu.stem.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.pnu.stem.api.exception.UndefinedDocumentException;
import edu.pnu.stem.binder.Convert2Json;
import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.dao.IndoorFeaturesDAO;
import edu.pnu.stem.dao.IndoorFeaturesForIMDFDAO;
import edu.pnu.stem.feature.imdf.Address;
import edu.pnu.stem.feature.imdf.FeatureInformation;
import edu.pnu.stem.feature.imdf.IndoorFeaturesForIMDF;
import edu.pnu.stem.feature.imdf.Labels;
import edu.pnu.stem.feature.imdf.Manifest;
import edu.pnu.stem.feature.imdf.Occupant;
import edu.pnu.stem.feature.imdf.Relationship;
import edu.pnu.stem.feature.imdf.Temporality;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.DIRECTION;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FEATURECATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.FEATUREREFERENCE;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.LANGUAGETAG;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.OCCUPANTCATEGORY;
import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.RELATIONSHIPCATEGORY;

@RestController
@RequestMapping("/documents/{docId}/IndoorFeaturesForIMDF")
public class IndoorFeaturesForIMDFController {

	@Autowired
	private ApplicationContext applicationContext;

	@PostMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createIndoorFeaturesForIMDF(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {

		String name = null;
		String description = null;

		String envelope = null;

		String multilayeredgraph = null;
		String primalspacefeatures = null;

		Manifest manifest = null;
		Address address = null;
		ArrayList<Occupant> occupantList = null;
		ArrayList<Relationship> relationshipList = null;

		if (id == null || id.isEmpty()) {
			id = UUID.randomUUID().toString();
		}

		if (json.has("properties")) {
			if (json.get("properties").has("name")) {
				name = json.get("properties").get("name").asText().trim();
			}
			if (json.get("properties").has("description")) {
				description = json.get("properties").get("description").asText().trim();
			}
			if (json.get("properties").has("multiLayeredGraph")) {
				multilayeredgraph = json.get("properties").get("multiLayeredGraph").asText().trim();
			}
			if (json.get("properties").has("primalSpaceFeatures")) {
				primalspacefeatures = json.get("properties").get("primalSpaceFeatures").asText().trim();
			}
			if (json.get("properties").has("envelope")) {
				envelope = json.get("properties").get("envelope").asText().trim();
			}
		}
		if (json.has("imdf")) {
			if (json.get("imdf").has("manifest")) {
				manifest = new Manifest();

				if (json.get("imdf").get("manifest").has("version")) {
					String version = json.get("imdf").get("manifest").get("version").asText().trim();
					manifest.setVersion(version);
				}
				if (json.get("imdf").get("manifest").has("created")) {
					String created = json.get("imdf").get("manifest").get("created").asText().trim();
					manifest.setCreated(created);

				}
				if (json.get("imdf").get("manifest").has("generated_by")) {
					String generated_by = json.get("imdf").get("manifest").get("generated_by").asText().trim();
					manifest.setGeneratedBy(generated_by);
				}
				if (json.get("imdf").get("manifest").has("language")) {
					String language = json.get("imdf").get("manifest").get("language").asText().trim();
					manifest.setLanguage(language);
				}
				if (json.get("imdf").get("manifest").has("extensions")) {
					String extensions = json.get("imdf").get("manifest").get("extensions").asText().trim();
					manifest.setExtensions(extensions);
				}

			}
			if (json.get("imdf").has("address")) {
				address = new Address();

				if (json.get("imdf").get("address").has("featureInformation")) {

					FeatureInformation featureInformation = new FeatureInformation();

					if (json.get("imdf").get("address").get("featureInformation").has("id")) {

						String str = json.get("imdf").get("address").get("featureInformation").get("id").asText()
								.trim();

						featureInformation.setId(str);
					}
					if (json.get("imdf").get("address").get("featureInformation").has("type")) {

						String str = json.get("imdf").get("address").get("featureInformation").get("type").asText()
								.trim();

						featureInformation.setType(str);
					}
					if (json.get("imdf").get("address").get("featureInformation").has("feature_type")) {

						String str = json.get("imdf").get("address").get("featureInformation").get("feature_type")
								.asText().trim();

						for (FEATURECATEGORY value : FEATURECATEGORY.values()) {
							if (str.toUpperCase().equals(value.toString())) {
								featureInformation.setFeatureType(value);
							}
						}
					}
					if (json.get("imdf").get("address").get("featureInformation").has("gml_geometry")) {

						String str = json.get("imdf").get("address").get("featureInformation").get("gml_geometry")
								.asText().trim();

						featureInformation.setGmlGeometry(str);
					}
					address.setFeatureInformation(featureInformation);
				}
				if (json.get("imdf").get("address").has("address")) {
					String addressString = json.get("imdf").get("address").get("address").asText().trim();
					address.setAddress(addressString);
				}
				if (json.get("imdf").get("address").has("unit")) {
					String unit = json.get("imdf").get("address").get("unit").asText().trim();
					address.setUnit(unit);
				}
				if (json.get("imdf").get("address").has("locality")) {
					String locality = json.get("imdf").get("address").get("locality").asText().trim();
					address.setLocality(locality);
				}
				if (json.get("imdf").get("address").has("province")) {
					String province = json.get("imdf").get("address").get("province").asText().trim();
					address.setProvince(province);
				}
				if (json.get("imdf").get("address").has("country")) {
					String country = json.get("imdf").get("address").get("country").asText().trim();
					address.setCountry(country);
				}
				if (json.get("imdf").get("address").has("postal_code")) {
					String postal_code = json.get("imdf").get("address").get("postal_code").asText().trim();
					address.setPostalCode(postal_code);
				}
				if (json.get("imdf").get("address").has("postal_code_ext")) {
					String postal_code_ext = json.get("imdf").get("address").get("postal_code_ext").asText().trim();
					address.setPostalCodeExt(postal_code_ext);
				}
				if (json.get("imdf").get("address").has("postal_code_vanity")) {
					String postal_code_vanity = json.get("imdf").get("address").get("postal_code_vanity").asText()
							.trim();
					address.setPostalCodeVanity(postal_code_vanity);
				}

			}
			if (json.get("imdf").has("occupant")) {

				occupantList = new ArrayList<Occupant>();

				JsonNode occupantJson = json.get("imdf").get("occupant");

				for (int i = 0; i < occupantJson.size(); i++) {

					Occupant occupant = new Occupant();

					if (occupantJson.get(i).has("featureInformation")) {

						FeatureInformation featureInformation = new FeatureInformation();

						if (occupantJson.get(i).get("featureInformation").has("id")) {

							String str = occupantJson.get(i).get("featureInformation").get("id").asText().trim();

							featureInformation.setId(str);
						}
						if (occupantJson.get(i).get("featureInformation").has("type")) {

							String str = occupantJson.get(i).get("featureInformation").get("type").asText().trim();

							featureInformation.setType(str);
						}
						if (occupantJson.get(i).get("featureInformation").has("feature_type")) {

							String str = occupantJson.get(i).get("featureInformation").get("feature_type").asText()
									.trim();

							for (FEATURECATEGORY value : FEATURECATEGORY.values()) {
								if (str.toUpperCase().equals(value.toString())) {
									featureInformation.setFeatureType(value);
								}
							}
						}
						if (occupantJson.get(i).get("featureInformation").has("gml_geometry")) {

							String str = occupantJson.get(i).get("featureInformation").get("gml_geometry").asText()
									.trim();

							featureInformation.setGmlGeometry(str);
						}
						occupant.setFeatureInformation(featureInformation);
					}
					if (occupantJson.get(i).has("category")) {
						String occupantCategry = occupantJson.get(i).get("category").asText().trim();
						for (OCCUPANTCATEGORY value : OCCUPANTCATEGORY.values()) {
							if (occupantCategry.toUpperCase().equals(value.toString())) {

								occupant.setCategory(value);
							}
						}
					}
					if (occupantJson.get(i).has("anchor_id")) {

						String anchor_id = occupantJson.get(i).get("anchor_id").asText().trim();
						occupant.setAnchorId(anchor_id);

					}
					if (occupantJson.get(i).has("hours")) {

						String hours = occupantJson.get(i).get("hours").asText().trim();
						occupant.setHours(hours);

					}
					if (occupantJson.get(i).has("ordinal")) {

						String ordinal = occupantJson.get(i).get("ordinal").asText().trim();
						occupant.setOrdinal(ordinal);

					}
					if (occupantJson.get(i).has("phone")) {

						JsonNode phone = occupantJson.get(i).get("phone");
						String[] phones = new String[phone.size()];
						for (int j = 0; j < phone.size(); j++) {
							phones[j] = phone.get(j).asText().trim();
						}

						occupant.setPhone(phones);

					}
					if (occupantJson.get(i).has("website")) {
						String website = occupantJson.get(i).get("website").asText().trim();
						occupant.setWebsite(website);

					}
					if (occupantJson.get(i).has("validity")) {
						Temporality temporality = new Temporality();
						if (occupantJson.get(i).get("validity").has("start")) {

							String start = occupantJson.get(i).get("validity").get("start").asText().trim();

							temporality.setStart(start);
						}
						if (occupantJson.get(i).get("validity").has("end")) {

							String end = occupantJson.get(i).get("validity").get("end").asText().trim();

							temporality.setEnd(end);
						}
						if (occupantJson.get(i).get("validity").has("modified")) {

							String modified = occupantJson.get(i).get("validity").get("modified").asText().trim();

							temporality.setModified(modified);
						}

						occupant.setValidity(temporality);

					}
					if (occupantJson.get(i).has("correlation_id")) {
						Labels correlation_id = new Labels();
						if (occupantJson.get(i).get("correlation_id").has("language")) {
							String language = occupantJson.get(i).get("correlation_id").get("language").asText().trim();
							for (LANGUAGETAG value : LANGUAGETAG.values()) {
								if (language.toUpperCase().equals(value.toString())) {

									correlation_id.setLanguage(value);
								}
							}

						}
						if (occupantJson.get(i).get("correlation_id").has("name")) {
							String name1 = occupantJson.get(i).get("correlation_id").get("name").asText().trim();

							correlation_id.setName(name1);
						}
						occupant.setCorrelationId(correlation_id);

					}
					occupantList.add(occupant);

				}

			}
			if (json.get("imdf").has("relationship")) {

				relationshipList = new ArrayList<Relationship>();

				JsonNode relationshipJSON = json.get("imdf").get("relationship");

				for (int i = 0; i < relationshipJSON.size(); i++) {

					Relationship relationship = new Relationship();

					if (relationshipJSON.get(i).has("featureInformation")) {
						FeatureInformation featureInformation = new FeatureInformation();

						if (relationshipJSON.get(i).get("featureInformation").has("id")) {

							String str = relationshipJSON.get(i).get("featureInformation").get("id").asText().trim();

							featureInformation.setId(str);
						}
						if (relationshipJSON.get(i).get("featureInformation").has("type")) {

							String str = relationshipJSON.get(i).get("featureInformation").get("type").asText().trim();

							featureInformation.setType(str);
						}
						if (relationshipJSON.get(i).get("featureInformation").has("feature_type")) {

							String feature_type = relationshipJSON.get(i).get("featureInformation").get("feature_type")
									.asText().trim();

							for (FEATURECATEGORY value : FEATURECATEGORY.values()) {
								if (feature_type.toUpperCase().equals(value.toString())) {
									featureInformation.setFeatureType(value);
								}
							}
						}
						if (relationshipJSON.get(i).get("featureInformation").has("gml_geometry")) {

							String gml_geometry = relationshipJSON.get(i).get("featureInformation").get("gml_geometry")
									.asText().trim();

							featureInformation.setGmlGeometry(gml_geometry);
						}
						relationship.setFeatureInformation(featureInformation);

					}
					if (relationshipJSON.get(i).has("category")) {
						RELATIONSHIPCATEGORY category = null;

						String str = relationshipJSON.get(i).get("category").asText().trim();

						for (RELATIONSHIPCATEGORY value : RELATIONSHIPCATEGORY.values()) {
							if (str.toUpperCase().equals(value.toString())) {
								category = value;
							}
						}
						relationship.setCategory(category);

					}
					if (relationshipJSON.get(i).has("direction")) {

						DIRECTION direction = null;
						String str = relationshipJSON.get(i).get("direction").asText().trim();
						for (DIRECTION value : DIRECTION.values()) {
							if (str.toUpperCase().equals(value.toString())) {
								direction = value;
							}
						}

						relationship.setDirection(direction);

					}
					if (relationshipJSON.get(i).has("hours")) {
						String hours = relationshipJSON.get(i).get("hours").asText().trim();
						relationship.setHours(hours);

					}
					if (relationshipJSON.get(i).has("destination")) {

						FEATUREREFERENCE destination = new FEATUREREFERENCE();

						if (relationshipJSON.get(i).get("destination").has("id")) {
							String str = relationshipJSON.get(i).get("destination").get("id").asText().trim();
							destination.setId(str);
						}
						if (relationshipJSON.get(i).get("destination").has("feature_type")) {
							String str = relationshipJSON.get(i).get("destination").get("feature_type").asText().trim();
							destination.setFeatureType(str);
						}

						relationship.setDestination(destination);

					}
					if (relationshipJSON.get(i).has("origin")) {
						FEATUREREFERENCE origin = new FEATUREREFERENCE();

						if (relationshipJSON.get(i).get("origin").has("id")) {
							String str = relationshipJSON.get(i).get("origin").get("id").asText().trim();
							origin.setId(str);
						}
						if (relationshipJSON.get(i).get("origin").has("feature_type")) {
							String str = relationshipJSON.get(i).get("origin").get("feature_type").asText().trim();
							origin.setFeatureType(str);
						}

						relationship.setOrigin(origin);

					}
					if (relationshipJSON.get(i).has("intermediary")) {
						ArrayList<FEATUREREFERENCE> intermediaryList = new ArrayList<FEATUREREFERENCE>();

						JsonNode intermediaryJSON = relationshipJSON.get(i).get("intermediary");
						for (int j = 0; j < intermediaryJSON.size(); j++) {
							FEATUREREFERENCE intermediary = new FEATUREREFERENCE();

							if (intermediaryJSON.get(j).has("id")) {
								String str = intermediaryJSON.get(j).get("id").asText().trim();
								intermediary.setId(str);
							}
							if (intermediaryJSON.get(j).has("feature_type")) {
								String str = intermediaryJSON.get(j).get("feature_type").asText().trim();
								intermediary.setFeatureType(str);
							}

							intermediaryList.add(intermediary);
						}
						relationship.setIntermediary(intermediaryList);

					}
					relationshipList.add(relationship);

				}

			}

		}

		IndoorFeaturesForIMDF f;

		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			f = IndoorFeaturesForIMDFDAO.createIndoorFeaturesForIMDF(map, id, name, description, envelope,
					multilayeredgraph, primalspacefeatures, manifest, address, occupantList, relationshipList);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
		response.setHeader("Location", request.getRequestURL().append(f.getId()).toString());

		// System.out.println("IndoorFeatures is created : "+id);
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.FOUND)
	public void getIndoorFeaturesForIMDF(@PathVariable("docId") String docId, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);

			ObjectNode target = Convert2Json.convert2JSON(map,
					IndoorFeaturesForIMDFDAO.readIndoorFeaturesForIMDF(map, id));
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
	public void deleteIndoorFeaturesForIMDF(@PathVariable("docId") String docId, @PathVariable("id") String id,
			@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			IndoorFeaturesDAO.deleteIndoorFeatures(map, id);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

}
