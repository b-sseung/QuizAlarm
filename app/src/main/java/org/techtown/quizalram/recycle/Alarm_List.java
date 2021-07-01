package org.techtown.quizalram.recycle;

public class Alarm_List {

    int _id;
    int hour;
    int minute;
    String label;
    String repeateDay;
    String ringtone;
    String vibrator;
    boolean turn;

    public Alarm_List(int _id, int hour, int minute, String label, String repeateDay, String ringtone, String vibrator, boolean turn){
        this._id = _id;
        this.hour = hour;
        this.minute = minute;
        this.label = label;
        this.repeateDay = repeateDay;
        this.ringtone = ringtone;
        this.vibrator = vibrator;
        this.turn = turn;
    }

    public int getId(){
        return this._id;
    }

    public void setId(int id){
        this._id = id;
    }

    public int getHour(){
        return this.hour;
    }

    public void setHour(int hour){
        this.hour = hour;
    }

    public int getMinute(){
        return this.minute;
    }

    public void setMinute(int minute){
        this.minute = minute;
    }

    public String getLabel(){
        return this.label;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public String getRepeateDay(){
        return this.repeateDay;
    }

    public void setRepeateDay(String repeateDay){
        this.repeateDay = repeateDay;
    }

    public String getRingtone(){
        return this.ringtone;
    }

    public void setRingtone(String ringtone){
        this.ringtone = ringtone;
    }

    public String getVibrator(){
        return this.vibrator;
    }

    public void setVibrator(String vibrator){
        this.vibrator = vibrator;
    }

    public boolean getTurn(){
        return this.turn;
    }

    public void setTurn(boolean turn){
        this.turn = turn;
    }

}
