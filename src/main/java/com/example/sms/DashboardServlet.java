package com.example.sms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private StudentDAOJdbc daoJdbc;

    @Override
    public void init() throws ServletException {
        daoJdbc = new StudentDAOJdbc();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // LOGIN PROTECTION
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login");
            return;
        }

        try {

            // Get total students
            int totalStudents = daoJdbc.count(null);

            // Get all students for stats
            List<Student> all = daoJdbc.getAll();

            // Group by course
            Map<String, Integer> perCourseCounts = new LinkedHashMap<>();
            for (Student s : all) {
                perCourseCounts.put(
                        s.getCourse(),
                        perCourseCounts.getOrDefault(s.getCourse(), 0) + 1
                );
            }

            // Pass values to JSP
            req.setAttribute("totalStudents", totalStudents);
            req.setAttribute("courseStats", perCourseCounts);

            req.getRequestDispatcher("dashboard.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
