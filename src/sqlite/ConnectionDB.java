package sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


public class ConnectionDB {
    private static boolean logs = false;
    private static boolean inMemory = false;
	
	private static Connection conn = null;
	
    public static Connection connect() throws SQLException {
        try {
        	String dir = System.getProperty("user.dir");

           String url = String.format(
                            "jdbc:sqlite:%s" + "database.db", 
                            dir + "/src/Data/"
    				    );

           if (inMemory) {
                url = "jdbc:sqlite::memory:";
           }

            if (conn == null) {
            	conn = DriverManager.getConnection(url);
            	logger("Connection to SQLite has been established.");
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
            throw e;
        }
        
        return conn;
    }
    
    public static void disconnect() {
    	try {
    		logger("Connection to SQLite has been disconnected");
    		
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static ResultSet executeSQL(String sql) {
        try {
        	Connection conection = ConnectionDB.connect();
        	Statement stmt = conection.createStatement();
        	
        	logger(sql);
        	ResultSet rs = stmt.executeQuery(sql);
            
        	return rs;
        } catch (SQLException e) {
        	logger(e.getMessage());
        }
        
		return null;
    }

    public static boolean executeSQLOperation(String sql) {
        try {
        	Connection conection = ConnectionDB.connect();
        	Statement stmt = conection.createStatement();
        	
        	logger(sql);
        	return stmt.execute(sql);

        } catch (SQLException e) {
            logger(e.getMessage());

        	return false;
        }
        
    }
    
    private static void logger(String text) {
    	if ( logs ) {
    		System.out.println("SQLITECONNECTION: " + text);
    	}
    }
}
