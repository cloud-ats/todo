package util;

import com.mongodb.DB;

public interface Command {

  public void execute();
  
  public DB getConnection();
}
