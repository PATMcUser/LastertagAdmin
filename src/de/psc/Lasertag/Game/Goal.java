package de.psc.Lasertag.Game;

public class Goal implements Cloneable{
    private boolean editable;
    private boolean used;
    private boolean visible;
    private long value;
    private String name;
    private String description;
    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public Goal (){
        this.editable = false;
        this.used = false;
        this.visible = false;
        this.value = 0;
    }

    public Goal (String name, String description, boolean ena, boolean use, boolean vis, long val) {
        this.name = name;
        this.description = description;
        this.editable = ena;
        this.used = use;
        this.visible = vis;
        this.value = val;
    }

    public long getTotalVal(){
        return this.used ?this.value:0;
    }

    public boolean getHitGoal(long val){
        return this.used ? val-this.value<=0 :false;
    }

    public Goal clone() {
        Goal newG = new Goal (this.name, this.description, this.editable, this.used, this.visible, this.value);
        newG.setGame(this.game);
        return newG;
    }
}
