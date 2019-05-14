package com.ramanic.firebasetodolist;


public class toDoListClass {
    private String title, description,date;
    private int completed;

    public toDoListClass() {
    }
    public  String getDate(){
        return date;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }

    public Boolean getCompleted() {
        if(completed==0){
            return false;
        }else{
            return  true;
        }

    }


}
