package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisNumeros() {
		//Cenário
		int a = 5;
		int b = 3;
		
		//Ação
		int resultado = calc.somar(a, b);
		
		//Verificação
		Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrairDoisNumeros() {
		//Cenário
		int a = 5;
		int b = 3;
		
		//Ação
		int resultado = calc.subtrair(a, b);
		
		//Verificação
		Assert.assertEquals(2, resultado);
	}
	
	@Test
	public void deveDividirDoisNumeros() throws NaoPodeDividirPorZeroException {
		//Cenário
		int a = 6;
		int b = 3;
		
		//Ação
		int resultado = calc.dividir(a, b);
		
		//Verificação
		Assert.assertEquals(2, resultado);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcessaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		//Cenário
		int a = 10;
		int b = 0;
		
		//Ação
		int resultado = calc.dividir(a, b);
		
		//Verificação
		Assert.assertEquals(2, resultado);
	}

}
