package ar.edu.itba.paw.model.image;

public class Image {

	private final byte[] data;
	private final ContentType contentType;

	public Image(final byte[] data, final ContentType contentType) {
		this.data = data;
		this.contentType = contentType;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public ContentType getContentType() {
		return contentType;
	}

}
