package edu.pnu.stem.binder;


import java.math.BigDecimal;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdapterForDouble
    extends XmlAdapter<BigDecimal, Double>
{

	@Override
	public Double unmarshal(BigDecimal v) throws Exception {
		System.out.println(v.toString());
		
		return new Double(v.toString());
	}

	@Override
	public BigDecimal marshal(Double v) throws Exception {
		System.out.println(v.toString());		
		BigDecimal bigdecimal = new BigDecimal(v);
		System.out.println(bigdecimal);
		
		return new BigDecimal(v);
	}

}
