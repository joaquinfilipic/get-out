package ar.edu.itba.paw.interfaces.functional;

	/**
	* <p>Esta interfaz permite proporcionar un método para sincronizar el
	* estado de un entidad modificada con el estado almacenado en el contexto
	* de persistencia. De esta forma los cambios se reflejan inmediatamente en
	* los modelos.</p>
	*/

@FunctionalInterface
public interface Mergeable<T> {

	public T update(final T client);
}
