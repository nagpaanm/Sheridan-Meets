package ca.sheridancollege.controllers;

/*
 * admin login details:
 * username: smadmin
 * password: root
 * 
 * 
 * dummy users login details
 * username equals their 'firstname'
 * password: test
 */


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import ca.sheridancollege.email.EmailServiceImpl;
import ca.sheridancollege.Logic.Generator;
import ca.sheridancollege.beans.User;
import ca.sheridancollege.database.DatabaseAccess;

@Controller
public class HomeController {

	@Autowired
	DatabaseAccess da;
	
	@Autowired
	EmailServiceImpl esi;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("user", new User());
		return "redirect:/sm/profile";
	}
	
	@GetMapping("/access-denied")
	public String goaccessdenied(Authentication authentication) {
		ArrayList<String> roles = new ArrayList<String>();
		for (GrantedAuthority ga : authentication.getAuthorities()) {
			roles.add(ga.getAuthority());
		}
		if(roles.contains("ROLE_ADMIN")) {
			return "redirect:/admin";
		}
		return "/error/access-denied.html";
	}
	
	@GetMapping("/login")
	public String gologin(Model model) {
		model.addAttribute("user", new User());
		return "login.html";
	}
	
	@PostMapping("/register")
	public String register(Model model, @ModelAttribute User user, 
			@RequestParam String role, @RequestParam String password,
			@RequestParam String bio) throws IOException{
		byte[] bytes = user.getPic().getBytes();
		if(bytes.length > 2000000) { //2mb
			bytes = new byte[1];//don't actual size add to db;
		}
		user.setEncryptedPassword(password);
		user.setPicBytes(bytes);
		user.setRole(role);
		user.setBio(bio);
		user.setLikes(0);
		user.setMatches(0);
		da.addUser(user);
		long userId = da.findUserAccount(user.getUserName()).getId();
		da.addRole(userId, 2); //assign as regular user
		da.addUserFilter(user.getUserName(), user.getCampus(), "All", "All", user.getInterestedIn(),
				user.getShowMe());
		model.addAttribute("contact", new User());
		return "redirect:/sm/profile";
	}
	
	
	@GetMapping("/sm/profile")
	public String profile(Model model, Authentication authentication){
		User user = da.findUserAccount(authentication.getName());
		ArrayList<User> userLikes = da.retrieveWhoLikes(user);
		user.setLikes(userLikes.size());
		da.updateUserLike(user);
		user = da.getFilter(user);
		if(user.getPicBytes() != null) {
			user.setEncoded(new String(Base64.encodeBase64String(user.getPicBytes())));
		}
		user.setMatches(da.getMatches(user).size());
		da.updateUserMatch(user);
		/*
		MultipartFile mpf = new MockMultipartFile("fileThatDoesNotExists.txt",
	            "fileThatDoesNotExists.txt",
	            "text/plain",
	            user.getPicBytes());
		user.setPic(mpf);
		*/
		model.addAttribute("user", user);
		model.addAttribute("likes", userLikes);
		
		return "profile.html";
	}
	
	@GetMapping("/sm/meet")
	public String meet(Model model, Authentication authentication) {
		User user = da.findUserAccount(authentication.getName());
		ArrayList<User> userLikes = da.retrieveWhoLikes(user);
		user = da.getFilter(user);
		ArrayList<User> users = da.getUsersByFilter(user);
		ArrayList<String> likesAndDislikes = da.getUserDisLikes(
				da.findUserAccount(user.getUserName()));
		likesAndDislikes.addAll(da.getUserLikes(
				da.findUserAccount(user.getUserName())));
		for(String userName: likesAndDislikes) {
			users.remove(da.findUserAccount(userName));
		}
		if(users.size() > 0) {
			int randomUserInt = (int) (Math.random() * users.size());
			User candidate = users.get(randomUserInt);
			if(candidate.getPicBytes() != null) {
				candidate.setEncoded(new String(Base64.encodeBase64String(candidate.getPicBytes())));
			}
			model.addAttribute("candidate", candidate);
		}else {
			model.addAttribute("candidate", new User());
		}
		model.addAttribute("user", user);
		model.addAttribute("likes", userLikes);
		return "meet.html";
	}
	
	@GetMapping("/sm/disapprove")
	public String disApprove(Model model, Authentication authentication, @RequestParam String username) {
		User user = da.findUserAccount(authentication.getName());
		da.addUserDislike(user.getId(), username);
		return "redirect:/sm/meet";
	}
	
	@GetMapping("/sm/approve")
	public String approve(Model model, Authentication authentication, @RequestParam String username) {
		User user = da.findUserAccount(authentication.getName());
		User candidate = da.findUserAccount(username);
		candidate.setLikes(candidate.getLikes() + 1);
		da.updateUserLike(candidate);
		da.addUserLike(user.getId(), username);
		
		ArrayList<User> candidateLikes = da.getUserLike(candidate.getId());
		for(int i = 0; i < candidateLikes.size(); i++) {
			if(candidateLikes.get(i).getUserName().equals(user.getUserName())) {
				candidate.setMatches(candidate.getMatches() + 1);
				user.setMatches(user.getMatches() + 1);
				da.updateUserMatch(candidate);
				da.updateUserMatch(user);
				da.updateMatches(user, candidate);
			}	
		}	
		return "redirect:/sm/meet";
	}
	
	@GetMapping("/sm/matches")
	public String matches(Model model, Authentication authentication) {
		User user = da.findUserAccount(authentication.getName());
		ArrayList<User> userLikes = da.retrieveWhoLikes(user);
		if(user.getPicBytes() != null) {
			user.setEncoded(new String(Base64.encodeBase64String(user.getPicBytes())));
		}
		ArrayList<User> matches = da.getMatches(user);
		for(User m: matches) {
			if(m.getPicBytes() != null) {
				m.setEncoded(new String(Base64.encodeBase64String(m.getPicBytes())));
			}
		}
		model.addAttribute("user", user);
		model.addAttribute("likes", userLikes);
		model.addAttribute("matches", matches);
		return "matches.html";
	}
	
	@PostMapping("/sm/matches/messaging")
	public String messaging(Model model, Authentication authentication, @RequestParam String username,
			@RequestParam String message) {
		User user = da.findUserAccount(authentication.getName());
		ArrayList<User> userLikes = da.retrieveWhoLikes(user);
		User match = da.findUserAccount(username);
		if(match.getPicBytes() != null) {
			match.setEncoded(new String(Base64.encodeBase64String(match.getPicBytes())));
		}
		
		if(message.length() > 0){
			da.addMessage(user, match, message);
		}
		HashMap<Long, String> userMessages = da.getMessages(user, match);
		HashMap<Long, String> matchMessages = da.getMessages(match, user);
		ArrayList<Long> umI = da.getMessageIndices(user, match);
		ArrayList<Long> mmI = da.getMessageIndices(match, user);
		userMessages.putAll(matchMessages); //merged
		Map<Long, String> sortedMap = new TreeMap<Long, String>(userMessages);
		
		model.addAttribute("match", match);
		model.addAttribute("user", user);
		model.addAttribute("messages", sortedMap);
		model.addAttribute("umi", umI);
		model.addAttribute("mmi", mmI);
		model.addAttribute("likes", userLikes);
		return "messaging.html";
	}
	
	@PostMapping("/sm/matches/sendmessage")
	public String sendMessage(Model model, Authentication authentication, @RequestParam String username,
			@RequestParam String message) {
		User user = da.findUserAccount(authentication.getName());
		User match = da.findUserAccount(username);
		da.addMessage(user, match, message);
		
		return "redirect:/sm/matches/messaging";
	}
	
	@PostMapping("/sm/profile/edit")
	public String editUser(@ModelAttribute User user, @RequestParam String bio,
				Authentication authentication) throws IOException {
		if(user.getPic().getSize() > 0) {
			byte[] bytes = user.getPic().getBytes();
			if(bytes.length > 2000000) { //2mb
				bytes = new byte[1];//don't actual size add to db;
			}
			user.setPicBytes(bytes);
		}
		else {
			user.setPicBytes(da.findUserAccount(authentication.getName()).getPicBytes());
		}
		user.setBio(bio);
		da.updateUser(user);
		return "redirect:/sm/profile";
	}
	
	@PostMapping("/sm/profile/filter")
	public String editFilter(@ModelAttribute User user) {
		da.updateFilter(user);
		return "redirect:/sm/profile";
	}
	
	@GetMapping("/sm/settings")
	public String settings(Authentication authentication, Model model) {
		User user = da.findUserAccount(authentication.getName());
		ArrayList<User> userLikes = da.retrieveWhoLikes(user);
		model.addAttribute("user", user);
		model.addAttribute("likes", userLikes);
		return "settings.html";
	}
	
	@PostMapping("/sm/settings/deleteaccount")
	public String deleteAccount(@RequestParam String username) {
		da.deleteSecUser(username);
		return "redirect:/login";
	}
	
	@PostMapping("/sm/settings/changepassword")
	public String changePassword(@RequestParam String username, @RequestParam String newPassword) {
		da.updatePassword(username, newPassword);
		return "redirect:/sm/settings";
	}
	
	@PostMapping("/sm/settings/feedback")
	public String provideFeedBack(@RequestParam String subject, @RequestParam String enjoy,
			@RequestParam String rate, @RequestParam String friend, @RequestParam String bio) {
		String subjectLine = "User: " + subject + ", Feedback";
		String bodyMessage = "Did You Enjoy Using Sheridan Meets? - " + enjoy + "\n" +
				"How Would You Rate Sheridan Meets On a Scale Of 1 to 10 - " + rate + "\n" +
				"Would You Recommend Sheridan Meets To a Friend? - " + friend + "\n" +
				"Feel Free To Provide Us With More FeedBack - " + bio + "\n";
		esi.sendSimpleMessage("java3test123123@gmail.com", subjectLine, bodyMessage);
		return "redirect:/sm/settings";
	}
	
	@GetMapping(value="/getEncryptedPassword/{password}/{userName}", produces="application/json")
	@ResponseBody
	public Map<String, Object> getPassword(@PathVariable String password, @PathVariable String userName){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Map<String, Object> data = new HashMap<String, Object>();
		User user = da.findUserAccount(userName.substring(1, userName.length() - 1)); //substrings to remove quotations
		String encryptedPassword = user.getEncryptedPassword();
		if (passwordEncoder.matches(password, encryptedPassword)) {
			data.put("password", "true");
		}
		else {
			data.put("password", "false");
		}
		return data;
	}
	
	@GetMapping(value="/checkUserName/{userName}", produces="application/json")
	@ResponseBody
	public Map<String, Object> checkUsername(@PathVariable String userName){
		Map<String, Object> data = new HashMap<String, Object>();
		//userName = userName.substring(1, userName.length() - 1); //substrings to remove quotations
		ArrayList<String> userNames = da.getUserNames();
		data.put("password", "true");
		for(String un: userNames) {
			if(un.equalsIgnoreCase(userName)) {
				data.put("password", "false");
			}
		}
		return data;
	}
	
	@GetMapping("/admin")
	public String adminPage(Model model) {
		ArrayList<User> users = da.GetUserAccounts("smadmin");
		for(User u: users) {
			if(u.getPicBytes() != null) {
				u.setEncoded(new String(Base64.encodeBase64String(u.getPicBytes())));
			}
		}
		long noOfLikes = da.getNumberOfLikes();
		model.addAttribute("users", users);
		model.addAttribute("likes", noOfLikes);
		model.addAttribute("matches", da.getNumberOfMatches());
		model.addAttribute("males", da.getNumberOfGender("male"));
		model.addAttribute("females", da.getNumberOfGender("female"));
		model.addAttribute("others", da.getNumberOfGender("other"));
		model.addAttribute("ltr", da.getNumberOfInterestedIn("Long-Term Dating"));
		model.addAttribute("str", da.getNumberOfInterestedIn("Short-Term Dating"));
		model.addAttribute("hookups", da.getNumberOfInterestedIn("Hookups"));
		model.addAttribute("friends", da.getNumberOfInterestedIn("Friends"));
		return "admin.html";
	}
	
	@GetMapping("/admin/delete/{id}")
	public String adminDelete(@PathVariable long id) {
		User user = da.getUserById(id);
		da.deleteSecUser(user.getUserName());
		return "redirect:/admin";
	}
	
	@GetMapping("/admin/deleteallaccounts")
	public String adminDelete() {
		ArrayList<User> users = da.GetUserAccounts("smadmin");
		for(User u: users) {
			da.deleteSecUser(u.getUserName());
		}
		return "redirect:/admin";
	}
	
	@GetMapping("/admin/generatedummyusers")
	public String generateUsers(@RequestParam int amount) throws IOException {
		if(amount <= 60) {
			for(int i = 0; i < amount; i++) {
				User user = new User();
				if(i % 2 == 0) {
					user = Generator.generateRecords("male");
				}
				else {
					user = Generator.generateRecords("female");
				}
				
				if(da.findUserAccount(user.getUserName()) == null) {
					da.addUser(user);
					long userId = da.findUserAccount(user.getUserName()).getId();
					da.addRole(userId, 2); //assign as regular user
					da.addUserFilter(user.getUserName(), user.getCampus(), "All", "All", user.getInterestedIn(),
							user.getShowMe());
				}
				else {
					i--;
				}
			}
		}
		return "redirect:/admin";
	}
}
