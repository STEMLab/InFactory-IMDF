package database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.h2gis.ext.H2GISExtension;
import org.locationtech.jts.geom.Geometry;

import edu.pnu.stem.binder.Convert2Json;

/**
 * Short demo of in memory spatial database.
 */
public class Connector {
	private static void createTable(Statement st) throws SQLException {
		List<String> tableName = new ArrayList<String>();
		tableName.add("IndoorFeatures");
		tableName.add("PrimalSpaceFeatures");
		tableName.add("MultiLayeredGraph");
		tableName.add("CellSpace");
		tableName.add("CellSpaceBoundary");
		tableName.add("SpaceLayers");
		tableName.add("SpaceLayer");
		tableName.add("Nodes");
		tableName.add("Edges");
		tableName.add("State");
		tableName.add("Transition");
		tableName.add("InterEdges");
		tableName.add("InterLayerConnection");
		tableName.add("Geometry");
		tableName.add("Feature");

		st.execute("CREATE TABLE IF NOT EXISTS Documents (id CHAR(50),name CHAR(50))");
		st.execute(
				"CREATE TABLE IF NOT EXISTS IndoorFeatures (id CHAR(50), name CHAR(50), description CHAR(50), primalspacefeature CHAR(50), multilayeredgraph CHAR(50))");
		st.execute(
				"CREATE TABLE IF NOT EXISTS PrimalSpaceFeatures (id CHAR(50),parentId CHAR(50), name CHAR(50), description CHAR(50), cellspacemember array, cellspaceboundarymember array)");
		st.execute(
				"CREATE TABLE IF NOT EXISTS MultiLayeredGraph (id CHAR(50),parentId CHAR(50), name CHAR(50), description CHAR(50), interEdges array)");
		st.execute(
				"CREATE TABLE IF NOT EXISTS CellSpace (id CHAR(50),parentId CHAR(50), name CHAR(50), description CHAR(50), duality CHAR(50), partialBoundedBy array, geom CHAR(50))");
		st.execute(
				"CREATE TABLE IF NOT EXISTS CellSpaceBoundary (id CHAR(50),parentId CHAR(50), name CHAR(50), description CHAR(50), duality CHAR(50), geom CHAR(50) )");
		st.execute(
				"CREATE TABLE IF NOT EXISTS SpaceLayers (id CHAR(50),parentId CHAR(50), name CHAR(50), description CHAR(50), spaceLayerMember array) ");
		st.execute(
				"CREATE TABLE IF NOT EXISTS SpaceLayer (id CHAR(50),parentId CHAR(50), name CHAR(50), description CHAR(50), nodes array, edges array)");
		st.execute("CREATE TABLE IF NOT EXISTS Nodes (id CHAR(50),parentId CHAR(50), name CHAR(50), description CHAR(50), stateMember array)");
		st.execute("CREATE TABLE IF NOT EXISTS Edges (id CHAR(50),parentId CHAR(50), name CHAR(50), description CHAR(50), transitionMember array)");
		st.execute(
				"CREATE TABLE IF NOT EXISTS State (id CHAR(50),parentId CHAR(50), name CHAR(50), description CHAR(50), duality CHAR(50), geom CHAR(50))");
		st.execute(
				"CREATE TABLE IF NOT EXISTS Transition (id CHAR(50),parentId CHAR(50), name CHAR(50), description CHAR(50), duality CHAR(50), geom CHAR(50))");
		st.execute(
				"CREATE TABLE IF NOT EXISTS InterEdges (id CHAR(50),parentId CHAR(50), name CHAR(50), description CHAR(50), interLayerConnection array)");
		st.execute(
				"CREATE TABLE IF NOT EXISTS InterLayerConnection (id CHAR(50),parentId CHAR(50), name CHAR(50), description CHAR(50), interConnects array, connectedLayers array)");
		st.execute("CREATE TABLE IF NOT EXISTS Geometry (id CHAR(50), geom BLOB)");
		st.execute("CREATE TABLE IF NOT EXISTS Feature (id CHAR(50), type CHAR(50))");
	}

	public static byte[] changeGeometry2Binary(Geometry geom) throws IOException {
		byte[] result;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(geom);
		result = baos.toByteArray();

		return result;
	}
	
	public static void insertGeometry(Connection connection, String id, Geometry geom)
			throws IOException, SQLException {
		PreparedStatement pre = connection.prepareStatement("insert into geometry(id,geom)values(?,?)");
		byte[] serializedmember = changeGeometry2Binary(geom);
		pre.setString(1, id);
		pre.setBytes(2, serializedmember);
		pre.executeUpdate();
	}

	
	public static Geometry changeBinary2Geometry(Blob blob) throws SQLException, IOException, ClassNotFoundException {
		Geometry result = null;
		ByteArrayInputStream in = new ByteArrayInputStream(blob.getBytes(1, (int) blob.length()));
		ObjectInputStream is = new ObjectInputStream(in);
		result = (Geometry) is.readObject();
		return result;
	}

	public static Geometry searchGeometry(Connection connection, String id )
			throws SQLException, IOException, ClassNotFoundException {
		Geometry result = null;

		String getgeomsql = "SELECT geom FROM Geometry WHERE id = '" + id + "'";

		PreparedStatement pre = connection.prepareStatement(getgeomsql);
		ResultSet rs = pre.executeQuery();
		while (rs.next()) {
			Blob blob = rs.getBlob("geom");
			result = changeBinary2Geometry(blob);
		}

		return result;
	}

	public static void main(String[] args) {
		try {
			Class.forName("org.h2.Driver");
			// Open memory H2 table
			try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:syntax", "sa", "sa");
					Statement st = connection.createStatement()) {
				// Import spatial functions, domains and drivers
				// If you are using a file database, you have to do only that once.
				H2GISExtension.load(connection);
				// Create a table
				// st.execute("CREATE TABLE ROADS (the_geom MULTILINESTRING, speed_limit INT)");

				createTable(st);
				
				st.execute("INSERT INTO Documents VALUES ('doc1', 'testdata')");
				st.execute("INSERT INTO IndoorFeatures VALUES('lf1', 'indoorfeatures', null, 'psf1', 'mlg1')");
				st.execute(
						"INSERT INTO PrimalSpaceFeatures VALUES ('psf1','primalspacefeatures', null, ('c1'),('csb1'))");
				st.execute("INSERT INTO CellSpace VALUES ('c1','myroom',null,null,('csb1'),'cg1')");
				String cg1 = "SOLID (( ((0 0 0, 0 1 0, 1 1 0, 1 0 0, 0 0 0)), ((0 0 0, 0 1 0, 0 1 1, 0 0 1, 0 0 0)), ((0 0 0, 1 0 0, 1 0 1, 0 0 1, 0 0 0)), ((1 1 1, 1 0 1, 0 0 1, 0 1 1, 1 1 1)), ((1 1 1, 1 0 1, 1 0 0, 1 1 0, 1 1 1)), ((1 1 1, 1 1 0, 0 1 0, 0 1 1, 1 1 1)) ))";
				Geometry cg1g = Convert2Json.wkt2Geometry("cg1", cg1);

				insertGeometry(connection, "cg1", cg1g);
				cg1g = searchGeometry(connection, "cg1");
				
				

				// st.execute(qcg1);

				// Add some roads
				// st.execute("INSERT INTO ROADS VALUES ('MULTILINESTRING((15 5, 20 6, 25 7))',
				// 80)");
				// st.execute("INSERT INTO ROADS VALUES ('MULTILINESTRING((20 6, 21 15, 21
				// 25))', 50)");
				// Compute the sum of roads length
				/*
				 * try(ResultSet rs =
				 * st.executeQuery("SELECT SUM(ST_LENGTH(the_geom)) total_length from ROADS")) {
				 * if(rs.next()) {
				 * System.out.println("Total length of roads: "+rs.getDouble("total_length")
				 * +" m"); } }
				 */
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}