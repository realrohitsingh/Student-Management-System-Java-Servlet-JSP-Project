<%@ page import="com.example.sms.Student" %>
<%
    Student s = (Student) request.getAttribute("student");
    boolean edit = (s != null);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= edit ? "Edit Student" : "Add Student" %></title>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

<style>
  body { background:#f7f9fc; padding:40px }
  .form-box {
      max-width:650px;
      margin:auto;
      background:white;
      padding:25px;
      border-radius:10px;
      box-shadow:0 3px 10px rgba(0,0,0,0.1);
  }
</style>
</head>
<body>

<div class="form-box">

  <h3 class="text-center mb-3"><%= edit ? "Edit Student" : "Add Student" %></h3>

  <!-- IMPORTANT FIX: enctype added -->
  <form method="post" action="students" enctype="multipart/form-data">

    <% if (edit) { %>
      <input type="hidden" name="action" value="update" />
      <input type="hidden" name="id" value="<%= s.getId() %>" />
    <% } else { %>
      <input type="hidden" name="action" value="insert" />
    <% } %>

    <div class="mb-3">
      <label class="form-label">Name</label>
      <input class="form-control" name="name" value="<%= edit ? s.getName() : "" %>" required />
    </div>

    <div class="mb-3">
      <label class="form-label">Email</label>
      <input class="form-control" name="email" value="<%= edit ? s.getEmail() : "" %>" required />
    </div>

    <div class="mb-3">
      <label class="form-label">Course</label>
      <input class="form-control" name="course" value="<%= edit ? s.getCourse() : "" %>" required />
    </div>

    <div class="mb-3">
      <label class="form-label">Photo (optional)</label>
      <input class="form-control" type="file" name="photo" accept="image/*" />

      <% if (edit && s.getPhotoPath() != null) { %>
          <div class="mt-2">
            <img src="<%= request.getContextPath() + "/" + s.getPhotoPath() %>" width="120" />
          </div>
      <% } %>
    </div>

    <button class="btn btn-success w-100" type="submit"><%= edit ? "Update" : "Add" %></button>

    <div class="mt-2">
      <a class="btn btn-secondary w-100" href="students">Back to list</a>
    </div>

  </form>

</div>
</body>
</html>
