package models.dto;

public class TaskDTO {
	private Integer id;
	private String name;
	public TaskDTO() {
		// TODO Auto-generated constructor stub
	}
	public TaskDTO(int id,String name){
		this.setId(id);
		this.setName(name);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
