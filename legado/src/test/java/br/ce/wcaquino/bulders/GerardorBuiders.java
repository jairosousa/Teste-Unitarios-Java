package br.ce.wcaquino.bulders;

import br.ce.wcaquino.entidades.Locacao;
import buildermaster.BuilderMaster;

public class GerardorBuiders {

	public static void main(String[] args) {
		
		new BuilderMaster().gerarCodigoClasse(Locacao.class);

	}

}
