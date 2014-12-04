package models.task;

import models.generic.GenericDTO;
import models.generic.MapField;

/**
 * 
 * @author TrinhTV3
 *
 */
public class Task extends GenericDTO {
	
  /** .*/
  @MapField
	public int id;
	
  /** .*/
  @MapField
	public String name;

}
