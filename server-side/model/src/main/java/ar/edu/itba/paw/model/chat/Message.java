package ar.edu.itba.paw.model.chat;

public class Message {

	protected String message;

	Message() {
	}

	public Message(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof Message) {
			final Message message = (Message) object;
			return this.message.equals(message.getMessage());
		}
		else return false;
	}
}
