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
	
	/*
	 * UserService permet de recupérer l'utilisateur grace a son username (ici l'email dans ce cas)
	 * 
	 * @return UserDetails qui implements UserDetails
	 * @return null si aucun utilisateur n'est trouver
	 * @param String username (email)
	 * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé (erreur voulu qui ne bloque pas le code)
	 */
	
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
