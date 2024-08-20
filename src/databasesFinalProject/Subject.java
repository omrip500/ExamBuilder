package databasesFinalProject;

public class Subject {
	private int subjectId;
	private String subjectName;

	public Subject(int subjectId, String subjectName) {
		this.subjectId = subjectId;
		this.subjectName = subjectName;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	@Override
	public String toString() {
		return "Subject ID: " + subjectId + ", Subject Name: " + subjectName;
	}
}
