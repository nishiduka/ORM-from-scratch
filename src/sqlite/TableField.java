package sqlite;

public class TableField {

	private String name;
	private String type;
	private String modifiers;

	public TableField(String name, String type, String modifiers) {
		super();

		this.name = name;
		this.type = type;
		this.modifiers = modifiers;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModifiers() {
		return modifiers;
	}

	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}

	public String toString() {
		return String.format("%s %s %s", this.name, this.type, this.modifiers);
	}
}
