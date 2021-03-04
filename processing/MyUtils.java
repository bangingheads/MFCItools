package processing;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;


public class MyUtils {
	static boolean useLocalHost = false;
	static final String networkHostName = "neuromancer.icts.uiowa.edu";
	static final String localHostName = "localhost";
	static Connection conn = null;
public static void Init() {


	System.setProperty("java.awt.headless", "true");
	try {
		Class.forName("org.postgresql.Driver");

		Properties props = new Properties();
		props.setProperty("user", "cmadlock");
		props.setProperty("password", "crmb261981");
		props.setProperty("sslfactory",
				"org.postgresql.ssl.NonValidatingFactory");
		props.setProperty("ssl", "true");
		conn = DriverManager.getConnection("jdbc:postgresql://"
				+ networkHostName + "/bioinformatics", props);

		PreparedStatement pathStmt = conn
				.prepareStatement("set search_path to scientometrics");
		pathStmt.executeUpdate();
		pathStmt.close();
	} catch (Exception e) {

	}

}
}
