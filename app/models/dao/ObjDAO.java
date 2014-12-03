package models.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import play.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.fpt.su11.conn.Command;
import com.fpt.su11.conn.MongoDBHelper;
import com.fpt.su11.util.Utils;

public class ObjDAO {

  public <T> List<T> getListAll(Class<T> clazz, String collectionName) {
    DB conn = null;
    DBCursor cursor = null;
    List<T> listTask = new ArrayList<T>();
    T task = null;
    Method methods[] = clazz.getMethods();
    try {
      conn = MongoDBHelper.getConnection();
      MongoDBHelper.open_Con(conn);
      DBCollection coll = conn.getCollection(collectionName);
      cursor = coll.find();
      while (cursor.hasNext()) {
        BasicDBObject cur = (BasicDBObject) cursor.next();
        task = clazz.newInstance();
        for (int i = 0; i < methods.length; i++) {
          if (!methods[i].getName().startsWith("set")) {
            continue;
          }
          String fieldName = methods[i].getName().replaceFirst("set", "")
              .toLowerCase();
          Class[] parameterTypes = methods[i].getParameterTypes();
          Object valueInDB = parameterTypes[0].cast(cur.get(fieldName));
          methods[i].invoke(task, valueInDB);
        }

        listTask.add(task);
      }
    } catch (Exception e) {
      Logger.info(Utils.stackTraceToString(e));
    }
    return listTask;

  }

  public <T> List<T> getListAll(Class<T> clazz, String collectionName,
      int pageSize, int skip) {
    DB conn = null;
    DBCursor cursor = null;
    List<T> listTask = new ArrayList<T>();
    T task = null;
    Method methods[] = clazz.getMethods();
    try {
      conn = MongoDBHelper.getConnection();
      MongoDBHelper.open_Con(conn);
      DBCollection coll = conn.getCollection(collectionName);
      cursor = coll.find().skip(skip).limit(pageSize);
      while (cursor.hasNext()) {
        BasicDBObject cur = (BasicDBObject) cursor.next();
        task = clazz.newInstance();
        for (int i = 0; i < methods.length; i++) {
          if (!methods[i].getName().startsWith("set")) {
            continue;
          }
          String fieldName = methods[i].getName().replaceFirst("set", "").toLowerCase();
          Class[] parameterTypes = methods[i].getParameterTypes();
          Object valueInDB = parameterTypes[0].cast(cur.get(fieldName));
          methods[i].invoke(task, valueInDB);
        }

        listTask.add(task);
      }
    } catch (Exception e) {
      Logger.info(Utils.stackTraceToString(e));
    } finally {
      cursor.close();
      MongoDBHelper.release(conn);
    }
    return listTask;

  }

  public void createCollection(String collectionName) {
    DB conn = null;
    try {
      conn = MongoDBHelper.getConnection();
      MongoDBHelper.open_Con(conn);
      DBCollection coll = conn.createCollection(collectionName, null);
    } catch (Exception e) {
      Logger.info(Utils.stackTraceToString(e));
    } finally {
      MongoDBHelper.release(conn);
    }
  }

  /**
   * Clustering enable
   * @param collectionName
   * @param doc
   */
//  public void insertDocument(String collectionName, BasicDBObject doc) {
//    
//    try {
//      final DB conn = MongoDBHelper.getConnection();  
//      Command cmd = new Command() {
//        
//        @Override
//        public DB getConnection() {
//          return conn;
//        }
//        @Override
//        public void execute() {
//          DBCollection coll = conn.getCollection(collectionName);
//          coll.insert(doc);
//        }
//      };
//      
//      MongoDBHelper.execute(cmd);
//      
//    } catch (Exception e) {
//      Logger.info(Utils.stackTraceToString(e));
//    }
//  }

  public <T> void insertDocument(String collectionName, T obj) {
    try {
      BasicDBObject doc = transformDataToDBOjb(obj);
      insertDocument(collectionName, doc);
    } catch (Exception e) {
      // TODO: handle exception
      Logger.info(Utils.stackTraceToString(e));
    } finally {

    }

  }

  private <T> BasicDBObject transformDataToDBOjb(T obj) {
    BasicDBObject doc = null;
    try {
      doc = new BasicDBObject();
      Method methods[] = obj.getClass().getMethods();
      for (int i = 0; i < methods.length; i++) {
        if (!methods[i].getName().startsWith("get")) {
          continue;
        }
        if (methods[i].getName().contains("getClass")) {
          continue;
        }
        String fieldName = methods[i].getName().replaceFirst("get", "")
            .toLowerCase();
        //Object valueField = methods[i].invoke(obj, null);
       // doc.append(fieldName, valueField);
      }

    } catch (Exception e) {
      // TODO: handle exception
      Logger.info(Utils.stackTraceToString(e));
    } finally {

    }
    return doc;
  }

}
