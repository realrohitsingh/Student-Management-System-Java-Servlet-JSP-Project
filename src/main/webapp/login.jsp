<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

    <style>
        body { background:#f5f6fa; }
        .login-box {
            max-width:400px;
            margin:auto;
            margin-top:80px;
            background:white;
            padding:30px;
            border-radius:10px;
            box-shadow:0 4px 12px rgba(0,0,0,0.1);
        }
        h2 { font-weight:700; }
    </style>
</head>
<body>

<div class="login-box">
    <h2 class="text-center mb-4">Admin Login</h2>

    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
    <% } %>

    <form method="post" action="login">
        <div class="mb-3">
            <label class="form-label">Username</label>
            <input class="form-control" name="username" placeholder="enter the username" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Password</label>
            <input class="form-control" type="password" name="password" placeholder="enter the password" required>
        </div>

        <button class="btn btn-primary w-100">Login</button>
    </form>
</div>

</body>
</html>
