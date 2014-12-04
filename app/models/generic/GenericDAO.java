package models.generic;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import play.Logger;
import util.Command;
import util.MongoDBHelper;
import util.Util;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public abstract  class GenericDAO {
	public static void main(String[] args) {
		System.out.println("hello world");
		
	 
	}
	
	/**
	 * Create collection 
	 * @param collectionName	
	 * @return
	 */	
  public void createCollection(String collectionName) {
	    DB conn = null;
	    try {
	      conn = MongoDBHelper.getConnection();
	      MongoDBHelper.open_Con(conn);
	      DBCollection coll = conn.createCollection(collectionName, null);
	    } catch (Exception e) {
	      Logger.info(Util.stackTraceToString(e));
	      
	    } finally {
	      MongoDBHelper.release(conn);
	    }
	    
	  }
	/**
	 * Insert a document to collection
	 * @param collectionName
	 * @param doc
	 */
  public void insertDoc( String collectionName, BasicDBObject doc) {
		    DB conn=null;
		    try {
		    	conn=MongoDBHelper.getConnection();
		    	DBCollection coll = conn.getCollection(collectionName);
		        coll.insert(doc);
				
			} catch (Exception e) {
				// TODO: handle exception
				Logger.info(Util.stackTraceToString(e));
			}finally{
				MongoDBHelper.release(conn);
			}
		    
	  }
	
	/**
	 * Update a document to collection
	 * @param collectionName
	 * @param doc
	 */	
  public boolean updateDoc(String collectionName,BasicDBObject basicObj){
		DB conn=null;
		DBCursor cursor =null;
		boolean sucess=false;
		try{   
			conn = MongoDBHelper.getConnection();
			MongoDBHelper.open_Con(conn);			
			DBCollection coll = conn.getCollection(collectionName);     
			BasicDBObject whereQuery = new BasicDBObject();			
			BasicDBObject updateDocument = new BasicDBObject();					
			whereQuery.put("id",basicObj.get("id"));
			updateDocument.append("$set", basicObj);			
			cursor = coll.find(whereQuery);
			while(cursor.hasNext()) {			 
				DBObject cur=cursor.next();			 
				coll.update(cur, updateDocument);				
			}
			sucess=true;
		}catch(Exception e){
		// TODO: handle exception
		Logger.info(Util.stackTraceToString(e));
		}finally{
			cursor.close();
			MongoDBHelper.release(conn);
		}
		return sucess;
		
	}
  /**
	 * Update a document to collection
	 * @param collectionName
	 * @param doc
	 */	
	public boolean updateDoc(String collectionName,BasicDBObject basicObj,String fieldName){
			DB conn=null;
			DBCursor cursor =null;
			boolean sucess=false;
			try{   
				conn = MongoDBHelper.getConnection();
				MongoDBHelper.open_Con(conn);			
				DBCollection coll = conn.getCollection(collectionName);     
				BasicDBObject whereQuery = new BasicDBObject();			
				BasicDBObject updateDocument = new BasicDBObject();					
				whereQuery.put(fieldName,basicObj.get(fieldName));
				updateDocument.append("$set", basicObj);			
				cursor = coll.find(whereQuery);
				while(cursor.hasNext()) {			 
					DBObject cur=cursor.next();			 
					coll.update(cur, updateDocument);				
				}
				sucess=true;
			}catch(Exception e){
			// TODO: handle exception
			Logger.info(Util.stackTraceToString(e));
			}finally{
				cursor.close();
				MongoDBHelper.release(conn);
			}
			return sucess;
			
		}
	
	
	/**
	 * Delete document 
	 * @param collectionName
	 * @param basicObj
	 * @return
	 */
	public boolean deleteDoc(String collectionName,BasicDBObject basicObj){
		DB conn=null;
		DBCursor cursor =null;
		boolean sucess=false;
		try{   
			conn = MongoDBHelper.getConnection();
			MongoDBHelper.open_Con(conn);			
			DBCollection coll = conn.getCollection(collectionName);     
			BasicDBObject whereQuery = new BasicDBObject();	
			System.out.println(basicObj.get("id"));
			whereQuery.append("id",basicObj.get("id"));				
			cursor = coll.find(whereQuery);
			while(cursor.hasNext()) {			 
				DBObject cur=cursor.next();			 
				coll.remove(cur);			
			}
			sucess=true;
		}catch(Exception e){
		// TODO: handle exception
		Logger.info(Util.stackTraceToString(e));
		}finally{
			cursor.close();
			MongoDBHelper.release(conn);
		}
		return sucess;
		
	}
	/**
	 * Delete document by field name
	 * @param collectionName
	 * @param basicObj
	 * @return
	 */
	public boolean deleteDoc(String collectionName,BasicDBObject basicObj,String fieldName){
		DB conn=null;
		DBCursor cursor =null;
		boolean sucess=false;
		try{   
			conn = MongoDBHelper.getConnection();
			MongoDBHelper.open_Con(conn);			
			DBCollection coll = conn.getCollection(collectionName);     
			BasicDBObject whereQuery = new BasicDBObject();							
			whereQuery.put(fieldName,basicObj.get(fieldName));				
			cursor = coll.find(whereQuery);
			while(cursor.hasNext()) {			 
				DBObject cur=cursor.next();			 
				coll.remove(cur);			
			}
			sucess=true;
		}catch(Exception e){
		// TODO: handle exception
		Logger.info(Util.stackTraceToString(e));
		}finally{
			cursor.close();
			MongoDBHelper.release(conn);
		}
		return sucess;
		
	}

	/**
	 * Get list document
	 * @param collectionName
	 * @param 
	 * @return List<BasicDBObject>listDoc
	 * 
	 */   
	
  public List<BasicDBObject> getList(String collectionName){	
		DB conn = null;
		DBCursor cursor = null;
		List<BasicDBObject> listDoc = new ArrayList<BasicDBObject>();
		BasicDBObject doc = null;
		try {
			conn = MongoDBHelper.getConnection();
			MongoDBHelper.open_Con(conn);
			DBCollection coll = conn.getCollection(collectionName);
			cursor = coll.find();
			while (cursor.hasNext()) {
				doc = (BasicDBObject) cursor.next();
				listDoc.add(doc);
			}
		} catch (Exception e) {
			Logger.info(Util.stackTraceToString(e));
		} finally {
			cursor.close();
			MongoDBHelper.release(conn);
		}
		return listDoc;
	
	  }
	     
	 /**
	  * Get list document have pagging param	
	  * @param collectionName
	  * @param pageSize
	  * @param skip
	  * @return
	  */
  public List<BasicDBObject> getList(String collectionName,int pageSize,int skip){

		DB conn = null;
		DBCursor cursor = null;
		List<BasicDBObject> listDoc = new ArrayList<BasicDBObject>();
		BasicDBObject doc = null;	
		try {
			conn = MongoDBHelper.getConnection();
			MongoDBHelper.open_Con(conn);
			DBCollection coll = conn.getCollection(collectionName);
			cursor = coll.find().limit(pageSize).skip(skip);
			while (cursor.hasNext()) {
				doc = (BasicDBObject) cursor.next();
				listDoc.add(doc);
			}
		} catch (Exception e) {
			Logger.info(Util.stackTraceToString(e));
		} finally {
			cursor.close();
			MongoDBHelper.release(conn);
		}
		return listDoc;
			
	  }
  /**
	 * Get list document
	 * @param collectionName
	 * @param 
	 * @return List<BasicDBObject>listDoc
	 * 
	 */   
	
  public <T extends GenericDTO>List<T> getListGeneric(String collectionName,Class<T> clazz){	
		DB conn = null;
		DBCursor cursor = null;
		List<T> listDoc = new ArrayList<T>();
		T task = null;
		try {
			conn = MongoDBHelper.getConnection();
			MongoDBHelper.open_Con(conn);
			DBCollection coll = conn.getCollection(collectionName);
			cursor = coll.find();
			while (cursor.hasNext()) {	
				task=clazz.newInstance();
				task.from(cursor.next());
				listDoc.add(task);				
			}
		} catch (Exception e) {
			Logger.info(Util.stackTraceToString(e));
		} finally {
			cursor.close();
			MongoDBHelper.release(conn);
		}
		return listDoc;
	
  }	 
  public <T extends GenericDTO>List<T> getListGeneric(String collectionName,Class<T> clazz,int pagSize,int skip){	
		DB conn = null;
		DBCursor cursor = null;
		List<T> listDoc = new ArrayList<T>();
		T task = null;
		try {
			conn = MongoDBHelper.getConnection();
			MongoDBHelper.open_Con(conn);
			DBCollection coll = conn.getCollection(collectionName);
			cursor = coll.find().limit(pagSize).skip(skip);
			while (cursor.hasNext()) {	
				task=clazz.newInstance();
				task.from(cursor.next());
				listDoc.add(task);				
			}
		} catch (Exception e) {
			Logger.info(Util.stackTraceToString(e));
		} finally {
			cursor.close();
			MongoDBHelper.release(conn);
		}
		return listDoc;
	
  }		
  
  
  /**
   * REGION ADD CODE
   */
  
  
  public <T> boolean updateDocument(String collectionName,T obj) throws Exception{
		DB conn=null;
		DBCursor cursor =null;
		boolean haveIdField=false;
		try{   
			conn = MongoDBHelper.getConnection();
			MongoDBHelper.open_Con(conn);			
			DBCollection coll = conn.getCollection(collectionName);     
			BasicDBObject whereQuery = new BasicDBObject();
			BasicDBObject newDocument = new BasicDBObject();
			BasicDBObject updateDocument = new BasicDBObject();			
			Method methods[]=obj.getClass().getMethods();			
			for(int i=0;i<methods.length;i++){			
				
				if(!methods[i].getName().startsWith("get")){
					continue;
				}
				if(methods[i].getName().contains("getClass")) {
					continue;
				}
				/*
				 *  set value quey object
				*/
				
				if(methods[i].getName()=="getID"){
					whereQuery.put("id", methods[i].invoke(obj, null));
					haveIdField=true;
				}else{
					String fieldName = methods[i].getName().replaceFirst("get", "").toLowerCase();
	 				Object valueField= methods[i].invoke(obj, null);	 				
					updateDocument.append(fieldName, valueField);
				}
			}
			if(!haveIdField){
				throw new Exception("obj have no id field");
			}
			newDocument.append("$set", updateDocument);			
			cursor = coll.find(whereQuery);
			while(cursor.hasNext()) {			 
				DBObject cur=cursor.next();			 
				coll.update(cur, newDocument);				
			}
		}catch(Exception e){
			throw e;
		}finally{
			cursor.close();
			MongoDBHelper.release(conn);
		}
		return haveIdField;
	}	
	
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
      Logger.info(Util.stackTraceToString(e));
    }finally{
    	cursor.close();
    	MongoDBHelper.release(conn);
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
      Logger.info(Util.stackTraceToString(e));
    } finally {
      cursor.close();
      MongoDBHelper.release(conn);
    }
    return listTask;

  }
 
  /**
   * Clustering enable
   * @param collectionName
   * @param doc
   */
  public void insertDocument(final String collectionName, final BasicDBObject doc) {
    
    try {
      final DB conn = MongoDBHelper.getConnection();  
      Command cmd = new Command() {
        
        @Override
        public DB getConnection() {
          return conn;
        }
        @Override
        public void execute() {
          DBCollection coll = conn.getCollection(collectionName);
          coll.insert(doc);
        }
      };
      
      MongoDBHelper.execute(cmd);
      
    } catch (Exception e) {
      Logger.info(Util.stackTraceToString(e));
    }
  }
   
  public <T> void insertDocument(String collectionName, T obj) {
    try {
      BasicDBObject doc = transformDataToDBOjb(obj);
      insertDoc(collectionName, doc);
    } catch (Exception e) {
      // TODO: handle exception
      Logger.info(Util.stackTraceToString(e));
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
      Logger.info(Util.stackTraceToString(e));
    } finally {

    }
    return doc;
  }

}
