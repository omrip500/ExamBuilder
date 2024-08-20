package databasesFinalProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteRecords {
	private final String url = "jdbc:postgresql:examManagerDB";
	private final String user = "postgres";
	private final String password = "2236";
	
	
	private static final String DELETE_QUESTION_FROM_EXAM = "DELETE FROM exam_question WHERE exam_id = ? AND question_id = ? \r\n";
	
	private static final String DELETE_ANSWER_BY_ITS_ID = "DELETE FROM answer WHERE answer_id = ?\r\n";
	
	private static final String DELETE_EXAMS_WITHOUT_QUESTIONS = "DELETE FROM exam WHERE exam_id NOT IN (SELECT DISTINCT exam_id FROM exam_question)";
	
	private static final String DELETE_ALL_EXAM_QUESTIONS_BY_ITS_ID = "DELETE FROM exam_question WHERE exam_id = ?";
	
	private static final String DELETE_EXAM_BY_ITS_ID = "DELETE FROM exam WHERE exam_id = ?";
	
	private void closeResources(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			System.out.println("Error closing resources: " + e.getMessage());
		}
	}
	
	public void deleteQuestionFromExamByItsId(int exam_id, int question_id) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
	        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUESTION_FROM_EXAM);
			preparedStatement.setInt(1, exam_id);
			preparedStatement.setInt(2, question_id);
			preparedStatement.executeUpdate();
			System.out.println("The question has been deleted from this exam sucessfully!");
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);
		}
	}
	public void deleteAnswerByItsId(int answer_id) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
	        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_ANSWER_BY_ITS_ID);
			preparedStatement.setInt(1, answer_id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);
		}
	}

	public void deleteExamsWithoutQuestions() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(DELETE_EXAMS_WITHOUT_QUESTIONS);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);
		}
	}
	public void deleteExamQuestionsByItsId(int exam_id) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
	        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_ALL_EXAM_QUESTIONS_BY_ITS_ID);
			preparedStatement.setInt(1, exam_id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);
		}
	}
	public void deleteExamByItsId(int exam_id) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
	        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_EXAM_BY_ITS_ID);
			preparedStatement.setInt(1, exam_id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn);
		}
	}
}
