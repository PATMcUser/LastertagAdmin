package de.psc.Lasertag.Game;

import de.psc.Lasertag.Naming;

public class Player  implements Cloneable{
    private Team team;
    private String name;
    private int score;
    private boolean tag;
    private Byte id;
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    public Byte getId() {
        return id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public Player(){
        this.name = Naming.RandomeName();
        this.score = 0;
        this.tag=false;
        this.id=-1;
    }

    public Player(Team t, Byte id){
        this.name = Naming.RandomeName();
        this.score = 0;
        this.tag=false;
        this.id=id;
        this.team=t;
    }

    public void sendStat (){
/*
        this.ip;
*//*
        this.id;
        this.team.getId;
        this.team.getColor();
        this.team.getScore();
        this.score;
        if (this.team.getGame().getScore()!=-1)this.team.getScore()+"/"+this.team.getGame().getScore();
        if (this.team.getGame().getTime()!=-1)+"/"+this.team.getGame().getTime();
        if (this.team.getGame().getMinTeamSize()!=-1) ""+this.team.getGame().getMinTeamSize() +"/"+ this.team.getPlayers().size();
*/

    }
}
