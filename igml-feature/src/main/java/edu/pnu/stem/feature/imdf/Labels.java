package edu.pnu.stem.feature.imdf;

import net.opengis.indoorgml.imdf.indoorgmlimdf.v_1_0.LANGUAGETAG;

public class Labels {
	LANGUAGETAG language;
	String name;

	public void setLanguage(LANGUAGETAG language) {
		this.language = language;
	}

	public LANGUAGETAG getLanguage() {
		return language;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
