package br.ce.wcaquino.services;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTest {
	
	private static int contador = 0;

	@Test
	public void inicia() {
		contador = 1;
	}
	
	@Test
	public void verifica() {
		Assert.assertEquals(1, contador);
	}
	
	/**
	 * Uma opção de garantir a ordem dos testes
	 */
	
//	@Test
//	public void testGeral() {
//		inicia();
//		verifica();
//	}
}
