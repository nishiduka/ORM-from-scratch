package sqlite;

public class TableForeignKey {

	private String name;
	private String tableRef;
	
	public TableForeignKey(String name, String tableRef) {
		super();
		this.name = name;
		this.tableRef = tableRef;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTableRef() {
		return tableRef;
	}

	public void setTableRef(String tableRef) {
		this.tableRef = tableRef;
	}

	
}
