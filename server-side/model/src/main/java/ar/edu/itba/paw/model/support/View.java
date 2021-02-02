package ar.edu.itba.paw.model.support;

public interface View {

	/** Vista por defecto, sin ocultamiento. */
	public interface Public {}

	/** Vista interna completa. */
	public interface Private extends Public {}

	/** Vista de recuperación, simplificada. */
	public interface Retrieval extends Public {}

	/** Vista de perfiles públicos para matching */
	public interface Matching extends Public {}
}
