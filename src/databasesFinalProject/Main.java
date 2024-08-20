package databasesFinalProject;

import java.awt.Menu;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Scanner;


// Make sure you added the external jar file to the project.
// Make sure that the database name is examManagerDB.
// Make sure to change the password to your database password in "InsertRecord.java",
// "RetrieveRecords.java" file, "updateRecord.java" and "DeleteRecords.java" file.


public class Main {
	static Scanner reader = new Scanner(System.in);
	public static Lecturer lecturer;
	public static enum DifficultyLevel {
		EASY, MEDIUM, HARD;
	}

	public static String[] questionTypes = { "easy", "medium", "hard" };

	public static void initSubjects()
	{
		System.out.println("Please enter the number of subjects you want to add: ");
		int numOfSubjects = reader.nextInt();
		while (numOfSubjects <= 0) {
			System.out.println("Invalid number of subjects, min 1, please try again!");
			numOfSubjects = reader.nextInt();
		}
		String[] subjects = new String[numOfSubjects];
		reader.nextLine();
		for (int i = 0; i < numOfSubjects; i++) {
			System.out.println("Please enter the name of the subject: ");
			subjects[i] = reader.nextLine();
			for (int j = 0; j < i; j++) {
				if (subjects[i].equals(subjects[j])) {
					System.out.println("This subject already exists, please try again");
					subjects[i] = null;
					i--;
					break;
				}
			}
		}
		InsertRecord insertRecord = new InsertRecord();
		insertRecord.insertSubjects(subjects);
	}
	
	public static int authenticationMenu() {
		System.out.println("For login press 1");
		System.out.println("For register press 2");
		System.out.println("For exit press 0");

		int choice = reader.nextInt();
		while (choice < 0 || choice > 2) {
			System.out.println("Invalid choice, please try again!");
			choice = reader.nextInt();
		}
		return choice;
	}

	public static Lecturer loginMenu() {
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		Lecturer lecturer = null;

		while (lecturer == null) {
			System.out.println("Please enter your username: ");
			String username = reader.next();
			System.out.println("Please enter your password: ");
			String password = reader.next();
			lecturer = retrieveRecords.validateLecturer(username, password);
			if(lecturer == null || !PasswordAuthenticator.checkPassword(password, lecturer.getPassword()))
			{
				System.out.println("Invalid username or password, please try again!");
				lecturer = null;
			}
		}
		return lecturer;
	}
	
	public static String getPassword()
	{
		System.out.println("Please choose your password (1-15 characters): ");
		String password = reader.next();
		while (password.length() < 1 || password.length() > 15) {
			System.out.println("Invalid password length, please try again!");
			password = reader.next();
		}
		System.out.println("Your password is: " + password);
		return PasswordAuthenticator.hashPassword(password);
	}
	
	public static void registerMenu() {
		InsertRecord insertRecord = new InsertRecord();
		RetrieveRecords retrieveRecords = new RetrieveRecords();

		System.out.println("Please choose your username: ");
		String username = reader.next();

		while (retrieveRecords.findLecturerByUsername(username) != null) {
			System.out.println("Username already exists, please try again!");
			username = reader.next();
		}

		String password = getPassword();
		
		int numOfSubjects = retrieveRecords.getSubjectsCount();

		System.out.println("Please enter the the number of subjects you will be teaching: (1-" + numOfSubjects + ")");
		int numOfUserSubjects = reader.nextInt();
		while (numOfUserSubjects < 1 || numOfUserSubjects > numOfSubjects) {
			System.out.println("Invalid number of subjects, please try again!");
			numOfUserSubjects = reader.nextInt();
		}

		retrieveRecords.getAllSubjects();
		int[] subjectIds = new int[numOfUserSubjects];
		boolean[] isSubjectAlreadyChosen = new boolean[numOfSubjects];
		for (int i = 0; i < numOfUserSubjects; i++) {
			System.out.println("Please enter the id of the subject: ");
			int chosenSubjectId = reader.nextInt();
			while(true)
			{
				if(chosenSubjectId < 1 || chosenSubjectId > numOfSubjects) {
					System.out.println("Invalid subject id, please try again!");
					chosenSubjectId = reader.nextInt();
				}
				else if (isSubjectAlreadyChosen[chosenSubjectId - 1]) {
					System.out.println("You have already chosen this subject, please try again!");
					chosenSubjectId = reader.nextInt();
				}
				else {
					break;
				}
			}
			isSubjectAlreadyChosen[chosenSubjectId - 1] = true;
			subjectIds[i] = chosenSubjectId;
		}

		insertRecord.insertLecturerRecord(username, password, subjectIds);
		System.out.println("You have successfully registered! Please login to continue.");
	}
	
	public static void changePassword()
	{
		UpdateRecords updateRecords = new UpdateRecords();
		String newPassword = getPassword();
		updateRecords.updateLecturerPasswordByItsId(lecturer.getLecturerId(), newPassword);

	}
	
	public static String getQuestionText()
	{
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		boolean isQuestionAlreadyExists = true;
		
		System.out.println("Please enter the question text: ");
		String questionText = null;
		while (isQuestionAlreadyExists) {
			questionText = reader.nextLine();
			
			isQuestionAlreadyExists = retrieveRecords.findQuestionByTitle(questionText);
			if(isQuestionAlreadyExists)
				System.out.println("Question already exists! please try again");
		}
		return questionText;
	}

	public static String getQuestionDifficultyLevel() {
		System.out.println("Please enter the difficulty level:");
		for (int i = 0; i < DifficultyLevel.values().length; i++) {
			System.out.println(i + " - " + DifficultyLevel.values()[i]);
		}
		int difficultyLevel = reader.nextInt();
		while (difficultyLevel < 0 || difficultyLevel > DifficultyLevel.values().length - 1) {
			System.out.println("Invalid difficulty level, please try again!");
			difficultyLevel = reader.nextInt();
		}
		return questionTypes[difficultyLevel];
	}
	
	public static void addANewQuestion() {
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		InsertRecord insertRecord = new InsertRecord();
		boolean isQuestionAlreadyExists = false;
		reader.nextLine();

		lecturer.showSubjects();
		System.out.println("Please enter the subject id: ");
		int subjectId = reader.nextInt();
		while (!lecturer.findLecturerSubjectByItsId(subjectId)) {
            System.out.println("Invalid subject id, please try again!");
            subjectId = reader.nextInt();
		}
		reader.nextLine();

		System.out.println("Please enter the question title: ");
		String questionTitle = reader.nextLine();
		isQuestionAlreadyExists = retrieveRecords.findQuestionByTitle(questionTitle);
		if (isQuestionAlreadyExists) {
			System.out.println("Question already exists!");
			return;
		}

		String difficulty = getQuestionDifficultyLevel();
		int questionType = chooseQuestionType();
		
		if (questionType == 0) {
			System.out.println("Please enter the answer: ");
			reader.nextLine();
			String answer = reader.nextLine();
			int answerId = insertRecord.insertAnswer(answer);
			if (answerId != -1) {
				int questionId = insertRecord.insertQuestion(questionTitle, difficulty, lecturer.getLecturerId(), subjectId);
				if (questionId == -1) {
					System.out.println("Error adding the question!");
					return;
				}
				insertRecord.insertToOpenQuestionTable(questionId, answerId);
			}
		} else if (questionType == 1) {
			boolean hasTheCorrectAnswerAppeared = false;
			int numberOfAnswers = 0;
			System.out.println("Please enter the number of answers for the question: ");
			numberOfAnswers = reader.nextInt();
			while (numberOfAnswers <= 0) {
				System.out.println("Invalid number of answers, please try again!");
				numberOfAnswers = reader.nextInt();
			}
			int correctAnswerIndex = -1;
			int answersIds[] = new int[numberOfAnswers];
			String allAnswersText[] = new String[numberOfAnswers];
			reader.nextLine();
			for (int i = 0; i < numberOfAnswers; i++) {
				System.out.println("Please enter answer " + (i + 1) + ": ");
				String answer = reader.nextLine();
				allAnswersText[i] = answer;
				if (!hasTheCorrectAnswerAppeared) {
					System.out.println("Is this the correct answer? 1 - yes, 0 - no");
					int correctAnswer = reader.nextInt();
					reader.nextLine();
					if (correctAnswer == 1) {
						correctAnswerIndex = i;
						hasTheCorrectAnswerAppeared = true;
					}
				}
			}
			int questionId = insertRecord.insertQuestion(questionTitle, difficulty, lecturer.getLecturerId(), subjectId);
			if (questionId == -1) {
				System.out.println("Error adding the question!");
				return;
			}
			for (int i = 0; i < numberOfAnswers; i++) {
				answersIds[i] = insertRecord.insertAnswer(allAnswersText[i]);
				if (answersIds[i] == -1) {
					System.out.println("Error adding an answer!");
					return;
				}
			}
			insertRecord.insertMultipleChoiceQuestion(questionId);
			for (int i = 0; i < numberOfAnswers; i++) {
				insertRecord.insertToMultipleChoiceAnswerTable(questionId, answersIds[i],
						i == correctAnswerIndex ? 1 : 0);
			}
		}
	}
	
	public static int chooseQuestionType() {
		System.out.println("Enter question type: 0 - open question, 1 - multiple choice question");
		int questionType = reader.nextInt();
		while (questionType < 0 || questionType > 1) {
			System.out.println("Invalid question type, please try again!");
			questionType = reader.nextInt();
		}
		return questionType;
	}

	public static int chooseLecturerSubject() {
		Subject[] lecturerSubjects = lecturer.getSubjects();
		for (int i = 0; i < lecturerSubjects.length; i++) {
			if (lecturerSubjects[i] != null)
				System.out.println((i + 1) + ". " + lecturerSubjects[i].getSubjectName());
		}

		System.out.println("Please enter the subject number: ");
		int subjectIndex = reader.nextInt() - 1;

		while (subjectIndex < 0 || subjectIndex > lecturerSubjects.length - 1
				|| lecturerSubjects[subjectIndex] == null) {
			System.out.println("Invalid subject number, please try again!");
			subjectIndex = reader.nextInt() - 1;
		}

		return lecturerSubjects[subjectIndex].getSubjectId();
	}


	public static void editAQuestionAnswers(int question_id)
	{
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		UpdateRecords updateRecords = new UpdateRecords();
		InsertRecord insertRecord = new InsertRecord();
		DeleteRecords deleteRecords = new DeleteRecords();
		int answer_id = retrieveRecords.getQuestionAnswersByItsId(question_id);
		int questionType;
		if(answer_id == -2)
		{
			System.out.println("An error occurred");
			return;
		}
		else if(answer_id == -1)
		{
			questionType = 1;
			System.out.println("Please enter the id of the answer you want to edit");
			answer_id = reader.nextInt();
			while(!retrieveRecords.isAnswerInMultipleChoiceQuestion(question_id, answer_id))
			{
				System.out.println("This answer isn't part of this question, please try again");
				answer_id = reader.nextInt();
			}
		}
		else 
			questionType = 0;
		reader.nextLine();
		System.out.println("Please enter the new answer");
		String newAnswer = reader.nextLine();
		int newAnswerId = retrieveRecords.getAnswerIdByText(newAnswer);
		if(newAnswerId == -2)
		{
			System.out.println("An error occurred");
			return;
		}
		boolean hasToDeleteOldAnswer = false;
		if(!retrieveRecords.isAnAnswerUsedMoreThanOnce(answer_id))
		{
			if(newAnswerId == -1)
			{
				updateRecords.updateAnswerTextByItsId(answer_id, newAnswer);
                return;	
			}
			else
				hasToDeleteOldAnswer = true;
		}
		else if(newAnswerId == -1)
			newAnswerId = insertRecord.insertAnswer(newAnswer);
		
		if(questionType == 0)
			updateRecords.updateAnswerToOpenQuestion(question_id, newAnswerId);
		else
			updateRecords.updateOneOfTheAnswersToMultipleChoiceQuestion(question_id, answer_id, newAnswerId);
		if(hasToDeleteOldAnswer)
			deleteRecords.deleteAnswerByItsId(answer_id);
		return;
	}
	
	public static void editAQuestion()
	{
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		UpdateRecords updateRecords = new UpdateRecords();
		int numOfQ = retrieveRecords.getQuestionsByLecturerId(lecturer.getLecturerId());
		if(numOfQ == -1)
		{
			System.out.println("An error occurred");
			return;
		}
		else if (numOfQ == 0) {
			System.out.println("No questions to edit, add some and come back");
			return;
		}
		
		System.out.println("Please choose one of your questions to edit, enter its id");
		int question_id;
		while(true)
		{
			question_id = reader.nextInt();
			if(retrieveRecords.isQuestionValidToEdit(question_id, lecturer.getLecturerId()))
				break;
			else
				System.out.println("Invalid question id, please try again");
		}
		reader.nextLine();
		boolean editing = true;
		while(editing)
		{
			System.out.println("Please choose what do you want to edit in this question");
			System.out.println("1 - Editing the question text itself");
			System.out.println("2 - Editing one of its answers");
			System.out.println("3 - Editing its difficulty level");
			System.out.println("4 - Print this question in its current edited form");
			System.out.println("0 - Stop editing this question");
			int editingOption = reader.nextInt();
			String newQuestionText;
			switch(editingOption)
			{
				case 1:
					reader.nextLine();
					newQuestionText = getQuestionText();
					updateRecords.updateQuestionTitleByItsId(question_id, newQuestionText);
					break;
				case 2:
					editAQuestionAnswers(question_id);
					break;
				case 3:
					String newDifficulty = getQuestionDifficultyLevel();
					updateRecords.updateQuestionDifficultyByItsId(question_id, newDifficulty);
					break;
				case 4:
					retrieveRecords.getQuestionByItsId(question_id);
                    break;
				case 0:
					editing = false;
					break;
			}
		}
	}

	public static void showQuestionsInGeneralBySubjectId() {
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		int subjectId = chooseLecturerSubject();
		int questionType = chooseQuestionType();
		if (questionType == 0) {
			retrieveRecords.getOpenQuestionsBySubjectId(subjectId);
		} else {
			retrieveRecords.getMultipleChoiceQuestionsBySubjectId(subjectId);
		}
		reader.nextLine();
	}
	
	
	public static void showQuestionsForExam(int examId, int subjectId, int questionType) {
		RetrieveRecords retrieveRecords = new RetrieveRecords();
        if(questionType == 0) {
			retrieveRecords.getOpenQuestionsForExam(examId, subjectId, true);
		} else {
			retrieveRecords.getMultipleChoiceQuestionsForExam(examId, subjectId, true);
        }
	}
	
	public static void createExam() {
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		InsertRecord insertRecord = new InsertRecord();
		System.out.println("Please choose the type of exam you want to create:");
		System.out.println("1 - Manual exam");
		System.out.println("2 - Automatic exam");
		int examType = reader.nextInt();
		while (examType < 1 || examType > 2) {
			System.out.println("Invalid exam type, please try again!");
			examType = reader.nextInt();
		}
		int subjectId = chooseLecturerSubject();
		int countOpenQuestions = retrieveRecords.getQuestionsCountBySubjectIdAndType(subjectId, 0);
		int countMultipleChoiceQuestions = retrieveRecords.getQuestionsCountBySubjectIdAndType(subjectId, 1);
		int numberOfQuestions = 0;
		while(numberOfQuestions <= 0)
		{
			System.out.println("How many questions would you like the exam to have?");
			numberOfQuestions = reader.nextInt();
			if(numberOfQuestions <= 0)
				System.out.println("Can't have less than or 0 questions in an exam");
		}
		if(countOpenQuestions + countMultipleChoiceQuestions < numberOfQuestions)
		{
			System.out.println("There aren't enough questions to build the requested exam");
			System.out.println("Please add more questions and return later");
			return;
		}
		int examId = insertRecord.insertExam(subjectId, lecturer.getLecturerId());
		if (examId == -1) {
			System.out.println("Error creating the exam!");
			return;
		}
		if (examType == 1) {
			createManualExam(examId, subjectId, numberOfQuestions, countOpenQuestions, countMultipleChoiceQuestions);
		} else {
			createAutomaticExam(examId, subjectId, numberOfQuestions);
		}
	}
	
	public static void createManualExam(int examId, int subjectId, int numberOfQuestions, int countOpenQuestions, int countMultipleChoiceQuestions) {
		InsertRecord insertRecord = new InsertRecord();
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		int questionType;
		for (int i = 0; i < numberOfQuestions; i++) {
			if(countOpenQuestions == 0)
			{
				questionType = 1;
				System.out.println("There aren't any open questions left to add");
				System.out.println("Please choose a multiple choice question instead");
				System.out.println();
			}
			else if(countMultipleChoiceQuestions == 0)
			{
				questionType = 0;
				System.out.println("There aren't any multiple choice questions left to add");
				System.out.println("Please choose an open question instead");
				System.out.println();
			}
			else
			{
				questionType = chooseQuestionType();
			}
			showQuestionsForExam(examId, subjectId, questionType);
			
			System.out.println("Please enter the question id: ");
			int questionId = reader.nextInt();
			
			boolean isQuestionIdValid = retrieveRecords.isQuestionValidForExam(examId, questionId, questionType, subjectId);
			while (!isQuestionIdValid) {
				System.out.println("The question exists in the exam or is not valid for the exam, please try again!");
				questionId = reader.nextInt();
				isQuestionIdValid = retrieveRecords.isQuestionValidForExam(examId, questionId, questionType, subjectId);
			}
			insertRecord.insertExamQuestion(examId, questionId);
			if(questionType == 0)
				countOpenQuestions--;
			else
				countMultipleChoiceQuestions--;
		}
		System.out.println("The exam has been added successfully");
	}
	
	public static void createAutomaticExam(int examId, int subjectId, int numberOfQuestions)
	{
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		InsertRecord insertRecord = new InsertRecord();
		int random_question_ids[] = new int[numberOfQuestions];
		random_question_ids = retrieveRecords.getRandomQuestionsIdsForAutomaticExam(subjectId, numberOfQuestions);
		if (random_question_ids == null) {
			System.out.println("An error occurred");
			return;
		}
		for (int i = 0; i < numberOfQuestions; i++) {
			insertRecord.insertExamQuestion(examId, random_question_ids[i]);
		}
	}
	
	public static int chooseExamToEdit()
	{
		System.out.println("Choose one of the exam above to edit, enter its id");
		int examId = reader.nextInt();
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		while (!retrieveRecords.validateExamIdByLecturerId(examId, lecturer.getLecturerId())) {
			System.out.println("Invalid exam id, please try again!");
			examId = reader.nextInt();
		}
		reader.nextLine();
		return examId;
	}
	
	
	public static void addQuestionToAnExistingExam(int examId, int subjectId)
	{
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		InsertRecord insertRecord = new InsertRecord();
		int countOpenQuestions = retrieveRecords.getOpenQuestionsForExam(examId, subjectId, false);
		int countMultipleChoiceQuestions = retrieveRecords.getMultipleChoiceQuestionsForExam(examId, subjectId, false);
		int questionType;
		if(countOpenQuestions == 0 && countMultipleChoiceQuestions == 0)
		{
			System.out.println("No avaliable questions to add to this exam");
			return;
		}
		else if(countOpenQuestions == 0)
		{
			questionType = 1;
			System.out.println("There aren't any open questions to choose from");
			System.out.println("Choose one of the multiple choice questions below");
			System.out.println();
		}
		else if(countMultipleChoiceQuestions == 0)
		{
			questionType = 0;
			System.out.println("There aren't any multiple choice questions to choose from");
			System.out.println("Choose one of the open questions below");
			System.out.println();
		}
		else
		{
			questionType = chooseQuestionType();
		}
		showQuestionsForExam(examId, subjectId, questionType);
		
		System.out.println("Please enter the question id: ");
		int questionId = reader.nextInt();
		
		boolean isQuestionIdValid = retrieveRecords.isQuestionValidForExam(examId, questionId, questionType, subjectId);
		while (!isQuestionIdValid) {
			System.out.println("The question exists in the exam or is not valid for the exam, please try again!");
			questionId = reader.nextInt();
			isQuestionIdValid = retrieveRecords.isQuestionValidForExam(examId, questionId, questionType, subjectId);
		}
		insertRecord.insertExamQuestion(examId, questionId);
	}
	
	public static void deleteQuestionToAnExistingExam(int examId)
	{
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		DeleteRecords deleteRecords = new DeleteRecords();
		int numOfExamQuestions = retrieveRecords.getExamQuestionsByExamId(examId, false);
		if (numOfExamQuestions <= 1) {
			System.out.println("No questions to delete from this exam, Minimum number of questions is 1");
			return;
		}
		System.out.println("Please enter the question id: ");
		int questionId = reader.nextInt();
		boolean isQuestionIdValid = retrieveRecords.isQuestionInExam(examId, questionId);
		while (!isQuestionIdValid) {
			System.out.println("The question doesn't exist in the exam, please try again!");
			questionId = reader.nextInt();
			isQuestionIdValid = retrieveRecords.isQuestionInExam(examId, questionId);
		}
		deleteRecords.deleteQuestionFromExamByItsId(examId, questionId);
	}
	
	public static void editExam()
	{
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		DeleteRecords deleteRecords = new DeleteRecords();
		if(!retrieveRecords.getExamsInfoByLecturerId(lecturer.getLecturerId()))
		{
			System.out.println("No exams to edit, add some and come back");
			return;
		}
		int examId = chooseExamToEdit();
		int subjectId = retrieveRecords.getSubjectOfAnExamByItsId(examId);
		boolean editing = true;
		while(editing)
		{
			System.out.println("Please choose one of the following editing options:");
			System.out.println("1 for adding a question for this exam");
			System.out.println("2 for removing a question from this exam");
			System.out.println("3 for deleting this exam");
			System.out.println("0 to go back to the main menu");
			int editOption = reader.nextInt();
			reader.nextLine();
			
			switch (editOption) 
			{
				case 0:
					editing = false;
					break;
				case 1:
					addQuestionToAnExistingExam(examId, subjectId);
					break;
				case 2:
					deleteQuestionToAnExistingExam(examId);
					break;
				case 3:
					deleteRecords.deleteExamQuestionsByItsId(examId);
					deleteRecords.deleteExamByItsId(examId);
					System.out.println("The exam has been deleted successfully");
					editing = false;
					break;
			}
		}
	}
	
	public static void exportExamToFile()
	{
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		if(!retrieveRecords.getExamsInfoByLecturerId(lecturer.getLecturerId()))
		{
			System.out.println("No Exams to export, add some and come back");
			return;
		}
		int examId = chooseExamToEdit();
		retrieveRecords.getExamQuestionsByExamId(examId, true);
	}
	
	public static void panelMenu() {
		while (true) {
			System.out.println("\n*********** Main Menu ***********");
	        System.out.println("1: Add a new question");
	        System.out.println("2: Edit one of your questions or its answers");
	        System.out.println("3: Show all questions");
	        System.out.println("4: Create a new exam");
	        System.out.println("5: Edit one of your existing exams");
	        System.out.println("6: Export one of your exam to a txt file");
	        System.out.println("7: Change your password");
	        System.out.println("0: Exit");
	        System.out.print("\nPlease choose one of the options above: ");

			int choice = reader.nextInt();
			while (choice < 0 || choice > 7) {
				System.out.println("Invalid choice, please try again!");
				choice = reader.nextInt();
			}

			switch (choice) {

			case 0:
				System.out.println("Goodbye!");
				return;

			case 1:
				addANewQuestion();
				break;
			case 2:
				editAQuestion();
				break;
			case 3:
				showQuestionsInGeneralBySubjectId();
				break;
			case 4:
				createExam();
				break;
			case 5:
				editExam();
				break;
			case 6:
				exportExamToFile();
				break;
			case 7:
				changePassword();
				break;
			}
		}
	}

	public static void main(String[] args) {
		RetrieveRecords retrieveRecords = new RetrieveRecords();
		if (retrieveRecords.getSubjectsCount() == 0) {
			System.out.println("Welcome to our our exam management system for the first time!");
			System.out.println("Please add the subjects your lecturers will be teaching!");
			initSubjects();
			System.out.println("Now your lecturers can register and login to the system");
		}
		else
			System.out.println("Welcome to our our exam management system!");
		DeleteRecords deleteRecords = new DeleteRecords();
		deleteRecords.deleteExamsWithoutQuestions();
		int authChoice = authenticationMenu();
		if (authChoice == 0) {
			System.out.println("Goodbye!");
			return;
		} else if (authChoice == 1) {
			lecturer = loginMenu();
			if (lecturer != null) {
				System.out.println("Hello " + lecturer.getUsername() + "!");
				panelMenu();
			}

		} else if (authChoice == 2) {
			registerMenu();
			lecturer = loginMenu();
			if (lecturer != null) {
				panelMenu();
			}

		}
	}
}
