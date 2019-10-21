package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.bulders.FilmeBuilder.umFilme;
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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {

	private LocacaoService service;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private SPCService spc;

	private LocacaoDAO dao;

	@Before
	public void setup() {
		service = new LocacaoService();
		dao = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDao(dao);
		spc = Mockito.mock(SPCService.class);
		service.setSpcService(spc);
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// Cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

		// ação
		Locacao locacao;

		locacao = service.alugarFilme(usuario, filmes);

		// Verificação
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

		// Cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

		// ação
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
		// Cenário
		List<Filme> filmes = Arrays.asList(umFilme().agora());

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
	 * @throws FilmesSemEstoqueException
	 * @throws LocadoraException
	 */
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmesSemEstoqueException, LocadoraException {
		// Cenário
		Usuario usuario = umUsuario().agora();;

		// ação
		Locacao locacao;

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		locacao = service.alugarFilme(usuario, null);

		System.out.println("Forma Nova");
	}
	
	@Test
	public void devePagar75PctoNoFilme3() throws FilmesSemEstoqueException, LocadoraException {
		
		// cenário
		Usuario usuario = umUsuario().agora();;
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0));
		
		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// Verificação (4 + 4 + 3(o terceiro tem desconto 25%) = 11
		assertThat(resultado.getValor(), is(11.0));
	}
	
	@Test
	public void devePagar50PctoNoFilme4() throws FilmesSemEstoqueException, LocadoraException {
		
		// cenário
		Usuario usuario = umUsuario().agora();;
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0)
				,new Filme("Filme 4", 2, 4.0));
		
		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// Verificação (4 + 4 + 3(o terceiro tem desconto 25%) + 2 = 13
		assertThat(resultado.getValor(), is(13.0));
	}
	
	@Test
	public void devePagar75PctoNoFilme5() throws FilmesSemEstoqueException, LocadoraException {
		
		// cenário
		Usuario usuario = umUsuario().agora();;
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0)
				,new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0));
		
		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// Verificação (4 + 4 + 3(o terceiro tem desconto 25%) + 2  + 1 = 14
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void devePagar0PctoNoFilme6() throws FilmesSemEstoqueException, LocadoraException {
		
		// cenário
		Usuario usuario = umUsuario().agora();;
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0)
				,new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0), new Filme("Filme 6", 2, 4.0));
		
		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// Verificação (4 + 4 + 3(o terceiro tem desconto 25%) + 2  + 1 + 0 = 14
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmesSemEstoqueException, LocadoraException {

		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// cenário
		Usuario usuario = umUsuario().agora();;
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		// Ação
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		// Verificação 
//		assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//		assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiNmaSegunda());
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmesSemEstoqueException, LocadoraException {
		//cenário
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Usuario Negativado");
		
		//Ação
		service.alugarFilme(usuario, filmes);
	}
}
