package edu.letu.lvkms.db;

import java.util.Set;

import org.mapdb.BTreeMap;
import org.mapdb.Serializer;

import edu.letu.lvkms.Database;
import edu.letu.lvkms.Util;

public class UserList extends Database<UserList> {

	public UserList() {
		super("userList");
	}
	public static enum AccessLevel {
		
		Default(false, false, false, 10), 
		Printer_Manager(true, false, false, 20), 
		Administrator(true, true, false, 30), 
		System(true, true, true, 100);
		
		private boolean printerAccess, userAccess, systemAccess;
		private int power;
		
		private AccessLevel(boolean printerAccess, boolean userAccess, boolean systemAccess, int power) {
			this.printerAccess = printerAccess;
			this.userAccess = userAccess;
			this.systemAccess = systemAccess;
			this.power = power;
		}
		
		public String toString() {
			return super.toString().replace('_', ' ');
		}
		
		public boolean hasPrinterAccess() {return printerAccess;}
		public boolean hasUserAccess() {return userAccess;}
		public boolean hasSystemAccess() {return systemAccess;}
		
		public int powerLevel() {return power;}
	}
	
	private BTreeMap<String, String> tempPasswords = db
			.treeMap("tempPasswords",Serializer.STRING, Serializer.STRING)
			.createOrOpen();
	private BTreeMap<String, String> passwordHashes = db
			.treeMap("passwordHashes",Serializer.STRING, Serializer.STRING)
			.createOrOpen();
	private BTreeMap<String, String> accessLevels = db
			.treeMap("accessLevels",Serializer.STRING, Serializer.STRING)
			.createOrOpen();
	
	/**
	 * 
	 * @return the number of users in this list
	 */
	public int size() {
		return accessLevels.size();
	}
	
	/**
	 * Add a new user with default access policies
	 * @param username A unique username
	 * @param password Plaintext password (will be hashed)
	 */
	public void addUser(String username, char[] password) {
		passwordHashes.put(username, Util.hashPassword(password));
		accessLevels.put(username, AccessLevel.Default.name());
	}
	
	/**
	 * Add a new user with default access policies and a temporary password (to be set by user later)
	 * @param username A unique username
	 * @param password Plaintext temporary password
	 */
	public void addUserTemp(String username) {
		tempPasswords.put(username, Util.generateTempPassword());
		accessLevels.put(username, AccessLevel.Default.name());
	}
	
	/**
	 * Add a new user with administrator access policies
	 * @param username A unique username
	 * @param password Plaintext password (will be hashed)
	 */
	public void addSysadmin(String username, char[] password) {
		passwordHashes.put(username, Util.hashPassword(password));
		accessLevels.put(username, AccessLevel.System.name());
	}
	
	/**
	 * Completely remove a user from this list
	 * @param username
	 */
	public void removeUser(String username) {
		tempPasswords.remove(username);
		passwordHashes.remove(username);
		accessLevels.remove(username);
	}
	
	/**
	 * Erase this list
	 */
	public void clear() {
		tempPasswords.clear();
		passwordHashes.clear();
		accessLevels.clear();
	}
	
	/**
	 * 
	 * @return A set of usernames
	 */
	@SuppressWarnings("unchecked")
	public Set<String> getUsernames() {
		return accessLevels.keySet();
	}
	
	/**
	 * Use to set the user's password
	 * @param username
	 * @param password A plaintext password
	 */
	public void setHashedPassword(String username, char[] password) {
		passwordHashes.put(username, Util.hashPassword(password));
		tempPasswords.remove(username);
	}
	
	/**
	 * Reset a user back to a temporary password
	 * @param username
	 */
	public void resetPassword(String username) {
		passwordHashes.remove(username);
		tempPasswords.put(username, Util.generateTempPassword());
	}
	
	/**
	 * Use to change the user's password
	 * @param username
	 * @param password A plaintext password
	 */
	public void setAccessPolicies(String username, AccessLevel accessLevel) {
		this.accessLevels.put(username, accessLevel.name());
	}
	
	/**
	 * 
	 * @param username
	 * @return Whether the user exists or not
	 */
	public boolean userExists(String username) {
		return accessLevels.containsKey(username);
	}
	
	/**
	 * Checks if the given password is correct for a specific user
	 * @param username
	 * @param password The plaintext password
	 * @return True if the password matches
	 */
	public boolean passwordMatches(String username, char[] password) {
		if (tempPasswords.containsKey(username)) {
			return new String(password).equals(tempPasswords.get(username));
		} else {
			return Util.verifyPassword(password, passwordHashes.get(username));
		}
	}
	
	/**
	 * Check if a user has not set their password yet
	 * @param username
	 * @return True if the temp password is still in place
	 */
	public boolean hasTempPassword(String username) {
		return tempPasswords.containsKey(username);
	}
	
	/**
	 * Return the user's temporary password
	 * @param username
	 * @return
	 */
	public String getTempPassword(String username) {
		return tempPasswords.get(username);
	}
	
	/**
	 * 
	 * @param username
	 * @return Whether or not the user can modify user policies
	 */
	public boolean hasUserAccess(String username) {
		return AccessLevel.valueOf(accessLevels.get(username)).hasUserAccess();
	}
	
	/**
	 * 
	 * @param username
	 * @return Whether or not the user can modify system policies
	 */
	public boolean hasSystemAccess(String username) {
		return AccessLevel.valueOf(accessLevels.get(username)).hasSystemAccess();
	}
	
	/**
	 * 
	 * @param username
	 * @return The user's access level
	 */
	public AccessLevel getAccessLevel(String username) {
		return AccessLevel.valueOf(accessLevels.get(username));
	}
}
