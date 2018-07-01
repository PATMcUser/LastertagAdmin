package de.psc.Lasertag.Game;

import javafx.util.Pair;

import java.util.Vector;

public class Mutator {

    private Vector<Pair<String, Integer>> varset= new Vector();
    private String name;
    private String description;
    private boolean editable;
    private boolean used;
    private boolean visible;

    private Game game;

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Vector<Pair<String, Integer>> getVarset() {
        return varset;
    }

    public void setVarset(Vector<Pair<String, Integer>> varset) {
        this.varset = varset;
    }

    public void addVarset(Pair<String, Integer> varset) {
        this.varset.add( varset );
    }

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

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Mutator (String name, String description, boolean ena, boolean use, boolean vis){
        this.name = name;
        this.description = description;
        this.used=use;
        this.visible=vis;
        this.editable=ena;
    }
}
