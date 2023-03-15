package com.shiqi.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import com.symbolic.db.SymbolicResultSet;
import com.symbolic.db.SymbolicStatement;
import com.symbolic.db.SymbolicConnection;
import com.symbolic.db.SymbolicDataSource;
import com.symbolic.db.SymbolicPreparedStatement;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.symbc.sequences.SymbolicSequenceListener;
import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.symbc.Symbolic;

public class StudentDbUtil {
	private DataSource dataSource;
	public SymbolicDataSource symDataSource;
	
//	public StudentDbUtil() {
//	 	dataSource = null;
//	}
	
//	public void test(int x, int y) {
//		int z=x-y;
////		 z = Debug.makeSymbolicInteger(Debug.getSymbolicIntegerValue(z));
//		
//		if (x > y && y > 0) {
//	        if (z > 0) {
//	            System.out.println("z>0");
//	        } else {
//	            System.out.println("z<=0");
//	        }
//		}
//	}	
	
	public StudentDbUtil(DataSource dataSource) {
		// initialize JPF
//		String[] args = {"dbutil.jpf"};
//		Config config = JPF.createConfig(args);
//		JPF jpf = new JPF(config);
//		SymbolicSequenceListener listener = new SymbolicSequenceListener(config, jpf);
//		jpf.addListener(listener);
		
		// create symbolic DataSource
		this.symDataSource = new SymbolicDataSource();
		
		// start JPF
//		jpf.run();
	}
	
	public List<Student> getStudents() throws Exception {
		List<Student> students = new ArrayList<>();
		
//		Connection realConnection = null;
//		Statement stmt = null;
//		ResultSet rs = null;
		
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
			// close JDBC objects
//			close(realConnection, stmt, rs);
//			this.searchStudents("key");
		}
	}

	private void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addStudent(Student newStudent) throws Exception {
//		Connection connection = null;
//		PreparedStatement stmt = null;
		
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
			// clean up jdbc objects
//			close(connection, stmt, null);
		}
	}
	
	public Student getStudent(String sid) throws Exception {

		Student student = null;
		
//		Connection connection = null;
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
		
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
		}
		finally {
			// clean up JDBC objects
//			close(connection, stmt, rs);
		}
	}
	
	public void updateStudent(Student student) throws Exception {	
//		Connection connection = null;
//		PreparedStatement stmt = null;

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
		}
		finally {
		// clean up JDBC objects
//			close(connection, stmt, null);
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
		}
		finally {
			// clean up JDBC code
//			close(connection, stmt, null);
		}	
	}
	
	public List<Student> searchStudents(String searchName, boolean next)  throws Exception {
        List<Student> students = new ArrayList<>();
        
//        if (next) {
//        	System.out.println("sfsdfsdfs");
//        } else {
//        	System.out.println("else");
//        }
        
        SymbolicPreparedStatement stmt = null;
        
        try {
            
            // get connection to database
        	SymbolicConnection symConnection = (SymbolicConnection) symDataSource.getConnection();
            
            // only search by name if theSearchName is not empty
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
            SymbolicResultSet rs = (SymbolicResultSet) stmt.executeQuery();
         
            // retrieve data from result set row
            if (rs.next()) {
                
                // retrieve data from result set row
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
//                firstName = Debug.makeSymbolicString(Debug.getSymbolicStringValue(firstName));
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                
                // create new student object
                Student newStudent = new Student(id, firstName, lastName, email);
                
                // add it to the list of students
                students.add(newStudent);   
                System.out.println("IFXXXXXXXX");
            } 
            else {
            	System.out.println("ELSEXXXXXXX");
            }
            
            return students;
        }
        finally {
            // clean up JDBC objects
//            close(connection, stmt, rs);
        }
    }
	
	public static void main(String[] args) {
        StudentDbUtil studentDbUtil = new StudentDbUtil(null);
        
        try{
        	SymbolicConnection symConnection = (SymbolicConnection) studentDbUtil.symDataSource.getConnection();
            List<Student> students = studentDbUtil.searchStudents("test", true);
//        	List<Student> students = studentDbUtil.searchStudents(1, 1);
        } catch (Exception e){

        }
	}	
	
}
