package br.ce.wcaquino.services;

import static br.ce.wcaquino.buiders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.buiders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.buiders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MathersProprios.caiEm;
import static br.ce.wcaquino.matchers.MathersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MathersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MathersProprios.ehHojeComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.dao.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.utils.DataUtils;
//import buildermaster.BuilderMaster;

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
		LocacaoDAO dao = new LocacaoDAOFake();
		service.setLocacaoDao(dao);
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// Cen�rio -> onde as variav�is s�o inicializadas
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

		// A��o -> onde invocamos o metodo que vamos testar
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Valida��o -> onde vamos coletar o resultado da a��o com o cen�rio
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));

	}

	/**
	 * 1� forma: ELEGANTE
	 * 
	 * @throws Exception
	 */
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// Cen�rio -> onde as variav�is s�o inicializadas
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		// A��o -> onde invocamos o metodo que vamos testar
		service.alugarFilme(usuario, filmes);

	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {

		// Cen�rio
		List<Filme> filmes = Arrays.asList(umFilme().agora());

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
		Usuario usuario = umUsuario().agora();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// A��o
		service.alugarFilme(usuario, null);

		System.out.println("Forma Nova");
	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// Cen�rio -> 
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// A��o -> onde invocamos o metodo que vamos testar
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		// Verifica��o
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
		
		//Matches Pr�prios
		assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//		assertThat(retorno.getDataRetorno(), MathersProprios.caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY)); // import static
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

	}
	
//	public static void main(String[] args) {
//		new BuilderMaster().gerarCodigoClasse(Locacao.class);
//	}

}
