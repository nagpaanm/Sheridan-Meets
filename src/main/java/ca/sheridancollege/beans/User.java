package ca.sheridancollege.beans;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements java.io.Serializable{

	private static final long serialVersionUID = -2236104756441383295L;
	private long id;
	private String userName;
	private String firstName;
	private String encryptedPassword;
	private int age;
	private String gender;
	private String ethnicity;
	private String campus;
	private String program;
	private String bio;
	private String role;
	private String interestedIn;
	private String showMe;
	private MultipartFile pic;
	private byte[] picBytes;
	private long likes;
	private long matches;
	private String encoded;
	private ArrayList<User> potentialCandidates;
	private ArrayList<User> likeList;
	private ArrayList<User> matchList;
	private ArrayList<User> disLikeList;
	private ArrayList<String> messages;
	private String campusSearch;
	private String programSearch;
	private String ethnicitySearch;
	private String interestedInSearch;
	private String showMeSearch;
	
	
	private String[] shows = {"Females", "Males", "Other", "All"};
	
	private String[] interests = {"Short-Term Dating", "Long-Term Dating", 
									"Hookups", "Friends"};
	
	private String[] campuses = {"Davis", "Trafalgar", "HMC"};
	
	private String[] genders = {"Male", "Female", "Other"};
	
	private String[] ethnicities = {
								"Asian",
								"Caucasian",
								"Hispanic/Latino",
								"African American",
								"Native American",
								"Native Hawaiian",
								"Pacific Islander"};
	
	private String[] programs = {"Animation and Game Design", 
								"Applied Computing",
								"Applied Health",
								"Architectural Studies",
								"Business",
								"Chemical and Environmental Sciences",
								"Community Studies",
								"Design, Illustration and Photography",
								"Education",
								"Engineering Sciences",
								"Film, TV and Journalism",
								"Humanities & Social Sciences",
								"Material Art and Design",
								"Public Safety",
								"Skilled Trades",
								"Technology Fundamentals",
								"Visual and Performing Arts"};
	
	public User(String username, String firstName, String password, int age, String gender, String ethnicity,
			String campus, String program, String role, String interestedIn, String showMe, byte[] picBytes) {
		super();
		this.userName = username;
		this.firstName = firstName;
		this.encryptedPassword = password;
		this.age = age;
		this.gender = gender;
		this.ethnicity = ethnicity;
		this.campus = campus;
		this.program = program;
		this.role = role;
		this.interestedIn = interestedIn;
		this.showMe = showMe;
		this.picBytes = picBytes;
	}

}
