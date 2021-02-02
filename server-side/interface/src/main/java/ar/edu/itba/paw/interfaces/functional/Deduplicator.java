package ar.edu.itba.paw.interfaces.functional;

import java.util.Optional;

/**
	* <p>Expone un único método que determina si cierto objeto es único bajo el
	* dominio de cierta clave. La semántica que expresa la clave es de dominio
	* específico. Si la clave ya existe en la entidad de ID especificado, se
	* ignora su existencia.</p>
	*/

@FunctionalInterface
public interface Deduplicator {

	public boolean isDuplicated(final Optional<Long> id, final String key, final Object value);
	
}
