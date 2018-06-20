package de.psc.Lasertag.Game;

import com.sun.xml.internal.ws.policy.PolicyMapMutator;

import java.util.Vector;

public class Game  implements Cloneable {
    private String name;
    private String description;
    public Vector<Team> teams = new Vector();

    private Vector<Goal>  goals = new Vector();
    private Vector<Score> scores = new Vector();
    private Vector<Mutator> mutators = new Vector();

    private int playerPerTeamMin=-1;
    private int basePerTeamMin=-1;
    private int basePerTeamMax=-1;

    //Scores
    private Score hit =         new Score ("","",true,false,false, 100);
    private Score shooted =     new Score ("","",true,false,false, -25);
    private Score friendlyHit = new Score ("","",true,false,false, -250);
    private Score magazinSize = new Score ("","",true,false,false, 50);
    private Score tag =         new Score ("","",true,false,false, 2000);
    private Score retTag =      new Score ("","",true,false,false, 250);
    private Score lives =       new Score ("","",true,false,false, 1);
    private Score livesBase =   new Score ("","",true,false,false, 25);

    //Goals
    private Goal score = new Goal ("","",true,false,false, 25000);
    private Goal time = new Goal ("","",true,false,false, 72000); //20 min
    private Goal minTeamSize = new Goal ("","",true,false,false, 250);

    public Vector<Goal> getGoals() {
        return goals;
    }

    public void setGoals(Vector<Goal> goals) {
        this.goals = goals;
    }

    public void addGoal(Goal goals) {
        this.goals.add(goals);
    }

    public Vector<Score> getScores() {
        return scores;
    }

    public void setScores(Vector<Score> score) {
        this.scores = score;
    }

    public void addScore(Score score) {
        this.scores.add(score);
    }

    public Score getLivesBase() {
        return livesBase;
    }


    public void setLivesBase(Score livesBase) {
        this.livesBase = livesBase;
    }

    public Score getLives() {
        return lives;
    }

    public void setLives(Score lives) {
        this.lives = lives;
    }

    public Vector<Mutator> getMutators() {
        return mutators;
    }

    public void setMutators(Vector<Mutator> mutators) {
        this.mutators = mutators;
    }

    public void addMutator(Mutator mutator) {
        mutator.setGame(this);
        this.mutators.add(mutator);
    }

//Mutators
    /*
    private Mutator changeTeam;
    {"changeTeam": true},
    {"minBases": 0},
    {"PlayerPerTeamMin": 1},
    {"Teamsmin": 2},
    {"Teamsmax": null}
    {"ReloadOnBase": true},
    {"LimitMunition": true},
    {"MinBase": 1}
    {"friendlyFire": true}
    {"changeTeam": true},
    {"minBases": 0},
    {"PlayerPerTeamMin": 1},
    {"Teamsmin": 2},
    {"Teamsmax": null}
    {"HealOnBase": true},
    {"LimitMunition": true},
    {"MinBase": 1}
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Vector<Team> getTeams() {
        return teams;
    }

    public void setTeams(Vector<Team> teams) {
        this.teams = teams;
    }

    public void addTeams(Team team) {
        team.setGame(this);
        this.teams.add(team);
    }

    public int getPlayerPerTeamMin() {
        return playerPerTeamMin;
    }

    public void setPlayerPerTeamMin(int playerPerTeamMin) {
        this.playerPerTeamMin = playerPerTeamMin;
    }

    public int getBasePerTeamMin() {
        return basePerTeamMin;
    }

    public void setBasePerTeamMin(int basePerTeamMin) {
        this.basePerTeamMin = basePerTeamMin;
    }

    public int getBasePerTeamMax() {
        return basePerTeamMax;
    }

    public void setBasePerTeamMax(int basePerTeamMax) {
        this.basePerTeamMax = basePerTeamMax;
    }

    public Score getHit() {
        return hit;
    }

    public void setHit(Score hit) {
        hit.setGame(this);
        this.hit = hit;
    }

    public Score getShooted() {
        return shooted;
    }

    public void setShooted(Score shooted) {
        shooted.setGame(this);
        this.shooted = shooted;
    }

    public Score getFriendlyHit() {
        return friendlyHit;
    }

    public void setFriendlyHit(Score friendlyHit) {
        friendlyHit.setGame(this);
        this.friendlyHit = friendlyHit;
    }

    public Score getMagazinSize() {
        return magazinSize;
    }

    public void setMagazinSize(Score magazinSize) {
        magazinSize.setGame(this);
        this.magazinSize = magazinSize;
    }

    public Score getTag() {
        return tag;
    }

    public void setTag(Score tag) {
        tag.setGame(this);
        this.tag = tag;
    }

    public Score getRetTag() {
        return retTag;
    }

    public void setRetTag(Score retTag) {
        retTag.setGame(this);
        this.retTag = retTag;
    }

    public Goal getScore() {
        return score;
    }

    public void setScore(Goal score) {
        score.setGame(this);
        this.score = score;
    }

    public Goal getTime() {
        return time;
    }

    public void setTime(Goal time) {
        time.setGame(this);
        this.time = time;
    }

    public Goal getMinTeamSize() {
        return minTeamSize;
    }

    public void setMinTeamSize(Goal minTeamSize) {
        minTeamSize.setGame(this);
        this.minTeamSize = minTeamSize;
    }

    public Game(String name, String description){
        this.name= name;
        this.description= description;
    }

    @Override
    public Game clone()
    {
        try
        {
            return (Game) super.clone();
        }
        catch ( CloneNotSupportedException e ) {
            // Kann eigentlich nicht passieren, da Cloneable
            throw new InternalError();
        }
    }

}
