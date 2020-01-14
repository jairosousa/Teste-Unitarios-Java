package br.ce.wcaquino.services;

import br.ce.wcaquino.entidades.Usuario;

public interface EmailService {

	public void notificarAtrasos(Usuario usuario);
}
