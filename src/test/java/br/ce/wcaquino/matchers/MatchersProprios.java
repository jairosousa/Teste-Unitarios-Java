package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MatchersProprios {

	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}

	public static DiaSemanaMatcher caiNaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}
	
	public static DataDiferencaDiaMatcher ehHojeComDiferencasDeDias(Integer qtdDias) {
		return new DataDiferencaDiaMatcher(qtdDias);
	}
	
	public static DataDiferencaDiaMatcher ehHoje() {
		return new DataDiferencaDiaMatcher(0);
	}
}
