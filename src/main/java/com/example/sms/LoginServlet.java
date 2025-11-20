package com.example.sms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private LoginDAO loginDAO;

    @Override
    public void init() throws ServletException {
        loginDAO = new LoginDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (loginDAO.validate(username, password)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", username);
            resp.sendRedirect("dashboard");
        } else {
            req.setAttribute("error", "Invalid username or password");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
