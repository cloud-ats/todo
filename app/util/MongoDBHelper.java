package com.fpt.su11.conn;
import play.Logger;

import com.fpt.su11.util.Utils;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

public  class MongoDBHelper { 
  
  /** .*/
  private static String host_name;
  
  /** .*/
  private static int port;
  
  /** .*/
  private static String db_name;
  
  /** .*/
  private static MongoClient mongoClient = null;
  
  /** .*/
  private static int maxConnection;
  
  static {
    try {
      loadProperties();
      mongoClient = new MongoClient(new ServerAddress(host_name,port),
          new MongoClientOptions.Builder().connectionsPerHost(maxConnection).build());

    } catch (Exception e) {
      Logger.info(Utils.stackTraceToString(e));
    } 
  }
  
  public static void loadProperties() {
    host_name = Utils.getProperty("mongo_ip", "localhost");
    port = Utils.getProperty("mongo_port", 27017);
    db_name = Utils.getProperty("mongo_name", "todoList");
    maxConnection = Utils.getProperty("mongo_connection", 20);
  }

  public static  DB getConnection() throws Exception {    

    if (mongoClient==null) {
      mongoClient = new MongoClient(new ServerAddress(host_name,port),
          new MongoClientOptions.Builder().connectionsPerHost(maxConnection).build());

    }
    DB conn=mongoClient.getDB(db_name);
    return conn;
  } 
  
  public static DB getConnection(String db_name) throws Exception {

    if (mongoClient==null) {
      mongoClient = new MongoClient( host_name , port );      
    }
    return mongoClient.getDB(db_name);
  }
  
  /**
   * To execute command in clustering mode
   * 
   * @param cmd
   */
  public static void execute(Command cmd) {
    try {
      open_Con(cmd.getConnection());
      cmd.execute();
    } catch (Exception e) {
      Logger.info(Utils.stackTraceToString(e));
    } finally {
      release(cmd.getConnection());
    }
  }
  
  /**
   * 
   * @param conn
   */
  public static void release(DB conn) {
    try {
      conn.requestDone();
    } catch (Exception e) {
      Logger.info(Utils.stackTraceToString(e));
    }    
  }
  
  /**
   * 
   * @param conn
   */
  public static void open_Con(DB conn) {
    try {
      conn.requestStart();
    } catch (Exception e) {
      Logger.info(Utils.stackTraceToString(e));
    }
  }

}