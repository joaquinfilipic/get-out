package ar.edu.itba.paw.interfaces.config;

import org.springframework.core.io.Resource;

public interface PersistenceProperties {

	public Resource [] getScripts();

	public String getDBMS();
	public String getHost();
	public String getPort();
	public String getDatabase();
	public String getUsername();
	public String getPassword();

	public String getHibernateAuto();
	public String getDialect();
	public boolean showSQL();
	public boolean formatSQL();

	public default String getDatabaseURL() {
		return "jdbc:" + getDBMS() + "://" + getHost() + ":" + getPort() + "/" + getDatabase();
	}
}
