package model;

import sqlite.ConnectionDB;
import sqlite.Table;

public class Base {
	protected boolean createTable(Table table) {
		return ConnectionDB.executeSQLOperation(table.toString());
	}
}
 