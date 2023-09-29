package cc.freezinghell.messagerie.config;

/*
 * juste une classe qui recup la config
 */

public class Config {
	public String dbUrl;
	public String jwtKey;
	public String jwtKeyAlgo;
	/** in seconds */
	public long jwtExpiration;
}
