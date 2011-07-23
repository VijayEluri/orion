/**
 * 
 */
package com.k99k.app.orion;

import java.net.UnknownHostException;
import java.util.Date;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBPort;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

/**
 * MongoDB 连接
 * @author keel
 *
 */
public class MongoCol {

	private String ip = "202.102.40.43";
	private int port = 27017;
	private String dbName = "fwall";
	private String user = "sikewall009";
	private String pwd = "6667441";
	private static Mongo mongo;
	private DB db;
	private int maxWaitTime;
	private int connectionsPerHost;
	private int threadsAllowedToBlockForConnectionMultiplier;
	
	
	
	/**
	 * 默认参数配置
	 */
	public MongoCol() {
		super();
	}

	/**
	 * @param ip
	 * @param port
	 * @param dbName
	 * @param user
	 * @param pwd
	 * @param maxWaitTime
	 * @param connectionsPerHost
	 * @param threadsAllowedToBlockForConnectionMultiplier
	 */
	public MongoCol(String ip, int port, String dbName, String user, String pwd,int maxWaitTime,int connectionsPerHost,int threadsAllowedToBlockForConnectionMultiplier) {
		super();
		this.ip = ip;
		this.port = port;
		this.dbName = dbName;
		this.user = user;
		this.pwd = pwd;
		this.connectionsPerHost = connectionsPerHost;
		this.maxWaitTime = maxWaitTime;
		this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
	}

	/**
	 * 获取一个数据库连接
	 * @param colName
	 * @return DBCollection
	 */
	public DBCollection getColl(String colName){
		try {
			//this.db = this.getDB();
//			boolean auth = db.authenticate(this.user, this.pwd.toCharArray());
//			if (auth) {
//				DBCollection coll = db.getCollection(colName);
//				return coll;
//			}
			return this.getDB().getCollection(colName);
		} catch (Exception e) {
			System.out.println("------"+new Date());e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取DB
	 * @return DB
	 */
	public DB getDB(){
		
		try {
			if (this.db != null && this.db.getName().equals(this.dbName)) {
				return this.db;
			}
			boolean auth = false;
			if (mongo == null) {
				//System.setProperty("MONGO.POOLSIZE", "100");
				ServerAddress sadd = new ServerAddress(this.ip, this.port);
				MongoOptions opt = new MongoOptions();
				opt.autoConnectRetry = false;
				opt.connectionsPerHost = this.connectionsPerHost;
				opt.threadsAllowedToBlockForConnectionMultiplier = this.threadsAllowedToBlockForConnectionMultiplier;
				opt.maxWaitTime = this.maxWaitTime;
				mongo = new Mongo(sadd,opt);
				this.db = mongo.getDB(this.dbName);
				auth = db.authenticate(this.user, this.pwd.toCharArray());
				System.out.println("===========new mongo built!!============");
				System.out.println(this.ip+" "+ this.dbName +" "+new Date());
			}
			
			if (auth) {
				return db;
			}else{
				System.out.println("======DB auth failed!!=======");
			}
			return null;
		} catch (UnknownHostException e) {
			System.out.println("------"+new Date());e.printStackTrace();
			if (mongo != null) {
				mongo.close();
				mongo = null;
			}
			
		} catch (MongoException e) {
			
			System.out.println("------"+new Date());e.printStackTrace();
			if (mongo != null) {
				mongo.close();
				mongo = null;
			}
		}
		return null;
	}
	
	public void close(){
		if (mongo != null) {
			this.db = null;
			mongo.close();
			mongo = null;
			System.out.println("MongoCol closed!");
		}
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

//	public static void main(String[] args) {
//		MongoCol m = new MongoCol();
//		m.getDB();
//	}
//	
}
