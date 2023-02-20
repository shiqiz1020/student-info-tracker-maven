<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>
	<head>
		<title>Student Tracker App</title>
	</head>
	
	<%-- <%
		// get students from request object
		List<Student> students = (List<Student>) request.getAttribute("STUDENT_LIST");
	%> --%>
	
	<body>
		<div>
			<div>
				<h2>Class xxx</h2>
			</div>
		</div>
		
		<div>
			<div>
				
				<!-- Add Student Button -->
				<input type="button" value="Add Student" 
					onclick="window.location.href='add-student-form.jsp'; return false;"
				/>
				
				<form action="StudentControllerServlet" method="GET">
	        
	                <input type="hidden" name="command" value="LIST" />
	                <input type="submit" value="View Student List"/>
	            
	            </form>
				
				<!--  add a search box -->
	            <form action="StudentControllerServlet" method="GET">
	        
	                <input type="hidden" name="command" value="SEARCH" />
	            
	                Search student: <input type="text" name="searchName" />
	                
	                <input type="submit" value="Search"/>
	            
	            </form>
				
				<br/>
			
				<table>
					<tr>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Email</th>
						<th>Action</th>
					</tr>
					
					<c:forEach var="student" items="${STUDENT_LIST}">
					
					<!-- set up a link for each student -->
					<c:url var="updateLink" value="StudentControllerServlet">
						<c:param name="command" value="LOAD" />
						<c:param name="studentId" value="${student.id}" />
					</c:url>

					<!--  set up a link to delete a student -->
					<c:url var="deleteLink" value="StudentControllerServlet">
						<c:param name="command" value="DELETE" />
						<c:param name="studentId" value="${student.id}" />
					</c:url>
																		
					<tr>
						<td> ${student.firstName} </td>
						<td> ${student.lastName} </td>
						<td> ${student.email} </td>
						<td> 
							<a href="${updateLink}">Update</a> 
							| 
							<a href="${deleteLink}"
							onclick="if (!(confirm('Are you sure you want to delete this student?'))) return false">
							Delete</a>
						</td>
					</tr>
				
				</c:forEach>
					
				</table>
			</div>
		</div>
	
	</body>

</html>