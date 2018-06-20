package de.psc.Lasertag.Game;

public class Score {
    private String name;
    private String description;
    private boolean editable;
    private boolean used;
    private boolean visible;
    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int value;

    public Score (){
        this.editable = false;
        this.used = false;
        this.visible = false;
        this.value = 0;
    }

    public Score (String name, String description, boolean ena, boolean use, boolean vis, int val) {
        this.name = name;
        this.description = description;
        this.editable = ena;
        this.used = use;
        this.visible = vis;
        this.value = val;
    }

    public int getTotalVal(){
        return this.used ?this.value:0;
    }
}
