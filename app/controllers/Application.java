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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.BasicDBObject;

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
    
    return ok(views.html.index1.render());
  }
  
  public static Result tasks(){
       
    List<BasicDBObject> listDoc =new TaskDAO().getList("TodoList");
    //List<Task> tasks = new ObjDAO().getListAll(TaskDTO.class, "TodoList");
    ArrayNode array = Json.newObject().arrayNode();
    ObjectNode json = null;
    response().setContentType("text/json");
  
    for (BasicDBObject doc : listDoc) {
      Task task=new Task();
      task.from(doc);
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
    play.Logger.info(String.valueOf(id));
    Map<String, String[]> values = request().body().asFormUrlEncoded();
    String todoName = values.get("name")[0];
    Task task = new Task();
    task.setId(id + 1);
    task.setName(todoName);
    
    new TaskDAO().insertDoc("TodoList", task);
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
    BasicDBObject doc=new BasicDBObject().append("id", Integer.valueOf(id)).append("name", name);
    new TaskDAO().updateDoc("TodoList", doc);
       
    return ok();  
  }
  
  /**
   * delete all task selected
   * @return
   */
  public static Result deleteAll() {
    Map<String, String[]> values = request().body().asFormUrlEncoded();
    String[] id_all = values.get("id_all[]");
    TaskDAO taskDao=new TaskDAO(); 
    if(id_all != null){
      
//      TaskHelper.deleteAll(id_all);
    	
    	for (int i=0;i<id_all.length;i++){
    		BasicDBObject doc=new BasicDBObject().append("id", Integer.valueOf(id_all[i]));
    		taskDao.deleteDoc("TodoList", doc);
       	}
    }
    
    else {
      String id = values.get("id")[0];   
      BasicDBObject doc=new BasicDBObject().append("id", Integer.valueOf(id));
      taskDao.deleteDoc("TodoList", doc);
    }
    
    return ok();
  }
  
  
}
