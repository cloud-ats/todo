package models.dao;
import org.bson.BasicBSONObject;

import play.Logger;
import models.dto.TaskDTO;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.fpt.su11.conn.MongoDBHelper;
import com.fpt.su11.util.Utils;
public class TaskDAO {	
	public boolean updateDocument(String collectionName,TaskDTO task){
		DB conn=null;
		DBCursor cursor =null;
		try{   
			conn = MongoDBHelper.getConnection();
			MongoDBHelper.open_Con(conn);			
			DBCollection coll = conn.getCollection(collectionName);     
			BasicDBObject whereQuery = new BasicDBObject();
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append("name",task.getName()));			
			whereQuery.put("id", task.getId());
			cursor = coll.find(whereQuery);
			while(cursor.hasNext()) {
			 /* Logger.info(cursor.next());*/			 
				DBObject updateDocument = cursor.next();			 
				coll.update(updateDocument, newDocument);				
			}
		}catch(Exception e){
			Logger.info(Utils.stackTraceToString(e));
		}finally{
			cursor.close();
		}
		
		return false;
	}
	public boolean deleteDocument(String collectionName,TaskDTO task){
		DB conn=null;
		DBCursor cursor =null;
		try{   
			conn = MongoDBHelper.getConnection();
			MongoDBHelper.open_Con(conn);			
			DBCollection coll = conn.getCollection(collectionName);     
			BasicDBObject whereQuery = new BasicDBObject();						
			whereQuery.put("id", task.getId());
			cursor = coll.find(whereQuery);
			while(cursor.hasNext()) {
			 /* Logger.info(cursor.next());*/			 
				DBObject deleteDocument = cursor.next();
				coll.remove(deleteDocument);								
			}
		}catch(Exception e){
			Logger.info(Utils.stackTraceToString(e));
		}finally{
			cursor.close();
		}
		
		return false;
	}
	/**
	 * 
	 * @param collectionName
	 * @param fieldName field order
	 * @param typeSort 1 is asc, -1 is desc
	 * @return
	 */
	public int getMaxID(String collectionName,String fieldName,int typeSort) {
		DB conn=null;
		DBCursor cursor =null;
		int max=-1;
		try{   
			conn = MongoDBHelper.getConnection();
			MongoDBHelper.open_Con(conn);			
			DBCollection coll = conn.getCollection(collectionName);
			DBObject sort = new BasicDBObject();
		    sort.put(fieldName, typeSort);
			cursor=coll.find().sort(sort).limit(1);
			while (cursor.hasNext()) {
			      max = ((BasicBSONObject) cursor.next()).getInt(fieldName);
			}
		}catch(Exception e){
			max=-1;
			Logger.info(Utils.stackTraceToString(e));
		}finally{
			
		}
		return max;
		
	  }
}
