package edu.pnu.stem.feature.imdf;

public class Manifest {

	String version;
	String created;
	String generated_by;
	String language;
	String extensions;

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getCreated() {
		return created;
	}

	public void setGeneratedBy(String generated_by) {
		this.generated_by = generated_by;
	}

	public String getGeneratedBy() {
		return generated_by;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public void setExtensions(String extensions) {
		this.extensions = extensions;
	}

	public String getExtension() {
		return extensions;
	}

}
