<!DOCTYPE html>
<html>
	<head>
		<title>Update Student</title>
	</head>

	<body>
		<div>
			<div>
				<h2>Class xxx</h2>
			</div>
		</div>
		
		<div>
			<h3>Update Student</h3>
			
			<form action="StudentControllerServlet" method="GET">
				<input type="hidden" name="command" value="UPDATE"/>
				<input type="hidden" name="studentId" value="${STUDENT.id}"/>	
				<table>
					<tbody>
						<tr>
							<td><label>First name:</label></td>
							<td><input type="text" name="firstName" value="${STUDENT.firstName}"/></td>
						</tr>
						<tr>
							<td><label>Last name:</label></td>
							<td><input type="text" name="lastName" value="${STUDENT.lastName}"/></td>
						</tr>
						<tr>
							<td><label>Email:</label></td>
							<td><input type="text" name="email" value="${STUDENT.email}"/></td>
						</tr>
						<tr>
							<td><label></label></td>
							<td><input type="submit" value="Save" /></td>
						</tr>
					</tbody>
				</table>		
			</form>
			
			<div></div>
			
			<p>
				<a href="StudentcontrollerServlet">Back to student list</a>
			</p>
		</div>
	</body>
</html>