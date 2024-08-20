package databasesFinalProject;

public class Lecturer {
	private int lecturerId;
	private String username;
	private String password;
	private Subject[] subjects;

	public Lecturer(int lecturerId, String username, String password, Subject[] subjects) {
		this.lecturerId = lecturerId;
		this.username = username;
		this.password = password;
		this.subjects = subjects;
	}

	public int getLecturerId() {
		return lecturerId;
	}

	public void setLecturerId(int lecturerId) {
		this.lecturerId = lecturerId;
	}

	public String getUsername() {
		return username;
	}
	
	public Subject[] getSubjects() {
		return subjects;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void showSubjects() {
		for (int i = 0; i < subjects.length; i++) {
			if(subjects[i] != null)
				System.out.println(subjects[i]);
		}
	}
	
	public boolean findLecturerSubjectByItsId(int subject_id) {
		for (int i = 0; i < subjects.length; i++) {
			if (subjects[i] != null && subjects[i].getSubjectId() == subject_id)
				return true;
		}
		return false;
	}
	
	public void setSubjects(Subject[] subjects) {
		this.subjects = subjects;
	}

	@Override
	public String toString() {
		return "Lecturer [lecturerId=" + lecturerId + ", username=" + username + ", password=" + password + "]";
	}

}
