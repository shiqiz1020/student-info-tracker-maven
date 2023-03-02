package com.shiqi.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.annotation.Resource;

// import gov.nasa.jpf.symbc.Debug;

public class StudentDbUtil {
	private DataSource dataSource;

	// public StudentDbUtil() {
	// 	dataSource = null;
	// }
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception {
		List<Student> students = new ArrayList<>();
		
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			// get connection
			connection = dataSource.getConnection();
			
			// create sql statement
			String sql = "SELECT * FROM student ORDER BY last_name";
			stmt = connection.createStatement();
			
			// execute query
			rs = stmt.executeQuery(sql);
			
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
			close(connection, stmt, rs);
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
		Connection connection = null;
		PreparedStatement stmt = null;
		
		try {
			// get db connection
			connection = dataSource.getConnection();
			
			// create sql for insert
			String sql = "INSERT INTO student " + "(first_name, last_name, email)" + "values (?, ?, ?)";
			stmt = connection.prepareStatement(sql);
		
			// set param values for student
			stmt.setString(1, newStudent.getFirstName());
			stmt.setString(2, newStudent.getLastName());
			stmt.setString(3, newStudent.getEmail());
			
			// execute sql insert
			stmt.execute();
		
		} finally {
			// clean up jdbc objects
			close(connection, stmt, null);
		}
	}
	
	public Student getStudent(String sid) throws Exception {

		Student student = null;
		
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int studentId;
		
		try {
			// convert student id to int
			studentId = Integer.parseInt(sid);
			
			// get connection to database
			connection = dataSource.getConnection();
			
			// create sql to get selected student
			String sql = "SELECT * FROM student WHERE id=?";
			
			// create prepared statement
			stmt = connection.prepareStatement(sql);
			
			// set params
			stmt.setInt(1, studentId);
			
			// execute statement
			rs = stmt.executeQuery();
			
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
			close(connection, stmt, rs);
		}
	}
	
	public void updateStudent(Student student) throws Exception {	
		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			// get db connection
			connection = dataSource.getConnection();
			
			// create SQL update statement
			String sql = "UPDATE student "
						+ "SET first_name=?, last_name=?, email=? "
						+ "WHERE id=?";
			
			// prepare statement
			stmt = connection.prepareStatement(sql);
			
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
			close(connection, stmt, null);
		}
	}
	
	public void deleteStudent(String sid) throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		
		try {
			// convert student id to int
			int studentId = Integer.parseInt(sid);
			
			// get connection to database
			connection = dataSource.getConnection();
			
			// create sql to delete student
			String sql = "DELETE FROM student WHERE id=?";
			
			// prepare statement
			stmt = connection.prepareStatement(sql);
			
			// set params
			stmt.setInt(1, studentId);
			
			// execute sql statement
			stmt.execute();
		}
		finally {
			// clean up JDBC code
			close(connection, stmt, null);
		}	
	}
	
	public List<Student> searchStudents(String searchName)  throws Exception {
        List<Student> students = new ArrayList<>();
        
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            
            // get connection to database
        	connection = dataSource.getConnection();
            
            // only search by name if theSearchName is not empty
            if (searchName != null && searchName.trim().length() > 0) {
                // create sql to search for students by name
                String sql = "SELECT * FROM student WHERE lower(first_name) LIKE ? OR lower(last_name) LIKE ?";
                // create prepared statement
                stmt = connection.prepareStatement(sql);
                // set params
                String searchNameLike = "%" + searchName.toLowerCase() + "%";
                stmt.setString(1, searchNameLike);
                stmt.setString(2, searchNameLike);
                
            } else {
                // create sql to get all students
                String sql = "SELECT * FROM student ORDER BY last_name";
                // create prepared statement
                stmt = connection.prepareStatement(sql);
            }
            
            // execute statement
            rs = stmt.executeQuery();
            
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
            }
            
            return students;
        }
        finally {
            // clean up JDBC objects
            close(connection, stmt, rs);
        }
    }
	
	// public static void main(String[] args) {
    //     StudentDbUtil studentDbUtil = new StudentDbUtil();
    //     try{
    //         List<Student> students = studentDbUtil.searchStudents("test");
    //     } catch (Exception e){

    //     }
	// }	
	
}
