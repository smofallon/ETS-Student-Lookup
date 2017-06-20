import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

////access
import java.sql.*;
////access

////excel
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
////excel

public class Main extends Application {
	Stage window;
	Button submitBtn, addNewStudentsBtn, backBtn, backBtn2, uploadBtn;
	Scene searchForStudents, uploadStudentData, studentData;
	TableView<Student> table;
	Connection cn;
	ResultSet rs;
	Statement st;
	int year = 0;
	int classOf = 0;
	String college = "";
	
	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("Student Lookup");

		//Form for searchForStudents
		Label yearLabel = new Label("High School Graduation Year: (must have a number)");
		Label collegeLabel = new Label("College:");
		TextField yearInput = new TextField();
		yearInput.setMaxWidth(300);
		yearInput.setText("0");
		TextField collegeInput = new TextField();
		collegeInput.setMaxWidth(300);
		submitBtn = new Button("Submit");
		addNewStudentsBtn = new Button("Add New Students");
		
		addNewStudentsBtn.setOnAction(e -> window.setScene(uploadStudentData));
		submitBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
					if(isInt(yearInput.getText())){
						college = collegeInput.getText();
						table.setItems(getTable(Integer.parseInt(yearInput.getText())));
						window.setScene(studentData);
					}
					else{
						college = collegeInput.getText();
						table.setItems(getTable(Integer.parseInt(yearInput.getText())));
						window.setScene(studentData);
					}
			}
		});
		
		//Layout for searchForStudents
		VBox vSearch = new VBox(10);
		vSearch.setPadding(new Insets(20,20,10,10));
		vSearch.getChildren().addAll(addNewStudentsBtn, yearLabel, yearInput, collegeLabel, collegeInput, submitBtn);
		
		//Form for uploadStudentData
		Label uploadLabel = new Label("Clicking the upload button will upload data from the excel file in the same folder as this program named 'uploadMe'");
		backBtn = new Button("Back");
		uploadBtn = new Button("Upload");
		Label classOfLabel = new Label("Please Enter High School Graduation Year: ");
		TextField classOfInput = new TextField();
		classOfInput.setMaxWidth(300);
		classOfInput.setText("0");
		
		backBtn.setOnAction(e -> window.setScene(searchForStudents));
		uploadBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				try {
					if(isInt(classOfInput.getText())){
						classOf = Integer.parseInt(classOfInput.getText());
						if(readExcel()){
							uploadMessage.display("success, please remove Excel file from this folder.");
						}
						else{
							uploadMessage.display("failed to read Excel file, make sure it is in the same folder as this program and named 'uploadMe'");
						}
					}
					else{
						classOf = 0;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		//Layout for uploadStudentData
		HBox hUpload = new HBox(30);
		hUpload.setPadding(new Insets(20, 20, 20, 10));
		hUpload.getChildren().addAll(backBtn, uploadBtn);
		VBox vUpload = new VBox(10);
		vUpload.getChildren().addAll(uploadLabel, classOfLabel, classOfInput, hUpload);
		
		//Form for studentData
				//Student First Name Column
				TableColumn<Student, Double> firstNameColumn = new TableColumn<>("First Name");
				firstNameColumn.setMinWidth(100);
				firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
				
				//Student Last Name Column
				TableColumn<Student, Integer> lastNameColumn = new TableColumn<>("Last Name");
				lastNameColumn.setMinWidth(100);
				lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
				
				//Student Full Name Column
				TableColumn<Student, Integer> fullNameColumn = new TableColumn<>("Full Name");
				fullNameColumn.setMinWidth(180);
				fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
		
				//Student Highschool Grad Year Column
				TableColumn<Student, Integer> hsGradColumn = new TableColumn<>("Grad Year");
				hsGradColumn.setMinWidth(50);
				hsGradColumn.setCellValueFactory(new PropertyValueFactory<>("highschoolGradYear"));
		
				//Student College Name Column
				TableColumn<Student, Double> collegeNameColumn = new TableColumn<>("College Name");
				collegeNameColumn.setMinWidth(120);
				collegeNameColumn.setCellValueFactory(new PropertyValueFactory<>("collegeName"));
				
				//Student Enrollment Status Column
				TableColumn<Student, Double> enrollColumn = new TableColumn<>("Enrollment Status");
				enrollColumn.setMinWidth(120);
				enrollColumn.setCellValueFactory(new PropertyValueFactory<>("enrollmentStatus"));
				
				//Student Email Column
				TableColumn<Student, Double> emailColumn = new TableColumn<>("Email");
				emailColumn.setMinWidth(200);
				emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
				
				//Student Cell Number Column
				TableColumn<Student, Double> cellNumberColumn = new TableColumn<>("Cell Number");
				cellNumberColumn.setMinWidth(150);
				cellNumberColumn.setCellValueFactory(new PropertyValueFactory<>("cellNumber"));
				
		table = new TableView<>();	
		table.setPrefHeight(600);
		table.getColumns().addAll(fullNameColumn, firstNameColumn, lastNameColumn, hsGradColumn, collegeNameColumn, enrollColumn, emailColumn, cellNumberColumn);
	
		backBtn2 = new Button("Back");
		backBtn2.setOnAction(e -> window.setScene(searchForStudents));
		
		//Layout for studentData
		VBox vStudents = new VBox(20);
		vStudents.getChildren().addAll(backBtn2, table);
		
		studentData = new Scene(vStudents, 1100, 700);
		uploadStudentData = new Scene(vUpload, 1100, 700);
		searchForStudents = new Scene(vSearch, 1100, 700);
		window.setScene(searchForStudents);
		window.show();
	}
	
	private boolean isInt(String message){
		try{
			year = Integer.parseInt(message);
			if(year < 2000 || year > 2100)
				year = 0;
			return true;
			
		} catch(NumberFormatException e){
			System.out.println("Error: " + message + " is not a number");
			return false;
		}
	}
	
	private ObservableList<Student> getTable(int year){
		
		ObservableList<Student> students = FXCollections.observableArrayList();

		try{
			Connection conn = DriverManager.getConnection("jdbc:ucanaccess://StudentLookup.accdb;memory=false");
			st = conn.createStatement();
			String fullName, firstName, lastName, collegeName, enrollmentStatus, email, cellNumber, sql;
			int highschoolGradYear;
			
			if(year == 0 && college.equals(""))
				sql = "SELECT * FROM Students";
			else if(year == 0 && !college.equals(""))
				sql = "SELECT * FROM Students WHERE College = '" + college + "'";
			else if(year != 0 && college.equals(""))
				sql = "SELECT * FROM Students WHERE HighschoolGradYear = '" + year + "'";
			else
				sql = "SELECT * FROM Students WHERE HighschoolGradYear = " + year + " AND College = '" + college + "'";
			
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				
				if(rs.getString(2) != null)
					fullName = rs.getString(2);
				else
					fullName = " ";
				if(rs.getString(3) != null)
					firstName = rs.getString(3);
				else
					firstName = " ";
				if(rs.getString(4) != null)
					lastName = rs.getString(4);
				else
					lastName = " ";
				if(rs.getString(5) != null)
					highschoolGradYear = Integer.parseInt(rs.getString(5));
				else
					highschoolGradYear = 0;
				if(rs.getString(6) != null){
					if (!college.equals(""))
						collegeName = college;
					else	
						collegeName = rs.getString(6);
				}
				else
					collegeName = " ";
				if(rs.getString(7) != null)
					enrollmentStatus = rs.getString(7);
				else
					enrollmentStatus = " ";
				if(rs.getString(8) != null)
					email = rs.getString(8);
				else
					email = " ";
				if(rs.getString(9) != null)
					cellNumber = rs.getString(9);
				else
					cellNumber = " ";
				
				students.add(new Student(fullName, firstName, lastName, highschoolGradYear, collegeName, enrollmentStatus, email, cellNumber));
				
			}
		} catch(Exception e){
			System.out.println(e);
		}
		return students;
	}
	
	private boolean readExcel() throws IOException{ 
		try {
			FileInputStream file = new FileInputStream(new File("uploadMe.xlsx"));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			String tempFullName, tempFirstName, tempLastName, tempCollege, tempEnrollmentStatus, tempEmail, tempPhone;
			int tempHighschoolGradYear = classOf;
			tempFullName = tempFirstName = tempLastName = tempCollege = tempEnrollmentStatus = tempEmail = tempPhone = "";

			for (int k = 1; k <= sheet.getLastRowNum(); k++){
				tempFullName = tempFirstName = tempLastName = tempCollege = tempEnrollmentStatus = tempEmail = tempPhone = "";
				
				for (int i=0; i < sheet.getRow(0).getLastCellNum(); i++){
				
					String cellValue = "";
					
					XSSFRow row = sheet.getRow(k);
					if (row != null) {
					    XSSFCell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
					    if (cell != null) {
					    	cell.setCellType(Cell.CELL_TYPE_STRING);
					    	cellValue = sheet.getRow(k).getCell(i).getStringCellValue();
					    }
					}
					
						if (sheet.getRow(0).getCell(i).getStringCellValue().equalsIgnoreCase("name"))
							tempFullName = sheet.getRow(k).getCell(i).getStringCellValue();
						else if (sheet.getRow(0).getCell(i).getStringCellValue().equalsIgnoreCase("first name"))
							tempFirstName = sheet.getRow(k).getCell(i).getStringCellValue();
						else if (sheet.getRow(0).getCell(i).getStringCellValue().equalsIgnoreCase("last name"))
							tempLastName = sheet.getRow(k).getCell(i).getStringCellValue();
						else if (sheet.getRow(0).getCell(i).getStringCellValue().equalsIgnoreCase("college") || sheet.getRow(0).getCell(i).getStringCellValue().equalsIgnoreCase("Current Post Secondary Education"))
							tempCollege = sheet.getRow(k).getCell(i).getStringCellValue();
						else if (enrolled(sheet.getRow(0).getCell(i).getStringCellValue()))
							tempEnrollmentStatus = sheet.getRow(k).getCell(i).getStringCellValue();
						else if (sheet.getRow(0).getCell(i).getStringCellValue().equalsIgnoreCase("email"))
							tempEmail = sheet.getRow(k).getCell(i).getStringCellValue();
						else if (sheet.getRow(0).getCell(i).getStringCellValue().equalsIgnoreCase("cell phone"))
							tempPhone = sheet.getRow(k).getCell(i).getStringCellValue();
					
				}
				try{
					Connection conn = DriverManager.getConnection("jdbc:ucanaccess://StudentLookup.accdb;memory=false");
					st = conn.createStatement();
				
					String sql = "INSERT INTO Students (FullName, FirstName, LastName, HighschoolGradYear, College, EnrollmentStatus, Email, CellNumber) " +
							"VALUES ('" + tempFullName + "', '" + tempFirstName + "', '" + tempLastName + "', " + tempHighschoolGradYear + ", '" + 
							tempCollege + "', '" + tempEnrollmentStatus + "', '" + tempEmail + "', '" + tempPhone + "')";
					
					st.execute(sql);
				} catch(Exception e){
					System.out.println(e);
				}
			}
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		}
		return true;
	}
	
	public static boolean enrolled(String e){
		if(e.matches("S'.*") ||e.matches("SP'.*") || e.matches("F'.*"))
			return true;
		else
			return false;
		
	}
}