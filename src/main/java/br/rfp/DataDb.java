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
@WebServlet("/dataDb")
public class DataDb extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Connection connection;
	private Statement statement;
	ResultSet rs;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DataDb() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		conectar();
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		out.println("<html>\r\n");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		out.println("<link rel = \"stylesheet\" type=\"text/css\" href=\"_css/estilo.css\"/>\n");

		out.println("</head>");		
		out.println("<body>");
				
		
		try {
			out.println("<h2>Lista de dados cadastrados!</h2>");
			out.println("<br/>");
			rs = statement.executeQuery("SELECT * FROM testando");
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
		
		out.println("<div class=\"container\">");
		out.println("<table class=\"table table-fixed\">");
		out.println("<thead>");
		out.println("<tr>");
		out.println("<th scope=\"col\">Id</th>");
		out.println("<th scope=\"col\">Data</th>");
		out.println("<th scope=\"col\">Corrente</th>");
		out.println("<th scope=\"col\">Tensáo</th>");
		out.println("<th scope=\"col\">Ampere-Hora Total</th>");
		out.println("<th scope=\"col\">Potência Total</th>");
		out.println("<th scope=\"col\">Tensão de Barramento</th>");
		out.println("</tr>");
		out.println("</thead>");

		
		
		out.println("<tbody>");

		if (rs != null) {
            try {
                while (rs.next()) {
                	out.println("<tr>");
                	out.println("<th scope=\"row\"> " + rs.getString("id") + "</th>");
                	out.println("<th scope=\"row\"> " + rs.getString("data1") + "</th>");
                	out.println("<th scope=\"row\"> " + rs.getString("corrente") + "</th>");
                	out.println("<th scope=\"row\"> " + rs.getString("tensao") + "</th>");
                	out.println("<th scope=\"row\"> " + rs.getString("ahtotal") + "</th>");
                	out.println("<th scope=\"row\"> " + rs.getString("whtotal") + "</th>");
                	out.println("<th scope=\"row\"> " + rs.getString("vbus") + "</th>");
            		out.println("</tr>");

                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
		out.println("</tbody>");
		out.println("</table>");
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
