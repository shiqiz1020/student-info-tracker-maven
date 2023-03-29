package com.shiqi.web;

//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.sql.DataSource;

import com.symbolic.db.SymbolicResultSet;
import com.symbolic.db.SymbolicStatement;
import com.symbolic.db.SymbolicConnection;
import com.symbolic.db.SymbolicDataSource;
import com.symbolic.db.SymbolicPreparedStatement;
import gov.nasa.jpf.symbc.Debug;

public class StudentDbUtil {
//	private DataSource dataSource;
	public SymbolicDataSource symDataSource;
	
	public StudentDbUtil(DataSource dataSource) {
		// create symbolic DataSource
		this.symDataSource = new SymbolicDataSource();
	}
	
	public List<Student> getStudents() throws Exception {
		List<Student> students = new ArrayList<>();
		
		try {
			// get connection
			SymbolicConnection symConnection = (SymbolicConnection) symDataSource.getConnection();
			
			// create sql statement
			String sql = "SELECT * FROM student ORDER BY last_name";
			SymbolicStatement stmt = (SymbolicStatement) symConnection.createStatement();
			
			// execute query
			SymbolicResultSet rs = (SymbolicResultSet) stmt.executeQuery(sql);
			
			// process result set
			while (rs.next()) {
				// retrieve data from result set row
				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				
				// create new student object
				Student newStudent = new Student(id, firstName, lastName, email);
				
				// add to student list
				students.add(newStudent);
			}
			return students;
		} finally {
			
		}
	}

	public void addStudent(Student newStudent) throws Exception {
		try {
			// get db connection
			SymbolicConnection symConnection = (SymbolicConnection) symDataSource.getConnection();
			
			// create sql for insert
			String sql = "INSERT INTO student " + "(first_name, last_name, email)" + "values (?, ?, ?)";
			SymbolicPreparedStatement stmt = (SymbolicPreparedStatement) symConnection.prepareStatement(sql);
		
			// set param values for student
			stmt.setString(1, newStudent.getFirstName());
			stmt.setString(2, newStudent.getLastName());
			stmt.setString(3, newStudent.getEmail());
			
			// execute sql insert
			stmt.execute();
		
		} finally {
			
		}
	}
	
	public Student getStudent(String sid) throws Exception {
		Student student = null;
		int studentId;
		
		try {
			// convert student id to int
			studentId = Integer.parseInt(sid);
			
			// get connection to database
			SymbolicConnection symConnection = (SymbolicConnection) symDataSource.getConnection();
			
			// create sql to get selected student
			String sql = "SELECT * FROM student WHERE id=?";
			
			// create prepared statement
			SymbolicPreparedStatement stmt = (SymbolicPreparedStatement) symConnection.prepareStatement(sql);
			
			// set params
			stmt.setInt(1, studentId);
			
			// execute statement
			SymbolicResultSet rs = (SymbolicResultSet) stmt.executeQuery();
			
			// retrieve data from result set row
			if (rs.next()) {
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				
				// use the studentId during construction
				student = new Student(studentId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find student id: " + studentId);
			}				
			
			return student;
		} finally {
			
		}
	}
	
	public void updateStudent(Student student) throws Exception {	
		try {
			// get db connection
			SymbolicConnection symConnection = (SymbolicConnection) symDataSource.getConnection();
			
			// create SQL update statement
			String sql = "UPDATE student "
						+ "SET first_name=?, last_name=?, email=? "
						+ "WHERE id=?";
			
			// prepare statement
			SymbolicPreparedStatement stmt = (SymbolicPreparedStatement) symConnection.prepareStatement(sql);
			
			// set params
			stmt.setString(1, student.getFirstName());
			stmt.setString(2, student.getLastName());
			stmt.setString(3, student.getEmail());
			stmt.setInt(4, student.getId());
			
			// execute SQL statement
			stmt.execute();
		} finally {
			
		}
	}
	
	public void deleteStudent(String sid) throws Exception {
		try {
			// convert student id to int
			int studentId = Integer.parseInt(sid);
			
			// get connection to database
			SymbolicConnection symConnection = (SymbolicConnection) symDataSource.getConnection();
			
			// create sql to delete student
			String sql = "DELETE FROM student WHERE id=?";
			
			// prepare statement
			SymbolicPreparedStatement stmt = (SymbolicPreparedStatement) symConnection.prepareStatement(sql);
			
			// set params
			stmt.setInt(1, studentId);
			
			// execute sql statement
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Student> searchStudents(String searchName)  throws Exception {
        try {
        	List<Student> students = new ArrayList<>();
            SymbolicPreparedStatement stmt;
            
            // get connection to database
        	SymbolicConnection symConnection = (SymbolicConnection) symDataSource.getConnection();
            
            // only search by name if theSearchName is not empty
//        	int searchNameLength = searchName.trim().length();
//        	searchNameLength = Debug.makeSymbolicInteger(Debug.getSymbolicIntegerValue(searchNameLength));
            if (searchName != null && searchName.trim().length() > 0) {
                // create sql to search for students by name
                String sql = "SELECT * FROM student WHERE lower(first_name) LIKE ? OR lower(last_name) LIKE ?";
                
                // create prepared statement
                stmt = (SymbolicPreparedStatement) symConnection.prepareStatement(sql);
                
                // set params
//                String searchNameLike = "%" + searchName.toLowerCase() + "%";
                String searchNameLike = "%" + searchName + "%";
                searchNameLike = Debug.makeSymbolicString(Debug.getSymbolicStringValue(searchNameLike));
                stmt.setString(1, searchNameLike);
                stmt.setString(2, searchNameLike);
            } else {
                // create sql to get all students
                String sql = "SELECT * FROM student ORDER BY last_name";
                // create prepared statement
                stmt = (SymbolicPreparedStatement) symConnection.prepareStatement(sql);
            }
                
            // execute statement
            String result = Debug.getPC_prefix_notation();
            SymbolicResultSet rs = (SymbolicResultSet) stmt.executeQuery();
//            printPC();
            printPC(result, symConnection);
         
            // retrieve data from result set row
            while (rs.next()) {
                // retrieve data from result set row
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                
                // create new student object
                Student newStudent = new Student(id, firstName, lastName, email);
                
                // add it to the list of students
                students.add(newStudent);   
                
                if (firstName.equals("Admin")) {
                	String sql = "SELECT * FROM student WHERE first_name = ? AND last_name = ?";
                	stmt = (SymbolicPreparedStatement) symConnection.prepareStatement(sql);
                	stmt.setString(1, "John");
                	stmt.setString(2, "Doe");
                	result = Debug.getPC_prefix_notation();
                	SymbolicResultSet adminRs = (SymbolicResultSet) stmt.executeQuery();
//                	printPC();
                	printPC(result, symConnection);
                	
                	if (adminRs.next()) {
                		sql = "SELECT email FROM student WHERE first_name = John AND last_name LIKE = Doe";
                		stmt = (SymbolicPreparedStatement) symConnection.prepareStatement(sql);
                		result = Debug.getPC_prefix_notation();
                		SymbolicResultSet jdRs = (SymbolicResultSet) stmt.executeQuery();
//                    	printPC();
                		printPC(result, symConnection);
                    	
                    	id = jdRs.getInt("id");
                        firstName = jdRs.getString("first_name");
                        lastName = jdRs.getString("last_name");
                        email = jdRs.getString("email");
                        
                        sql = "UPDATE student "
        						+ "SET first_name = ? , last_name = ? , email = ? "
        						+ "WHERE id = ? ";
        			
    	    			// prepare statement
                    	stmt = (SymbolicPreparedStatement) symConnection.prepareStatement(sql);

//    	    			// set params
                    	stmt.setString(1, firstName);
                    	stmt.setString(2, lastName);
                    	stmt.setString(3, email);
                    	stmt.setInt(4, id);
    	    			
                    	result = Debug.getPC_prefix_notation();
                    	stmt.execute();
//    	    			printPC();
                    	printPC(result, symConnection);
                    	
                	}
                } else if (id > 100) {
                	String sql = "UPDATE student "
    						+ "SET first_name = ? , last_name = ? , email = ? , included = ? "
    						+ "WHERE id = ? ";
    			
	    			// prepare statement
                	stmt = (SymbolicPreparedStatement) symConnection.prepareStatement(sql);

//	    			// set params
                	stmt.setString(1, firstName);
                	stmt.setString(2, lastName);
                	stmt.setString(3, email);
                	stmt.setBoolean(4, false);
                	stmt.setInt(5, id);
	    			
                	result = Debug.getPC_prefix_notation();
                	stmt.execute();
//	    			printPC();
                	printPC(result, symConnection);
                }
            } 
            return students;
        }
        finally {
        }
    }
	
	private void printPC() {
		System.out.println("\n===== SOLVED PATH CONSTRAINT SO FAR =====");
		System.out.println(Debug.getPC_prefix_notation());
    	System.out.println("##################################################\n");
	}
	
	private void printPC(String result, SymbolicConnection symConnection) {
		System.out.println("\n===== SOLVED PATH CONSTRAINT SO FAR =====");
    	String[] splitResult = result.split("\\n");
//    	for (int i=0; i < splitResult.length; i++) {
//    		System.out.println(i + ": " + splitResult[i]);
//    	}
    	System.out.println(splitResult[1]);
    	if (splitResult.length > 2) {
    		System.out.println(splitResult[2]);
    	}
    	System.out.println();
    	
    	Set<String> allMatches = new HashSet<String>();
    	Matcher matcher = Pattern.compile("\\s#(\\d+)#_").matcher(result);
    	while (matcher.find()) {
    		String r = matcher.group(1);
    		if (!allMatches.contains(r)) {
    			allMatches.add(r);
    			symConnection.printRelatedSql(Integer.parseInt(r));
    		}
    		
//    		System.out.println(r);
    	}
    	System.out.println("##################################################\n");
	}
	
	public static void main(String[] args) {
        StudentDbUtil studentDbUtil = new StudentDbUtil(null);
        
        try{
            List<Student> students = studentDbUtil.searchStudents("test");
        } catch (Exception e){

        }
	}	
	
}
