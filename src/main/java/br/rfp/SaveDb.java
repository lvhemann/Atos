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


/**
 * Servlet implementation class MyServletJDBC
 */
@WebServlet("/SaveDb")
public class SaveDb extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Connection connection;
	private Statement statement;
	ResultSet rs;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveDb() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nome = request.getParameter("nome");
		String email = request.getParameter("email");
		String cidade = request.getParameter("cidade");
		String userPass = request.getParameter("password");
		
		String userPass1 = encoder(userPass);
		System.out.println(nome);
		System.out.println(email);
		System.out.println(cidade);
		System.out.println(userPass);
		System.out.println(userPass1);
		conectar();
		
		// INSERIR A PESSOA
		String query = "INSERT INTO import (id, username, password, email, cidade) "
                + "values (Null,'"+nome+"','"+userPass1+"' ,'"+email+"', '"+cidade+"')";
        int status = executeUpdate(query);
		
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		out.println("<html>\r\n");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		out.println("<body>");
		if (status == 1) {
			out.println("<h3>A Pessoa "+ nome + " foi cadastrada com sucesso!</h3>");
		}
		

		//EXIBIR TODAS AS PESSOAS CADASTRADAS NO BD
		
		try {
			out.println("<h2>Lista de Pessoas cadastradas no banco de dados</h2>");
            rs = statement.executeQuery("SELECT * FROM import");
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
		

		if (rs != null) {
            try {
                while (rs.next()) {
                	out.println("<strong>ID: </strong>" + rs.getString("ID") + "<br>");
                    out.println("<strong>Nome: </strong>" + rs.getString("username") + "<br>");
                    out.println("<strong>Pass: </strong>" + rs.getString("password") + "<br>");
                    out.println("<strong>Email: </strong>" + rs.getString("email") + "<br>");
                    out.println("<strong>Cidade: </strong>" + rs.getString("cidade") + "<br><br>");
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
		
		
		out.println("</body>\r\n");
		out.println("</html>");
		
		
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
	
	
	@Bean
	public String encoder(String userPass) {
		return BCrypt.hashpw(userPass, BCrypt.gensalt(12));
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
