package br.ce.wcaquino.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class CalculadoraMockTest {
	
	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
		
		Mockito.when(calc.somar(argCapt.capture(),argCapt.capture())).thenReturn(5);
		
		
		assertEquals(5,calc.somar(1, 10000));
		
		System.out.println(argCapt.getValue());
		System.out.println(argCapt.getAllValues());
	}

}
