package br.ce.wcaquino.services;

import static br.ce.wcaquino.buiders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.buiders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.buiders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.buiders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MathersProprios.caiEm;
import static br.ce.wcaquino.matchers.MathersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MathersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MathersProprios.ehHojeComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.utils.DataUtils;
//import buildermaster.BuilderMaster;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LocacaoService.class)
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

	@InjectMocks
	private LocacaoService service;

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
//		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(10, 1, 2020));
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
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {


		// Cen�rio ->
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
//		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(11, 1, 2020));

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 11);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.YEAR, 2020);
		
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		
		// A��o -> onde invocamos o metodo que vamos testar
		Locacao retorno = service.alugarFilme(usuario, filmes);

		// Verifica��o
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);

		// Matches Pr�prios
		assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//		assertThat(retorno.getDataRetorno(), MathersProprios.caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY)); // import static
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
		
		PowerMockito.verifyStatic(Mockito.times(2));
		Calendar.getInstance();

	}

	@Test
	public void naoDeveAlugarFilmeParaNegativado() throws Exception {

		// Cen�rio ->
		Usuario usuario = umUsuario().agora();
//		Usuario usuario2 = umUsuario().comNome("Usuario 2").agora(); teste falso positivo
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		Mockito.when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		// A��o -> onde invocamos o metodo que vamos testar
		try {
			service.alugarFilme(usuario, filmes);
			
			// Verifica��o
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario negativado"));
		}

//		Mockito.verify(spc).possuiNegativacao(usuario2);
		Mockito.verify(spc).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		
		// Cen�rio
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();
		
		List<Locacao> locacoes = Arrays.asList
				(umLocacao()
						.atrasado()
						.comUsuario(usuario)
						.agora(),
				umLocacao()
						.comUsuario(usuario2)
						.agora(),
				umLocacao()
						.atrasado()
						.comUsuario(usuario3)
						.agora(),
				umLocacao()
						.atrasado()
						.comUsuario(usuario3)
						.agora());
		
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		// A��o
		service.notificarAtrasos();
		
		// Verifica��o
		Mockito.verify(email, Mockito.times(3)).notificarAtrasos(Mockito.any(Usuario.class));// verifica se foi passado pelo menos dois emails pelo metodo idependente de usuarios
		Mockito.verify(email).notificarAtrasos(usuario);
		Mockito.verify(email, Mockito.atLeastOnce()).notificarAtrasos(usuario3); //deve ter enviado pelo menos um  e-mail n�o impoorta a quantidade
		Mockito.verify(email, Mockito.never()).notificarAtrasos(usuario2); // assegurar que usuario 2 n�o recebeu e-mail
		Mockito.verifyNoMoreInteractions(email); //Verifica que nenhum outro email tenha recebido a��o n�o verificada
		Mockito.verifyZeroInteractions(spc); // garatir que n�o foi chamado outro servi�o 9
		
		
	}

	@Test
	public void deveTratarErronoSPC() throws Exception {
	
		// Cen�rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		/**
		 * Deve  lan�ar uma falha caso falha no servidor SPC
		 */
		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catrastr�fica"));
		
		// Verifica��o
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPC, tente novamente");
		
		// A��o
		service.alugarFilme(usuario, filmes);
				
		
	}
	
	@Test
	public void deveProrrogarUmaLocacao() {
	
		// Cen�rio
		Locacao locacao = umLocacao().agora();
		
		// A��o
		service.prorrogarLocacacao(locacao, 3);
				
		// Verifica��o
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		
		Mockito.verify(dao).salvar(argCapt.capture());
		
		Locacao locacaoRetornada = argCapt.getValue();
		
		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
	}
}
