package databasesFinalProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetrieveRecords {

	private final String url = "jdbc:postgresql:examManagerDB";
	private final String user = "postgres";
	private final String password = "2236";
	
	
	private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
	    try {
	        if (rs != null) rs.close();
	        if (ps != null) ps.close();
	        if (conn != null) conn.close();
	    } catch (SQLException e) {
	        System.out.println("Error closing resources: " + e.getMessage());
	    }
	}
	
	public Lecturer validateLecturer(String usernameEntered, String passwordEntered) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_LECTURER_BY_USERNAME_QUERY);
			preparedStatement.setString(1, usernameEntered);
			ResultSet results = preparedStatement.executeQuery();

			while (results.next()) {
					Lecturer lecturer = new Lecturer(results.getInt("lecturer_id"), results.getString("username"),
							results.getString("password"), getSubjectsByLecturerId(results.getInt("lecturer_id")));
					return lecturer;
				}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return null;
	}

	public Subject[] getSubjectsByLecturerId(int lecturerId) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_SUBJECTS_BY_LECTURER_ID);
			preparedStatement.setInt(1, lecturerId);
			ResultSet results = preparedStatement.executeQuery();
			Subject[] subjects = new Subject[getSubjectsCount()];
			int i = 0;
			while (results.next()) {
				subjects[i] = new Subject(results.getInt("subject_id"), results.getString("subject_name"));
				i++;
			}
			return subjects;
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return null;
	}
	
	public int getSubjectsCount() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.COUNT_SUBJECTS_ON_DATABASE);
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				return results.getInt("count");
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
			return -1;
		} finally {
			closeResources(conn, null, null);
		}
		return -1;
	}

	public Lecturer findLecturerByUsername(String usernameEntered) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_LECTURER_BY_USERNAME_QUERY);
			preparedStatement.setString(1, usernameEntered);
			ResultSet results = preparedStatement.executeQuery();
			Subject[] subjects = new Subject[getSubjectsCount()];
			while (results.next()) {
				subjects = getSubjectsByLecturerId(results.getInt("lecturer_id"));
				return new Lecturer(results.getInt("lecturer_id"), results.getString("username"),
						results.getString("password"), subjects);
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return null;
	}

	public boolean findQuestionByTitle(String questionTitle) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_QUESTION_BY_TITLE);
			preparedStatement.setString(1, questionTitle);
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return false;
	}

	public void getAllSubjects() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_ALL_SUBJECTS);
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				System.out.println(results.getInt("subject_id") + " - " + results.getString("subject_name"));
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}

	}

	public int getAnswerIdByText(String answerText) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_ANSWER_BY_TEXT);
			preparedStatement.setString(1, answerText);
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				return results.getInt("answer_id");
			}
			return -1;
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return -2;
	}

	public void getOpenQuestionsBySubjectId(int subjectId) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_OPEN_QUESTIONS_BY_SUBJECT_ID);
			preparedStatement.setInt(1, subjectId);
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				System.out.println("Question Id: " + results.getInt("question_id") + "\n" + "Question: "
						+ results.getString("title") + "\nDifficulty: " + results.getString("difficulty")
						+ "\nLecturer Creator: " + results.getString("username") + "\nCorrect Answer: "
						+ results.getString("text") + "\n");
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
	}

	public void getMultipleChoiceQuestionsBySubjectId(int subjectId) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_MULTIPLE_CHOICE_QUESTIONS_BY_SUBJECT_ID);
			preparedStatement.setInt(1, subjectId);
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				System.out.println("Question Id: " + results.getString("question_id") + "\n"
						+ results.getString("question_text") + "\nDifficulty: " + results.getString("difficulty")
						+ "\nLecturer Creator: " + results.getString("lecturer_name") + "\nAll Answers: "
						+ results.getString("all_answers") + "\nCorrect Answer: " + results.getString("correct_answer")
						+ "\n");
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
	}
	
	public void getQuestionByItsId(int question_id) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_QUESTION_BY_ITS_ID);
			preparedStatement.setInt(1, question_id);
			preparedStatement.setInt(2, question_id);
			ResultSet results = preparedStatement.executeQuery();
			if (results.next()) {
				System.out.println("Question Id: " + results.getInt("question_id") + "\n" + "Question: "
						+ results.getString("question_text") + "\nDifficulty: " + results.getString("difficulty")); 
				if (results.getInt("question_type") == 0) {
					System.out.println("Answer: " + results.getString("correct_answer") + "\n");
				} else {
					System.out.println("All Answers: " + results.getString("all_answers") + "\nCorrect Answer: "
							+ results.getString("correct_answer") + "\n");
				}
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
	}
	
	public int getQuestionsByLecturerId(int lecturerId) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_ALL_QUESTIONS_BY_LECTURER_ID);
			preparedStatement.setInt(1, lecturerId);
			preparedStatement.setInt(2, lecturerId);
			ResultSet results = preparedStatement.executeQuery();
			int countNumOfQ = 0;
			while (results.next()) {
				countNumOfQ++;
				System.out.println("Question Id: " + results.getInt("question_id") + "\n" + "Question: "
						+ results.getString("question_text") + "\nDifficulty: " + results.getString("difficulty")); 
				if (results.getInt("question_type") == 0) {
					System.out.println("Answer: " + results.getString("correct_answer") + "\n");
				} else {
					System.out.println("All Answers: " + results.getString("all_answers") + "\nCorrect Answer: "
							+ results.getString("correct_answer") + "\n");
				}
			}
			return countNumOfQ;
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return -1;
	}

	public boolean isQuestionValidToEdit(int question_id, int lecturer_id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_QUESTION_BY_ITS_ID_AND_LECTURER_ID);
            preparedStatement.setInt(1, question_id);
            preparedStatement.setInt(2, lecturer_id);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            closeResources(conn, null, null);
        }
        return false;
    }
	
	// returns -1 if the question is multiple choice, the answer id if it's open, and -2 if there was an error
	public int getQuestionAnswersByItsId(int question_id)
	{
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_ANSWERS_TO_QUESTION_BY_ITS_ID);
			preparedStatement.setInt(1, question_id);
			preparedStatement.setInt(2, question_id);
			ResultSet results = preparedStatement.executeQuery();
			boolean firstAnswer = true;
			while (results.next()) {
				if(results.getInt("question_type") == 0)
				{
					System.out.println("The answer to the question you chose is: " +  results.getString("answer"));
					return results.getInt("answer_id");
				}
				else
				{
					if(firstAnswer)	
						System.out.println("The answers to the question you chose are: ");
					firstAnswer = false;
					System.out.println("Answer id " +  results.getString("answer_id") + ": " + results.getString("answer"));
				}
			}
			return -1;
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return -2;
	}
	
	public boolean isAnAnswerUsedMoreThanOnce(int answer_id)
	{
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.COUNT_QUESTIONS_USING_THE_SAME_ANSWER_BY_ITS_ID);
			preparedStatement.setInt(1, answer_id);
			preparedStatement.setInt(2, answer_id);
			ResultSet results = preparedStatement.executeQuery();
			if (results.next()) {
				return (results.getInt("sum_count") > 1);	
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return false;
	}
	
	public boolean isAnswerInMultipleChoiceQuestion(int question_id, int answer_id)
	{
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.VALIDATE_ANSWER_IN_MULTIPLE_CHOICE_QUESTION);
			preparedStatement.setInt(1, question_id);
			preparedStatement.setInt(2, answer_id);
			ResultSet results = preparedStatement.executeQuery();
			if (results.next()) {
				return true;	
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return false;
	}
	
	public boolean isQuestionValidForExam(int examId, int questionId, int questionType, int subjectId) {
		Connection conn = null;

		if (questionType == 0) {
			try {
				conn = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.VALIDATE_OPEN_QUESTION_FOR_EXAM);
				preparedStatement.setInt(1, subjectId);
				preparedStatement.setInt(2, questionId);
				preparedStatement.setInt(3, examId);
				ResultSet results = preparedStatement.executeQuery();
				while (results.next()) {
					return true;
				}
				return false;
			} catch (SQLException e) {
				System.out.println("SQL Error: " + e.getMessage());
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				closeResources(conn, null, null);
			}
		} else {
			try {
				conn = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = conn
						.prepareStatement(RetrieveQueries.VALIDATE_MULTIPLE_CHOICE_QUESTION_FOR_EXAM);
				preparedStatement.setInt(1, subjectId);
				preparedStatement.setInt(2, questionId);
				preparedStatement.setInt(3, examId);
				ResultSet results = preparedStatement.executeQuery();
				while (results.next()) {
					return true;
				}
				return false;
			} catch (SQLException e) {
				System.out.println("SQL Error: " + e.getMessage());
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				closeResources(conn, null, null);
			}
		}
		return false;
		
	}
	
	public int getOpenQuestionsForExam(int examId, int subjectId, boolean printResults) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql:examManagerDB", "postgres", "2236");
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_OPEN_QUESTION_FOR_EXAM);
			preparedStatement.setInt(1, subjectId);
			preparedStatement.setInt(2, examId);
			ResultSet results = preparedStatement.executeQuery();
			int countNumOfQ = 0;
			while (results.next()) {
				if(printResults)
				{
					System.out.println("Question Id: " + results.getInt("question_id") + "\n" + "Question: "
							+ results.getString("title") + "\nDifficulty: " + results.getString("difficulty")
							+ "\nLecturer Creator: " + results.getString("username") + "\nCorrect Answer: "
							+ results.getString("text") + "\n");
				}
				countNumOfQ++;
			}
			return countNumOfQ;
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return 0;
	}
	
	public int getMultipleChoiceQuestionsForExam(int examId, int subjectId, boolean printResults) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql:examManagerDB", "postgres", "2236");
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.MULTIPLE_CHOICE_QUESTION_FOR_EXAM);
			preparedStatement.setInt(1, subjectId);
			preparedStatement.setInt(2, examId);
			ResultSet results = preparedStatement.executeQuery();
			int countNumOfQ = 0;
			while (results.next()) {
				if(printResults)
				{
					System.out.println("Question Id: " + results.getString("question_id") + "\n"
							+ results.getString("question_text") + "\nDifficulty: " + results.getString("difficulty")
							+ "\nLecturer Creator: " + results.getString("lecturer_name") + "\nAll Answers: "
							+ results.getString("all_answers") + "\nCorrect Answer: " + results.getString("correct_answer")
							+ "\n");
				}
				countNumOfQ++;
			}
			return countNumOfQ;
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return 0;
	}
	public int getQuestionsCountBySubjectIdAndType(int subjectId, int questionType) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			String query = "";
			if(questionType == 0) query = RetrieveQueries.COUNT_OPEN_QUESTIONS_BY_SUBJECT_ID;
			if(questionType == 1) query = RetrieveQueries.COUNT_MULTIPLE_CHOICE_QUESTIONS_BY_SUBJECT_ID;
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, subjectId);
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				return results.getInt("count");
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return -1;
	}
	public boolean getExamsInfoByLecturerId(int lecturerId) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_EXAMS_BY_LECTURER_ID);
			preparedStatement.setInt(1, lecturerId);
			ResultSet results = preparedStatement.executeQuery();
			boolean hasAtLeastOneExam = false;
			while (results.next()) {
				System.out.println("The exam ID: " + results.getString("exam_id") + "\nThe exam subject is: "
						+ results.getString("subject_name") + "\nThis exam contains: " + results.getString("count") + " questions" + "\n");
				hasAtLeastOneExam = true;
			}
			return hasAtLeastOneExam;
			
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return false;
	}
	public boolean validateExamIdByLecturerId(int examId ,int lecturerId) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.VALIDATE_EXAM_BY_LECTURER_ID);
			preparedStatement.setInt(1, examId);
			preparedStatement.setInt(2, lecturerId);
			ResultSet results = preparedStatement.executeQuery();
			if (results.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return false;
	}
	public int getSubjectOfAnExamByItsId(int examId) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_SUBJECT_ID_BY_EXAM_ID);
			preparedStatement.setInt(1, examId);
			ResultSet results = preparedStatement.executeQuery();
			if (results.next()) {
				return results.getInt("subject_id");
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return -1;
	}
	
	public int getExamQuestionsByExamId(int examId, boolean writeToFile) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn
					.prepareStatement(RetrieveQueries.SELECT_EXAM_QUESTIONS_BY_EXAM_ID);
			preparedStatement.setInt(1, examId);
			preparedStatement.setInt(2, examId);
			ResultSet results = preparedStatement.executeQuery();
			int countNumOfQ = 1;
			PrintWriter pwOnlyQuestions = null;
			PrintWriter pwQuestionsWithAnswers = null;

			File fOnlyQuestions = null;
			File fQuestionsWithAnswers = null;
			int numOfExamQuestions = 0;

			if(writeToFile)
			{
				fOnlyQuestions = new File("exam_" + examId + ".txt");
				fQuestionsWithAnswers = new File("exam_" + examId + "_answers" + ".txt");
				pwOnlyQuestions = new PrintWriter(fOnlyQuestions);
				pwQuestionsWithAnswers = new PrintWriter(fQuestionsWithAnswers);
			}
			String difficulty = null;
			int i = 0;
			while (results.next()) {
				numOfExamQuestions++;
				if(writeToFile)
				{
					pwOnlyQuestions.print(countNumOfQ + ". ");
					pwQuestionsWithAnswers.print(countNumOfQ + ". ");
					difficulty = results.getString("difficulty");
					pwOnlyQuestions.print(results.getString("title") + "(*");
					pwQuestionsWithAnswers.print(results.getString("title") + "(*");
			        i = 0;
			        while(Main.questionTypes[i].equals(difficulty))
					{
			        	pwOnlyQuestions.print("*");
			        	pwQuestionsWithAnswers.print("*");
						i++;
					}
			        pwOnlyQuestions.println(")");
			        pwQuestionsWithAnswers.println(")");


		        	if (results.getInt("question_type") == 0) {
		        		pwQuestionsWithAnswers.println("Answer: " + results.getString("correct_answer"));
		        	}
			        
			        if (results.getInt("question_type") == 1) {
			        	pwOnlyQuestions.println(results.getString("all_answers"));
			        	pwQuestionsWithAnswers.println(results.getString("all_answers"));
			        	pwQuestionsWithAnswers.println("Answer: " + results.getString("correct_answer"));
			        }
			        	
			        
			        pwOnlyQuestions.println(); 
			        pwQuestionsWithAnswers.println(); 
					countNumOfQ++;
				}
				else
				{
					System.out.println("Question Id: " + results.getInt("question_id") + "\n" + "Question: "
							+ results.getString("title") + "\nDifficulty: " + results.getString("difficulty")
							+ "\nLecturer: " + results.getString("lecturer_name")); 
					if (results.getInt("question_type") == 0) {
						System.out.println("Answer: " + results.getString("correct_answer") + "\n");
					} else {
						System.out.println("All Answers: " + results.getString("all_answers") + "\nCorrect Answer: "
								+ results.getString("correct_answer") + "\n");
					}
				}
			}
			if(writeToFile)
			{
				pwOnlyQuestions.close();
				pwQuestionsWithAnswers.close();
				System.out.println("An exam file and an answers file have been created successfully");
			}
			return numOfExamQuestions;
	
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			closeResources(conn, null, null);
		}
		return -1;
	}
	
	public boolean isQuestionInExam(int examId, int questionId) {
		Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql:examManagerDB", "postgres", "2236");
            PreparedStatement preparedStatement = conn.prepareStatement(RetrieveQueries.SELECT_QUESTION_FROM_EXAM);
            preparedStatement.setInt(1, examId);
            preparedStatement.setInt(2, questionId);
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
			closeResources(conn, null, null);
        }
        return false;
    }
	
	public int[] getRandomQuestionsIdsForAutomaticExam(int subjectId, int numOfQuestions) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = conn
					.prepareStatement(RetrieveQueries.SELECT_RANDOM_QUESTION_IDS_FOR_AUTOMATIC_EXAM);
			preparedStatement.setInt(1, subjectId);
			preparedStatement.setInt(2, numOfQuestions);
			ResultSet results = preparedStatement.executeQuery();
			int[] questionsIds = new int[numOfQuestions];
			int i = 0;
			while (results.next()) {
				questionsIds[i] = results.getInt("question_id");
				i++;
			}
			return questionsIds;
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println(e);
			return null;
		} finally {
			closeResources(conn, null, null);
		}
		return null;
	}
}

