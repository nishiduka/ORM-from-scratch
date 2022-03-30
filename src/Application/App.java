package Application;

import model.*;

public class App {
	public static void main(String[] args) {
//
		Pessoa pessoa = new Pessoa();
		
		pessoa.setNome("Teste Teste");
		pessoa.setCpf("00632693096");
		pessoa.setDataNascimento("2022-03-24");
		pessoa.save();
		pessoa.retriveAllString();
		
		Afazer afazer = new Afazer();
		
		afazer.setDescricao("Limpar banco");
		afazer.setConcluido(true);
		afazer.setPessoaId(pessoa.getId());
		afazer.save();
				
		afazer.retriveAllString();
		
		Contas contas = new Contas();	
		
		contas.setDescricao("Gasolina");
		contas.setValor(50.0);
		contas.setDonoId(1);
		contas.save();

		contas.retriveAllString();

		Pet pet = new Pet();
		
		pet.setNome("Thor");
		pet.setTipo("Gato");
		pet.setDonoId(1);
		
		pet.save();
		
		pet.retriveAllString();
		
	}
}
