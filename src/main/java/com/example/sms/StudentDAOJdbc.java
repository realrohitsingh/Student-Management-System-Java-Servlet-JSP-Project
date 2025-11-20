package com.example.sms;

import java.sql.*;
import java.util.*;

public class StudentDAOJdbc {

	private final String url = "jdbc:mysql://localhost:3306/sms_db?useUnicode=true&characterEncoding=utf8";
	private final String user = "root";
	private final String pass = "Rohit888399";

	public StudentDAOJdbc() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception ignored) {
		}
	}

	// ---------------- BASIC CRUD ----------------

	public void add(Student s) throws SQLException {
		String sql = "INSERT INTO students (name, email, course, photo_path) VALUES (?,?,?,?)";
		try (Connection c = DriverManager.getConnection(url, user, pass);
				PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			p.setString(1, s.getName());
			p.setString(2, s.getEmail());
			p.setString(3, s.getCourse());
			p.setString(4, s.getPhotoPath());
			p.executeUpdate();

			try (ResultSet rs = p.getGeneratedKeys()) {
				if (rs.next())
					s.setId(rs.getInt(1));
			}
		}
	}

	public List<Student> getAll() throws SQLException {
		String sql = "SELECT * FROM students ORDER BY id DESC";
		List<Student> list = new ArrayList<>();

		try (Connection c = DriverManager.getConnection(url, user, pass);
				PreparedStatement p = c.prepareStatement(sql);
				ResultSet rs = p.executeQuery()) {

			while (rs.next())
				list.add(mapRow(rs));
		}
		return list;
	}

	public void update(Student s) throws SQLException {
		String sql = "UPDATE students SET name=?, email=?, course=?, photo_path=? WHERE id=?";
		try (Connection c = DriverManager.getConnection(url, user, pass);
				PreparedStatement p = c.prepareStatement(sql)) {

			p.setString(1, s.getName());
			p.setString(2, s.getEmail());
			p.setString(3, s.getCourse());
			p.setString(4, s.getPhotoPath());
			p.setInt(5, s.getId());
			p.executeUpdate();
		}
	}

	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM students WHERE id=?";
		try (Connection c = DriverManager.getConnection(url, user, pass);
				PreparedStatement p = c.prepareStatement(sql)) {

			p.setInt(1, id);
			p.executeUpdate();
		}
	}

	public Student get(int id) throws SQLException {
		String sql = "SELECT * FROM students WHERE id=?";
		try (Connection c = DriverManager.getConnection(url, user, pass);
				PreparedStatement p = c.prepareStatement(sql)) {

			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			if (rs.next())
				return mapRow(rs);
		}
		return null;
	}

	// ---------------- SEARCH + SORT + PAGINATION ----------------

	public int count(String search) throws SQLException {
		boolean hasSearch = search != null && !search.isBlank();
		String sql = "SELECT COUNT(*) FROM students";

		if (hasSearch) {
			sql += " WHERE name LIKE ? OR email LIKE ? OR course LIKE ?";
		}

		try (Connection c = DriverManager.getConnection(url, user, pass);
				PreparedStatement p = c.prepareStatement(sql)) {

			if (hasSearch) {
				String q = "%" + search + "%";
				p.setString(1, q);
				p.setString(2, q);
				p.setString(3, q);
			}

			ResultSet rs = p.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		}
		return 0;
	}

	public List<Student> getPaged(int page, int pageSize, String search, String sortBy, String sortDir)
			throws SQLException {

		List<String> validSorts = List.of("id", "name", "email", "course");
		if (sortBy == null || !validSorts.contains(sortBy)) {
			sortBy = "id";
		}

		if (sortDir == null || (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc"))) {
			sortDir = "asc"; // default ascending
		}

		StringBuilder sql = new StringBuilder("SELECT * FROM students");

		boolean hasSearch = search != null && !search.isBlank();
		List<Object> params = new ArrayList<>();

		if (hasSearch) {
			sql.append(" WHERE name LIKE ? OR email LIKE ? OR course LIKE ?");
			String q = "%" + search + "%";
			params.add(q);
			params.add(q);
			params.add(q);
		}

		sql.append(" ORDER BY ").append(sortBy).append(" ").append(sortDir);
		sql.append(" LIMIT ? OFFSET ?");

		params.add(pageSize);
		params.add((page - 1) * pageSize);

		List<Student> list = new ArrayList<>();

		try (Connection c = DriverManager.getConnection(url, user, pass);
				PreparedStatement p = c.prepareStatement(sql.toString())) {

			for (int i = 0; i < params.size(); i++) {
				p.setObject(i + 1, params.get(i));
			}

			ResultSet rs = p.executeQuery();
			while (rs.next())
				list.add(mapRow(rs));
		}

		return list;
	}

	private Student mapRow(ResultSet rs) throws SQLException {
		return new Student(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("course"),
				rs.getString("photo_path"));
	}
}