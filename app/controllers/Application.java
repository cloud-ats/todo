package controllers;

import java.util.List;
import java.util.Map;

import models.Task;
import models.dao.ObjDAO;
import models.dao.TaskDAO;
import models.dto.TaskDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.MongoDBJDBC;
import util.TaskHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/**
 * 
 * @author TrinhTV3
 *  Class Application to receive request and handle
 */
public class Application extends Controller {
  public static Logger logger = LoggerFactory.getLogger("Application");
  /**
   * show home screen with list task in database
   * @return
   */
  public static Result index() {
    
    return ok(views.html.index1.render());
  }
  
  public static Result tasks(){
    
    List<Task> tasks =TaskHelper.getAllTask(); 
    //List<Task> tasks = new ObjDAO().getListAll(TaskDTO.class, "TodoList");
    ArrayNode array = Json.newObject().arrayNode();
    ObjectNode json = null;
    response().setContentType("text/json");
    System.out.println(tasks.size());
    for (Task task : tasks) {
      json = Json.newObject();
      json.put("id", task.getId());
      json.put("name", task.getName());
      array.add(json);
    }
    
    return ok(array);
    
  }
  
  /**
   * 
   * @return js file
   */
  public static Result getIndexJs() {
    return ok(views.js.index.render()).as("text/javascript");
  }
  
  /**
   * create new Task
   * @return json data
   *  
   */
  public static Result newTask() {
  
    int id = new TaskDAO().getMaxID("TodoList", "id", -1);
    System.out.println(id);
    Map<String, String[]> values = request().body().asFormUrlEncoded();
    String todoName = values.get("name")[0];
    Task task = new Task();
    task.setId(id + 1);
    task.setName(todoName);
    
    new ObjDAO().insertDocument("TodoList", task);
   // TaskHelper.create(task);
    
    return ok(Integer.toString(id + 1));  
  }
  
  /**
   * edit task
   * @return home screen
   */
  public static Result editTask(){ 
    Map<String, String[]> values = request().body().asFormUrlEncoded();
    String name = values.get("name")[0];
    String id = values.get("id")[0];
    
    TaskHelper.edit(Integer.parseInt(id),name);
    
    return ok();  
  }
  
  /**
   * delete all task selected
   * @return
   */
  public static Result deleteAll() {
    Map<String, String[]> values = request().body().asFormUrlEncoded();
    String[] id_all = values.get("id_all[]"); 
    if(id_all != null){
      
      TaskHelper.deleteAll(id_all);
    }
    
    else {
      String id = values.get("id")[0];
      TaskHelper.delete(Integer.parseInt(id));
    }
    
    return ok();
  }
  
  
}
