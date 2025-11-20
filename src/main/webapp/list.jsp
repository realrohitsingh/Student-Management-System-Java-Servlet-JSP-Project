<%@ page import="java.util.List"%>
<%@ page import="com.example.sms.Student"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%
List<Student> students = (List<Student>) request.getAttribute("students");
Integer currentPage = (Integer) request.getAttribute("page");
Integer totalPages = (Integer) request.getAttribute("totalPages");

// ALWAYS read search from request attribute (servlet)
String search = (String) request.getAttribute("search");
if (search == null)
	search = "";

String sortBy = (String) request.getAttribute("sortBy");
if (sortBy == null)
	sortBy = "id";

String sortDir = (String) request.getAttribute("sortDir");
if (sortDir == null)
	sortDir = "asc";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Student List</title>

<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

<style>
body.light {
	background: #f7f9fc;
	color: #000;
}

body.dark {
	background: #121212;
	color: #fff;
}

.container-box {
	background: white;
	border-radius: 12px;
	padding: 25px;
	margin-top: 20px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

body.dark .container-box {
	background: #1e1e1e;
	color: white;
}

table img {
	width: 60px;
	height: 60px;
	object-fit: cover;
	border-radius: 8px;
}
</style>

<script>
function toggleTheme() {
    const body = document.body;
    const newTheme = body.classList.contains("dark") ? "light" : "dark";
    body.className = newTheme;
    localStorage.setItem("theme", newTheme);
    document.getElementById("themeIcon").textContent =
        newTheme === "light" ? "🌙" : "☀️";
}

window.onload = () => {
    const saved = localStorage.getItem("theme") || "light";
    document.body.className = saved;
    document.getElementById("themeIcon").textContent =
        saved === "light" ? "🌙" : "☀️";
};
</script>

</head>
<body class="light">

	<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
		<div class="container">
			<a class="navbar-brand" href="students">Student Management</a>

			<div class="ms-auto d-flex gap-2">
				<button class="btn btn-light" onclick="toggleTheme()">
					<span id="themeIcon">🌙</span>
				</button>

				<a class="btn btn-warning" href="dashboard">Dashboard</a> <a
					class="btn btn-danger" href="logout">Logout</a>
			</div>
		</div>
	</nav>

	<div class="container">
		<div class="container-box">

			<div class="d-flex justify-content-between align-items-center mb-3">
				<h2 class="fw-bold">Students</h2>
				<a class="btn btn-success" href="students?action=new">+ Add
					Student</a>
			</div>

			<!-- SEARCH BAR -->
			<form class="input-group mb-3" method="get" action="students">
				<input type="text" class="form-control" name="search"
					value="<%=search%>" placeholder="Search by name, email or course">

				<button class="btn btn-primary" type="submit">Search</button>
			</form>

			<!-- TABLE -->
			<table class="table table-hover table-bordered align-middle">
				<thead class="table-light">
					<tr>
						<th><a
							href="students?search=<%=search%>&sortBy=id&sortDir=<%=sortDir.equals("asc") ? "desc" : "asc"%>">ID</a></th>
						<th><a
							href="students?search=<%=search%>&sortBy=name&sortDir=<%=sortDir.equals("asc") ? "desc" : "asc"%>">Name</a></th>
						<th><a
							href="students?search=<%=search%>&sortBy=email&sortDir=<%=sortDir.equals("asc") ? "desc" : "asc"%>">Email</a></th>
						<th><a
							href="students?search=<%=search%>&sortBy=course&sortDir=<%=sortDir.equals("asc") ? "desc" : "asc"%>">Course</a></th>
						<th>Photo</th>
						<th>Actions</th>
					</tr>
				</thead>

				<tbody>
					<%
					if (students != null && !students.isEmpty()) {
						for (Student s : students) {
					%>
					<tr>
						<td><%=s.getId()%></td>
						<td><%=s.getName()%></td>
						<td><%=s.getEmail()%></td>
						<td><%=s.getCourse()%></td>

						<td>
							<%
							if (s.getPhotoPath() != null) {
							%> <img
							src="<%=request.getContextPath() + "/" + s.getPhotoPath()%>"> <%
 } else {
 %>
							<span class="text-muted">No Photo</span> <%
 }
 %>
						</td>

						<td><a class="btn btn-warning btn-sm"
							href="students?action=edit&id=<%=s.getId()%>">Edit</a> <a
							class="btn btn-danger btn-sm"
							onclick="return confirm('Delete this student?')"
							href="students?action=delete&id=<%=s.getId()%>">Delete</a></td>
					</tr>

					<%
					}
					} else {
					%>
					<tr>
						<td colspan="6" class="text-center text-muted">No students
							found.</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>

			<!-- PAGINATION -->
			<nav>
				<ul class="pagination justify-content-center">

					<%
					if (currentPage > 1) {
					%>
					<li class="page-item"><a class="page-link"
						href="students?page=<%=currentPage - 1%>&search=<%=search%>&sortBy=<%=sortBy%>&sortDir=<%=sortDir%>">
							Previous </a></li>
					<%
					}
					%>

					<%
					for (int i = 1; i <= totalPages; i++) {
					%>
					<li class="page-item <%=(i == currentPage) ? "active" : ""%>">
						<a class="page-link"
						href="students?page=<%=i%>&search=<%=search%>&sortBy=<%=sortBy%>&sortDir=<%=sortDir%>">
							<%=i%>
					</a>
					</li>
					<%
					}
					%>

					<%
					if (currentPage < totalPages) {
					%>
					<li class="page-item"><a class="page-link"
						href="students?page=<%=currentPage + 1%>&search=<%=search%>&sortBy=<%=sortBy%>&sortDir=<%=sortDir%>">
							Next </a></li>
					<%
					}
					%>

				</ul>
			</nav>

		</div>
	</div>

	<footer class="text-center py-3 mt-4 bg-light">
		<p class="text-muted mb-0">&copy; 2025 Student Management System</p>
	</footer>

</body>
</html>