package models.generic;
import java.util.ArrayList;
import java.util.List;

import play.Logger;
import util.MongoDBHelper;
import util.Util;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * 
 * @author TuanHQ
 *
 */
public abstract  class GenericDAO {
	
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
      Logger.info(Util.stackTraceToString(e));
    } finally {
      MongoDBHelper.release(conn);
    }
  }
	
	/**
	 * Update a document to collection by id
	 * @param collectionName
	 * @param doc
	 */	
  public boolean updateDoc(String collectionName,BasicDBObject basicObj){
   return updateDoc(collectionName, basicObj, "id");
  }
  
  /**
	 * Update a document to collection by field name
	 * @param collectionName
	 * @param doc
	 * @param fieldName
	 */	
	public boolean updateDoc(String collectionName,BasicDBObject basicObj,String fieldName){
	  DB conn=null;
	  DBCursor cursor =null;
	  
	  try {   
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
	  } catch(Exception e) {
	    Logger.info(Util.stackTraceToString(e));
	    return false;
	  } finally {
	    cursor.close();
	    MongoDBHelper.release(conn);
	  }
	  
	  //
	  return true;
	}
	
	
	/**
	 * Delete document by Id
	 * @param collectionName
	 * @param basicObj
	 * @return
	 */
	public boolean deleteDoc(String collectionName,BasicDBObject basicObj) {
		return deleteDoc(collectionName, basicObj, "id");
	}
	
	/**
	 * Delete document by field name
	 * @param collectionName
	 * @param basicObj
	 * @param fieldName
	 * @return
	 */
	public boolean deleteDoc(String collectionName,BasicDBObject basicObj,String fieldName) {
		DB conn=null;
		DBCursor cursor =null;
		
		try {   
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
		}catch(Exception e){
		Logger.info(Util.stackTraceToString(e));
		return false;
		}finally{
			cursor.close();
			MongoDBHelper.release(conn);
		}
		
		//
		return true;
	}

	/**
	 * Get all list document
	 * @param collectionName
	 * @param 
	 * @return List<BasicDBObject>listDoc
	 * 
	 */   
	public List<BasicDBObject> getList(String collectionName) {	
	  return getList(collectionName, -1, -1);
	}
	     
	 /**
	  * Get list document have pagination size	
	  * @param collectionName
	  * @param pageSize
	  * @param skip
	  * @return
	  */
	public List<BasicDBObject> getList(String collectionName,int pageSize, int skip) {
	  DB conn = null;
	  DBCursor cursor = null;
	  List<BasicDBObject> listDoc = new ArrayList<BasicDBObject>();
	  BasicDBObject doc = null;	

	  try {
	    conn = MongoDBHelper.getConnection();
	    MongoDBHelper.open_Con(conn);
	    DBCollection coll = conn.getCollection(collectionName);

	    cursor = coll.find();
	    if (pageSize != -1) cursor.limit(pageSize);
	    if(skip != -1) cursor.skip(skip);
	    
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

	  //
	  return listDoc;
	}
	
  /**
	 * Get list document
	 * @param collectionName
	 * @param 
	 * @return List<BasicDBObject>listDoc
	 * 
	 */   
  public <T extends GenericDTO>List<T> getListGeneric(String collectionName,Class<T> clazz) {	
		return getListGeneric(collectionName, clazz, -1, -1);
  }
  
  /**
   * 
   * @param collectionName
   * @param clazz
   * @param pageSize
   * @param skip
   * @return
   */
  public <T extends GenericDTO>List<T> getListGeneric(String collectionName,Class<T> clazz, int pageSize, int skip) {	
		DB conn = null;
		DBCursor cursor = null;
		List<T> listDoc = new ArrayList<T>();
		T task = null;
		
		try {
			conn = MongoDBHelper.getConnection();
			MongoDBHelper.open_Con(conn);
			DBCollection coll = conn.getCollection(collectionName);
		
			cursor = coll.find();
			if (pageSize != -1) cursor.limit(pageSize);
			if (skip != -1) cursor.skip(skip);
			
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
		
		//
		return listDoc;
  }		
}
