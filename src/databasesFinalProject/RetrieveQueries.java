package databasesFinalProject;

public class RetrieveQueries {

	public static final String SELECT_LECTURER_BY_USERNAME_QUERY = "SELECT * FROM lecturer WHERE username = ?";

	public static final String SELECT_ALL_SUBJECTS = "SELECT * FROM subject";
	
	public static final String COUNT_SUBJECTS_ON_DATABASE = "SELECT COUNT(*) FROM subject";

	public static final String SELECT_SUBJECTS_BY_LECTURER_ID = "SELECT * FROM subject WHERE subject_id IN"
			+ " (SELECT subject_id FROM subject_lecturer WHERE lecturer_id = ?)";
	
	public static final String SELECT_ANSWER_BY_TEXT = "SELECT * FROM answer WHERE text = ?";	
	
	//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||| Question related queries |||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	public static final String SELECT_QUESTION_BY_TITLE = "SELECT * FROM question WHERE title = ?";

	public static final String VALIDATE_ANSWER_IN_MULTIPLE_CHOICE_QUESTION ="SELECT *\r\n"
	+ "FROM multiple_choice_question_answer\r\n"
	+ "WHERE question_id = ? AND answer_id = ?;";
	
	public static final String COUNT_QUESTIONS_USING_THE_SAME_ANSWER_BY_ITS_ID = "SELECT\r\n"
	+ "(SELECT COUNT(DISTINCT question_id)\r\n"
	+ "FROM multiple_choice_question_answer\r\n"
	+ "WHERE answer_id = ?) + \r\n"
	+ "\r\n"
	+ "(SELECT COUNT(DISTINCT question_id)\r\n"
	+ "FROM open_question\r\n"
	+ "WHERE answer_id = ?)\r\n"
	+ "	\r\n"
	+ "AS sum_count";
	
	public static final String SELECT_ANSWERS_TO_QUESTION_BY_ITS_ID = "SELECT \r\n"
	+ "       answer.text AS answer,\r\n"
	+ "	   answer.answer_id AS answer_id,\r\n"
	+ "       0 AS question_type\r\n"
	+ "FROM question\r\n"
	+ "JOIN open_question ON question.question_id = open_question.question_id\r\n"
	+ "JOIN answer ON answer.answer_id = open_question.answer_id\r\n"
	+ "WHERE question.question_id = ?\r\n"
	+ "\r\n"
	+ "UNION\r\n"
	+ "\r\n"
	+ "SELECT\r\n"
	+ "       a.text AS answer,\r\n"
	+ "	   a.answer_id AS answer_id,\r\n"
	+ "       1 AS question_type\r\n"
	+ "FROM multiple_choice_question mq\r\n"
	+ "JOIN multiple_choice_question_answer mcqa ON mq.question_id = mcqa.question_id\r\n"
	+ "JOIN answer a ON mcqa.answer_id = a.answer_id\r\n"
	+ "JOIN question q ON mq.question_id = q.question_id\r\n"
	+ "JOIN lecturer l ON q.lecturer_id = l.lecturer_id\r\n"
	+ "WHERE q.question_id = ?\r\n";
	
	public static final String SELECT_QUESTION_BY_ITS_ID_AND_LECTURER_ID = "SELECT * FROM question\r\n"
			+ "WHERE question_id = ? AND lecturer_id = ?";
	
	
	public static final String SELECT_OPEN_QUESTIONS_BY_SUBJECT_ID = "SELECT question.question_id, question.title, question.difficulty, answer.text ,lecturer.username from question join open_question on question.question_id = open_question.question_id\r\n"
			+ "join lecturer on lecturer.lecturer_id = question.lecturer_id\r\n"
			+ "join answer on answer.answer_id = open_question.answer_id\r\n" + "where question.subject_id = ?";
	
	
	public static final String SELECT_MULTIPLE_CHOICE_QUESTIONS_BY_SUBJECT_ID = "SELECT \r\n"
			+ "q.question_id AS question_id,	\r\n" + "q.title AS question_text,\r\n"
			+ "q.difficulty AS difficulty,\r\n" + "l.username AS lecturer_name,\r\n"
			+ "STRING_AGG(a.text, ', ') AS all_answers,\r\n"
			+ "MAX(CASE WHEN mcqa.is_correct_answer THEN a.text ELSE NULL END) AS correct_answer\r\n" + "FROM \r\n"
			+ "	multiple_choice_question mq\r\n" + "JOIN \r\n"
			+ "	multiple_choice_question_answer mcqa ON mq.question_id = mcqa.question_id\r\n" + "JOIN \r\n"
			+ "	answer a ON mcqa.answer_id = a.answer_id\r\n" + "JOIN \r\n"
			+ "	question q ON mq.question_id = q.question_id\r\n" + "JOIN \r\n"
			+ "	lecturer l ON q.lecturer_id = l.lecturer_id\r\n" + "WHERE \r\n" + "   q.subject_id = ?\r\n"
			+ "GROUP BY \r\n" + "	q.question_id, q.title, q.difficulty, l.username";
	
	public static final String SELECT_QUESTION_BY_ITS_ID = "SELECT question.question_id AS question_id,\r\n"
			+ "	question.title AS question_text,\r\n"
			+ "	question.difficulty AS difficulty, \r\n"
			+ "	lecturer.username AS lecturer_name,\r\n"
			+ "	answer.text AS all_answers,\r\n"
			+ "	answer.text AS correct_answer,\r\n"
			+ "	0 AS question_type\r\n"
			+ "	from question join open_question on question.question_id = open_question.question_id\r\n"
			+ "			join lecturer on lecturer.lecturer_id = question.lecturer_id\r\n"
			+ "			join answer on answer.answer_id = open_question.answer_id\r\n"
			+ "	where question.question_id = ?\r\n"
			+ "\r\n"
			+ "UNION\r\n"
			+ "\r\n"
			+ "SELECT \r\n"
			+ "    q.question_id AS question_id,\r\n"
			+ "    q.title AS question_text,\r\n"
			+ "    q.difficulty AS difficulty,\r\n"
			+ "    l.username AS lecturer_name,\r\n"
			+ "    STRING_AGG(a.text, ', ') AS all_answers,\r\n"
			+ "    MAX(CASE WHEN mcqa.is_correct_answer THEN a.text ELSE NULL END) AS correct_answer,\r\n"
			+ "	1 AS question_type\r\n"
			+ "FROM \r\n"
			+ "    multiple_choice_question mq\r\n"
			+ "JOIN \r\n"
			+ "    multiple_choice_question_answer mcqa ON mq.question_id = mcqa.question_id\r\n"
			+ "JOIN \r\n"
			+ "    answer a ON mcqa.answer_id = a.answer_id\r\n"
			+ "JOIN \r\n"
			+ "    question q ON mq.question_id = q.question_id\r\n"
			+ "JOIN \r\n"
			+ "    lecturer l ON q.lecturer_id = l.lecturer_id\r\n"
			+ "WHERE \r\n"
			+ "    q.question_id = ?\r\n"
			+ "GROUP BY \r\n"
			+ "    q.question_id, q.title, q.difficulty, l.username";
	
	public static final String SELECT_ALL_QUESTIONS_BY_LECTURER_ID = "SELECT question.question_id AS question_id,\r\n"
			+ "	question.title AS question_text,\r\n"
			+ "	question.difficulty AS difficulty, \r\n"
			+ "	lecturer.username AS lecturer_name,\r\n"
			+ "	answer.text AS all_answers,\r\n"
			+ "	answer.text AS correct_answer,\r\n"
			+ "	0 AS question_type\r\n"
			+ "	from question join open_question on question.question_id = open_question.question_id\r\n"
			+ "			join lecturer on lecturer.lecturer_id = question.lecturer_id\r\n"
			+ "			join answer on answer.answer_id = open_question.answer_id\r\n"
			+ "	where question.lecturer_id = ?\r\n"
			+ "\r\n"
			+ "UNION\r\n"
			+ "\r\n"
			+ "SELECT \r\n"
			+ "    q.question_id AS question_id,\r\n"
			+ "    q.title AS question_text,\r\n"
			+ "    q.difficulty AS difficulty,\r\n"
			+ "    l.username AS lecturer_name,\r\n"
			+ "    STRING_AGG(a.text, ', ') AS all_answers,\r\n"
			+ "    MAX(CASE WHEN mcqa.is_correct_answer THEN a.text ELSE NULL END) AS correct_answer,\r\n"
			+ "	1 AS question_type\r\n"
			+ "FROM \r\n"
			+ "    multiple_choice_question mq\r\n"
			+ "JOIN \r\n"
			+ "    multiple_choice_question_answer mcqa ON mq.question_id = mcqa.question_id\r\n"
			+ "JOIN \r\n"
			+ "    answer a ON mcqa.answer_id = a.answer_id\r\n"
			+ "JOIN \r\n"
			+ "    question q ON mq.question_id = q.question_id\r\n"
			+ "JOIN \r\n"
			+ "    lecturer l ON q.lecturer_id = l.lecturer_id\r\n"
			+ "WHERE \r\n"
			+ "    q.lecturer_id = ?\r\n"
			+ "GROUP BY \r\n"
			+ "    q.question_id, q.title, q.difficulty, l.username";
	
	
	public static final String VALIDATE_OPEN_QUESTION_FOR_EXAM = "select question.question_id from open_question\r\n"
			+ "join question on question.question_id = open_question.question_id\r\n"
			+ "where question.subject_id = ? and question.question_id = ?\r\n"
			+ "AND question.question_id NOT IN (\r\n"
			+ "	SELECT eq.question_id\r\n"
			+ "	FROM exam_question eq\r\n"
			+ "	WHERE eq.exam_id = ?\r\n"
			+ ");";
	
	public static final String VALIDATE_MULTIPLE_CHOICE_QUESTION_FOR_EXAM = "SELECT question.question_id \r\n"
			+ "FROM multiple_choice_question\r\n"
			+ "JOIN question ON question.question_id = multiple_choice_question.question_id\r\n"
			+ "WHERE question.subject_id = ? \r\n"
			+ "AND question.question_id = ?\r\n"
			+ "AND question.question_id NOT IN (\r\n"
			+ "	SELECT eq.question_id\r\n"
			+ "	FROM exam_question eq\r\n"
			+ "	WHERE eq.exam_id = ?\r\n"
			+ ");";
	
	public static final String SELECT_OPEN_QUESTION_FOR_EXAM = "SELECT question.question_id, question.title, question.difficulty, answer.text ,lecturer.username from question\r\n"
			+ "join open_question on question.question_id = open_question.question_id\r\n"
			+ "join lecturer on lecturer.lecturer_id = question.lecturer_id\r\n"
			+ "join answer on answer.answer_id = open_question.answer_id\r\n"
			+ "where question.subject_id = ?\r\n"
			+ "AND question.question_id NOT IN (\r\n"
			+ "	SELECT eq.question_id\r\n"
			+ "	FROM exam_question eq\r\n"
			+ "	WHERE eq.exam_id = ?\r\n"
			+ ")";
	
	public static final String MULTIPLE_CHOICE_QUESTION_FOR_EXAM = "SELECT \r\n"
			+ "    q.question_id AS question_id, \r\n"
			+ "    q.title AS question_text,\r\n"
			+ "    q.difficulty AS difficulty, \r\n"
			+ "    l.username AS lecturer_name,\r\n"
			+ "    STRING_AGG(a.text, ', ') AS all_answers,\r\n"
			+ "    MAX(CASE WHEN mcqa.is_correct_answer THEN a.text ELSE NULL END) AS correct_answer\r\n"
			+ "FROM \r\n"
			+ "    multiple_choice_question mq\r\n"
			+ "JOIN\r\n"
			+ "    multiple_choice_question_answer mcqa ON mq.question_id = mcqa.question_id\r\n"
			+ "JOIN \r\n"
			+ "    answer a ON mcqa.answer_id = a.answer_id\r\n"
			+ "JOIN \r\n"
			+ "    question q ON mq.question_id = q.question_id\r\n"
			+ "JOIN \r\n"
			+ "    lecturer l ON q.lecturer_id = l.lecturer_id\r\n"
			+ "WHERE \r\n"
			+ "    q.subject_id = ?\r\n"
			+ "AND q.question_id NOT IN (\r\n"
			+ "	SELECT eq.question_id\r\n"
			+ "	FROM exam_question eq\r\n"
			+ "	WHERE eq.exam_id = ?\r\n"
			+ ")\r\n"
			+ "GROUP BY \r\n"
			+ "    q.question_id, q.title, q.difficulty, l.username";

	public static final String COUNT_OPEN_QUESTIONS_BY_SUBJECT_ID = "SELECT COUNT(DISTINCT open_question.question_id)\r\n"
			+ "FROM open_question JOIN question ON open_question.question_id = question.question_id\r\n"
			+ "WHERE question.subject_id = ?";
	
	public static final String COUNT_MULTIPLE_CHOICE_QUESTIONS_BY_SUBJECT_ID = "SELECT COUNT(DISTINCT multiple_choice_question.question_id) \r\n"
	+ "FROM multiple_choice_question JOIN question ON multiple_choice_question.question_id = question.question_id\r\n"
	+ "WHERE question.subject_id = ?";
	
	//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||| Exam related queries ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	public static final String SELECT_EXAMS_BY_LECTURER_ID = "SELECT exam.exam_id, subject.subject_name, COUNT(DISTINCT exam_question.question_id)\r\n"
	+ "FROM lecturer JOIN exam ON lecturer.lecturer_id = exam.lecturer_id\r\n"
	+ "JOIN subject ON subject.subject_id = exam.subject_id\r\n"
	+ "JOIN exam_question ON exam_question.exam_id = exam.exam_id\r\n"
	+ "WHERE lecturer.lecturer_id = ?\r\n"
	+ "GROUP BY exam.exam_id, subject.subject_name\r\n";
	
	public static final String VALIDATE_EXAM_BY_LECTURER_ID = "SELECT exam_id\r\n"
	+ "FROM exam\r\n"
	+ "WHERE exam_id = ? AND lecturer_id = ?\r\n";
	
	public static final String SELECT_SUBJECT_ID_BY_EXAM_ID = "SELECT subject_id FROM exam WHERE exam_id = ?";
	
	public static final String SELECT_EXAM_QUESTIONS_BY_EXAM_ID = "SELECT \r\n"
			+ "    q.question_id AS question_id, \r\n"
			+ "    q.title AS title,\r\n"
			+ "    q.difficulty AS difficulty,\r\n"
			+ "    l.username AS lecturer_name,\r\n"
			+ "    STRING_AGG(a.text, ', ') AS all_answers,\r\n"
			+ "    MAX(CASE WHEN mcqa.is_correct_answer THEN a.text ELSE NULL END) AS correct_answer,\r\n"
			+ "	1 AS question_type\r\n"
			+ "FROM exam_question JOIN multiple_choice_question mq ON exam_question.question_id = mq.question_id\r\n"
			+ "JOIN \r\n"
			+ "    multiple_choice_question_answer mcqa ON mq.question_id = mcqa.question_id\r\n"
			+ "JOIN \r\n"
			+ "    answer a ON mcqa.answer_id = a.answer_id\r\n"
			+ "JOIN \r\n"
			+ "    question q ON mq.question_id = q.question_id\r\n"
			+ "JOIN \r\n"
			+ "    lecturer l ON q.lecturer_id = l.lecturer_id\r\n"
			+ "WHERE \r\n"
			+ "    exam_question.exam_id = ?\r\n"
			+ "GROUP BY \r\n"
			+ "    q.question_id, q.title, q.difficulty, l.username\r\n"
			+ "UNION\r\n"
			+ "\r\n"
			+ "SELECT question.question_id, question.title, question.difficulty,\r\n"
			+ "lecturer.username AS lecturer_name, \r\n"
			+ "answer.text AS all_answers,\r\n"
			+ "answer.text AS correct_answer,\r\n"
			+ "0 AS question_type\r\n"
			+ "FROM exam_question JOIN question ON question.question_id = exam_question.question_id\r\n"
			+ "JOIN open_question ON question.question_id = open_question.question_id\r\n"
			+ "JOIN lecturer ON lecturer.lecturer_id = question.lecturer_id\r\n"
			+ "JOIN answer ON answer.answer_id = open_question.answer_id\r\n"
			+ "WHERE exam_question.exam_id = ?";
	
	public static final String SELECT_QUESTION_FROM_EXAM = "SELECT * FROM exam_question WHERE exam_id = ? AND question_id = ?";

	public static final String SELECT_RANDOM_QUESTION_IDS_FOR_AUTOMATIC_EXAM = "SELECT question_id FROM question\r\n"
			+ "WHERE subject_id = ?\r\n"
			+ "ORDER BY RANDOM()\r\n"
			+ "LIMIT ?;";
}