package de.psc.Lasertag.GUI;

import com.codename1.components.FloatingActionButton;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Image;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.GenericListCellRenderer;
import com.codename1.ui.list.MultiList;
import de.psc.Lasertag.Game.Base;
import de.psc.Lasertag.Game.Game;
import de.psc.Lasertag.Game.Player;
import de.psc.Lasertag.Game.Team;
import de.psc.Lasertag.LaserTagAdministrator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class UpdateTabGUI {
    public enum GUI_Elements {
        GAMES, TEAMS, SCORES, GOALS,MUTATORS
    }


    public static void UpdateGames (
                HashMap<GUI_Elements, Container> allContainer, Vector<Game> games, LaserTagAdministrator LTA){
        Container container_games = allContainer.get(GUI_Elements.GAMES);
        container_games.removeAll();
        container_games.setScrollableY(true);
        container_games.setScrollVisible(true);

        int mm = Display.getInstance().convertToPixels(3);
        EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(mm * 3, mm * 4, 0), false);
        Image testIcon = URLImage.createToStorage(placeholder, "icon1", "http://georgerrmartin.com/gallery/art/dragons05.jpg");

        ButtonGroup bg = new ButtonGroup();
        for(Game mod:games){
            MultiButton twoLinesIconCheckBox = new MultiButton(mod.getName());
            twoLinesIconCheckBox.setIcon(testIcon);
            twoLinesIconCheckBox.setRadioButton(true);
            twoLinesIconCheckBox.setTextLine2(mod.getDescription());
            twoLinesIconCheckBox.setGroup(bg);
            container_games.addComponent(twoLinesIconCheckBox);
        }

        if(LTA.selGame!=null){
            MultiButton twoLinesIconCheckBox = new MultiButton("Last: " + LTA.selGame.getName());
            twoLinesIconCheckBox.setIcon(testIcon);
            twoLinesIconCheckBox.setRadioButton(true);
            twoLinesIconCheckBox.setTextLine2(LTA.selGame.getDescription());
            twoLinesIconCheckBox.setGroup(bg);
            container_games.addComponent(twoLinesIconCheckBox);
        }

        bg.addActionListener(e -> {
            try {
                LTA.selGame = (Game) games.get( bg.getSelectedIndex() ).clone();
            } catch (ArrayIndexOutOfBoundsException cat_err){
                if(LTA.selGame==null) {
                    LTA.selGame = (Game) games.get(1).clone();
                }
            }

            UpdateTabGUI.UpdateTeams(allContainer, games, LTA);
            UpdateTabGUI.UpdateGoals(allContainer, games, LTA);
            UpdateTabGUI.UpdateMutators(allContainer, games, LTA);
            UpdateTabGUI.UpdateScores(allContainer, games, LTA);
            UpdateTabGUI.UpdateGames(allContainer, games, LTA);

            LTA.tab.setSelectedIndex(1);
            //ToastBar.showMessage("You selected " + (  games.get(bg.getSelectedIndex()).getName() ), FontImage.MATERIAL_INFO);

        });
    }


    public static void UpdateTeams (
                HashMap<GUI_Elements, Container> allContainer, Vector<Game> games, LaserTagAdministrator LTA){
        Container container_teams = allContainer.get(GUI_Elements.TEAMS);
        container_teams.removeAll();
        container_teams.setScrollableY(true);
        container_teams.setScrollVisible(true);


        //DEMO
        if(LTA.selGame.getTeams().size()==0) {
            String[] tnames = {"Bunnys", "Pussys", "Brooos"};
            for (String tname : tnames) {
                Team t = new Team(tname, new Color((0xFF0000 >> (4 * LTA.selGame.getTeams().size())) ));
                for (int j = 0; j < 5; j++) {
                    Player p = new Player();
                    p.setTeam(t);
                    t.addPlayers(p);
                }
                LTA.selGame.addTeams(t);
            }
        }
        //ENDE DEMO



        ComboBox teamCombo =new ComboBox<>();
        teamCombo.setRenderer(new GenericListCellRenderer<>(new MultiButton(), new MultiButton()));

        for(Team team:LTA.selGame.getTeams()) {
            int mm = Display.getInstance().convertToPixels(3);
            EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(mm * 3, mm * 4, 0), false);
            Image testIcon = URLImage.createToStorage(placeholder, "icon1", "http://georgerrmartin.com/gallery/art/dragons05.jpg");

            teamCombo.addItem(createListEntry(team.getTeamName(), "" + team.getPlayers().size()));
        }

        teamCombo.addActionListener(
                e2 -> {
                    LTA.selTeam = LTA.selGame.getTeams().get(teamCombo.getSelectedIndex());
                    UpdateTabGUI.UpdateTeams(allContainer, games, LTA);
                }
        );

        container_teams.add(teamCombo);

        if(LTA.selTeam!=null) {
            Container text = BoxLayout.encloseY();
            UpdateTeams_text(text,games,LTA);
            Container list = BoxLayout.encloseY();
            UpdateTeams_list(list,games,LTA);
            container_teams.addComponent(text);
            container_teams.addComponent(list);

            //TODO: zuordnen von Spielern und Perepherie in Teams
            //TODO: teams auf validität Prüfen
        }else{
            container_teams.add(new SpanLabel("Select a Team"));
        }
    }


    private static Container UpdateTeams_text (Container container, Vector<Game> games, LaserTagAdministrator LTA){
        TextField teamNameField = new TextField(LTA.selTeam.getTeamName());
        teamNameField.addActionListener(
                e1 -> {
                    LTA.selTeam.setTeamName(teamNameField.getText());
                }
        );
        container.add(teamNameField);
        return container;
    }


    private static Container UpdateTeams_list (Container container, Vector<Game> games, LaserTagAdministrator LTA){
        container.removeAll();
        ArrayList<Map<String, Object>> tPlayers = new ArrayList<>();
        for (Player sPlayer : LTA.selTeam.getPlayers()) {
            tPlayers.add(createListEntry(sPlayer.getName(), "" + sPlayer.getId()));
        }
        DefaultListModel<Map<String, Object>> model = new DefaultListModel<>(tPlayers);
        MultiList teamPlayerList = new MultiList(model);

        teamPlayerList.addActionListener(
                e1 -> {
/*
                    ToastBar.showMessage(
                            teamPlayerList.getSelectedButton().getTextLine1()
                                    + " - " +
                                   teamPlayerList.getSelectedButton().getTextLine2(),
                            FontImage.MATERIAL_INFO
                    );
*/
                    for(Player pl:LTA.selTeam.getPlayers()) {
                        if (    (pl.getName().equals(teamPlayerList.getSelectedButton().getTextLine1()))&&
                                ((""+pl.getId()).equals(teamPlayerList.getSelectedButton().getTextLine2())) )
                            LTA.selPlayer = pl;
                    }
                    Dialog d = new Dialog("Player config");
                    d.setLayout( new BorderLayout() );
                    d.setBlurBackgroundRadius(8);


                    Container disp = BoxLayout.encloseY();




                    TextField playerNameField = new TextField(LTA.selPlayer.getName());
                    playerNameField.addActionListener(
                            e2 -> {
                                LTA.selPlayer.setName(playerNameField.getText());
                                UpdateTeams_list( container, games, LTA);
                            });
                    disp.add( playerNameField);

                    TextField idNameField = new TextField("ID: " + LTA.selPlayer.getId() );
                    idNameField.setEditable(false);
                    disp.add( idNameField );


                    TextField ipNameField = new TextField("IP: " + LTA.selPlayer.getIp() );
                    ipNameField.setEditable(false);
                    disp.add( ipNameField );

                    //d.add(new SpanLabel("IP:",  LTA.selPlayer.getIp().toString() ));
                    Button del = new Button("Delete from Team");
                    del.addActionListener(e2 -> {
                        LTA.selTeam.getPlayers().remove(LTA.selPlayer);
                        UpdateTeams_list( container, games, LTA);
                        d.dispose();
                    });
                    disp.add(del);

                    d.add(BorderLayout.CENTER, disp);

                    Button close = new Button("Close");
                    close.addActionListener(e2 -> {
                        d.dispose();
                    });
                    d.add(BorderLayout.SOUTH, close);
                    d.show();
                }
        );
        container.add(teamPlayerList);
        return container;
    }


    private static void UpdateTeams_combo (
            Container allContainer, Vector<Game> games, LaserTagAdministrator LTA){

    }


    public static void UpdateScores (
                HashMap<GUI_Elements, Container> allContainer, Vector<Game> games, LaserTagAdministrator LTA){
        Container container_scores = allContainer.get(GUI_Elements.SCORES);
        container_scores.removeAll();
        container_scores.setScrollableY(true);
        container_scores.setScrollVisible(true);

    }


    public static void UpdateGoals (
                HashMap<GUI_Elements, Container> allContainer, Vector<Game> games, LaserTagAdministrator LTA){
        Container container_goals = allContainer.get(GUI_Elements.GOALS);
        container_goals.removeAll();
        container_goals.setScrollableY(true);
        container_goals.setScrollVisible(true);

    }


    public static void UpdateMutators (
                HashMap<GUI_Elements, Container> allContainer, Vector<Game> games, LaserTagAdministrator LTA){
        Container container_mutators = allContainer.get(GUI_Elements.MUTATORS);
        container_mutators.removeAll();
        container_mutators.setScrollableY(true);
        container_mutators.setScrollVisible(true);

        container_mutators.add(new SpanLabel("Clicked " + LTA.selGame.getName()));

    }


    private static Map<String, Object> createListEntry(String name, String player) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("Line1", name);
        entry.put("Line2", player);
        return entry;
    }


    public static Map<String, Object> createListEntry(String name, String player, int color) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("Line1", name);
        entry.put("Line2", player);
        entry.put("icon", FontImage.createFixed("\uE161", FontImage.getMaterialDesignFont(), color, 50, 50));
        return entry;
    }


    public static Map<String, Object> createListEntry(String name, int color) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("Line1", name);
        entry.put("icon", FontImage.createFixed("\uE161", FontImage.getMaterialDesignFont(), color, 50, 50));
        return entry;
    }


    public static Map<String, Object> createListEntry(String name, Image icon) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("Line1", name);
        entry.put("icon", icon);
        return entry;
    }
}
