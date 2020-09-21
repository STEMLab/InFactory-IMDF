package edu.pnu.stem.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.locationtech.jts.geom.Geometry;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;
import edu.pnu.stem.feature.core.CellSpaceBoundary;
import edu.pnu.stem.feature.core.Edges;
import edu.pnu.stem.feature.core.IndoorFeatures;
import edu.pnu.stem.feature.core.InterEdges;
import edu.pnu.stem.feature.core.MultiLayeredGraph;
import edu.pnu.stem.feature.core.Nodes;
import edu.pnu.stem.feature.core.PrimalSpaceFeatures;
import edu.pnu.stem.feature.core.SpaceLayer;
import edu.pnu.stem.feature.core.SpaceLayers;
import edu.pnu.stem.feature.core.State;
import edu.pnu.stem.feature.core.Transition;
import edu.pnu.stem.util.GeometryUtil;

public class InsertMap {
	public static void insert(Connection connection, IndoorGMLMap map) throws IOException, SQLException {

		List<String> containerNameList = new ArrayList<String>();
		containerNameList.add("IndoorFeatures");
		containerNameList.add("MultiLayeredGraph");
		containerNameList.add("PrimalSpaceFeatures");
		containerNameList.add("CellSpace");
		containerNameList.add("CellSpaceBoundary");
		containerNameList.add("SpaceLayers");
		containerNameList.add("SpaceLayer");
		containerNameList.add("Nodes");
		containerNameList.add("Edges");
		containerNameList.add("State");
		containerNameList.add("Transition");
		containerNameList.add("Geometry");
		//containerNameList.add("Documents");
		Statement st = connection.createStatement();
		
		st.execute(createInsertSqlDocument(map));
		
		for (String name : containerNameList) {
			ConcurrentHashMap<String, Object> featureContainer = map.getFeatureContainer(name);
			for (Entry<String, Object> elem : featureContainer.entrySet()) {
				if (name.equals("Geometry"))
					insertGeometry(connection, map.getDocId(), GeometryUtil.getMetadata((Geometry) elem.getValue(), "id"),
							(Geometry) elem.getValue());
				else {
					String sql = createInsertSql(elem.getValue());
					
					try {
						
						System.out.println(sql);
						st.executeUpdate(sql);
					} catch (SQLException e) {
						System.out.println("error at insert " + name);
						e.printStackTrace();
					}
				}

			}
		}
		
		insertSql4Id(connection, map.getDocId(), map.getFeatureContainer("ID"));
		connection.commit();

	}
	
	public static String createInsertSqlDocument(IndoorGMLMap map) {
		String sql = null;
		String id = change2SqlString(map.getDocId());
		String name = null;
		
		sql = "Insert into " + "Documents" + " values(" + id + "," + name + ")";
		
		return sql;
		
	}

	public static String createInsertSql(Object feature) {
		String sql = null;
		if (feature instanceof IndoorFeatures)
			sql = createInsertSql4IndoorFeatures((IndoorFeatures) feature);
		else if (feature instanceof PrimalSpaceFeatures)
			sql = createInsertSql4PrimalSpaceFeatures((PrimalSpaceFeatures) feature);
		else if (feature instanceof CellSpace)
			sql = createInsertSql4CellSpace((CellSpace) feature);
		else if (feature instanceof CellSpaceBoundary)
			sql = createInsertSql4CellSpaceBoundary((CellSpaceBoundary) feature);
		else if (feature instanceof MultiLayeredGraph)
			sql = createInsertSql4MultiLayeredGraph((MultiLayeredGraph) feature);
		else if (feature instanceof SpaceLayers)
			sql = createInsertSql4SpaceLayers((SpaceLayers) feature);
		else if (feature instanceof SpaceLayer)
			sql = createInsertSql4SpaceLayer((SpaceLayer) feature);
		else if (feature instanceof Nodes)
			sql = createInsertSql4Nodes((Nodes) feature);
		else if (feature instanceof Edges)
			sql = createInsertSql4Edges((Edges) feature);
		else if (feature instanceof State)
			sql = createInsertSql4State((State) feature);
		else if (feature instanceof Transition)
			sql = createInsertSql4Transition((Transition) feature);

		return sql;
	}

	public static void insertSql4Id(Connection connection, String docId, ConcurrentHashMap<String, Object> map) {
		try {
			Statement st = connection.createStatement();
			for (Entry<String, Object> elem : map.entrySet()) {
				String sql = "Insert into " + "Feature" + " values(" + change2SqlString(docId)+","+change2SqlString(elem.getKey()) + "," + change2SqlString((String) elem.getValue()) + ")";
				
			
					st = connection.createStatement();
					st.execute(sql);
			}
			st.close();
			}catch (SQLException e) {
			System.out.println("error at insert feature id");
			e.printStackTrace();
			}
	}

	public static String change2SqlString(String n) {
		if (n != null)
			n = "'" + n + "'";
		return n;
	}

	public static String createInsertSql4IndoorFeatures(IndoorFeatures feature) {
		String tableName = "IndoorFeatures";

		String id = change2SqlString(feature.getId());
		String docId = change2SqlString(feature.getDocId());
		String name = change2SqlString(feature.getName());
		String description = change2SqlString(feature.getDescription());
		String primalspacefeatures = null;
		if(feature.getPrimalSpaceFeatures() != null)
			primalspacefeatures = change2SqlString(feature.getPrimalSpaceFeatures().getId());
		String multilayeredgraph = null;
		if(feature.getMultiLayeredGraph() != null)
			multilayeredgraph = change2SqlString(feature.getMultiLayeredGraph().getId());

		String sql = "Insert into " + tableName + " values(" +docId+ ","+ id + ","+ name + "," + description + ","
				+ primalspacefeatures + "," + multilayeredgraph + ")";
		return sql;

	}

	public static String createInsertSql4PrimalSpaceFeatures(PrimalSpaceFeatures feature) {
		String sql = null;
		String tableName = "PrimalSpaceFeatures";

		String id = change2SqlString(feature.getId());
		String name = change2SqlString(feature.getName());
		String docId = change2SqlString(feature.getDocId());
		String description = change2SqlString(feature.getDescription());
		String parentId = change2SqlString(feature.getParent().getId());
		String csm = null;
		String csbm = null;
		
		if (feature.getCellSpaceMember() != null && feature.getCellSpaceMember().size() != 0) {
			csm = "(";
			for (CellSpace c : feature.getCellSpaceMember()) {
				csm += change2SqlString(c.getId());
				csm += ',';
			}
			csm = csm.substring(0, csm.length() - 1);
			csm += ")";

		}

		if (feature.getCellSpaceBoundaryMember() != null && feature.getCellSpaceBoundaryMember().size() != 0) {
			csbm = "(";
			for (CellSpaceBoundary c : feature.getCellSpaceBoundaryMember()) {
				csbm += change2SqlString(c.getId());
				csbm += ',';
			}
			csbm = csbm.substring(0, csm.length() - 1);
			csbm += ")";

		}

		sql = "Insert into " + tableName + " values(" +docId+ ","+ id + "," + parentId + "," + name + "," + description + "," + csm
				+ "," + csbm + ")";
		return sql;

	}

	public static String createInsertSql4CellSpace(CellSpace feature) {
		String sql = null;
		String tableName = "CellSpace";

		String id = change2SqlString(feature.getId());
		String name = change2SqlString(feature.getName());
		String docId = change2SqlString(feature.getDocId());
		String description = change2SqlString(feature.getDescription());
		String parentId = change2SqlString(feature.getParent().getId());
		String duality = null;
		if(feature.getDuality() != null)
			duality = change2SqlString(feature.getDuality().getId());
		String geom = null;
		String partialboundedby = null;

		if (feature.getGeometry() != null) {
			geom = change2SqlString(GeometryUtil.getMetadata(feature.getGeometry(), "id"));
		}

		if (feature.getPartialboundedBy() != null && feature.getPartialboundedBy().size() != 0) {
			partialboundedby = "(";
			for (CellSpaceBoundary c : feature.getPartialboundedBy()) {
				partialboundedby += change2SqlString(c.getId());
				partialboundedby += ',';
			}
			partialboundedby = partialboundedby.substring(0, partialboundedby.length() - 1);
			partialboundedby += ")";
		}
		sql = "Insert into " + tableName + " values(" +docId+ ","+ id + "," + parentId + "," + name + "," + description + ","
				+ duality + "," + partialboundedby + "," + geom + ")";
		return sql;
	}

	public static String createInsertSql4CellSpaceBoundary(CellSpaceBoundary feature) {
		String sql = null;
		String tableName = "CellSpaceBoundary";

		String id = change2SqlString(feature.getId());
		String name = change2SqlString(feature.getName());
		String docId = change2SqlString(feature.getDocId());
		String description = change2SqlString(feature.getDescription());
		String parentId = change2SqlString(feature.getParent().getId());

		String duality = null;
		if(feature.getDuality() != null)
			duality = change2SqlString(feature.getDuality().getId());
		String geom = null;
		if (feature.getGeometry() != null) {
			geom = change2SqlString(GeometryUtil.getMetadata(feature.getGeometry(), "id"));
		}
		sql = "Insert into " + tableName + " values("+docId+ "," + id + "," + parentId + "," + name + "," + description + ","
				+ duality + "," + geom + ")";
		return sql;
	}

	public static String createInsertSql4MultiLayeredGraph(MultiLayeredGraph feature) {
		String sql = null;
		String tableName = "MultiLayeredGraph";

		String id = change2SqlString(feature.getId());
		String name = change2SqlString(feature.getName());
		String docId = change2SqlString(feature.getDocId());
		String description = change2SqlString(feature.getDescription());
		String parentId = change2SqlString(feature.getParent().getId());

		String csm = null;
		String csbm = null;

		if (feature.getSpaceLayers() != null && feature.getSpaceLayers().size() != 0) {
			csm = "(";
			for (SpaceLayers c : feature.getSpaceLayers()) {
				csm += change2SqlString(c.getId());
				csm += ',';
			}
			csm = csm.substring(0, csm.length() - 1);
			csm += ")";
		}

		if (feature.getInterEdges() != null && feature.getInterEdges().size() != 0) {
			csbm = "(";
			for (InterEdges c : feature.getInterEdges()) {
				csbm += change2SqlString(c.getId());
				csbm += ',';
			}
			csbm = csbm.substring(0, csm.length() - 1);
			csbm += ")";
		}

		sql = "Insert into " + tableName + " values(" +docId+ "," + id + "," + parentId + "," + name + "," + description + "," + csm
				+ "," + csbm + ")";
		return sql;
	}

	public static String createInsertSql4SpaceLayers(SpaceLayers feature) {
		String sql = null;
		String tableName = "SpaceLayers";
		String docId = change2SqlString(feature.getDocId());
		String id = change2SqlString(feature.getId());
		String name = change2SqlString(feature.getName());
		
		String description = change2SqlString(feature.getDescription());
		String parentId = change2SqlString(feature.getParent().getId());

		String csm = null;

		if (feature.getSpaceLayerMember() != null && feature.getSpaceLayerMember().size() != 0) {
			csm = "(";
			for (SpaceLayer c : feature.getSpaceLayerMember()) {
				csm += change2SqlString(c.getId());
				csm += ',';
			}
			csm = csm.substring(0, csm.length() - 1);
			csm += ")";

		}

		sql = "Insert into " + tableName + " values(" +docId+ "," + id + "," + parentId + "," + name + "," + description + "," + csm
				+ ")";
		return sql;
	}

	public static String createInsertSql4SpaceLayer(SpaceLayer feature) {
		String sql = null;
		String tableName = "SpaceLayer";
		String docId = change2SqlString(feature.getDocId());
		String id = change2SqlString(feature.getId());
		String name = change2SqlString(feature.getName());
		String description = change2SqlString(feature.getDescription());
		String parentId = change2SqlString(feature.getParent().getId());

		String csm = null;
		String csbm = null;

		if (feature.getNodes() != null && feature.getNodes().size() != 0) {
			csm = "(";
			for (Nodes c : feature.getNodes()) {
				csm += change2SqlString(c.getId());
				csm += ',';
			}
			csm = csm.substring(0, csm.length() - 1);
			csm += ")";
		}

		if (feature.getEdges() != null && feature.getEdges().size() != 0) {
			csbm = "(";
			for (Edges c : feature.getEdges()) {
				csbm += change2SqlString(c.getId());
				csbm += ',';
			}
			csbm = csbm.substring(0, csm.length() - 1);
			csbm += ")";

		}

		sql = "Insert into " + tableName + " values("+docId+ "," + id + "," + parentId + "," + name + "," + description + "," + csm
				+ "," + csbm + ")";
		return sql;
	}

	public static String createInsertSql4Nodes(Nodes feature) {
		String sql = null;
		String tableName = "Nodes";
		String docId = change2SqlString(feature.getDocId());
		String id = change2SqlString(feature.getId());
		String name = change2SqlString(feature.getName());
		String description = change2SqlString(feature.getDescription());
		String parentId = change2SqlString(feature.getParent().getId());

		String csm = null;

		if (feature.getStateMember() != null && feature.getStateMember().size() != 0) {
			csm = "(";
			for (State c : feature.getStateMember()) {
				csm += change2SqlString(c.getId());
				csm += ',';
			}
			csm = csm.substring(0, csm.length() - 1);
			csm += ")";

		}

		sql = "Insert into " + tableName + " values(" +docId+ ","+ id + "," + parentId + "," + name + "," + description + "," + csm
				+ ")";
		return sql;
	}

	public static String createInsertSql4Edges(Edges feature) {
		String sql = null;
		String tableName = "Edges";
		String docId = change2SqlString(feature.getDocId());
		String id = change2SqlString(feature.getId());
		String name = change2SqlString(feature.getName());
		String description = change2SqlString(feature.getDescription());
		String parentId = change2SqlString(feature.getParent().getId());

		String csm = null;

		if (feature.getTransitionMember() != null && feature.getTransitionMember().size() != 0) {
			csm = "(";
			for (Transition c : feature.getTransitionMember()) {
				csm += change2SqlString(c.getId());
				csm += ',';
			}
			csm = csm.substring(0, csm.length() - 1);
			csm += ")";

		}
		sql = "Insert into " + tableName + " values("+docId+ "," + id + "," + parentId + "," + name + "," + description + "," + csm
				+ ")";
		return sql;
	}

	public static String createInsertSql4State(State feature) {
		String sql = null;
		String tableName = "State";
		String docId = change2SqlString(feature.getDocId());
		String id = change2SqlString(feature.getId());
		String name = change2SqlString(feature.getName());
		String description = change2SqlString(feature.getDescription());
		String parentId = change2SqlString(feature.getParent().getId());

		String duality = null;
		if(feature.getDuality() != null)
			change2SqlString(feature.getDuality().getId());

		String geom = null;
		if (feature.getGeometry() != null)
			geom = change2SqlString(GeometryUtil.getMetadata(feature.getGeometry(), "id"));

		String connects = null;
		if (feature.getConnects() != null && feature.getConnects().size() != 0) {
			connects = "(";
			for (Transition c : feature.getConnects()) {
				connects += change2SqlString(c.getId());
				connects += ',';
			}
			connects = connects.substring(0, connects.length() - 1);
			connects += ")";

		}
		sql = "Insert into " + tableName + " values("+docId+ "," + id + "," + parentId + "," + name + "," + description + ","
				+ duality + "," + connects + "," + geom + ")";
		return sql;
	}

	public static String createInsertSql4Transition(Transition feature) {
		String sql = null;
		String tableName = "Transition";
		String docId = change2SqlString(feature.getDocId());
		String id = change2SqlString(feature.getId());
		String name = change2SqlString(feature.getName());
		String description = change2SqlString(feature.getDescription());
		String parentId = change2SqlString(feature.getParent().getId());

		String duality = null;
		if(feature.getDuality() != null)
			change2SqlString(feature.getDuality().getId());
		String geom = null;
		if (feature.getGeometry() != null)
			geom = change2SqlString(GeometryUtil.getMetadata(feature.getGeometry(), "id"));

		String connects = null;

		if (feature.getConnects() != null && feature.getConnects().length != 0) {
			connects = "(";
			for (State c : feature.getConnects()) {
				connects += change2SqlString(c.getId());
				connects += ',';
			}
			connects = connects.substring(0, connects.length() - 1);
			connects += ")";

		}
		sql = "Insert into " + tableName + " values("+docId+ "," + id + "," + parentId + "," + name + "," + description + ","
				+ duality + "," + connects + "," + geom + ")";
		return sql;
	}



	public static void insertGeometry(Connection connection,String docId, String id, Geometry geom)
			throws IOException, SQLException {
		PreparedStatement pre = connection.prepareStatement("insert into geometry(documentId,id,geom) values(?,?,?)");
		byte[] serializedmember = SqlUtil.changeGeometry2Binary(geom);
		pre.setString(1, docId);
		pre.setString(2, id);
		pre.setBytes(3, serializedmember);
		pre.executeUpdate();
	}
}
