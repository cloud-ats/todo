package controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import models.Task;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import util.MongoDBJDBC;

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
   
    return ok(views.html.index.render(Task.all()));
  }
  
  public static Result getIndexJs() {
    return ok(views.js.index.render()).as("text/javascript");
  }
  
  /**
   * create new Task
   * @return json data
   *  
   */
  public static Result newTask() {
    MongoDBJDBC getConnect = new MongoDBJDBC();
    logger.info("start debug");
    int id = getConnect.getMaxID();
    System.out.println(id);
    Map<String, String[]> values = request().body().asFormUrlEncoded();
    String todoName = values.get("name")[0];
    Task task = new Task();
    task.setId(id + 1);
    task.setName(todoName);
    Task.create(task);
    
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
    
    Task.edit(Integer.parseInt(id),name);
    
    return redirect(routes.Application.index());  
  }
  
  /**
   * delete all task selected
   * @return
   */
  public static Result deleteAll() {
    Map<String, String[]> values = request().body().asFormUrlEncoded();
    String[] id_all = values.get("id_all[]"); 
    if(id_all != null){
      
      Task.deleteAll(id_all);
    }
    
    else {
      String id = values.get("id")[0];
      Task.delete(Integer.parseInt(id));
    }
//    else if(id != null){
 //     
 //   }
    
    
    return redirect(routes.Application.index());
  }
  
  
}
