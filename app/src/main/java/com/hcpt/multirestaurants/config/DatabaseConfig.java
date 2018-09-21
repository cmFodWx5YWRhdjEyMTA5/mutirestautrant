package com.hcpt.multirestaurants.config;

import com.hcpt.multirestaurants.PacketUtility;

public final class DatabaseConfig {
	private final int DB_VERSION = 3;
	private final String DB_NAME = "DBService.sqlite";

	public static String TABLE_SERVICE = "tbService";
	public static String TABLE_YEAR = "tbYear";

	public static String KEY_NAME = "name";
	public static String KEY_DAY = "day";
	public static String KEY_MONTH = "month";
	public static String KEY_YEAR = "year";
	public static String KEY_MESSAGE = "message";
	public static String KEY_PRAYER_POINT = "prayer_point";
	public static String KEY_PROPHECY = "prophecy";
	public static String KEY_OPENING = "opening_session";
	public static String KEY_ALTAR = "altar_call";
	private static DatabaseConfig instance = null;
	
	public static DatabaseConfig getInstance() {
		if (instance == null) {
			instance = new DatabaseConfig();
		}
		return instance;
	}

	/**
	 * Get database version
	 * 
	 * @return
	 */
	public int getDatabaseVersion() {
		return DB_VERSION;
	}

	/**
	 * Get database name
	 * 
	 * @return
	 */
	public String getDatabaseName() {
		return DB_NAME;
	}

	/**
	 * Get database path
	 * 
	 * @return
	 */
	public String getDatabasepath()
	{
		PacketUtility packetUtility=new PacketUtility();
		return "data/data/"+packetUtility.getPacketName()+"/databases/";
		
	}

	/**
	 * Get database path
	 * 
	 * @return
	 */
	public String getDatabaseFullPath() {
		return getDatabasepath() + DB_NAME;
	}
}
