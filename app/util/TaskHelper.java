package util;

import java.util.ArrayList;
import java.util.List;

import org.bson.BasicBSONObject;

import models.Task;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class TaskHelper {
  public static List<Task> listTodo;
  public static MongoDBJDBC getConnect;
  private static int max;

  /**
   * 
   * @return all document from collection TodoList
   */
  public static List<Task> getAllTask() {
    listTodo = new ArrayList<Task>();
    
    getConnect = new MongoDBJDBC();
    DBCollection todoCollection = getConnect.getDB().getCollection("TodoList");
    DBCursor cursor = todoCollection.find();
    
    Task task;
    while (cursor.hasNext()) {
      task = new Task();
      BasicDBObject obj = (BasicDBObject) cursor.next();
      
      task.from(obj);
      listTodo.add(task);
    }
    getConnect.closeConnection();
    return listTodo;
  }
  
  public static int size() {
    List<Task> listTodoList = TaskHelper.getAllTask();
    return listTodoList.size();
  }
  
  /**
   * 
   * @param task to insert into database
   */
  public static void create(Task task) {
    getConnect = new MongoDBJDBC();
    DBCollection todoCollection = getConnect.getDB().getCollection("TodoList");
    
    BasicDBObject document = new BasicDBObject();
   
    document.put("id", task.getId());
    document.put("name", task.getName());
    
    todoCollection.insert(document);
    
    getConnect.closeConnection();
  }
  
  /**
   * 
   * @param id to delete todo into database with id
   */
  public static void delete(Integer id) {
    getConnect = new MongoDBJDBC();
    DBCollection todoCollection = getConnect.getDB().getCollection("TodoList");
    
    DBObject query = new BasicDBObject();
    ((BasicDBObject) query).append("id", id);
    
    todoCollection.remove(query);
    
    getConnect.closeConnection();
  }

  /**
   * 
   * @param id is to search document
   * @param name is value to update
   */
  public static void edit(Integer id, String name) {
    getConnect = new MongoDBJDBC();
    DBCollection todoCollection = getConnect.getDB().getCollection("TodoList");
    
    BasicDBObject searchUpdate = new BasicDBObject();
    searchUpdate.append("id", id);
    BasicDBObject objectUpdate = new BasicDBObject();
    objectUpdate.append("$set", new BasicDBObject("name", name));
    
    todoCollection.update(searchUpdate, objectUpdate);
    
    getConnect.closeConnection();
  }

   /**
    * 
    * @param id is array that store list id that user choose to delete
    */
  public static void deleteAll(String[] id) {
    getConnect = new MongoDBJDBC();
    DBCollection todoCollection = getConnect.getDB().getCollection("TodoList");
    
    int size = id.length;
    
    BasicDBObject object;
    for (int i = 0; i < size; i++) {
      object = new BasicDBObject();
      object.append("id", Integer.parseInt(id[i]));
      
      todoCollection.remove(object);
    }
    
    getConnect.closeConnection();
  }
  /**
   * 
   * @return max id of data in collection
   */
  public static int getMaxID() {
    getConnect = new MongoDBJDBC();
    DBCollection todoTable = getConnect.getDB().getCollection("TodoList");
    DBObject sort = new BasicDBObject();
    sort.put("id", -1);
    DBCursor cursor = todoTable.find().sort(sort).limit(1);
    while (cursor.hasNext()) {
      max = ((BasicBSONObject) cursor.next()).getInt("id");
    }
    
    return max;

  }
}
