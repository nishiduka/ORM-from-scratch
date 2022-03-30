package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import sqlite.ConnectionDB;
import sqlite.Table;
import sqlite.TableField;
import sqlite.TableForeignKey;

public class Pet extends Base {
	private static boolean alreadyLoadedTable = false;
	private static String tableName = "pet"; 

	private Integer id;
	private String nome;
	private String tipo;
	private Integer donoId;

	private Table schema() {
		ArrayList<TableField> fields = new ArrayList<TableField>();
		fields.add(new TableField("id", "integer", "PRIMARY KEY AUTOINCREMENT"));
		fields.add(new TableField("nome", "varchar(100)", ""));
		fields.add(new TableField("tipo", "varchar(20)", ""));
		fields.add(new TableField("donoId", "integer", ""));
		
		ArrayList<TableForeignKey> foreignKeys = new ArrayList<TableForeignKey>();
		foreignKeys.add(new TableForeignKey("donoId", "pessoa"));

		return new Table(tableName, fields, foreignKeys);
	}
	
	public Pet(Integer id, String nome, String tipo, Integer donoId) {
		super();
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
		this.donoId = donoId;
	}

	public Pet() {
		super();
		if (!alreadyLoadedTable) {
			alreadyLoadedTable = true;
			this.createTable(schema());
		}
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public Integer getDonoId() {
		return donoId;
	}
	
	public void setDonoId(Integer donoId) {
		this.donoId = donoId;
	}
	
	public Pessoa getPessoa() {
		if (this.donoId == null) {
			throw new Error("Id dono nao setado");
		}

		Pessoa pessoa = new Pessoa();
		pessoa.setId(donoId);
		
		return pessoa.getPessoa();
	}
	
	public boolean validateAllFields() {
		  if ( this.getNome().isEmpty() ) {
		    throw new Error("Nome é obrigatório");
		  }
		  
		  if ( this.getTipo().isEmpty() ) {
			  throw new Error("Tipo é obrigatório");
		  }
		  
		  if ( this.getDonoId() == null ) {
			  throw new Error("Dono id é obrigatório");
		  }

		  return true;
		}
	
	public void save() {
		this.validateAllFields();
		
		String sql = String.format(Locale.ENGLISH, "INSERT INTO %s (nome, tipo, donoId) VALUES (\"%s\", \"%s\", %d);", tableName, this.getNome(), this.getTipo(), this.getDonoId());
		
		try {
			ConnectionDB.executeSQLOperation(sql);
			ResultSet rs = ConnectionDB.executeSQL("select last_insert_rowid()");

			if ( rs != null ) {			
				this.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	
	}
	

	public ArrayList<Pet> retriveAll() {
		ArrayList<Pet> array = new ArrayList<Pet>();
		
		String sql = String.format("SELECT * FROM %s", tableName);
		
		
		try {
			ResultSet rs = ConnectionDB.executeSQL(sql);
			
			if ( rs == null ) {
				return array;
			}
			
			while(rs.next()) {
				array.add(new Pet(rs.getInt("id"), rs.getString("nome"), rs.getString("tipo"), rs.getInt("donoId")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return array;
	}
	
	public void retriveAllString() {
		ArrayList<Pet> array = this.retriveAll();
		
		this.showContentHeader();
		
		for (int i = 0; i < array.size(); i++) {
			array.get(i).showContentRow();
		}
		
		System.out.println("\n\n");
	}
	
	public void showContentHeader() {
		System.out.println(
			"+-----------------------------------------------------------------------+\n" + 
			"|\tID\t|\tDescricao\t|\tTipo\t| Nome do dono  |\n" +
			"+-----------------------------------------------------------------------+"
		);
	}
	
	public void showContentRow() {
		System.out.println(
				String.format(
						"|\t%d\t|\t%s\t\t|\t%s\t|\t%s\t|",
						this.getId(),
						this.getNome(),
						this.getTipo(),
						this.getPessoa().getNome()
						)
				);
	}
}
