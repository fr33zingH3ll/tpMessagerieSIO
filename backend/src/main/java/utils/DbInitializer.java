package utils;

import static com.rethinkdb.RethinkDB.r;

import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Result;

public class DbInitializer {
	
	private Config config;
	private Connection connect;
	
	public DbInitializer(Config config, Connection connect) {
		this.config = config;
		this.connect = connect;
	}
	
	public void tablesInit() {
		Result<Object> table1 = r.tableList().contains("user").run(this.connect);
		Result<Object> table2 = r.tableList().contains("conversation").run(this.connect);
		Result<Object> table3 = r.tableList().contains("message").run(this.connect);
		if (table1.toList() == null) {
			r.tableCreate("user").run(this.connect);
		}
		if (table2.toList() == null) {
			r.tableCreate("conversation").run(this.connect);
		}
		if (table3.toList() == null) {
			r.tableCreate("message").run(this.connect);
		}
	}
}
