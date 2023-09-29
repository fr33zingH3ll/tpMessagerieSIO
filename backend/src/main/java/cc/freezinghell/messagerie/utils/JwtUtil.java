package cc.freezinghell.messagerie.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import cc.freezinghell.messagerie.BackApplication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/*
 * gère toute les fonctions du jwt
 */

@Component
public class JwtUtil {
	
	/*
	 * recupere le username dans le token
	 * 
	 * @return String qui est le username
	 * @param String qui est le token
	 */
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	/*
	 * recupere la date d'expiration dans le token
	 * 
	 * @return Date qui est la date d'expiration
	 * @param String qui est le token
	 */

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	/*
	 * recupere un claim dans le token
	 * 
	 * @return <Claims, T>
	 * @param String qui est le token
	 * @param Function<Claims, T>
	 */

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/*
	 * recupere tous les claims dans le token
	 * 
	 * @return Claims qui sont les données stocker dans le token
	 * @param String qui est le token
	 */

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(this.getSecret()).build().parseClaimsJws(token).getBody();
	}
	
	/*
	 * génère le token grace au username
	 * 
	 * @return String qui est le token
	 * @param String qui est le username
	 */

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}
	
	/*
	 * créer le jwt 
	 * 
	 * @return String qui est le token générer
	 * @param Map<String, Object> 
	 * @param String qui est l'utilisateur pour qui générer le token
	*/

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + BackApplication.getConfig().jwtExpiration))
				.signWith(this.getSecret())
				.compact();
	}
	
	/*
	 * vérifier si la date d'expiration est atteinte
	 * 
	 * @return boolean
	 * @param String
	 */

	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	/*
	 * vérifier si le token est valide 
	 * 
	 * @return boolean
	 * @param String 
	 * @param UserDetails
	 */

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	/*
	 * recupere la clé de cryptage
	 * 
	 * @return SecretKeySpec
	 */

	private SecretKeySpec getSecret() {
		return new SecretKeySpec(Hex.decode(BackApplication.getConfig().jwtKey),
				BackApplication.getConfig().jwtKeyAlgo);
	}
}
