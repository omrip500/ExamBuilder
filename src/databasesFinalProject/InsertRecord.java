package databasesFinalProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertRecord {
	private final String url = "jdbc:postgresql:examManagerDB";
	private final String user = "postgres";
	private final String password = "2236";

	private static final String SUBJECT_INSERT_RECORD = "INSERT INTO subject (subject_name) VALUES (?)";
	
	private static final String LECTURER_INSERT_RECORD = "INSERT INTO lecturer" + "(username, password) VALUES"
			+ "(?,?)";

	private static final String LECTURER_INSERT_SUBJECTS = "INSERT INTO subject_lecturer"
			+ "(subject_id, lecturer_id) VALUES" + "(?,?)";

	private static final String QUESTION_INSERT_RECORD = "INSERT INTO question (title, difficulty, lecturer_id, subject_id) VALUES(?,?,?,?)";

	private static final String ANSWER_INSERT_RECORD = "INSERT INTO answer (text) VALUES(?)";
	
	private static final String ANSWER_TO_OPEN_QUESTION_INSERT_RECORD = "INSERT INTO open_question (question_id, answer_id) VALUES(?,?)";
	
	private static final String MULTIPLE_CHOICE_QUESTION_INSERT_RECORD = "INSERT INTO multiple_choice_question (question_id) VALUES(?)";
	
	private static final String ANSWER_TO_MULTIPLE_CHOICE_QUESTION_INSERT_RECORD = "INSERT INTO multiple_choice_question_answer (question_id, answer_id, is_correct_answer) VALUES(?,?,?)";
	
	private static final String EXAM_INSERT_RECORD = "INSERT INTO exam (subject_id, lecturer_id) VALUES(?,?)";
	
	public static final String EXAM_QUESTION_INSERT_RECORD = "INSERT INTO exam_question (exam_id, question_id) VALUES(?,?)";
	
	private void closeResources(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			System.out.println("Error closing resources: " + e.getMessage());
		}
	}

	public void insertSubjects(String[] subjects)
	{
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(SUBJECT_INSERT_RECORD);
			for (int i = 0; i < subjects.length; i++) {
				preparedStatement.setString(1, subjects[i]);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);
		}
	}
	
	public void insertLecturerRecord(String lecturerUsername, String lecturerPassword, int[] subjectIds) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(LECTURER_INSERT_RECORD);
			preparedStatement.setString(1, lecturerUsername);
			preparedStatement.setString(2, lecturerPassword);
			preparedStatement.executeUpdate();

			RetrieveRecords retrieveRecords = new RetrieveRecords();

			Lecturer lecturer = retrieveRecords.findLecturerByUsername(lecturerUsername);

			for (int i = 0; i < subjectIds.length; i++) {
				insertSubjectLecturerRecord(subjectIds[i], lecturer.getLecturerId());
			}

		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);
		}
	}

	public void insertSubjectLecturerRecord(int subjectId, int lecturerId) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(LECTURER_INSERT_SUBJECTS);
			preparedStatement.setInt(1, subjectId);
			preparedStatement.setInt(2, lecturerId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);
		}
	}

	public int insertQuestion(String questionTitle, String difficulty, int lecturerId, int subject_id) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
	        PreparedStatement preparedStatement = conn.prepareStatement(QUESTION_INSERT_RECORD, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, questionTitle);
			preparedStatement.setString(2, difficulty);
			preparedStatement.setInt(3, lecturerId);
			preparedStatement.setInt(4, subject_id);
			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows > 0) {
				ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
				if (generatedKeys.next()) {
					int questionId = generatedKeys.getInt(1);
					return questionId;
				} else {
					throw new SQLException("Creating question failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);

		}
		return -1;
	}

	public int insertAnswer(String answer) {
		Connection conn = null;
		try {
			RetrieveRecords retrieveRecords = new RetrieveRecords();
			int answerId = retrieveRecords.getAnswerIdByText(answer);
			if (answerId != -1) {
				return answerId;
			}
			
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(ANSWER_INSERT_RECORD, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, answer);
			preparedStatement.executeUpdate();
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				int questionId = generatedKeys.getInt(1);
				return questionId;
			} else {
				throw new SQLException("Creating question failed, no ID obtained.");
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);
		}
		return -1;
	}
	
	public void insertToOpenQuestionTable(int questionId, int answerId) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(ANSWER_TO_OPEN_QUESTION_INSERT_RECORD);
			preparedStatement.setInt(1, questionId);
			preparedStatement.setInt(2, answerId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);
		}
		System.out.println("The question has been added successfully!");
	}
	
	public void insertMultipleChoiceQuestion(int questionId) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn
					.prepareStatement(MULTIPLE_CHOICE_QUESTION_INSERT_RECORD);
			preparedStatement.setInt(1, questionId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);
		}
		System.out.println("The question has been added successfully!");
	}
	
	public void insertToMultipleChoiceAnswerTable(int questionId, int answerId, int isCorrectAnswer) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(ANSWER_TO_MULTIPLE_CHOICE_QUESTION_INSERT_RECORD);
			preparedStatement.setInt(1, questionId);
			preparedStatement.setInt(2, answerId);
			preparedStatement.setBoolean(3, isCorrectAnswer == 0 ? false : true);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);
		}		
	}
	
	public int insertExam(int subjectId, int lecturerId) {
		Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = conn.prepareStatement(EXAM_INSERT_RECORD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, subjectId);
            preparedStatement.setInt(2, lecturerId);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				int examId = generatedKeys.getInt(1);
				return examId;
			} else {
				throw new SQLException("Creating exam failed, no ID obtained.");
			}
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
			closeResources(conn);
        }
        return -1;
	}
	
	public void insertExamQuestion(int examId, int questionId) {
		Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = conn.prepareStatement(EXAM_QUESTION_INSERT_RECORD);
            preparedStatement.setInt(1, examId);
            preparedStatement.setInt(2, questionId);
            preparedStatement.executeUpdate();
            System.out.println("The question with the ID " + questionId + " has been added to the exam successfully");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
			closeResources(conn);
        }
	}
}
