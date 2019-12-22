package ca.sheridancollege.Logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import ca.sheridancollege.beans.User;

public class Generator {
	
	static String[] malebytes = {""};
	
	static String[] userNameMales = { "Henry", "Michael", "Peter", "Jonathan", "Anmol", "Harman", "Bob", "Ryan", "Doug",
			"Tom", "LeBron", "Dwayne", "Pascal", "Kobe", "Will", "Niko", "Anthony", "Thomas", 
			"Robert", "Harsh", "Kelvin", "Tyler", "Delanno", "Donny", "Jimmy", "Lionel", "Chris",
			"Max", "Jose", "Connor", "Duncan", "Lukas", "Smith", "Will", "Sam", "Meyers",
			"Nikola", "Parker", "Isiah", "Tex", "Rishu", "Filipe", "Sid", "Melvin", "Ben",
			"Jonah", "Dakota", "Skyler", "Alex", "Jeremy", "Bill", "Todd", "Zak", "Jack", "Flint",
			"Jay", "Ajay", "Karl", "Sonny", "Jamario", "Demar", "William", "Jason", "TJ", "Hayden",
			"Jacob", "Eli", "Trenton", "Jarret", "David", "Phillip", "Tom", "Sahil", "Ron", "Harry",
			"Darko", "Drake", "Flynn", "Gavin", "Gary", "Henry", "Kalvin", "Kenny", "Nigel", "Molton",
			"Oscar", "Russell", "Charles", "Charlie", "Princeton", "Omeka", "Bobby", "Nicholas", "Nick",
			"Arthur", "Doug", "Spencer", "Deacon", "Demar", "Richard", "Alvin", "Elvis", "Dario", "Ping",
			"Romar", "Liam", "Simon", "Tasbir", "Travis", "Victor"};

	static String[] userNameFemales = { "Jennifer", "Sally", "Wilma", "Sarah", "Kylie", "Kim", "Courtney", "Ava",
			"Olivia", "Sophia", "Mia", "Emma", "Isabella", "Amelia", "Melissa", "Samantha",
			"Brittney", "Jordan", "Danika", "Donika", "Nathalee", "Harpreet", "Minnu", 
			"Lauren", "Miranda", "Rose", "Chelsea", "Cassandra", "Celia", "Ciara", "Molly",
			"Ria", "Hazel", "Yasmin", "Jasmine", "Niki", "Sienna", "Alexa", "Alicia", "Khadija",
			"Kaley", "Abbie", "Tiffany", "Ellen", "Lois", "Kimberly", "Penny", "Mya", "Megan",
			"Carmen", "Grace", "Josephine", "Moana", "Donna", "Wilma", "Sue", "Sunanna", "Carrie", 
			"Kelly", "Serra", "Bianca", "Harjot", "Louise", "Atinder", "Ajoone", "Lois", "Karla", 
			"Faith", "Summer", "Alyssa", "Jeanne", "Genieve", "Lisa", "Ping", "Esha", "Binal",
			"Simitri", "Breshna", "Zahra", "Vanessa", "Manisha", "Aimy", "Breanna", "Chloey",
			"Mackenizie", "Akanksha", "Shiva", "Deanne", "Bernadette", "Brooke", "Dani", "Jerusha",
			"Lana", "Lindsay", "Madison", "Maddy", "Nina", "Roya", "Ryanne", "Meerna", "Beth"};

	static String[] encryptedPasswords = { "test" };

	static int[] ages = { 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};

	static String[] genders = { "Male", "Female", "Other" };

	static String[] ethnicities = { "Asian", "Caucasian", "Hispanic/Latino", "African American", "Native American",
			"Native Hawaiian", "Pacific Islander" };

	static String[] campuses = { "Davis", "Trafalgar", "HMC" };

	static String[] programs = { "Animation and Game Design", "Applied Computing", "Applied Health",
			"Architectural Studies", "Business", "Chemical and Environmental Sciences", "Community Studies",
			"Design, Illustration and Photography", "Education", "Engineering Sciences", "Film, TV and Journalism",
			"Humanities & Social Sciences", "Material Art and Design", "Public Safety", "Skilled Trades",
			"Technology Fundamentals", "Visual and Performing Arts" };

	static String[] interestedIns = {"Short-Term Dating", "Long-Term Dating", 
			"Hookups", "Friends"};
	
	static String[] showMes = { "Females", "Males", "Other", "All" };
	
	static String[] bios = {"Love long walks on the beach",
			" ",
			"Click Right and I'll Treat You Right",
			"Java With Jonathon is amazing. Click Right if you Agree",
			"Looking for a partner in crime",
			"Hey now, you're a rockstar, so get your game on!",
			"Sheridan Is the Best College in Ontario, Don't You Agree? :)",
			"Love me some kush.",
			"Bio's are stupid. Just ask me whatever you wanna know",
			"Roses are red, violets are blue, I'm in love and so are you",
			"Looking for a respectful being to share my life with",
			"I may not be beautiful but I'll treat you right!",
			"Recent grad. Guess my ethnicity",
			"Match me if you can ;)",
			"DM me for my snapchat",
			"Professional Drinker",
			"Not sure why I'm here tbh. Just looking for some fun",
			"Just here for hookups, hmu if you're down, lmfao",
			"Probably your future spouse",
			"I'll treat you right",
			"Love playing basketball and doing Java homework",
			"If you like pinapple on pizza click right!",
			"If you put pinapples on pizza please click left, geeezzz",
			" ",
			" ",
			"Registered Nurse - I can probably save your life",
			"Master chef, love to cook. I can cook you up ;)",
			"Makeup artist, crazy about my baby pug",
			"If you click right I'll introduce you to my dog",
			"I really don't know what to say so swipe right or something",
			"Hit me up if you need financial advise",
			"Looking for some friends to party with",
			"If we match, please do not ignore my messages",
			"Rock and roll baby",
			"Follow me on IG if you like taking pictures: @mvinaaaa",
			"Here we go..",
			"Tell me your best dad jokes",
			"Funny pick up lines are a must",
			"If you ain't funny then do not click right",
			"Looking for a nice South Asian partner for marriage",
			"Looking to meet new people and make connections!",
			"looking for people to talk to and chill with",
			"Parent of 3, 420 friendly",
			"I need constant validation",
			"Looking for a partner to kick some a** with",
			" ",
			" ",
			"Hockey enthuasiast, Call of Duty Veteran",
			"Software Developer for Google. I can show you around",
			"Petting cats and forgetting important dates",
			"Exams are over, lets party! hmu",
			"Got 2 tickets for the game tonight, are you in?",
			"Looking for a clean, tidy person to share my boring life with",
			"New to Sheridan, looking to meet new people.",
			"Tequila shots all day, everyday",
			"Not on here much, ask me for my snapchat",
			"Biology student, if you give me the opportunity I can probably disect you",
			"Simple is the new complicated, don't you agree?",
			"Teaching Assistant at Davis, HMC andd Oakville. Will give you a discounted rate on tutoring",
			"It's almost the new year and I need some friends to party hard with",
			"If you can't cook and still live with your mom please do me a favour and click left",
			"Chemistry student, let me know if you wanna learn about some organic compounds",
			"Sheridan Meets and Chill? Sounds about right..",
			"The Toronto Raptors won the NBA Championship, how crazy is that?",
			"Are you trash? Because I would love to take you out ;)",
			"I'm good at making mistakes, so let me make another one",
			"If you think I am attractive click right",
			"If you think I'm beautiful click left - it's the inside that counts",
			"This is the best dating app ever, don't you agree?!",
			"If you're lucky I'll reply back",
			"Let's meet up and get a coffee :)",
			"Got nothing to do over the x-mas break, maybe you can keep me occupied?",
			"Looking for a cuddle buddy",
			"Looking for a travel buddy, all expenses covered",
			"Sushi, Burgers, Pizza and Taco's are life",
			"Probably your future ex",
			"Workout enthusiast, can probably military press more than you",
			"Won't be paying for the first date - just throwing that out there",
			"If you're lucky I might click right",
			"Down to experiement",
			"Love hiking and mountain biking"};
			
	public static User generateRecords(String gender) throws IOException{
		User user = new User();
		String userName = "";
		String g = "";
		if(gender.equalsIgnoreCase("male")) {
			int randomUserName = (int) (Math.random() * (userNameMales.length));
			int randomGender = 0;
			int randomPic= (int) (Math.random() * (31 - 1));
			int randomShowMe = 0;
			user.setShowMe(showMes[randomShowMe]);
			userName = userNameMales[randomUserName];
			g = genders[randomGender];
			File fi = new File("src/main/resources/maleImages/i"+randomPic+".jpg");
			byte[] bytes = Files.readAllBytes(fi.toPath());
			user.setPicBytes(bytes);
		}
		else {
			int randomUserName = (int) (Math.random() * (userNameFemales.length));
			int randomGender = 1;
			int randomPic= (int) (Math.random() * (31 - 1));
			int randomShowMe = 1;
			user.setShowMe(showMes[randomShowMe]);
			userName = userNameFemales[randomUserName];
			g = genders[randomGender];
			File fi = new File("src/main/resources/femaleImages/i"+randomPic+".jpg");
			byte[] bytes = Files.readAllBytes(fi.toPath());
			user.setPicBytes(bytes);
		}
		int randomEthnicity = (int) (Math.random() * (ethnicities.length));
		int randomCampus = (int) (Math.random() * (campuses.length));
		int randomProgram = (int) (Math.random() * (programs.length));
		int randomInterestedIn = (int) (Math.random() * (interestedIns.length));
		int randomAge = (int) (Math.random() * (ages.length));
		int randomBio = (int) (Math.random() * (bios.length));
		user.setUserName(userName);
		user.setFirstName(userName);
		user.setGender(g);
		user.setEncryptedPassword(encryptedPasswords[0]);
		user.setEthnicity(ethnicities[randomEthnicity]);
		user.setCampus(campuses[randomCampus]);
		user.setProgram(programs[randomProgram]);
		user.setInterestedIn(interestedIns[randomInterestedIn]);
		user.setAge(ages[randomAge]);
		user.setBio(bios[randomBio]);
		return user;
	}
}
