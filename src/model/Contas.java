package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import sqlite.ConnectionDB;
import sqlite.Table;
import sqlite.TableField;
import sqlite.TableForeignKey;

public class Contas extends Base {
	private static boolean alreadyLoadedTable = false;
	private static String tableName = "contas"; 
	
	private Integer id;
	private double valor;
	private String descricao;
	private Integer donoId;
	
	private Table schema() {
		ArrayList<TableField> fields = new ArrayList<TableField>();
		fields.add(new TableField("id", "integer", "PRIMARY KEY AUTOINCREMENT"));
		fields.add(new TableField("valor", "real", ""));
		fields.add(new TableField("descricao", "varchar(100)", ""));
		fields.add(new TableField("donoId", "integer", ""));
		
		ArrayList<TableForeignKey> foreignKeys = new ArrayList<TableForeignKey>();
		foreignKeys.add(new TableForeignKey("donoId", "pessoa"));
		
		return new Table(tableName, fields, foreignKeys);
	}
	
	public Contas() {
		if (!alreadyLoadedTable) {
			alreadyLoadedTable = true;
			this.createTable(schema());
		}	
	}
	
	public Contas(Integer id, double valor, String descricao, Integer donoId) {
		super();
		this.id = id;
		this.valor = valor;
		this.descricao = descricao;
		this.donoId = donoId;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
	  if ( this.getDonoId() == null ) {
	    throw new Error("Dono Id é obrigatório");
	  }
	  
	  if ( this.getValor() == 0 ) {
		  throw new Error("Valor é obrigatório");
	  }

	  if ( this.getDescricao().isEmpty() ) {
		  throw new Error("Descricao é obrigatório");
	  }
	  
	  return true;
	}
	
	public void save() {
		this.validateAllFields();
		
		String sql = String.format(Locale.ENGLISH, "INSERT INTO %s (descricao, valor, donoId) VALUES (\"%s\", %.2f, %d);", tableName, this.getDescricao(), this.getValor(), this.getDonoId());
		
		try {
			ConnectionDB.executeSQLOperation(sql);
			ResultSet rs = ConnectionDB.executeSQL("select last_insert_rowid()");

			if ( rs != null ) {			
				this.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	

	public ArrayList<Contas> retriveAll() {
		ArrayList<Contas> array = new ArrayList<Contas>();
		
		String sql = String.format("SELECT * FROM %s", tableName);
		
		
		try {
			ResultSet rs = ConnectionDB.executeSQL(sql);
			
			if ( rs == null ) {
				return array;
			}
			
			while(rs.next()) {
				array.add(new Contas(rs.getInt("id"), rs.getFloat("valor"), rs.getString("descricao"), rs.getInt("donoId")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return array;
	}
	
	public void retriveAllString() {
		ArrayList<Contas> array = this.retriveAll();
		
		this.showContentHeader();
		
		for (int i = 0; i < array.size(); i++) {
			array.get(i).showContentRow();
		}
		
		System.out.println("\n\n");
	}
	
	public void showContentHeader() {
		System.out.println(
			"+-----------------------------------------------------------------------+\n" + 
			"|\tID\t|\tDescricao\t|\tValor\t| Nome do dono  |\n" +
			"+-----------------------------------------------------------------------+"
		);
	}
	
	public void showContentRow() {
		System.out.println(
				String.format(
						"|\t%d\t|\t%s\t|\t%s\t|\t%s\t|",
						this.getId(),
						this.getDescricao(),
						this.getValor(),
						this.getDonoId()
						)
				);
	}
}
