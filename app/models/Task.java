package models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * @author TrinhTV3
 *  Class Task to manipulate with database, define object todo
 */
public class Task extends BasicDBObject {
 
  private static final long serialVersionUID = 1L;
 
  public static Logger logger = LoggerFactory.getLogger("models.Task");


  public int getId() {
    return this.getInt("id");
  }

  public void setId(int id) {
    this.put("id",id);
  }

  public String getName() {
    return this.getString("name");
  }

  public void setName(String name) {
    this.put("name",name);
  }
  
  public Task from(DBObject source) {
    this.put("id", source.get("id"));
    this.put("name", source.get("name"));
    return this;
  }
}
