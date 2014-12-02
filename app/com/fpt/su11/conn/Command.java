package com.fpt.su11.conn;

import com.mongodb.DB;

public interface Command {

  public void execute();
  
  public DB getConnection();
}
