package controller;

import static com.rethinkdb.RethinkDB.r;

import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Result;

public class DbInitializer {
	
	private Config config;
	private Connection connect;
	
	public DbInitializer(Config config, Connection connect) {
		this.config = config;
		this.connect = connect;
		this.DbInit();
		this.TablesInit();
	}
	
	public void DbInit() {
		Result<Object> isCreated = r.dbList().contains(this.config.dbName).run(this.connect);
		System.out.print(isCreated.getClass());
	}
	
	public void TablesInit() {
		
	}
}
