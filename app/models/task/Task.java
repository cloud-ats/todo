package models.task;

import com.mongodb.BasicDBObject;

public class Task extends BasicDBObject {
	private static final long serialVersionUID = 1L;

	public int getId() {
		return this.getInt("id");
	}

	public void setId(int id) {
		this.put("id", id);
	}

	public String getName() {
		return this.getString("name");
	}

	public void setName(String name) {
		this.put("name", name);
	}

	public Task from(BasicDBObject source) {
		this.put("id", source.get("id"));
		this.put("name", source.get("name"));
		return this;
	}
}
