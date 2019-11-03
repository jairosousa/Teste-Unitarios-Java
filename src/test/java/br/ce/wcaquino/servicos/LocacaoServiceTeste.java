package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.bulders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.bulders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.bulders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNmaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencasDeDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {

	@InjectMocks
	private LocacaoService service;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private SPCService spc;

	@Mock
	private LocacaoDAO dao;
	
	@Mock
	private EmailService email;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// Cen�rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

		// a��o
		Locacao locacao;

		locacao = service.alugarFilme(usuario, filmes);

		// Verifica��o
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getValor(), is(not(6.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
//		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),is(true));
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencasDeDias(1));

	}

	/**
	 * forma elegante
	 * 
	 * @throws Exception
	 */
	@Test(expected = FilmesSemEstoqueException.class)
	public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {

		// Cen�rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

		// a��o
		Locacao locacao;

		locacao = service.alugarFilme(usuario, filmes);

	}

	/**
	 * Utilizar quando precisar da mensagem
	 * 
	 * @throws FilmesSemEstoqueException
	 */
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmesSemEstoqueException {
		// Cen�rio
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// a��o
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
	 * Quando exece��o � importante
	 * 
	 * @throws FilmesSemEstoqueException
	 * @throws LocadoraException
	 */
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmesSemEstoqueException, LocadoraException {
		// Cen�rio
		Usuario usuario = umUsuario().agora();;

		// a��o
		Locacao locacao;

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		locacao = service.alugarFilme(usuario, null);

		System.out.println("Forma Nova");
	}
	
	@Test
	public void devePagar75PctoNoFilme3() throws FilmesSemEstoqueException, LocadoraException {
		
		// cen�rio
		Usuario usuario = umUsuario().agora();;
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0));
		
		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// Verifica��o (4 + 4 + 3(o terceiro tem desconto 25%) = 11
		assertThat(resultado.getValor(), is(11.0));
	}
	
	@Test
	public void devePagar50PctoNoFilme4() throws FilmesSemEstoqueException, LocadoraException {
		
		// cen�rio
		Usuario usuario = umUsuario().agora();;
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0)
				,new Filme("Filme 4", 2, 4.0));
		
		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// Verifica��o (4 + 4 + 3(o terceiro tem desconto 25%) + 2 = 13
		assertThat(resultado.getValor(), is(13.0));
	}
	
	@Test
	public void devePagar75PctoNoFilme5() throws FilmesSemEstoqueException, LocadoraException {
		
		// cen�rio
		Usuario usuario = umUsuario().agora();;
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0)
				,new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0));
		
		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// Verifica��o (4 + 4 + 3(o terceiro tem desconto 25%) + 2  + 1 = 14
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void devePagar0PctoNoFilme6() throws FilmesSemEstoqueException, LocadoraException {
		
		// cen�rio
		Usuario usuario = umUsuario().agora();;
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0)
				,new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0), new Filme("Filme 6", 2, 4.0));
		
		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// Verifica��o (4 + 4 + 3(o terceiro tem desconto 25%) + 2  + 1 + 0 = 14
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmesSemEstoqueException, LocadoraException {

		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// cen�rio
		Usuario usuario = umUsuario().agora();;
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		// A��o
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		// Verifica��o 
//		assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//		assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiNmaSegunda());
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmesSemEstoqueException {
		//cen�rio
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		//A��o
		try {
			service.alugarFilme(usuario, filmes);
			//Verifica��o
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario Negativado"));
		}
		
		verify(spc).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrazadas() {
		
		//cenario
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro trasado").agora();
		List<Locacao> locacoes = Arrays.asList(
				umLocacao().atrasada().comUsuario(usuario).agora(),
				umLocacao().comUsuario(usuario2).agora(),
				umLocacao().atrasada().comUsuario(usuario3).agora(),
				umLocacao().atrasada().comUsuario(usuario3).agora());
		
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		service.nofiticarAtrasos();
				
		//verificacao
		verify(email, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
		verify(email).notificarAtraso(usuario);
		verify(email, Mockito.atLeastOnce()).notificarAtraso(usuario3);
		verify(email, never()).notificarAtraso(usuario2);
		Mockito.verifyNoMoreInteractions(email);
		
	}
}




