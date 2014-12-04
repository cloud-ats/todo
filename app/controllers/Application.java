package controllers;

import java.util.List;
import java.util.Map;

import models.task.Task;
import models.task.TaskDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.MongoDBHelper;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * 
 * @author TrinhTV3
 *  Class Application to receive request and handle
 */
public class Application extends Controller {  
  /**
   * show home screen with list task in database
   * @return
   */
  public static Result index() {
    
    //
    return ok(views.html.index.render());
  }
  
  /**
   * get all task in database 
   * @return a json array to client
   */
  public static Result tasks() {
       
    List<Task>listDoc=new TaskDAO().getListGeneric("TodoList", Task.class);
    ArrayNode array = Json.newObject().arrayNode();
    ObjectNode json = null;
    response().setContentType("text/json");
  
    for (Task doc : listDoc) {
      json = Json.newObject();
      json.put("id", doc.id);
      json.put("name",doc.name);
      array.add(json);
    }
    
    //
    return ok(array);
  }
  
  /**
   * 
   * @return js file
   */
  public static Result getIndexJs() {
    
    //
    return ok(views.js.index.render()).as("text/javascript");
  }
  
  /**
   * create new Task
   * @return json data
   *  
   */
  public static Result newTask() {
  
    int id = new TaskDAO().getMaxID("TodoList", "id", -1);
    play.Logger.info(String.valueOf(id));
    
    Map<String, String[]> values = request().body().asFormUrlEncoded();
    String todoName = values.get("name")[0];
    
    BasicDBObject doc=new BasicDBObject().append("id", id+1).append("name", todoName);
    new TaskDAO().insertDoc("TodoList", doc);

    //
    return ok(Integer.toString(id + 1));  
  }
  
  /**
   * edit task
   * @return home screen
   */
  public static Result editTask() { 
    
    Map<String, String[]> values = request().body().asFormUrlEncoded();
    String name = values.get("name")[0];
    String id = values.get("id")[0];
    
    BasicDBObject doc=new BasicDBObject().append("id", Integer.valueOf(id)).append("name", name);
    new TaskDAO().updateDoc("TodoList", doc);
       
    //
    return ok();  
  }
  
  /**
   * delete task selected, this method handle all request relate deletion
   * @return true or false to client
   * @throws Exception 
   */
  public static Result delete() throws Exception {
    
    Map<String, String[]> values = request().body().asFormUrlEncoded();
    
    String check1 = (values.get("check")[0]);
      
    TaskDAO taskDao=new TaskDAO(); 
    if(check1 != null && !"true".equals(check1) && !"false".equals(check1)){
      System.out.println(check1);
      BasicDBObject doc=new BasicDBObject().append("id", Integer.parseInt(check1));
      taskDao.deleteDoc("TodoList", doc);
    }
    
    else{
      if ("true".equals(check1)) { // check if user click check all button
        DB db = MongoDBHelper. getConnection("test");
        DBCollection col = db.getCollection("TodoList");
        col.drop();
      }
      else {
          String[] id_all = values.get("id_all[]");
          for (int i=0;i<id_all.length;i++) {
          BasicDBObject doc=new BasicDBObject().append("id", Integer.valueOf(id_all[i]));
          taskDao.deleteDoc("TodoList", doc);
          }
        
      }
     
    }
    
    //
    return ok(String.valueOf(check1));
  }
  
  
}
