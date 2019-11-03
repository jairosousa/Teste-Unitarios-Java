package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.bulders.FilmeBuilder.umFilme;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.dao.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTeste {

	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private LocacaoDAO dao;
	
	@Mock
	private SPCService spc;

	@Parameter(value = 0)
	public List<Filme> filmes;

	@Parameter(value = 1)
	public Double valorLocacao;

	@Parameter(value = 2)
	public String cenario;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	private static Filme filme1 = umFilme().agora();
	private static Filme filme2 = umFilme().agora();
	private static Filme filme3 = umFilme().agora();
	private static Filme filme4 = umFilme().agora();
	private static Filme filme5 = umFilme().agora();
	private static Filme filme6 = umFilme().agora();
	private static Filme filme7 = umFilme().agora();

	@Parameters(name= "{2}")
	public static Collection<Object[]> getParemetros() {
		return Arrays.asList(new Object[][] { 
				{ Arrays.asList(filme1, filme2), 8.0, "2 Filmes: Sem Desconto" },
				{ Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes: 25%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 Filmes: 50%" }, 
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 Filmes: 75%" }, 
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "5 Filmes: 100%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 Filmes: Sem Desconto" }
		});
	}

	@Test
	public void deveCalcularValorLocacaoConsiderandoDesconto() throws FilmesSemEstoqueException, LocadoraException {

		// cenário
		Usuario usuario = new Usuario("Usuario 1");

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		//Verificação
		assertThat(resultado.getValor(), is(valorLocacao));
		
	}

}
