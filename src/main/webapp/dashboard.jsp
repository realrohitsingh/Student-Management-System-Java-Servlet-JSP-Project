<%@ page import="java.util.*" %>

<%
    int total = (int) request.getAttribute("totalStudents");
    Map<String, Integer> stats = (Map<String, Integer>) request.getAttribute("courseStats");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

    <style>
        body { background:#eef1f7; }
        .card-box {
            padding:25px;
            border-radius:10px;
            box-shadow:0 3px 10px rgba(0,0,0,0.1);
            background:white;
        }
        h1 { font-weight:700; }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">

        <a class="navbar-brand" href="dashboard">Dashboard</a>

        <div class="ms-auto d-flex gap-2">
            <a class="btn btn-light" href="students">Manage Students</a>
            <a class="btn btn-danger" href="logout">Logout</a>
        </div>
    </div>
</nav>


<div class="container mt-4">

    <div class="card-box mb-4">
        <h1>Total Students: <%= total %></h1>
    </div>

    <div class="card-box">
        <h3>Students Per Course</h3>
        <table class="table mt-3">
            <thead class="table-light">
                <tr>
                    <th>Course</th>
                    <th>Count</th>
                </tr>
            </thead>
            <tbody>
            <% for (String course : stats.keySet()) { %>
                <tr>
                    <td><%= course %></td>
                    <td><%= stats.get(course) %></td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
