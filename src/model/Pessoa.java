package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import sqlite.ConnectionDB;
import sqlite.Table;
import sqlite.TableField;

public class Pessoa extends Base{

	private static boolean alreadyLoadedTable = false;
	private static String tableName = "pessoa"; 
	
	private Integer id;
	private String nome;
	private String cpf;
	private LocalDate dataNascimento;

	private Table schema() {
		ArrayList<TableField> fields = new ArrayList<TableField>();
		fields.add(new TableField("id", "integer", "PRIMARY KEY AUTOINCREMENT"));
		fields.add(new TableField("nome", "varchar(100)", ""));
		fields.add(new TableField("cpf", "varchar(11)", ""));
		fields.add(new TableField("dataNascimento", "varchar(10)", ""));
		
		return new Table(tableName, fields);
	}
	
	public Pessoa() {
		super();
		if (!alreadyLoadedTable) {
			alreadyLoadedTable = true;
			this.createTable(schema());
		}
	}

	public Pessoa(Integer id, String nome, String cpf, LocalDate dataNascimento) {
		super();
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
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
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	
	public String getDataNascimentoString() {
		return dataNascimento.toString();
	}
	
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public void setDataNascimento(String dataNascimento) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
		this.dataNascimento = LocalDate.parse(dataNascimento, formatter);
	}

	public LocalDate convertStringToLocalDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
		return LocalDate.parse(date, formatter);
	}

	public boolean validateAllFields() {
	  if ( this.getNome() == null ) {
	    throw new Error("Nome é obrigatório");
	  }
	  
	  if ( this.getCpf() == null ) {
		  throw new Error("Cpf é obrigatório");
	  }
	  
	  if ( this.getDataNascimentoString() == null ) {
		  throw new Error("Data nascimento é obrigatório");
	  }

	  return true;
	}
	
	public Pessoa getPessoa() {
		if (this.id == null) {
			throw new Error("Id pessoa nao setado");
		}
		
		try {
			String sql = String.format("SELECT * FROM %s WHERE id = %d;", tableName, this.getId());
			ResultSet rs = ConnectionDB.executeSQL(sql);
			
			if (rs == null || !rs.next()) {
				throw new Error("Pessoa não encontrada");
			}
			
			
			Pessoa pessoa = new Pessoa(
								rs.getInt("id"),
								rs.getString("nome"),
								rs.getString("cpf"),
								this.convertStringToLocalDate(rs.getString("dataNascimento"))
							);
			return pessoa;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return new Pessoa();
		}

		
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

	public void save() {
		this.validateAllFields();
		
		String sql = String.format("INSERT INTO %s (nome, cpf, dataNascimento) VALUES (\"%s\", \"%s\", \"%s\");", tableName, this.getNome(), this.getCpf(), this.getDataNascimentoString());
		
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
	
	public ArrayList<Pessoa> retriveAll() {
		ArrayList<Pessoa> array = new ArrayList<Pessoa>();
		
		String sql = String.format("SELECT * FROM %s", tableName);
		
		
		try {
			ResultSet rs = ConnectionDB.executeSQL(sql);
			
			if ( rs == null ) {
				return array;
			}
			
			while(rs.next()) {
				array.add(new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"), this.convertStringToLocalDate(rs.getString("dataNascimento"))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return array;
	}
	
	public void retriveAllString() {
		ArrayList<Pessoa> array = this.retriveAll();
		
		this.showContentHeader();
		
		for (int i = 0; i < array.size(); i++) {
			array.get(i).showContentRow();
		}
		
		System.out.println("\n\n");
	}
	
	public void showContentHeader() {
		System.out.println(
			"+---------------------------------------------------------------------------------------+\n" + 
			"|\tID\t|\tNome\t\t|\tCpf\t\t|\tData Nascimento |\n" +
			"+---------------------------------------------------------------------------------------+"
		);
	}
	
	public void showContentRow() {
		System.out.println(
				String.format(
						"|\t%d\t|\t%s\t|\t%s\t|\t%s\t|",
						this.getId(),
						this.getNome(),
						this.getCpf(),
						this.getDataNascimentoString()
						)
				);
	}
}
