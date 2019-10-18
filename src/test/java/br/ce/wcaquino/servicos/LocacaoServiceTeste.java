package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemestoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {

	private LocacaoService service;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		service = new LocacaoService();
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// Cenário
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		// ação
		Locacao locacao;

		locacao = service.alugarFilme(usuario, filmes);

		// Verificação
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getValor(), is(not(6.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				is(true));

	}

	/**
	 * forma elegante
	 * 
	 * @throws Exception
	 */
	@Test(expected = FilmesSemestoqueException.class)
	public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {

		// Cenário
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

		// ação
		Locacao locacao;

		locacao = service.alugarFilme(usuario, filmes);

	}

	/**
	 * Utilizar quando precisar da mensagem
	 * 
	 * @throws FilmesSemestoqueException
	 */
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmesSemestoqueException {
		// Cenário
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		// ação
		Locacao locacao;

		try {
			locacao = service.alugarFilme(null, filmes);
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}

		System.out.println("Forma robusta");

	}

	/**
	 * Quando execeção é importante
	 * 
	 * @throws FilmesSemestoqueException
	 * @throws LocadoraException
	 */
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmesSemestoqueException, LocadoraException {
		// Cenário
		Usuario usuario = new Usuario("Usuario 1");

		// ação
		Locacao locacao;

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		locacao = service.alugarFilme(usuario, null);

		System.out.println("Forma Nova");
	}
	
	@Test
	public void devePagar75PctoNoFilme3() throws FilmesSemestoqueException, LocadoraException {
		
		// cenário
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0));
		
		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// Verificação (4 + 4 + 3(o terceiro tem desconto 25%) = 11
		assertThat(resultado.getValor(), is(11.0));
	}
	
	@Test
	public void devePagar50PctoNoFilme4() throws FilmesSemestoqueException, LocadoraException {
		
		// cenário
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0)
				,new Filme("Filme 4", 2, 4.0));
		
		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// Verificação (4 + 4 + 3(o terceiro tem desconto 25%) + 2 = 13
		assertThat(resultado.getValor(), is(13.0));
	}
	
	@Test
	public void devePagar25PctoNoFilme5() throws FilmesSemestoqueException, LocadoraException {
		
		// cenário
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0)
				,new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0));
		
		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// Verificação (4 + 4 + 3(o terceiro tem desconto 25%) + 2  + 1 = 14
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void devePagar0PctoNoFilme6() throws FilmesSemestoqueException, LocadoraException {
		
		// cenário
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0)
				,new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0), new Filme("Filme 6", 2, 4.0));
		
		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// Verificação (4 + 4 + 3(o terceiro tem desconto 25%) + 2  + 1 + 0 = 14
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmesSemestoqueException, LocadoraException {

		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// cenário
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		// Ação
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		// Verificação 
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.SUNDAY);
		Assert.assertTrue(ehSegunda);
	}
}
