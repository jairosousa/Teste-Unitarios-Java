package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

public class LocacaoServiceTeste {
	
	/**
	 * Os testes alem de independentes eles tem ser isolados
	 * ou seja cada teste deve verificar cada falha isoladamente,
	 * os mais radicais dizem que devemos utilizar no m�ximo uma assertiva
	 * 
	 */
	
	/**
	 * Roles � artificio recente no JUnit ele permite
	 * alterar alguns comportamentos dos testes, � possivel
	 * criar regras proprias tamb�m
	 */
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Test
	public void testeLocacao() {

		// Cen�rio -> onde as variav�is s�o inicializadas
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		// A��o -> onde invocamos o metodo que vamos testar
		Locacao locacao = service.alugarFilme(usuario, filme);

		// Valida��o -> onde vamos coletar o resiltado da a��o com o cen�rio 
		/**
		 * As Roles s�o utilizadas muito parecida com AssertThat
		 * Vantagem que ele testa todas as assertivas mesmo com erro no meio
		 * do caminho
		 */
		error.checkThat(locacao.getValor(), is(equalTo(6.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true)); 
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(false)); 
		
		
	}
}
