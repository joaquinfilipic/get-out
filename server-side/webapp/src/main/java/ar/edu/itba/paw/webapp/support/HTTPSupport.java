package ar.edu.itba.paw.webapp.support;

import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.error.GetOutError;
import ar.edu.itba.paw.model.support.GetOutException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

	/**
	* <p>Provee utilidades para manipular requests y responses HTTP.</p>
	*/

public final class HTTPSupport {

	private static final ObjectMapper mapper = new ObjectMapper();

	private HTTPSupport() {
		throw new IllegalStateException("Can't instance this.");
	}

	/**
	* <p>Extrae el token dentro del request, si existe, o una cadena vacía de
	* otro modo. El token debe ser provisto dentro del heaeder <i>Authorization
	* </i>.</p>
	*
	* @param request
	*	Un request HTTP válido.
	* @return
	*	El token, o una cadena vacía si no había ninguno, o si el header estaba
	*	malformado.
	*/
	public static String getBearerToken(final HttpServletRequest request) {
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null) return "";
		if (!header.startsWith("Bearer ")) return "";
		return header.substring(7, header.length());
	}

	/**
	* <p>Permite enviar un mensaje de error hacia el cliente, grabando el
	* buffer del response directamente. Esto es necesario durante la generación
	* de errores fuera de un controlador, ya que no hay otra forma de atrapar
	* el flujo dentro del servlet.</p>
	*
	* @param exception
	*	La excepción a enviar.
	* @param response
	*	El response HTTP que recibirá el cliente.
	* @throws IOException
	*	Si se produce un error al escribir en el response.
	*/
	public static void sendException(final GetOutException exception, final HttpServletResponse response)
			throws IOException {
		final GetOutError error = new GetOutError(exception);
		final byte [] body = mapper.writeValueAsBytes(error);
		response.setStatus(exception.getStatus().getStatusCode());
		response.setContentType(GetOutMediaType.APPLICATION_GETOUT_v1_JSON);
		response.setCharacterEncoding("UTF-8");
		response.getOutputStream().write(body);
		response.getOutputStream().flush();
	}

	/**
	* <p>Método conveniente para enviar excepciones al igual que
	* {@link #sendException(GetOutException, HttpServletResponse)}, pero
	* indicando directamente el tipo de error, en lugar de construir la
	* excepción.</p>
	*
	* @param type
	*	El tipo de error a enviar.
	* @param response
	*	El response HTTP que recibirá el cliente.
	* @throws IOException
	*	Si se produce un error al escribir en el response.
	*/
	public static void sendException(final ErrorType type, final HttpServletResponse response)
			throws IOException {
		sendException(GetOutException.of(type), response);
	}
}
