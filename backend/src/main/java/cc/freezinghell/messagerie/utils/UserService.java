package cc.freezinghell.messagerie.utils;

import java.util.Collection;

import static com.rethinkdb.RethinkDB.r;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rethinkdb.net.Result;

import cc.freezinghell.messagerie.BackApplication;
import cc.freezinghell.messagerie.entities.User;

@Service
public class UserService implements UserDetailsService {
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try (Result<User> user = r.table("user")
				.filter(row -> row.g("email").eq(username))
				.run(BackApplication.getConnect(), User.class)) {
			if (user.hasNext()) return user.next();
			return null;
		}
	}
}
