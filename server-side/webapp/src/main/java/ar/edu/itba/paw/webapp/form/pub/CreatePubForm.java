package ar.edu.itba.paw.webapp.form.pub;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import ar.edu.itba.paw.webapp.form.ImageForm;

import javax.validation.Valid;

public class CreatePubForm {
	
	@Valid
	@JsonUnwrapped
	protected PubForm pubForm;
	
	@Valid
	@JsonUnwrapped
	protected ImageForm imageForm;
	
	public CreatePubForm() {
		pubForm = new PubForm();
		imageForm = new ImageForm();
	}

	public PubForm getPubForm() {
		return pubForm;
	}

	public void setPubForm(final PubForm pubForm) {
		this.pubForm = pubForm;
	}

	public ImageForm getImageForm() {
		return imageForm;
	}

	public void setImageForm(final ImageForm imageForm) {
		this.imageForm = imageForm;
	}

}
