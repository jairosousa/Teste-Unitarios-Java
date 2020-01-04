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
	 * Não deve devolver filmes no domingo.
	 */

	/**
	 * DESCONTOS CRESCENTES 25% no 3º filme 50% no 4º filme 75% no 5º filme 100% no
	 * 6º filme
	 */

	/**
	 * Roles é artificio recente no JUnit ele permite alterar alguns comportamentos
	 * dos testes, é possivel criar regras proprias também
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

		// Cenário -> onde as variavéis são inicializadas
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

		// Ação -> onde invocamos o metodo que vamos testar
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Validação -> onde vamos coletar o resultado da ação com o cenário
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));

	}

	/**
	 * 1ª forma: ELEGANTE
	 * 
	 * @throws Exception
	 */
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// Cenário -> onde as variavéis são inicializadas
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		// Ação -> onde invocamos o metodo que vamos testar
		service.alugarFilme(usuario, filmes);

	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {

		// Cenário
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// Ação
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}

	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		Usuario usuario = umUsuario().agora();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// Ação
		service.alugarFilme(usuario, null);

		System.out.println("Forma Nova");
	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// Cenário -> 
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// Ação -> onde invocamos o metodo que vamos testar
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		// Verificação
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
		
		//Matches Próprios
		assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//		assertThat(retorno.getDataRetorno(), MathersProprios.caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY)); // import static
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

	}
	
//	public static void main(String[] args) {
//		new BuilderMaster().gerarCodigoClasse(Locacao.class);
//	}

}
