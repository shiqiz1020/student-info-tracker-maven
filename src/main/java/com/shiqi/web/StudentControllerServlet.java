package com.shiqi.web;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.symbc.sequences.SymbolicSequenceListener;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	

	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// read command param
			String command = request.getParameter("command");
			
			switch (command) {
				case "LIST":
					listStudents(request, response);
					break;
				case "LOAD":
					loadStudent(request, response);
					break;
				case "UPDATE":
					updateStudent(request, response);
					break;
				case "DELETE":
					deleteStudent(request, response);
					break;
				case "SEARCH":
	                searchStudents(request, response);
	                break;
				default:
					listStudents(request, response);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// read command param
			String command = request.getParameter("command");
			
			switch (command) {
				case "ADD":
					addStudent(request, response);
					break;
				default:
					listStudents(request, response);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	private void searchStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
       String[] args = {"servlet.jpf"};
       Config config = JPF.createConfig(args);
       JPF jpf = new JPF(config);
       SymbolicSequenceListener listener = new SymbolicSequenceListener(config, jpf);
       jpf.addListener(listener);
       jpf.run();
		
		// read search name from form data
        String searchName = request.getParameter("searchName");
        
        // search students from db util
        List<Student> students = studentDbUtil.searchStudents(searchName);
        
        // add students to the request
        request.setAttribute("STUDENT_LIST", students);
                
        // send to JSP page (view)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);
    }

    public List<Student> symbolicSearchStudents(HttpServletRequest request, HttpServletResponse response, String searchName) throws Exception {
        List<Student> students = studentDbUtil.searchStudents(searchName);
        return students;
    }
	
	private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// read student id from form data
		String studentID = request.getParameter("studentId");
		
		// delete student from database
		studentDbUtil.deleteStudent(studentID);
		
		// send them back to "list students" page
		listStudents(request, response);
	}
	
	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create a new student object
		Student theStudent = new Student(id, firstName, lastName, email);
		
		// perform update on database
		studentDbUtil.updateStudent(theStudent);
		
		// send them back to the "list students" page
		listStudents(request, response);	
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) 
		throws Exception {

		// read student id from form data
		String studentID = request.getParameter("studentId");
		
		// get student from database (db util)
		Student student = studentDbUtil.getStudent(studentID);
		
		// place student in the request attribute
		request.setAttribute("STUDENT", student);
		
		// send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = 
				request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);		
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// get students from db util
		List<Student> students = studentDbUtil.getStudents();
		
		// add students to the request
		request.setAttribute("STUDENT_LIST", students);
		
		// send to JSP page
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}
	

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create new student object
		Student newStudent = new Student(firstName, lastName, email);
		
		// add student to db
		studentDbUtil.addStudent(newStudent);
		
		// send back to main page (student list)
		response.sendRedirect(request.getContextPath() + "/StudentControllerServlet?command=LIST");
	}

	// public static void main(String[] args) {
	// 	StudentControllerServlet servlet = new StudentControllerServlet();
	// 	try {
	// 		servlet.symbolicSearchStudents(null, null, "searchName");
	// 	} catch (Exception e) {

	// 	}
	// }	
}




