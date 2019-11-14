package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {

	@Test
	public void teste() {
		
		/**
		 * Recebe como parametro um boleano
		 */
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		/**
		 *  Checa se um valor é igual 
		 *  ao outro, porém trata cada tipo de uma 
		 *  forma diferente
		 *  
		 *  Ideal para trabalhar inteiro, long,  char, short, boolean
		 *  
		 *  Se comporta diferente com Float e Double devido as casas decimais
		 *  
		 *  Delta -> é uma margem de erro de comparação
		 *  
		 */
		Assert.assertEquals(1, 1);
		Assert.assertEquals(0.51236, 0.51, 0.01); // esta depreciado ele pede que coloque um Delta de comparação
		Assert.assertEquals(Math.PI, 3.14, 0.01); // esta depreciado ele pede que coloque um Delta de comparação
		
		/**
		 * Pode declarar uma Strinh caso haja erro de no teste
		 */
		Assert.assertEquals("Erro de comparação",1, 1);
		
		/**
		 * Tratamento diferenciado para tipos primitivos
		 */
		int i = 5;
		Integer i2 = 5;
//		Assert.assertEquals(i, i2); --> Não aceita devido tipo primitivo 
		Assert.assertEquals(Integer.valueOf(i), i2);
		Assert.assertEquals(i, i2.intValue());
		
		
		/**
		 * Strings
		 */
		Assert.assertEquals("bola", "bola");
//		Assert.assertEquals("bola", "Bola");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		Assert.assertNotEquals("bola", "casa");
		
		/**
		 * Objetos
		 * São verificados através do metodo equals
		 */
		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 1");
		Usuario u3 = u2;
		Usuario u4 = null;
		
		Assert.assertEquals(u1, u2); //Tem implementar o metodo equals
		
		/**
		 *  Verifica se os objetos são da mesma instancia
		 */
//		Assert.assertSame(u1, u2); Falha pois tem insancia diferentes
		Assert.assertNotSame(u1, u2); 
		Assert.assertSame(u2, u2);
		Assert.assertSame(u2, u3);
		
		Assert.assertTrue(u4 == null);
		Assert.assertNull(u4);
		Assert.assertNotNull(u2);
		
	}

}
