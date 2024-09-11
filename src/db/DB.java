package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

	private static Connection conn = null;
	
	public static Connection getConnection() {
		
		if(conn == null) {
			
			try {
				Properties props = loadProperties();//pega as propriedades do banco de dados
				String url = props.getProperty("dburl");//pega a url do banco (criado no arquivo db.properties)
				conn = DriverManager.getConnection(url, props);
				
			} catch(SQLException e) {
				
				throw new DbException(e.getMessage());
			}
		}
		
		return conn;
	}
	
	public static void closeConnection() {//fecha a conexão
		
		if(conn != null) {
			
			try {
				
				conn.close();
				
			} catch(SQLException e) {
				
				throw new DbException(e.getMessage());
			}
		}
	}
	
	private static Properties loadProperties() {
		
		try(FileInputStream fs = new FileInputStream("db.properties")){
			
			Properties props = new Properties();
			props.load(fs);
			return props;
			
		} catch (IOException e) {
			
			throw new DbException(e.getMessage());
		}
	}

	public static void closeStatement(Statement st) {//para fechar com statement com try & catch
		
		if(st != null) {//verifica se o st está aberto
			
			try {
				
				st.close();
			} catch (SQLException e) {
				
				throw new DbException(e.getMessage());
			}
		}
	}

	public static void closeResultSet(ResultSet rs) {//para fechar com resultset com try & catch
		
		if(rs != null) {//verifica se o rs está aberto
			
			try {
				
				rs.close();
			} catch (SQLException e) {
				
				throw new DbException(e.getMessage());
			}
		}
	}
}
