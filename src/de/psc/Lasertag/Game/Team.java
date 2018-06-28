package de.psc.Lasertag.Game;

import java.util.Vector;

public class Team extends Base implements Cloneable{

    public int color;
    public String teamName;
    public Vector<Player> players = new Vector();
    public Vector<Base> bases = new Vector();
    public Game game;
    public Byte id;

    public Byte getId() {
        return id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Vector<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Vector<Player> players) {
        this.players = players;
    }


    public void addPlayers(Player player) {
        this.players.add( player);
    }

    public Vector getBases() {
        return bases;
    }

    public void setBase(Vector<Base> bases) {
        this.bases = bases;
    }

    public void addBase(Base base) {
        this.bases.add(base);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Team (String name, int color){
        this.teamName = name;
        this.color = color;
    }


    public boolean isTeamValide(){
        return ((this.players.size()>=this.game.getPlayerPerTeamMin()|| this.game.getPlayerPerTeamMin()==-1) && (this.game.getBasePerTeamMin()<=this.bases.size() || this.game.getBasePerTeamMin()==-1) && (this.game.getBasePerTeamMax()>this.bases.size() || this.game.getBasePerTeamMax()==-1));
    }

    public long getScore (){
        long score=0;
        for (Player pl: players){
            score += pl.getScore();
        }
        return score;
    }

    public void sendStat (){
        for(Player pl:players){
            pl.sendStat();
        }

        for(Base ba:bases){
            ba.sendStat();
        }

    }

    public Base clone() {
        return this;
    }
}
