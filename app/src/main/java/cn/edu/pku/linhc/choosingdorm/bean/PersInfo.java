package cn.edu.pku.linhc.choosingdorm.bean;

/**
 * Created by test on 2018/1/5.
 */

public class PersInfo {
    private String id;
    private String name;
    private String gender;
    private String Vcode;
    private String room;
    private String building;
    private String location;
    private String grade;

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getGender(){
        return gender;
    }
    public void setGender(String gender){
        this.gender=gender;
    }
    public String getVcode(){
        return Vcode;
    }
    public void setVcode(String vcode){
        this.Vcode=vcode;
    }
    public String getRoom(){
        return room;
    }
    public void setRoom(String room){
        this.room=room;
    }
    public String getBuilding(){
        return building;
    }
    public void setBuilding(String building){
        this.building=building;
    }
    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location=location;
    }
    public String getGrade(){
        return grade;
    }
    public void setGrade(String gender){
        this.gender=gender;
    }
}
