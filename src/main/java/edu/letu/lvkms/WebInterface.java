package edu.letu.lvkms;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.quirkygaming.propertylib.MutableProperty;

public class WebInterface {
	
	/*public static boolean canModifyAccessLevel(UserList ul, String currentUser, AccessLevel other) {
		return ul.getAccessLevel(currentUser).powerLevel() >= other.powerLevel();
	}*/

	private static int resolveID(HttpServletRequest request) {
		String IDString = request.getParameter("ID");
		
		int ID; // Cast to integer
		try {
			ID = (IDString == null ? -1 : Integer.parseUnsignedInt(IDString));
		} catch (NumberFormatException e) {
			ID = -1;
		}
		return ID;
	}
	
	public static String getCurrentUser(HttpSession session) {
		if (!sessionValid(session)) return null;
		return (String) session.getAttribute("user");
	}
	
	/**
	 * A valid session has a "user" attribute and must not be older than the configured amount of time.
	 * @param session
	 * @return
	 */
	public static boolean sessionValid(HttpSession session) {
		if (session != null && session.getAttribute("user") != null) {
			return (System.currentTimeMillis() - session.getCreationTime()) < DatabaseLifecycle.serverConf().access().sessionExpirationTime();
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	public static boolean hasTempPassword(HttpSession session) {
		boolean temp = DatabaseLifecycle.userList().access((ul) -> {
			return (Boolean) ul.hasTempPassword((String)session.getAttribute("user"));
		});
		return temp;
	}
	
	/**
	 * Verify that the current user exists and has the requested permissions (JSP, MutableProperty version)
	 * @param session
	 * @param error Set to a plaintext error message if an error occurs
	 * @param reqUserPerms
	 * @param reqPrintPerms
	 * @return
	 */
	public static boolean validateJSPSession(HttpSession session, MutableProperty<String> error, boolean reqUserPerms, boolean reqSystemPerms) {
		if (sessionValid(session)) {
			String user = getCurrentUser(session);
			DatabaseLifecycle.userList().access((ul) -> {
				if (ul.userExists(user)) {
					if (ul.hasSystemAccess(user) || !reqSystemPerms) {
						if (ul.hasUserAccess(user) || !reqUserPerms) {
							error.set(""); // All good
						} else {
							error.set("You do not have permission to edit users.");
						}
					} else {
						error.set("You do not have permission to edit system settings.");
					}
				} else {
					error.set("User " + user + " does not exist!");
				}
			});
			return error.equals(""); // True if no error
		} else {
			error.set("You are not logged in.");
			return false;
		}
	}
	
	public static boolean validateJSPSession(HttpSession session, MutableProperty<String> error, boolean reqUserPerms) {
		return validateJSPSession(session, error, reqUserPerms, false);
	}
	
	/**
	 * Verify that the current user exists and has the requested permissions (servlet output stream version)
	 * @param session
	 * @param out Used to output errors
	 * @param reqUserPerms
	 * @param reqPrintPerms
	 * @return
	 */
	public static boolean validateSession(HttpSession session, PrintWriter out, boolean reqUserPerms, boolean reqPrintPerms) {
		MutableProperty<String> error = MutableProperty.newProperty(null);
		boolean success = validateJSPSession(session, error, reqUserPerms, reqPrintPerms);
		if (!success) {
			printJsonMessage(out, error.get(), true);
		}
		return success;
	}
	
	private static void printJsonMessage(PrintWriter out, String message, boolean error) {
		out.println("{"
				+ "\"status\": \"" + (error ? "error" : "OK") + "\","
				+ "\"message\": \"" + message + "\""
				+ "}");
	}
}
