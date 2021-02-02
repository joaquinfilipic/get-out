package ar.edu.itba.paw.webapp.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ar.edu.itba.paw.model.image.ContentType;

@JsonInclude(Include.NON_NULL)
public class PubDto {
	
	private Long id;
	private String name;
	private String description;
	private String openTime;
	private byte[] image;
	private ContentType content;
	private Long attendance;
	
	public PubDto() {}
	
	public PubDto id(final Long id) {
		this.id = id;
		return this;
	}
	
	public PubDto name(final String name) {
		this.name = name;
		return this;
	}
	
	public PubDto description(final String description) {
		this.description = description;
		return this;
	}
	
	public PubDto openTime(final String openTime) {
		this.openTime = openTime;
		return this;
	}
	
	public PubDto image(final byte[] image) {
		this.image = image;
		return this;
	}
	
	public PubDto content(final ContentType content) {
		this.content = content;
		return this;
	}
	
	public PubDto attendance(final Long attendance) {
		this.attendance = attendance;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public ContentType getContent() {
		return content;
	}

	public void setContent(ContentType content) {
		this.content = content;
	}

	public Long getAttendance() {
		return attendance;
	}

	public void setAttendance(Long attendance) {
		this.attendance = attendance;
	}

}
