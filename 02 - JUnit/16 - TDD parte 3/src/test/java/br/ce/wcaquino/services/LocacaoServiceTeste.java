package br.ce.wcaquino.services;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {

	/**
	 * N�o deve devolver filmes no domingo.
	 */

	/**
	 * DESCONTOS CRESCENTES 25% no 3� filme 50% no 4� filme 75% no 5� filme 100% no
	 * 6� filme
	 */

	/**
	 * Roles � artificio recente no JUnit ele permite alterar alguns comportamentos
	 * dos testes, � possivel criar regras proprias tamb�m
	 */
	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private LocacaoService service;

	@Before
	public void setup() {
		service = new LocacaoService();
	}

	@Test
	public void deveAlugarFilme() throws Exception {

		// Cen�rio -> onde as variav�is s�o inicializadas
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		// A��o -> onde invocamos o metodo que vamos testar
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Valida��o -> onde vamos coletar o resiltado da a��o com o cen�rio
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
//		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

	}

	/**
	 * 1� forma: ELEGANTE
	 * 
	 * @throws Exception
	 */
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// Cen�rio -> onde as variav�is s�o inicializadas
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

		// A��o -> onde invocamos o metodo que vamos testar
		service.alugarFilme(usuario, filmes);

	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {

		// Cen�rio
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0));

		// A��o
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}

	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

		// Cen�rio
		Usuario usuario = new Usuario("Usuario 1");

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// A��o
		service.alugarFilme(usuario, null);

		System.out.println("Forma Nova");
	}

	@Test
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {

		// Cen�rio
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0));

		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verifica��o 4+4+3
		assertThat(resultado.getValor(), is(11.0));

	}

	@Test
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {

		// Cen�rio
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0));

		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verifica��o 4+4+3+2
		assertThat(resultado.getValor(), is(13.0));

	}

	@Test
	public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {

		// Cen�rio
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0));

		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verifica��o 4+4+3+2+1
		assertThat(resultado.getValor(), is(14.0));

	}

	@Test
	public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {

		// Cen�rio
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0),
				new Filme("Filme 6", 2, 4.0));

		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verifica��o 4+4+3+2+1+0
		assertThat(resultado.getValor(), is(14.0));

	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {

		// Cen�rio -> 
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		// A��o -> onde invocamos o metodo que vamos testar
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);

	}

}
