package edu.pnu.stem.feature.imdf;

public class Address {

	FeatureInformation featureInformation;
	String address;
	String unit;
	String locality;
	String povince;
	String country;
	String postal_code;
	String postal_code_ext;
	String postal_code_vanity;

	public void setFeatureInformation(FeatureInformation featureInformation) {
		this.featureInformation = featureInformation;

	}

	public FeatureInformation getFeatureInformation() {
		return featureInformation;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getLocality() {
		return locality;
	}

	public void setProvince(String povince) {
		this.povince = povince;
	}

	public String getProvince() {
		return povince;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setPostalCode(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getPostalCode() {
		return postal_code;
	}

	public void setPostalCodeExt(String postal_code_ext) {
		this.postal_code_ext = postal_code_ext;
	}

	public String getPostalCodeExt() {
		return postal_code_ext;
	}

	public void setPostalCodeVanity(String postal_code_vanity) {
		this.postal_code_vanity = postal_code_vanity;
	}

	public String getPostalCodeVanity() {
		return postal_code_vanity;
	}

}
