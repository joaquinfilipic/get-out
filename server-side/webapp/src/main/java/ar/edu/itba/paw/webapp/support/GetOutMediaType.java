package ar.edu.itba.paw.webapp.support;

import javax.ws.rs.core.MediaType;

	/**
	* <p>An abstraction for GetOut© media types. Instances are immutable.</p>
	*/

public class GetOutMediaType extends MediaType {

	/**
	* <p>A {@code String} constant representing
	* {@value #APPLICATION_GETOUT_v1_JSON} media type.</p>
	*/
	public static final String APPLICATION_GETOUT_v1_JSON
		= "application/vnd.getout.v1+json";

	/**
	* <p>A {@link MediaType} constant representing
	* {@value #APPLICATION_GETOUT_v1_JSON} media type.</p>
	*/
	public static final MediaType APPLICATION_GETOUT_v1_JSON_TYPE
		= new MediaType("application", "vnd.getout.v1+json");
}
