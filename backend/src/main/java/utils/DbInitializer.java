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
		this.DbInit();
		this.TablesInit();
	}
	
	public void DbInit() {
		Result<Object> isCreated = r.dbList().contains(this.config.dbName).run(this.connect);
		if (isCreated.toList() == null) {
			r.dbCreate(this.config.dbName).run(this.connect);
		}
	}
	
	public void TablesInit() {
		Result<Object> table1 = r.db(this.config.dbName).tableList().contains(this.config.table1).run(this.connect);
		Result<Object> table2 = r.db(this.config.dbName).tableList().contains(this.config.table2).run(this.connect);
		Result<Object> table3 = r.db(this.config.dbName).tableList().contains(this.config.table3).run(this.connect);
		if (table1.toList() == null) {
			r.db(this.config.dbName).tableCreate(this.config.table1).run(this.connect);
		}
		if (table2.toList() == null) {
			r.db(this.config.dbName).tableCreate(this.config.table2).run(this.connect);
		}
		if (table3.toList() == null) {
			r.db(this.config.dbName).tableCreate(this.config.table3).run(this.connect);
		}
	}
}
