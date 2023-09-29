package cc.freezinghell.messagerie.utils;

import static com.rethinkdb.RethinkDB.r;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Result;

/*
 * initialise la base de donnée
 */

public class DbInitializer {
	private static final List<String> TABLES_TO_CREATE = List.of("user", "message", "channel");
	private static final Logger LOG = LoggerFactory.getLogger(DbInitializer.class);
	private Connection connect;

	public DbInitializer(Connection connect) {
		this.connect = connect;
	}

	/**
	 * initialise les tables RethinkDB
	 */
	public void tablesInit() {
		Result<List<String>> res = r.tableList().run(this.connect, new TypeReference<List<String>>() {});
		List<String> existingTables = res.first();

		for (String toCreate : TABLES_TO_CREATE) {
			if (!existingTables.contains(toCreate)) {
				LOG.info("Creating table {}", toCreate);
				r.tableCreate(toCreate).run(connect).close();
			}
		}

	}
}
