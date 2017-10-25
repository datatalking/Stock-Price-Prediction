package bsds.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Connects to the AWS RDS MySQL Server
public class ConnectionManager {

	/*
	* AWS credentials for remote login
	* Command :
	* > mysql -h skiresort.cv4laudu9jlm.us-west-2.rds.amazonaws.com  -P 3306 -u divya -p
	* MySQL Commands to view the records :
	*  USE ski;
	*  SELECT * FROM Record;
	*  SELECT * FROM Record WHERE skierID = {skierID} AND DayNum = {DayNum};
	* */

	// Credentials for my instance of RDS on AWS
	private static final String ENDPOINT = "skiresort.cv4laudu9jlm.us-west-2.rds.amazonaws.com";
	private static final String PORT = "3306";

	private static final String URL_FORMAT = "jdbc:mysql://%s:%s/%s";

	private static final String DB = "ski";
	private static final String USER = "divya";
	private static final String PASSWORD = "bsds-pa2-sql";
	private static final String URL = String.format(URL_FORMAT, ENDPOINT, PORT, DB);

	// Establish connection by creating the URL
	public Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}


// UN-COMMENT FOR LOCAL MYSQL CONNECTION

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.Properties;

// Connects to the local database using the credentials.

//public class ConnectionManager {
//
//	private final String user = "root";
//	private final String password = "admin";
//	private final String hostName = "localhost";
//	private final int port= 3306;
//	private final String schema = "SkiResort";
//
//	// Establish Database connection
//	public Connection getConnection() throws SQLException {
//		Connection connection = null;
//		try {
//			Properties connectionProperties = new Properties();
//			connectionProperties.put("user", this.user);
//			connectionProperties.put("password", this.password);
//
//			// Ensure the JDBC driver is loaded by retrieving the runtime Class descriptor.
//			// Otherwise, Tomcat may have issues loading libraries in the proper order.
//			// One alternative is calling this in the HttpServlet init() override.
//			try {
//				Class.forName("com.mysql.jdbc.Driver");
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//			connection = DriverManager.getConnection(
//			    "jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.schema,
//			    connectionProperties);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw e;
//		}
//		return connection;
//	}
//}

