package br.ce.wcaquino.services;

import static br.ce.wcaquino.buiders.FilmeBuilder.umFilme;
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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.utils.DataUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LocacaoService.class)
public class LocacaoServiceTestePowerMock {


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
		service = PowerMockito.spy(service);
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

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {


		// Cen�rio ->
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(11, 1, 2020));
		
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
	public void deveAlugarFilme_SemCalcularValor() throws Exception {
		
		// cen�rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);
		
		// a��o
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		// verifica��o
		assertThat(locacao.getValor(), is(1.0));
		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);
	}
	
	@Test
	public void deveCalcularValor() throws Exception {
		
		// cen�rio
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		// a��o
		Double valor = Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);
		
		// verifica��o
		assertThat(valor, is(4.0));
	}
	
	
}
