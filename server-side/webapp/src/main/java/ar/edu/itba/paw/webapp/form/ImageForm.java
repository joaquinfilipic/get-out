package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

import ar.edu.itba.paw.model.image.ContentType;
import ar.edu.itba.paw.model.image.Image;
import ar.edu.itba.paw.webapp.constraint.Weight;
import ar.edu.itba.paw.webapp.constraint.Enum;
import ar.edu.itba.paw.webapp.constraint.ValidImage;

public class ImageForm {
	
	@NotNull
	@Weight(1024 * 1024)
	@ValidImage
	private byte[] image;
	
	@NotNull
	@Enum(enumClass = ContentType.class)
	private String content;
	
	public Image getImage() {
		return new Image(image, ContentType.fromContentType(content));
	}
	
	public void setImage(final byte[] image) {
		this.image = image;
	}
	
	public void setContent(final String content) {
		this.content = content;
	}

}
