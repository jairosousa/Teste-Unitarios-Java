package br.ce.wcaquino.services;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

public class LocacaoServiceTeste {

	/**
	 * Roles é artificio recente no JUnit ele permite alterar alguns comportamentos
	 * dos testes, é possivel criar regras proprias também
	 */
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testeLocacao() throws Exception {

		// Cenário -> onde as variavéis são inicializadas
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		// Ação -> onde invocamos o metodo que vamos testar
		Locacao locacao = service.alugarFilme(usuario, filme);

		// Validação -> onde vamos coletar o resiltado da ação com o cenário
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

	}

	/**
	 * Vai verificar o caso do filme não esta no estoque para o teste passar uma
	 * Exceção deve ser lançada
	 * 
	 * Existe 3 formas de tratar exceção
	 */
	/**
	 * 1ª forma: ELEGANTE
	 * 
	 * @throws Exception
	 */
	@Test(expected = Exception.class)
	public void testLocacao_filmeSemEstoque() throws Exception {
		// Cenário -> onde as variavéis são inicializadas
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		// Ação -> onde invocamos o metodo que vamos testar
		service.alugarFilme(usuario, filme);

	}

	/**
	 * 2ª forma: ROBUSTA
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLocacao_filmeSemEstoque_2() {
		// Cenário -> onde as variavéis são inicializadas
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		// Ação -> onde invocamos o metodo que vamos testar
		try {
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lançado uma exceção"); // para informar que tem falso positivo
		} catch (Exception e) {
			/**
			 * Nesse caso se o estoque não estiver zerado o teste passa do mesmo jeito
			 * gerando um falso positivo, pois o service não gera uma exceção e não passa no
			 * catch
			 */
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}

	}

	/**
	 * 3ª forma: NOVA
	 * Utiliza Rule
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLocacao_filmeSemEstoque_3() throws Exception {
		// Cenário -> onde as variavéis são inicializadas
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");
		
		// Ação -> onde invocamos o metodo que vamos testar
		service.alugarFilme(usuario, filme);
		

	}

}
