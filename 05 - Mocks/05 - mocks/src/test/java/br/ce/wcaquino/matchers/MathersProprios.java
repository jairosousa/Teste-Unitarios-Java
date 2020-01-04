package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MathersProprios {

	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.SUNDAY);
	}
	
	public static DataDiferencaDiasMatcher ehHojeComDiferencaDias(Integer qtsDias) {
		return new DataDiferencaDiasMatcher(qtsDias);
	}
	public static DataDiferencaDiasMatcher ehHoje() {
		return new DataDiferencaDiasMatcher(0);
	}

}
