<!DOCTYPE html>
<html>
	<head>
		<title>Add Student</title>
	</head>

	<body>
		<div>
			<div>
				<h2>Class xxx</h2>
			</div>
		</div>
		
		<div>
			<h3>Add Student</h3>
			
			<form action="StudentControllerServlet" method="POST">
				<input type="hidden" name="command" value="ADD"/>	
				<table>
					<tbody>
						<tr>
							<td><label>First name:</label></td>
							<td><input type="text" name="firstName" /></td>
						</tr>
						<tr>
							<td><label>Last name:</label></td>
							<td><input type="text" name="lastName" /></td>
						</tr>
						<tr>
							<td><label>Email:</label></td>
							<td><input type="text" name="email" /></td>
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