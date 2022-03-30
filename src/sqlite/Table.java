package sqlite;

import java.util.ArrayList;

public class Table {
	private String name;
	private ArrayList<TableField> fields;
	private ArrayList<TableForeignKey> foreignKeys;
	private String primaryKeyName = "id";
	
	public Table(String name, ArrayList<TableField> fields, ArrayList<TableForeignKey> foreignKeys) {
		super();
		this.name = name;
		this.fields = fields;
		this.foreignKeys = foreignKeys;
	}
	
	public Table(String name, ArrayList<TableField> fields) {
		super();
		this.name = name;
		this.fields = fields;
	}
	
	public String toString() {
		String sql = 
				String.format(
					"CREATE TABLE IF NOT EXISTS %s ( %s );",
					this.name,
					this.formatSql()
				);
		
		return sql;
	}
	
	private String formatSql() {
		String sql = "";
		
		for (int counter = 0; counter < this.fields.size(); counter++) { 		      
		    sql += this.fields.get(counter).toString();
		    
		    if ( counter < (this.fields.size() - 1) ) {
		    	sql += ", ";
		    }
		}
		
		if ( this.foreignKeys != null ) {
			sql += ", ";	

			for (int index = 0; index < this.foreignKeys.size(); index++) {
				sql += String.format(
						"FOREIGN KEY (%s) REFERENCES %s(id)",
						this.foreignKeys.get(index).getName(),
						this.foreignKeys.get(index).getTableRef()
					   );
				
				if ( index < (this.foreignKeys.size() - 1) ) {
			    	sql += ", ";
			    }
			}
		}
		
//		sql += String.format(" PRIMARY KEY (%s)", primaryKeyName);
		return sql;
	}

	public String getName() {
		return name;
	}

	public ArrayList<TableField> getFields() {
		return fields;
	}

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}
	
	
}
