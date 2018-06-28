package de.psc.Lasertag.Game;

public class Base  implements Cloneable{

    public enum LEDPattern {
        STATIC, BLINK, RUN, NONE;
    }

    private Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public LEDPattern getPattern() {
        return pattern;
    }

    public void setPattern(LEDPattern pattern) {
        this.pattern = pattern;
    }

    public Byte getId() {
        return id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public Byte getMode() {
        return mode;
    }

    public void setMode(Byte mode) {
        this.mode = mode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Byte getData() {
        return data;
    }

    public void setData(Byte data) {
        this.data = data;
    }

    private LEDPattern pattern;
    private Byte id;
    private Byte mode;
    private String ip;
    private Byte data;

    public Base (){

    }


    public void sendStat (){
/*
        this.ip;
*//*
        this.team.getColor();
        this.pattern.values();
        this.mode;
        this.id;
        this.data;
*/
    }

    public Base clone() {
        return this;
    }
}
