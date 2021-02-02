package ar.edu.itba.paw.interfaces.functional;

import java.util.Optional;

	/**
	* <p>El método expuesto permite extender un perfil agregando propiedades
	* adicionales al modelo original. Esto permite destrabar nueva funcionalidad de
	* forma incremental y modular. Además, garantiza que las entidades accedidas
	* bajo el <i>id</i> especificado se instalen en el contexto de persistencia.</p>
	*/

@FunctionalInterface
public interface Aggregator<T> {

	public Optional<T> aggregate(final Long id);
}
