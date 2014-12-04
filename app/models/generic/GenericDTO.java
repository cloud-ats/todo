package models.generic;
import java.lang.reflect.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


public abstract class GenericDTO extends BasicDBObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GenericDTO() {

	}

	public void from(DBObject doc) throws IllegalArgumentException, IllegalAccessException  {
		Field fields[] = this.getClass().getFields();
		for (int j = 0; j < fields.length; j++) {
			if (fields[j].isAnnotationPresent(MapField.class)) {	
				//set value to field
				fields[j].set(this,doc.get(fields[j].getName()) );
				//put value to json object
				this.put(fields[j].getName(), doc.get(fields[j].getName()));
			}
		}

	}

}
