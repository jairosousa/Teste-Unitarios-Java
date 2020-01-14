package br.ce.wcaquino.services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CalculadoraMockTest {

	@Mock
	private Calculadora calckMock;

	@Spy
	private Calculadora calcSpy;
	
//	@Spy
	private EmailService email; 

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void deveMostrarDiferencaEntrewMockSpy() {

		/**
		 * Quando o Mock n�o sabe o que fazer ele retorna o valor padr�o 0;
		 */
//		System.out.println(calckMock.somar(1, 2));

		/**
		 * Quando gravo uma espectativa
		 */
		Mockito.when(calckMock.somar(1, 2)).thenReturn(8);
//		System.out.println("MOCK: " + calckMock.somar(1, 2));
		
		/**
		 * Para que o Mock utilize o metodo real podemos fazer 
		 * desse modo
		 */
		Mockito.when(calckMock.somar(1, 2)).thenCallRealMethod();
//		System.out.println("MOCK REAL: " + calckMock.somar(1, 2));

		/**
		 * Comportamento do Spy
		 */
		Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);
//		System.out.println("SPY: " + calcSpy.somar(1, 2));
		
		/**
		 * Caso em que o spy n�o vai saber o que fazer
		 */
//		System.out.println("SPY: " + calcSpy.somar(5, 5)); 
		/**
		 * O spy como n�o sabe o que fazer ele faz a execu��o REAL do m�todo
		 * por isso n�o se pode usa-lo em inteface, n�o se pode "espiar uma inteface"
		 */

//		System.out.println("Mock");
//		calckMock.imprime();
//		System.out.println("Spy");
//		calcSpy.imprime();
		/**
		 * O Padr�o do Mock � N�O executar o metodo.
		 * O Padr�o do Spy � executar o metodo
		 */
		
		/**
		 * Para mudar o padr�o do Spy
		 */
		Mockito.doNothing().when(calcSpy).imprime();
//		System.out.println("No Spy");
//		calcSpy.imprime();
		
		/**
		 * Mudar o comportamento 
		 */
		Mockito.doReturn(5).when(calcSpy).somar(1, 2); //retorne 5 quando somar 1 e 2
		
	}

	@Test
	public void teste() {

		Calculadora calc = Mockito.mock(Calculadora.class);

		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);

		Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);

		assertEquals(5, calc.somar(1, 10000));

//		System.out.println(argCapt.getValue());
//		System.out.println(argCapt.getAllValues());
	}

}
