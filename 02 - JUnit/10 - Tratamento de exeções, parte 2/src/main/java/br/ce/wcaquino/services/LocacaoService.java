package br.ce.wcaquino.services;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Date;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) throws FilmeSemEstoqueException, LocadoraException {
		
		
		/**
		 * Usuario e Filme s�o obrigat�rios
		 */
		if (usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}
		
		if (filme == null) {
			throw new LocadoraException("Filme vazio");
		}
		
		/**
		 * N�o deve alugar filme sem estoque
		 * Se tiver sem estoque um exe��o ser� 
		 * lan�ada
		 */
		if (filme.getEstoque() == 0) {
			throw new FilmeSemEstoqueException();
		}
		
		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}
	
	
}