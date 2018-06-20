package de.psc.Lasertag.Game;

public class Base {

    public enum LEDPattern {
        STATIC, BLINK, RUN, NONE;
    }

    private Team team;
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
}
