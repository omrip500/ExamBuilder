package databasesFinalProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateRecords {
	private final String url = "jdbc:postgresql:examManagerDB";
	private final String user = "postgres";
	private final String password = "2236";
	
	private static final String UPDATE_LECTURER_PASSWORD_BY_ITS_ID = "UPDATE lecturer SET password = ? WHERE lecturer_id = ?\r\n";
	
	private static final String UPDATE_QUESTION_TITLE_BY_ITS_ID = "UPDATE question SET title = ? WHERE question_id = ?";

	private static final String UPDATE_QUESTION_DIFFICULTY_BY_ITS_ID = "UPDATE question SET difficulty = ? WHERE question_id = ?";

	private static final String UPDATE_ANSWER_TEXT_BY_ITS_ID = "UPDATE answer SET text = ? WHERE answer_id = ?";

	private static final String UPDATE_ANSWER_ID_TO_OPEN_QUESTION = "UPDATE open_question SET answer_id = ? WHERE question_id = ?";

	private static final String UPDATE_ONE_OF_THE_ANSWERS_ID_TO_MULTIPLE_CHOICE_QUESTION = "UPDATE multiple_choice_question_answer SET answer_id = ? WHERE question_id = ? AND answer_id = ?";

	public void updateLecturerPasswordByItsId(int lecturer_id, String newPassword) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
	        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_LECTURER_PASSWORD_BY_ITS_ID);
			preparedStatement.setString(1, newPassword);
			preparedStatement.setInt(2, lecturer_id);
			preparedStatement.executeUpdate();
			System.out.println("The password has been changed sucessfully!");
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources: " + e.getMessage());
			}
		}
	}
	public void updateQuestionTitleByItsId(int question_id, String newTitle) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
	        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUESTION_TITLE_BY_ITS_ID);
			preparedStatement.setString(1, newTitle);
			preparedStatement.setInt(2, question_id);
			preparedStatement.executeUpdate();
			System.out.println("The question's text has been updated sucessfully!");
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources: " + e.getMessage());
			}
		}
	}
	
	public void updateAnswerTextByItsId(int answer_id, String newAnswer) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
	        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_ANSWER_TEXT_BY_ITS_ID);
			preparedStatement.setString(1, newAnswer);
			preparedStatement.setInt(2, answer_id);
			preparedStatement.executeUpdate();
			System.out.println("The answer's text has been updated sucessfully!");
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources: " + e.getMessage());
			}
		}
	}
	public void updateAnswerToOpenQuestion(int question_id, int new_answer_id) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
	        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_ANSWER_ID_TO_OPEN_QUESTION);
			preparedStatement.setInt(1, new_answer_id);
			preparedStatement.setInt(2, question_id);
			preparedStatement.executeUpdate();
			System.out.println("The answer's text has been updated sucessfully!");
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources: " + e.getMessage());
			}
		}
	}
	public void updateOneOfTheAnswersToMultipleChoiceQuestion(int question_id, int old_answer_id, int new_answer_id) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
	        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_ONE_OF_THE_ANSWERS_ID_TO_MULTIPLE_CHOICE_QUESTION);
			preparedStatement.setInt(1, new_answer_id);
			preparedStatement.setInt(2, question_id);
			preparedStatement.setInt(3, old_answer_id);
			preparedStatement.executeUpdate();
			System.out.println("The answer's text has been updated sucessfully!");
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources: " + e.getMessage());
			}
		}
	}

	public void updateQuestionDifficultyByItsId(int question_id, String newDifficulty) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUESTION_DIFFICULTY_BY_ITS_ID);
			preparedStatement.setString(1, newDifficulty);
			preparedStatement.setInt(2, question_id);
			preparedStatement.executeUpdate();
			System.out.println("The question's difficulty has been updated sucessfully!");
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources: " + e.getMessage());
			}
		}
	}
}
