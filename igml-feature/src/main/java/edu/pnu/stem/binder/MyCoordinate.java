package edu.pnu.stem.binder;

import java.math.BigDecimal;

public class MyCoordinate {
	private BigDecimal x = null;
	private BigDecimal y = null;
	private BigDecimal z = null;
			
	public MyCoordinate() {
		this(0, 0, 0);
	}

	public MyCoordinate(double x, double y, double z) {
		this.x = BigDecimal.valueOf(x);
		this.y = BigDecimal.valueOf(y);
		this.z = BigDecimal.valueOf(z);
	}

	public BigDecimal getX() {
		return x;
	}
	public BigDecimal getY() {
		return x;
	}
	public BigDecimal getZ() {
		return x;
	}

}
