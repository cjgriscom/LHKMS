package edu.letu.lvkms;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import edu.letu.lvkms.db.ServerConf;
import edu.letu.lvkms.db.UserList;

/**
 * Application Lifecycle Listener implementation class DatabaseLifecycle
 *
 */
@WebListener
public class DatabaseLifecycle implements ServletContextListener {

	private static UserList userList;
	private static ServerConf serverConf;
	
	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		userList.close();
		serverConf.close();
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		userList = new UserList();
		serverConf = new ServerConf();
	}
	
	public static Accessor<UserList> userList() {
		return userList.accessor();
	}
	
	public static Accessor<ServerConf> serverConf() {
		return serverConf.accessor();
	}
}
