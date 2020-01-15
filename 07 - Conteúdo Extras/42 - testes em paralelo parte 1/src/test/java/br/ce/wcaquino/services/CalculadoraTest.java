package br.ce.wcaquino.services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.runners.ParaleloRunner;

@RunWith(ParaleloRunner.class)
public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
		System.out.println("Iniciando...");
	}
	
	@After
	public void tearDown() {
		System.out.println("Finalizando...");
	}

	@Test
	public void deveSomarDoisValores() {
		
		// Cen�rio
		int a = 5;
		int b = 3;
		
		calc = new Calculadora();
		
		//Verifica��o
		int resultado = calc.somar(a, b);
		
		Assert.assertEquals(8, resultado);
		
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		
		// Cen�rio
		int a = 8;
		int b = 5;
		
		calc = new Calculadora();
		
		//Verifica��o
		int resultado = calc.subtrair(a, b);
		
		Assert.assertEquals(3, resultado);
		
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		
		// Cen�rio
		int a = 8;
		int b = 4;
		
		// A��o
		int resultado = calc.dividir(a, b);
		
		//Verifica��o
		
		Assert.assertEquals(2, resultado);
		
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoDividirPorZero() throws NaoPodeDividirPorZeroException {
		
		// Cen�rio
		int a = 10;
		int b = 0;
		
		// A��o
		calc.dividir(a, b);
		
		//Verifica��o
		
	}

}
