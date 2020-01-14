package br.ce.wcaquino.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.services.CalculadoraTest;
import br.ce.wcaquino.services.CalculoValorLocacaoTeste;
import br.ce.wcaquino.services.LocacaoServiceTeste;

//@RunWith(Suite.class)
@SuiteClasses({
	CalculadoraTest.class,
	CalculoValorLocacaoTeste.class,
	LocacaoServiceTeste.class
})
public class SuiteExecucao {
	//Remova se poder
}
