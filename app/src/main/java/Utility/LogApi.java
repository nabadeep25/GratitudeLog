package Utility;

import android.app.Application;

public class LogApi extends Application {
   private  String userName;
   private String userId;
   private static LogApi instance;

    public static LogApi getInstance() {
        if(instance==null)
            instance=new LogApi();
        return instance;
    }
    public LogApi(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
