

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import protex.ShapeMatcher;

/**
 * Servlet implementation class for Servlet: PIGameServlet
 *
 */
 public class PIGameServlet extends HttpServlet implements Servlet {
	 
	 private ShapeMatcher shapeMatcher;
	 
	 public PIGameServlet() {
		super();
	}   	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String proteinSequence = (String) request.getAttribute("protein");
		String targetShape = (String) request.getAttribute("target");
		shapeMatcher = new ShapeMatcher(targetShape, false);
		
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}   	  	    
}