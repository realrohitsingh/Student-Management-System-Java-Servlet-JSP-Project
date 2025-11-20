package com.example.sms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/students")
@MultipartConfig
public class StudentServlet extends HttpServlet {

	private StudentDAOJdbc daoJdbc;

	@Override
	public void init() throws ServletException {
		daoJdbc = new StudentDAOJdbc();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// -----------------------
		// LOGIN PROTECTION
		// -----------------------
		HttpSession session = req.getSession(false);
		boolean loggedIn = (session != null && session.getAttribute("user") != null);

		String action = req.getParameter("action");
		if (action == null)
			action = "list";

		// Allow LIST page WITHOUT forcing login
		if (!loggedIn && !action.equals("list")) {
			resp.sendRedirect("login");
			return;
		}

		try {
			switch (action) {

			case "new":
				req.getRequestDispatcher("form.jsp").forward(req, resp);
				break;

			case "edit": {
				int id = Integer.parseInt(req.getParameter("id"));
				Student s = daoJdbc.get(id);
				req.setAttribute("student", s);
				req.getRequestDispatcher("form.jsp").forward(req, resp);
				break;
			}

			case "delete": {
				int id = Integer.parseInt(req.getParameter("id"));
				daoJdbc.delete(id);
				resp.sendRedirect("students");
				break;
			}

			default:
				handleList(req, resp);
			}

		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	private void handleList(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, ServletException, IOException {

		// Read parameters safely
		String search = req.getParameter("search");
		String sortBy = req.getParameter("sortBy");
		String sortDir = req.getParameter("sortDir");
		String pageStr = req.getParameter("page");

		if (search == null)
			search = "";
		if (sortBy == null)
			sortBy = "id";
		if (sortDir == null)
			sortDir = "asc";

		int page = (pageStr == null) ? 1 : Integer.parseInt(pageStr);
		int pageSize = 5;

		// Count total results WITH SEARCH
		int total = daoJdbc.count(search);
		int totalPages = (int) Math.ceil((double) total / pageSize);

		if (page < 1)
			page = 1;
		if (page > totalPages && totalPages > 0)
			page = totalPages;

		// Fetch results
		List<Student> students = daoJdbc.getPaged(page, pageSize, search, sortBy, sortDir);

		// Pass everything to JSP
		req.setAttribute("students", students);
		req.setAttribute("page", page);
		req.setAttribute("totalPages", totalPages);
		req.setAttribute("search", search);
		req.setAttribute("sortBy", sortBy);
		req.setAttribute("sortDir", sortDir);

		req.getRequestDispatcher("list.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		// LOGIN PROTECTION
		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			resp.sendRedirect("login");
			return;
		}

		String action = req.getParameter("action");
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String course = req.getParameter("course");

		// ---- Handle photo upload ----
		Part photo = req.getPart("photo");
		String photoPath = null;

		if (photo != null && photo.getSize() > 0) {

			String filename = Paths.get(photo.getSubmittedFileName()).getFileName().toString();
			String uploadFolder = req.getServletContext().getRealPath("/uploads");

			File folder = new File(uploadFolder);
			if (!folder.exists())
				folder.mkdirs();

			String newFileName = System.currentTimeMillis() + "_" + filename;
			File savedFile = new File(folder, newFileName);

			try (InputStream in = photo.getInputStream()) {
				Files.copy(in, savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}

			photoPath = "uploads/" + newFileName;
		}

		try {
			if ("update".equals(action)) {

				int id = Integer.parseInt(req.getParameter("id"));
				Student s = daoJdbc.get(id);

				s.setName(name);
				s.setEmail(email);
				s.setCourse(course);

				if (photoPath != null)
					s.setPhotoPath(photoPath);

				daoJdbc.update(s);

			} else {

				Student newStudent = new Student(0, name, email, course, photoPath);
				daoJdbc.add(newStudent);
			}

			resp.sendRedirect("students");

		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}
}
