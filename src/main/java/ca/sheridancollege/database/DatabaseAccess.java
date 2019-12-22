package ca.sheridancollege.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.beans.User;

@Repository
public class DatabaseAccess {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbc;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public void addUser(User user) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "\r\n" + 
				"INSERT INTO sec_user "
				+ "(userName, firstName, encryptedPassword, age, gender, ethnicity, campus, "
				+ "program, bio, interestedIn, showMe, picBytes, likes, matches, ENABLED) "
				+ "VALUES "
				+ "(:userName, :firstName, :encryptedPassword, :age, :gender, :ethnicity, :campus, "
				+ ":program, :bio, :interestedIn, :showMe, :picBytes, :likes, :matches, :ENABLED)";
		parameters.addValue("userName", user.getUserName());
		parameters.addValue("firstName", user.getFirstName());
		parameters.addValue("encryptedPassword", passwordEncoder.encode(user.getEncryptedPassword()));
		parameters.addValue("age", user.getAge());
		parameters.addValue("gender", user.getGender());
		parameters.addValue("ethnicity", user.getEthnicity());
		parameters.addValue("campus", user.getCampus());
		parameters.addValue("program", user.getProgram());
		parameters.addValue("bio", user.getBio());
		parameters.addValue("interestedIn", user.getInterestedIn());
		parameters.addValue("showMe", user.getShowMe());
		parameters.addValue("picBytes", user.getPicBytes());
		parameters.addValue("likes", user.getLikes());
		parameters.addValue("matches", user.getMatches());
		parameters.addValue("ENABLED", 1);
		jdbc.update(query, parameters);
	}
	
	public byte[] getImage(int id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		ArrayList<byte[]> images = new ArrayList<byte[]>();
		String query = "SELECT image FROM images where id=:id";
		parameters.addValue("id", id);
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for(Map<String, Object> row: rows) {
			images.add((byte[]) row.get("image"));
		}
		if(images.size() > 0) {
			return images.get(0);
		}
		return null;
	}
	
	public User getUserById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		ArrayList<User> users = new ArrayList<User>();
		String query = "SELECT * FROM sec_user where userId=:userId";
		parameters.addValue("userId", id);
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for (Map<String, Object> row: rows) {
			User u = new User();
			u.setId((long) row.get("userId"));
			u.setUserName((String) row.get("userName"));
			u.setFirstName((String) row.get("firstName"));
			u.setEncryptedPassword((String) row.get("encryptedPassword"));
			u.setAge((int) row.get("age"));
			u.setGender((String) row.get("gender"));
			u.setEthnicity((String) row.get("ethnicity"));
			u.setCampus((String) row.get("campus"));
			u.setProgram((String) row.get("program"));
			u.setBio((String) row.get("bio"));
			u.setInterestedIn((String) row.get("interestedIn"));
			u.setShowMe((String) row.get("showMe"));
			u.setPicBytes((byte[]) row.get("picbytes"));
			u.setLikes((long) row.get("likes"));
			u.setMatches((long) row.get("matches"));
			users.add(u);
		}
		if(users.size() > 0) {
			return users.get(0);
		}
		return null;
	}
	
	public User findUserAccount(String userName) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		ArrayList<User> users = new ArrayList<User>();
		String query = "SELECT * FROM sec_user where userName=:userName";
		parameters.addValue("userName", userName);
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for (Map<String, Object> row: rows) {
			User u = new User();
			u.setId((long) row.get("userId"));
			u.setUserName((String) row.get("userName"));
			u.setFirstName((String) row.get("firstName"));
			u.setEncryptedPassword((String) row.get("encryptedPassword"));
			u.setAge((int) row.get("age"));
			u.setGender((String) row.get("gender"));
			u.setEthnicity((String) row.get("ethnicity"));
			u.setCampus((String) row.get("campus"));
			u.setProgram((String) row.get("program"));
			u.setBio((String) row.get("bio"));
			u.setInterestedIn((String) row.get("interestedIn"));
			u.setShowMe((String) row.get("showMe"));
			u.setPicBytes((byte[]) row.get("picbytes"));
			u.setLikes((long) row.get("likes"));
			u.setMatches((long) row.get("matches"));
			users.add(u);
		}
		if(users.size() > 0) {
			return users.get(0);
		}
		return null;
	}
	
	public void addRole(long userId, long roleId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "insert into user_role " 
				+ "(userId, roleId)"
				+ " values (:userId, :roleId)";
		parameters.addValue("userId", userId);
		parameters.addValue("roleId", roleId);
		jdbc.update(query, parameters);
	}
	
	public List<String> getRolesById(long userId){
		ArrayList<String> roles = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query =
				"select user_role.userId, sec_role.roleName " +
						"FROM user_role, sec_role " +
						"WHERE user_role.roleId = sec_role.roleId " +
						"and userId=:userId";
		parameters.addValue("userId", userId);
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for(Map<String, Object> row: rows) {
			roles.add((String)row.get("roleName"));
		}
		return roles;
	}
	
	public ArrayList<String> getUserDisLikes(User user){
		ArrayList<String> userNames = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query =
				"select userMatch from USER_DISLIKE where userId =:userId";
		parameters.addValue("userId", user.getId());
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for(Map<String, Object> row: rows) {
			userNames.add((String)row.get("userMatch"));
		}
		return userNames;
	}
	
	public ArrayList<String> getUserLikes(User user){
		ArrayList<String> userNames = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query =
				"select userMatch from USER_LIKE where userId =:userId";
		parameters.addValue("userId", user.getId());
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for(Map<String, Object> row: rows) {
			userNames.add((String)row.get("userMatch"));
		}
		return userNames;
	}
	
	public ArrayList<User> retrieveWhoLikes(User user){
		ArrayList<User> users = new ArrayList<User>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query =
				"select userId from USER_LIKE where userMatch =:userMatch";
		parameters.addValue("userMatch", user.getUserName());
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for(Map<String, Object> row: rows) {
			User u = getUserById((long) row.get("userId"));
			users.add(u);
		}
		return users;
	}
	
	public ArrayList<User> GetUserAccounts(String userName) {
		ArrayList<User> users = new ArrayList<User>();
		String query = "SELECT * FROM sec_user WHERE userName!='smadmin'";
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, new HashMap<String, Object>());
		for (Map<String, Object> row: rows) {
			User u = new User();
			u.setId((long) row.get("userId"));
			u.setUserName((String) row.get("userName"));
			if(!userName.equalsIgnoreCase(u.getUserName())) {
				u.setFirstName((String) row.get("firstName"));
				u.setEncryptedPassword((String) row.get("encryptedPassword"));
				u.setAge((int) row.get("age"));
				u.setGender((String) row.get("gender"));
				u.setEthnicity((String) row.get("ethnicity"));
				u.setCampus((String) row.get("campus"));
				u.setProgram((String) row.get("program"));
				u.setBio((String) row.get("bio"));
				u.setInterestedIn((String) row.get("interestedIn"));
				u.setShowMe((String) row.get("showMe"));
				u.setPicBytes((byte[]) row.get("picbytes"));
				u.setLikes((long) row.get("likes"));
				u.setMatches((long) row.get("matches"));
				users.add(u);
			}
		}
		return users;
	}
	
	public void addUserDislike(long id, String username) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "\r\n" + 
				"INSERT INTO USER_DISLIKE "
				+ "VALUES "
				+ "(:id, :username)";
		parameters.addValue("id", id);
		parameters.addValue("username", username);
		jdbc.update(query, parameters);
	}
	
	public void addUserLike(long id, String username) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "\r\n" + 
				"INSERT INTO USER_LIKE "
				+ "VALUES "
				+ "(:id, :username)";
		parameters.addValue("id", id);
		parameters.addValue("username", username);
		jdbc.update(query, parameters);
	}
	
	public void updateUserLike(User user) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "\r\n" + 
				"UPDATE sec_user SET likes = :likes where userId =:userId";
		parameters.addValue("likes", user.getLikes());
		parameters.addValue("userId", user.getId());
		jdbc.update(query, parameters);
	}
	
	public ArrayList<User> getUserLike(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		ArrayList<User> users = new ArrayList<User>();
		String query = "\r\n" + 
				"select userMatch from user_like where userId = :userId";
		parameters.addValue("userId", id);
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for (Map<String, Object> row: rows) {	
			users.add(findUserAccount((String) row.get("userMatch")));
		}
		return users;
	}
	
	public void updateUserMatch(User user) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "\r\n" + 
				"UPDATE sec_user SET matches = :matches where userId =:userId";
		parameters.addValue("matches", user.getMatches());
		parameters.addValue("userId", user.getId());
		jdbc.update(query, parameters);
	}
	
	public void updateMatches(User user1, User user2) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "\r\n" + 
				"INSERT INTO user_match (userName1, userName2) VALUES (:userName1, :userName2)";
		parameters.addValue("userName1", user1.getUserName());
		parameters.addValue("userName2", user2.getUserName());
		jdbc.update(query, parameters);
	}
	
	public ArrayList<User> getMatches(User user){
		ArrayList<User> users = new ArrayList<User>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query =
				"select userName2 from user_match where userName1 =:userName";
		parameters.addValue("userName", user.getUserName());
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for(Map<String, Object> row: rows) {
			User u = findUserAccount((String) row.get("userName2"));
			users.add(u);
		}
		query =
				"select userName1 from user_match where userName2 =:userName";
		parameters.addValue("userName", user.getUserName());
		rows = jdbc.queryForList(query, parameters);
		for(Map<String, Object> row: rows) {
			User u = findUserAccount((String) row.get("userName1"));
			users.add(u);
		}
		return users;
	}
	
	public void addMessage(User user, User match, String message) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "INSERT INTO user_messages (from_, to_, msg) "
				+ "VALUES "
				+ "(:from_, :to_, :msg)";
		parameters.addValue("from_", user.getUserName());
		parameters.addValue("to_", match.getUserName());
		parameters.addValue("msg", message);
		jdbc.update(query, parameters);
	}
	
	public HashMap<Long, String> getMessages(User user, User match) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		HashMap<Long, String> msgs = new HashMap<Long, String>();
		String query = "\r\n" + 
				"select * from user_messages where from_=:from_ and to_=:to_";
		parameters.addValue("from_", user.getUserName());
		parameters.addValue("to_", match.getUserName());
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for (Map<String, Object> row: rows) {	
			msgs.put((long) row.get("message_id"), (String) row.get("msg"));
		}
		return msgs;
	}
	
	public ArrayList<Long> getMessageIndices(User user, User match) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		ArrayList<Long> msgs = new ArrayList<Long>();
		String query = "\r\n" + 
				"select message_id from user_messages where from_=:from_ and to_=:to_";
		parameters.addValue("from_", user.getUserName());
		parameters.addValue("to_", match.getUserName());
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for (Map<String, Object> row: rows) {	
			msgs.add((long) row.get("message_id"));
		}
		return msgs;
	}
	
	public void updateUser(User user) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "\r\n" +
				"Update sec_user SET firstName=:firstName, age=:age, ethnicity=:ethnicity, "
				+ "campus=:campus, program=:program, bio=:bio, interestedIn=:interestedIn, "
				+ "picBytes=:picBytes WHERE userName=:userName";
		parameters.addValue("firstName", user.getFirstName());
		parameters.addValue("age", user.getAge());
		parameters.addValue("ethnicity", user.getEthnicity());
		parameters.addValue("campus", user.getCampus());
		parameters.addValue("program", user.getProgram());
		parameters.addValue("bio", user.getBio());
		parameters.addValue("interestedIn", user.getInterestedIn());
		parameters.addValue("picBytes", user.getPicBytes());
		parameters.addValue("userName", user.getUserName());
		jdbc.update(query, parameters);
	}
	
	public void addUserFilter(String userName, String campus, String program, String ethnicity, 
			String interestedIn, String showMe) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "\r\n" +
				"INSERT INTO user_filter VALUES (:userName, :campus, :program, :ethnicity, :interestedIn,"
				+ " :showMe)";
		parameters.addValue("userName", userName);
		parameters.addValue("campus", campus);
		parameters.addValue("program", program);
		parameters.addValue("ethnicity", ethnicity);
		parameters.addValue("interestedIn", interestedIn);
		parameters.addValue("showMe", showMe);
		jdbc.update(query, parameters);
	}
	
	public User getFilter(User user){
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "\r\n" +
				"SELECT * from user_filter WHERE userName=:userName";
		parameters.addValue("userName", user.getUserName());
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for (Map<String, Object> row: rows) {
			user.setCampusSearch((String) row.get("campus"));
			user.setProgramSearch((String) row.get("program"));
			user.setEthnicitySearch((String) row.get("ethnicity"));
			user.setInterestedInSearch((String) row.get("InterestedIn"));
			user.setShowMeSearch((String) row.get("showMe"));
		}
		return user;
	}
	
	public void updateFilter(User user) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "\r\n" +
				"Update user_filter SET campus=:campus, program=:program, ethnicity=:ethnicity, "
				+ "interestedIn=:interestedIn, showMe=:showMe WHERE userName=:userName";
		parameters.addValue("campus", user.getCampusSearch());
		parameters.addValue("program", user.getProgramSearch());
		parameters.addValue("ethnicity", user.getEthnicitySearch());
		parameters.addValue("interestedIn", user.getInterestedInSearch());
		parameters.addValue("showMe", user.getShowMeSearch());
		parameters.addValue("userName", user.getUserName());
		jdbc.update(query, parameters);
	}
	
	public ArrayList<User> getUsersByFilter(User user) {
		ArrayList<User> users = new ArrayList<User>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user where campus LIKE :campus AND "
				+ "program LIKE :program AND ethnicity LIKE :ethnicity AND "
				+ "interestedIn LIKE :interestedIn AND gender LIKE :showMe";
		if(user.getCampusSearch().equalsIgnoreCase("all")) {
			user.setCampusSearch("%");
		}
		if(user.getProgramSearch().equalsIgnoreCase("all")) {
			user.setProgramSearch("%");
		}
		if(user.getEthnicitySearch().equalsIgnoreCase("all")) {
			user.setEthnicitySearch("%");
		}
		if(user.getInterestedInSearch().equalsIgnoreCase("all")) {
			user.setInterestedInSearch("%");
		}
		if(user.getShowMeSearch().equalsIgnoreCase("all")) {
			user.setShowMeSearch("%");
		}
		
		if(user.getShowMeSearch().equalsIgnoreCase("males") || user.getShowMeSearch().equalsIgnoreCase("females")) {
			user.setShowMeSearch(user.getShowMeSearch().substring(0, user.getShowMeSearch().length() - 1));
			//this is to remove the trailing s. --- i.e- want to search 'male', not 'males'.
			parameters.addValue("showMe", user.getShowMeSearch());
		}
		else {
			parameters.addValue("showMe", "%"+user.getShowMeSearch());
		}
		parameters.addValue("campus", "%"+user.getCampusSearch()); //wildcard everything
		parameters.addValue("program", "%"+user.getProgramSearch());
		parameters.addValue("ethnicity", "%"+user.getEthnicitySearch());
		parameters.addValue("interestedIn", "%"+user.getInterestedInSearch());
		
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		for (Map<String, Object> row: rows) {
			User u = new User();
			u.setId((long) row.get("userId"));
			u.setUserName((String) row.get("userName"));
			if(!user.getUserName().equalsIgnoreCase(u.getUserName())) {
				u.setFirstName((String) row.get("firstName"));
				u.setEncryptedPassword((String) row.get("encryptedPassword"));
				u.setAge((int) row.get("age"));
				u.setGender((String) row.get("gender"));
				u.setEthnicity((String) row.get("ethnicity"));
				u.setCampus((String) row.get("campus"));
				u.setProgram((String) row.get("program"));
				u.setBio((String) row.get("bio"));
				u.setInterestedIn((String) row.get("interestedIn"));
				u.setShowMe((String) row.get("showMe"));
				u.setPicBytes((byte[]) row.get("picbytes"));
				u.setLikes((long) row.get("likes"));
				u.setMatches((long) row.get("matches"));
				users.add(u);
			}
		}
		return users;
	}
	
	public void deleteSecUser(String userName) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "Delete from user_messages WHERE from_=:userName";
		parameters.addValue("userName", userName);
		jdbc.update(query, parameters);
		query = "Delete from user_messages WHERE to_=:userName";
		parameters.addValue("userName", userName);
		jdbc.update(query, parameters);
		query = "Delete from user_filter WHERE userName=:userName";
		parameters.addValue("userName", userName);
		jdbc.update(query, parameters);
		query = "Delete from user_match WHERE userName1=:userName";
		parameters.addValue("userName", userName);
		jdbc.update(query, parameters);
		query = "Delete from user_match WHERE userName2=:userName";
		parameters.addValue("userName", userName);
		jdbc.update(query, parameters);
		query = "Delete from user_like WHERE userId=:userId";
		parameters.addValue("userId", findUserAccount(userName).getId());
		jdbc.update(query, parameters);
		query = "Delete from user_like WHERE userMatch=:userName";
		parameters.addValue("userName", userName);
		jdbc.update(query, parameters);
		query = "Delete from user_dislike WHERE userMatch=:userName";
		parameters.addValue("userName", userName);
		jdbc.update(query, parameters);
		query = "Delete from user_dislike WHERE userId=:userId";
		parameters.addValue("userId", findUserAccount(userName).getId());
		jdbc.update(query, parameters);
		query = "Delete from user_role WHERE userId=:userId";
		parameters.addValue("userId", findUserAccount(userName).getId());
		jdbc.update(query, parameters);
		query = "Delete from sec_user WHERE userName=:userName";
		parameters.addValue("userName", userName);
		jdbc.update(query, parameters);
	}
	
	public void updatePassword(String username, String password) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "UPDATE sec_user SET encryptedPassword=:encryptedPassword WHERE userName=:userName";
		parameters.addValue("encryptedPassword", passwordEncoder.encode(password));
		parameters.addValue("userName", username);
		jdbc.update(query, parameters);
	}
	
	public ArrayList<String> getUserNames(){
		ArrayList<String> userNames = new ArrayList<String>();
		String query = "SELECT userName from sec_user";
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, new HashMap<String, Object>());
		for (Map<String, Object> row: rows) {
			userNames.add((String) row.get("userName"));
		}
		
		return userNames;
	}
	
	public long getNumberOfLikes() {
		String query = "SELECT count(likes) from sec_user WHERE likes > 0";
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, new HashMap<String, Object>());
		long total = 0;
		for (Map<String, Object> row: rows) {
			total = ((long) row.get("count(likes)"));
		}
		
		return total;
	}
	
	public long getNumberOfMatches() {
		String query = "SELECT count(matches) from sec_user WHERE matches > 0";
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, new HashMap<String, Object>());
		long total = 0;
		for (Map<String, Object> row: rows) {
			total = ((long) row.get("count(matches)"));
		}
		
		return total;
	}
	
	public long getNumberOfGender(String gender) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT count(gender) from sec_user WHERE gender=:gender";
		parameters.addValue("gender", gender);
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		long total = 0;
		for (Map<String, Object> row: rows) {
			total = ((long) row.get("count(gender)"));
		}
		
		return total;
	}
	
	public long getNumberOfInterestedIn(String thing) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT count(interestedIn) from sec_user WHERE interestedIn=:thing";
		parameters.addValue("thing", thing);
		List<Map<String, Object>> rows = 
				jdbc.queryForList(query, parameters);
		long total = 0;
		for (Map<String, Object> row: rows) {
			total = ((long) row.get("count(interestedIn)"));
		}
		
		return total;
	}
	
	/*
	 * Method is not used and should not be used - was initially built for testing purposes only.
	 * 
	public void deleteAllAccounts() {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "DELETE FROM sec_user where userName!='smadmin'";
		jdbc.update(query, parameters);
	}
	*/
}
