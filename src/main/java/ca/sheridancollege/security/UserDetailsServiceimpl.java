package ca.sheridancollege.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.database.DatabaseAccess;

@Service
public class UserDetailsServiceimpl implements UserDetailsService {
	
	@Autowired
	@Lazy
	DatabaseAccess da;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//get the user from the database
		ca.sheridancollege.beans.User user = da.findUserAccount(username);
		//stop if the user doesn't exist
		if(user == null) {
			System.out.println("User not found: " + username);
			throw new UsernameNotFoundException("User " + username +  " was not found in the database.");
		}
		//get the list of roles if they do exist of the user
		List<String> roleNames = da.getRolesById(user.getId());
		//we need to convert our role names into a list of granted authorities
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for(String role : roleNames) {
				grantList.add(new SimpleGrantedAuthority(role));
			}
		}
		//build a spring boot user
		UserDetails userDetails = (UserDetails) new User(user.getUserName(),
				user.getEncryptedPassword(),grantList);
		
		return userDetails;
	}
}
