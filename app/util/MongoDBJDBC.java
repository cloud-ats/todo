package util;

import org.bson.BasicBSONObject;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * 
 * @author TrinhTV3
 *  Class MongoDBJDBC to connect database
 */
public class MongoDBJDBC {
  protected static final Logger LOGGER = (Logger)LoggerFactory.getLogger("MongoDBJDBC"); 
  
  static MongoClient client;
  /**
   * 
   * @return a connection
   */
  public DB getDB() {
    try {
      LOGGER.info("create new connection");
      
      client = new MongoClient("localhost", 27017);

      // connect
      DB db = client.getDB("todoList");
      
      return db;
    } catch (Exception e)
    {
      return null;
    }
    finally{
   //  client.close();
    }

  }
  
  public void closeConnection(){
    LOGGER.info("close connection");
    client.close();
  }
 
}
