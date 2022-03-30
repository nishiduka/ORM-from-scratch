package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sqlite.ConnectionDB;
import sqlite.Table;
import sqlite.TableField;
import sqlite.TableForeignKey;

public class Afazer extends Base {
	private static boolean alreadyLoadedTable = false;

	private String tableName = "afazer";
	
	private Integer id;
	private String descricao;
	private boolean concluido = false;
	private Integer pessoaId;
	
	private Table schema() {
		ArrayList<TableField> fields = new ArrayList<TableField>();
		fields.add(new TableField("id", "integer", "PRIMARY KEY AUTOINCREMENT"));
		fields.add(new TableField("descricao", "varchar(100)", ""));
		fields.add(new TableField("concluido", "int", ""));
		fields.add(new TableField("pessoaId", "integer", ""));

		ArrayList<TableForeignKey> foreignKeys = new ArrayList<TableForeignKey>();
		foreignKeys.add(new TableForeignKey("pessoaId", "pessoa"));
		
		return new Table(tableName, fields, foreignKeys);
	}
	
	public Afazer() {
		if (!alreadyLoadedTable) {
			alreadyLoadedTable = true;
			this.createTable(schema());
		}	
	}
	
	public Afazer(Integer id, String descricao, boolean concluido, Integer pessoaId) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.concluido = concluido;
		this.pessoaId = pessoaId;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public boolean isConcluido() {
		return concluido;
	}
	
	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}
	
	public Integer getPessoaId() {
		return pessoaId;
	}
	
	public void setPessoaId(Integer pessoaId) {
		this.pessoaId = pessoaId;
	}
	
	public boolean validateAllFields() {
	  if ( this.getDescricao() == "" ) {
	    throw new Error("Descricao é obrigatório");
	  }

	  return true;
	}
	
	public boolean validateAllFields(boolean forUpdate) {

		this.validateAllFields();

		if ( forUpdate ) {
			if ( this.getId() == null ) {
				throw new Error("Id é obrigatório");
		    }
		  }

		return true;
	}
	
	public Pessoa getPessoa() {
		if (this.pessoaId == null) {
			throw new Error("Id pessoa nao setado");
		}

		Pessoa pessoa = new Pessoa();
		pessoa.setId(pessoaId);
		
		return pessoa.getPessoa();
	}
	
	public void save() {
		this.validateAllFields();
		
		String sql = String.format("INSERT INTO %s (descricao, concluido, pessoaId) VALUES (\"%s\", \"%s\", %d);", this.tableName, this.getDescricao(), this.isConcluido() ? "1" : "0", this.getPessoaId());
		
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
	
	public ArrayList<Afazer> retriveAll() {
		ArrayList<Afazer> array = new ArrayList<Afazer>();
		
		String sql = String.format("SELECT * FROM %s", this.tableName);
		
		
		try {
			ResultSet rs = ConnectionDB.executeSQL(sql);
			
			if ( rs == null ) {
				return array;
			}
			
			while(rs.next()) {
				array.add(new Afazer(rs.getInt("id"), rs.getString("descricao"), rs.getInt("concluido") == 1, rs.getInt("pessoaId")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return array;
	}
	
	public void retriveAllString() {
		ArrayList<Afazer> array = this.retriveAll();
		
		this.showContentHeader();
		
		for (int i = 0; i < array.size(); i++) {
			array.get(i).showContentRow();
		}
		
		System.out.println("\n\n");
	}
	
	public void showContentHeader() {
		System.out.println(
			"+-------------------------------------------------------+\n" + 
			"|\tID\t|\tDescricao\t| Concluido\t|\tPessoa\n|\n" +
			"+-------------------------------------------------------+"
		);
	}
	
	public void showContentRow() {
		System.out.println(
				String.format(
						"|\t%d\t|\t%s\t|\t%s\t|\t%s",
						this.getId(),
						this.getDescricao(),
						this.isConcluido() ? "V" : "F",
						this.getPessoa().getNome()
						)
				);
	}
}
