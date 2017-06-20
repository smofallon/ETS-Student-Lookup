
public class Student {
	private String firstName, lastName, fullName;
	private int highschoolGradYear;
	private String collegeName, enrollmentStatus, email, cellNumber;
	
	
	public Student(String fullName, String firstName, String lastName, int highschoolGradYear, String collegeName, String enrollmentStatus, String email, String cellNumber){
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = fullName;
		this.highschoolGradYear = highschoolGradYear;
		this.collegeName = collegeName;
		this.enrollmentStatus = enrollmentStatus;
		this.email = email;
		this.cellNumber = cellNumber;
	}
	
	public String getFirstName(){
		return firstName;
	}
	public String getLastName(){
		return lastName;
	}
	public String getFullName(){
		return fullName;
	}
	public int getHighschoolGradYear(){
		return highschoolGradYear;
	}
	public String getCollegeName(){
		return collegeName;
	}
	public String getEnrollmentStatus(){
		return enrollmentStatus;
	}
	public String getEmail(){
		return email;
	}
	public String getCellNumber(){
		return cellNumber;
	}
	
	
	
	
}
