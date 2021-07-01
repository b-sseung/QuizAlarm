package org.techtown.quizalram.recycle;

public class Quiz_Title_List {

    String name;
    boolean value;

    public Quiz_Title_List(String name, boolean value){
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean getValue(){
        return this.value;
    }

    public void setValue(boolean value){
        this.value = value;
    }

}
