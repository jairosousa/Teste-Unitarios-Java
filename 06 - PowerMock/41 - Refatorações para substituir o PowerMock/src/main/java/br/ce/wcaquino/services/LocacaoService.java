package br.ce.wcaquino.services;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {

	private LocacaoDAO dao;

	private SPCService spcServioce;

	private EmailService emailService;


	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {

		/**
		 * Usuario e Filme s�o obrigat�rios
		 */
		if (usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}

		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}

		/**
		 * N�o deve alugar filme sem estoque Se tiver sem estoque um exe��o ser� lan�ada
		 */

		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
		}
		
		boolean negativado;

		try {
			negativado = spcServioce.possuiNegativacao(usuario);
			
		} catch (Exception e) {
			throw new LocadoraException("Problemas com SPC, tente novamente");
		}
		
		if (negativado) {
			throw new LocadoraException("Usuario negativado");
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(obterData());

		locacao.setValor(calcularValorLocacao(filmes));

		// Entrega no dia seguinte
		Date dataEntrega = obterData();
		dataEntrega = adicionarDias(dataEntrega, 1);

		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}

		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		dao.salvar(locacao);

		return locacao;
	}

	protected Date obterData() {
		return new Date();
	}

	private Double calcularValorLocacao(List<Filme> filmes) {
		Double valorToral = 0d;
		for (int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			switch (i) {
			case 2:
				valorFilme = valorFilme * 0.75;
				break;
			case 3:
				valorFilme = valorFilme * 0.5;
				break;
			case 4:
				valorFilme = valorFilme * 0.25;
				break;
			case 5:
				valorFilme = valorFilme * 0;
				break;
			}

			valorToral += valorFilme;
		}
		return valorToral;
	}

	public void notificarAtrasos() {
		List<Locacao> locacoes = dao.obterLocacoesPendentes();
		for (Locacao locacao : locacoes) {
			if (locacao.getDataRetorno().before(obterData())) {
				emailService.notificarAtrasos(locacao.getUsuario());

			}
		}

	}
	
	public void prorrogarLocacacao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setDataLocacao(obterData());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		novaLocacao.setValor(locacao.getValor() * dias);
		dao.salvar(novaLocacao);
	}

}