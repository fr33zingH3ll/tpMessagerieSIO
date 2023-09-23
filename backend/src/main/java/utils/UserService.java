package utils;

import static com.rethinkdb.RethinkDB.r;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rethinkdb.net.Result;

import entities.User;
import messagerie.back.BackApplication;

@Service
public class UserService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try (Result<User> user = r.table("user").filter(r.row("email").eq(username)).run(BackApplication.getConnect(),
				User.class)) {
			return user.next();
		}
	}

}
