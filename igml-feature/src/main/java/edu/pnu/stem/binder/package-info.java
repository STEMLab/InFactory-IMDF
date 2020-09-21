/**
 * 
 */
/**
 * @author jungh
 *
 */

@XmlJavaTypeAdapters
({
    @XmlJavaTypeAdapter(value=edu.pnu.stem.binder.AdapterForDouble.class,type=java.lang.Double.class),
    @XmlJavaTypeAdapter(value=edu.pnu.stem.binder.AdapterForDouble.class,type=java.math.BigDecimal.class),
	
	
})



package edu.pnu.stem.binder;
import edu.pnu.stem.binder.AdapterForDouble;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;