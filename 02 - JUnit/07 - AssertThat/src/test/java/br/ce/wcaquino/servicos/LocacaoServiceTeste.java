package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {
	
	@Test
	public void teste() {

		// Cenário -> onde as variavéis são inicializadas
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		// Ação -> onde invocamos o metodo que vamos testar
		Locacao locacao = service.alugarFilme(usuario, filme);

		// Validação -> onde vamos coletar o resiltado da ação com o cenário
		// especificado
		// e avaliar se o resultado esta de acordo com resultado esperado
		// Assert.assertThat(valor retornado, Math);
		/**
		 *  assertThat -> Verifique que:
		 * O assertThat utiliza Math pacote hamcrest
		 */
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(5.0));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	
		/**
		 * Import static -> ctrl + shift + m
		 */
		assertThat(locacao.getValor(), is(5.0));// valor da locação é 5.0
		assertThat(locacao.getValor(), is(equalTo(5.0)));// valor da locação é igual a 5.0
		assertThat(locacao.getValor(), is(not(6.0)));// valor da locação não é 6.0
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true)); // A data locação é igual a de hoje
		assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true)); // A data de retorno é igua amanhã
		
	}
}
