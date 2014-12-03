package models.task;
import models.generic.GenericDAO;

import org.bson.BasicBSONObject;

import play.Logger;
import util.MongoDBHelper;
import util.Util;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
public class TaskDAO extends GenericDAO{	
	
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
			Logger.info(Util.stackTraceToString(e));
		}finally{
			
		}
		return max;
		
	  }
}
