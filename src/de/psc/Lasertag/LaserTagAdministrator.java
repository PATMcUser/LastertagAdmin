package de.psc.Lasertag;


import com.codename1.components.*;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Image;
import com.codename1.ui.TextField;
import com.codename1.ui.events.SelectionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.GenericListCellRenderer;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import de.psc.Lasertag.GUI.UpdateTabGUI;
import de.psc.Lasertag.Game.*;
import javafx.util.Pair;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import static com.codename1.ui.CN.*;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose 
 * of building native mobile applications using Java.
 */
public class LaserTagAdministrator {

    private Form current;
    private Resources theme;


    public Game selGame;
    public Team selTeam;
    public Player selPlayer;
    public Tabs tab;
    public Form hi;

    public int mm;

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if(err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });        
    }
    
    public void start(){
        if(current != null){
            current.show();
            return;
        }
        hi = new Form("LaserTag", new BorderLayout());
        //hi.add(new Label("Hi World"));

        this.mm = Display.getInstance().convertToPixels(3);


        //TODO: list of games!

        Vector<Game> games = new Vector();
        EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(this.mm * 3, this.mm * 4, 0), false);

        JSONParser json = new JSONParser();
        try(Reader r = new InputStreamReader(Display.getInstance().getResourceAsStream(getClass(), "/Mods.json"), "UTF-8")) {
            Map<String, Object> data = json.parseJSON(r);
            //Array
            java.util.List< Map<String, Object> > content = (java.util.List< Map<String, Object> >)data.get("root");
            for(Map<String, Object> obj : content) {
                String gameName = ((String) obj.get("Name"))==null?"":((String) obj.get("Name"));
                String gameDescription = ((String) obj.get("Description"))==null?"":((String) obj.get("Description"));

                Game g = new Game(gameName,gameDescription);
                Image img = Image.createImage(
                        Display.getInstance().getResourceAsStream(getClass(),
                                 ((String) obj.get("Image"))==null?"":"/" +((String) obj.get("Image")))
                );
                g.setIcon(img);


                //Scores
                java.util.List<Map<String, Object>> scores = (java.util.List<Map<String, Object>>) obj.get("Scores");
                for (Map<String, Object> score : scores) {
                    Score sc = new Score(
                            ((String) score.get("Name"))==null?"":((String) score.get("Name")),
                            ((String) score.get("Description"))==null?"":((String) score.get("Description")),
                            ((String) score.get("enable")).equals("true")?true:false,
                            ((String) score.get("default")).equals("true")?true:false,
                            ((String) score.get("visible")).equals("true")?true:false,
                            ((Double) score.get("Value")).intValue() );
                    sc.setGame(g);
                    g.addScore(sc);
                }

                //Goals
                java.util.List<Map<String, Object>> goals = (java.util.List<Map<String, Object>>) obj.get("Goals");
                for (Map<String, Object> goal : goals) {
                    Goal go = new Goal(
                            ((String) goal.get("Name"))==null?"":((String) goal.get("Name")),
                            ((String) goal.get("Description"))==null?"":((String) goal.get("Description")),
                            ((String) goal.get("enable")).equals("true")?true:false,
                            ((String) goal.get("default")).equals("true")?true:false,
                            ((String) goal.get("visible")).equals("true")?true:false,
                            ((Double) goal.get("Value")).intValue() );
                    go.setGame(g);
                    g.addGoal(go);
                }

                //Mutators
                java.util.List<Map<String, Object>> mutators = (java.util.List<Map<String, Object>>) obj.get("Mutators");
                for (Map<String, Object> mutator : mutators) {
                    Mutator mut = new Mutator(
                            ((String) mutator.get("Name"))==null?"":((String) mutator.get("Name")),
                            ((String) mutator.get("Description"))==null?"":((String) mutator.get("Description")),
                            ((String) mutator.get("enable")).equals("true")?true:false,
                            ((String) mutator.get("default")).equals("true")?true:false,
                            ((String) mutator.get("visible")).equals("true")?true:false);
                    mut.setGame(g);
                    java.util.List<Map<String, Object>> vars = (java.util.List< Map<String, Object> >) mutator.get("Variables");

                    for (Map<String, Object> var : vars) {
                        for (String key : var.keySet()) {
                            mut.addVarset(new Pair<String, Integer>(key, ((Double) var.get(key)).intValue() ));
                        }
                    }
                    g.addMutator(mut);
                }
                games.add(g);
            }
        } catch(IOException err) {
            Log.e(err);
        }


        tab = new Tabs();

        /*
        int mm = Display.getInstance().convertToPixels(3);
        EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(mm * 3, mm * 4, 0), false);

        Image iconB1 = URLImage.createToStorage(placeholder, "icon1", "Triangle_warning_sign.png");
*/
        //Component data = new Component();
        //Vector<MultiButton> data = new Vector();

        Container container_games = BoxLayout.encloseY();
        Container container_teams = BoxLayout.encloseY();
        Container container_scores = BoxLayout.encloseY();
        Container container_goals = BoxLayout.encloseY();
        Container container_mutators = BoxLayout.encloseY();
        Container container_go = BoxLayout.encloseY();

        HashMap<UpdateTabGUI.GUI_Elements, Container> allContainer = new HashMap<UpdateTabGUI.GUI_Elements, Container>();
        allContainer.put(UpdateTabGUI.GUI_Elements.GAMES, container_games);
        allContainer.put(UpdateTabGUI.GUI_Elements.TEAMS, container_teams);
        allContainer.put(UpdateTabGUI.GUI_Elements.SCORES, container_scores);
        allContainer.put(UpdateTabGUI.GUI_Elements.GOALS, container_goals);
        allContainer.put(UpdateTabGUI.GUI_Elements.MUTATORS, container_mutators);
        allContainer.put(UpdateTabGUI.GUI_Elements.GO, container_go);

        UpdateTabGUI.UpdateGames(allContainer, games, this);

        tab.addTab("Games", container_games);
        tab.addTab("Teams", container_teams);
        tab.addTab("Mutators", container_mutators);
        tab.addTab("Scores", container_scores);
        tab.addTab("Goals", container_goals);
        tab.addTab("GO", container_go);


        FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        //fab.addActionListener(e -> ToastBar.showErrorMessage("Not implemented yet..."));
        fab.createSubFAB(FontImage.MATERIAL_DELETE, "Delete Team").addActionListener(
                e -> {selGame.getTeams().remove(selTeam);
                    this.selTeam=null;
                    UpdateTabGUI.UpdateTeams(allContainer, games, this);
                });

        fab.createSubFAB(FontImage.MATERIAL_COLOR_LENS, "Team Color").addActionListener(
                e -> {
                    Dialog d = new Dialog("Change Team color");
                    d.setLayout(new BorderLayout());
                    d.setBlurBackgroundRadius(8);
                    Container disp = BoxLayout.encloseY();

                    Vector<Color> baseColors = new Vector();
                    baseColors.add(this.selTeam.getColor());
                    baseColors.add(new Color(0x000000));
                    baseColors.add(new Color(0xFF0000));
                    baseColors.add(new Color(0xFFFF00));
                    baseColors.add(new Color(0xFFFFFF));

                    ComboBox col_comb = new ComboBox<>();
                    col_comb.setRenderer(new GenericListCellRenderer<>(new MultiButton(), new MultiButton()));
                    for (Color col : baseColors) {
                        //Image testIcon = URLImage.createToStorage(placeholder, "icon1", "http://georgerrmartin.com/gallery/art/dragons05.jpg");
                        col_comb.addItem(UpdateTabGUI.createListEntry("" + col.getRGB(), col.getRGB()));
                    }
                    col_comb.setSelectedIndex(0);

                    disp.add(col_comb);

                    d.add(BorderLayout.CENTER, disp);


                    Button close = new Button("OK");
                    close.addActionListener(e2 ->
                            d.dispose()
                    );
                    d.add(BorderLayout.SOUTH, close);
                    d.show();

                    UpdateTabGUI.UpdateTeams(allContainer, games, this);
                });

        fab.createSubFAB(FontImage.MATERIAL_PEOPLE, "add Team").addActionListener(
                e -> {
                    Dialog d = new Dialog("Add new Team");
                    d.setLayout(new BorderLayout());
                    d.setBlurBackgroundRadius(8);
                    Container disp = BoxLayout.encloseY();
                    TextField teamNameField = new TextField("one");

                    disp.add(teamNameField);

                    Vector<Color> baseColors = new Vector();
                    baseColors.add(new Color(0x000000));
                    baseColors.add(new Color(0xFF0000));
                    baseColors.add(new Color(0xFFFF00));
                    baseColors.add(new Color(0xFFFFFF));

                    ComboBox col_comb = new ComboBox<>();
                    col_comb.setRenderer(new GenericListCellRenderer<>(new MultiButton(), new MultiButton()));
                    for (Color col : baseColors) {
                        //Image testIcon = URLImage.createToStorage(placeholder, "icon1", "http://georgerrmartin.com/gallery/art/dragons05.jpg");
                        col_comb.addItem(UpdateTabGUI.createListEntry("" + col.getRGB(), col.getRGB()));
                    }
                    col_comb.addActionListener(
                            e2 -> {
                                this.selTeam.setColor(baseColors.get(col_comb.getSelectedIndex()));
                                UpdateTabGUI.UpdateTeams(allContainer, games, this);
                            }
                    );
                    disp.add(col_comb);

                    d.add(BorderLayout.CENTER, disp);


                    Button close = new Button("OK");
                    close.addActionListener(e2 ->
                            d.dispose()
                    );
                    d.add(BorderLayout.SOUTH, close);
                    d.show();


                    Team t = new Team(teamNameField.getText(), baseColors.get(col_comb.getSelectedIndex()));
                    this.selGame.addTeams(t);
                    this.selTeam = t;
                    UpdateTabGUI.UpdateTeams(allContainer, games, this);
                });

            fab.createSubFAB(FontImage.MATERIAL_PERSON_ADD, "add Player").addActionListener(
                e -> {
                    Player p = new Player();
                    selTeam.addPlayers(p);
                    UpdateTabGUI.UpdateTeams(allContainer, games, this);
                });

            fab.bindFabToContainer(hi.getContentPane());
            fab.setVisible(false);

            tab.addSelectionListener(new SelectionListener() {
                 public void selectionChanged(int oldSelected, int newSelected){
                    if (oldSelected==1)
                        fab.setVisible(false);
                    if  (newSelected==1 && selGame!=null)
                        fab.setVisible(true);
                }
            });

        hi.add(BorderLayout.CENTER, tab);

        hi.show();
    }


    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }
    
    public void destroy() {
    }

}
