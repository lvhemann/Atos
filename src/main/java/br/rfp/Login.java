package br.rfp;

import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Servlet implementation class MyServletJDBC
 */
@WebServlet("/DoLogin")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Connection connection;
	private Statement statement;
	ResultSet rs;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userLogin = request.getParameter("userLogin");
		String userPass = (request.getParameter("userPass"));
		
		String userPass1 = encoder(userPass);
		
		System.out.println(userPass1);
		
		conectar();
    
        try {
            rs = statement.executeQuery("SELECT * FROM import where username='"+userLogin+"'");
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
		
        if (rs != null) {
            try {
                while (rs.next()) {
                	 System.out.println(rs.getString("ID"));
                	 System.out.println(rs.getString("username"));
                	 System.out.println(rs.getString("password"));
                	 String userDB = rs.getString("password");
                	 boolean ok = decodeString(userPass1, userDB);
                  	 System.out.println(rs.getString("role"));
                	 System.out.println(ok);
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }	   
	}    

	@Bean
	public String encoder(String userPass) {
		return BCrypt.hashpw(userPass, BCrypt.gensalt(12));
	}
	
	public boolean decodeString(String userPass1, String userDB) {
		boolean passwordValid = false;
		
	
		if (null == userDB || !userDB.startsWith("$2a$")) {
			throw new java.lang.IllegalArgumentException("invalid has");
		}
		
		passwordValid = BCrypt.checkpw(userPass1, userDB);
		
		return(passwordValid);
			
	}

	
	
	
	private void conectar() {
    	try {
			String address = "localhost";
			String port = "3306";
			String dataBaseName = "atos02";
			String user = "root";
			String password = "1003";
			
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://"+ address + ":" + port +"/"+ dataBaseName + "?user=" + user + "&password=" + password + "&useTimezone=true&serverTimezone=UTC"); 
			statement = connection.createStatement();
			
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    	
    }
	
	// Para inserções, alterações e exclusões   
    public int executeUpdate(String query) {     
        int status = 0;
        try {
        	statement = connection.createStatement();           
            status = statement.executeUpdate(query);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return status;
    }
    
    // Para consultas
    public ResultSet executeQuery(String query) {
        try {
        	statement = connection.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return rs;   
    }

	
	
	
	
}
