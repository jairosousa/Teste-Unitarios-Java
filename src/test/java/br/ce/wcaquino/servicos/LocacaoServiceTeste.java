package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Test
	public void testeLocacao() {
		// Cen�rio
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		// a��o
		Locacao locacao = service.alugarFilme(usuario, filme);
		
		// Verifica��o
		error.checkThat(locacao.getValor(), is(equalTo(6.0)));
		error.checkThat(locacao.getValor(), is(not(6.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), 
				DataUtils.obterDataComDiferencaDias(1)), is(false));

	}
}
