package org.techtown.quizalram.recycle;

public class Quiz_List {

    int id;
    String group;
    String question;
    String answer;

    public Quiz_List(int id, String group, String question, String answer){
        this.id = id;
        this.group = group;
        this.question = question;
        this.answer = answer;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setGroup(String group){
        this.group = group;
    }

    public String getGroup(){
        return group;
    }

    public void setQuestion(String question){
        this.question = question;
    }

    public String getQuestion(){
        return question;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public String getAnswer(){
        return answer;
    }
}
