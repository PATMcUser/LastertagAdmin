package de.psc.Lasertag.GUI;

import com.codename1.components.MultiButton;
import com.codename1.components.SpanButton;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.FileSystemStorage;
import com.codename1.ui.*;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Image;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.GenericListCellRenderer;
import com.codename1.ui.list.MultiList;
import de.psc.Lasertag.Game.*;
import de.psc.Lasertag.LaserTagAdministrator;
import de.psc.Lasertag.SUPPORT.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static com.codename1.ui.Component.CENTER;

public class UpdateTabGUI {
    public enum GUI_Elements {
        GAMES, TEAMS, SCORES, GOALS, MUTATORS, GO
    }


    public static void UpdateGames (
                HashMap<GUI_Elements, Container> allContainer, Vector<Game> games, LaserTagAdministrator LTA){
        Container container_games = allContainer.get(GUI_Elements.GAMES);
        container_games.removeAll();
        container_games.setScrollableY(true);
        container_games.setScrollVisible(true);

        ButtonGroup bg = new ButtonGroup();

        //list of games
        Utils.parseGames(LTA.getClass(), games);

        if(LTA.selGame!=null){
            MultiButton twoLinesIconCheckBox = new MultiButton("Last: " + LTA.selGame.getName());
            twoLinesIconCheckBox.setIcon(LTA.selGame.getIcon().scaled(LTA.mm * 4, LTA.mm * 4));
            twoLinesIconCheckBox.setRadioButton(true);
            twoLinesIconCheckBox.setTextLine2(LTA.selGame.getDescription());
            twoLinesIconCheckBox.setGroup(bg);
            container_games.addComponent(twoLinesIconCheckBox);
        }

        for(Game mod:games){
            MultiButton twoLinesIconCheckBox = new MultiButton(mod.getName());
            twoLinesIconCheckBox.setIcon(mod.getIcon().scaled(LTA.mm * 4, LTA.mm * 4));
            twoLinesIconCheckBox.setRadioButton(true);
            twoLinesIconCheckBox.setTextLine2(mod.getDescription());
            twoLinesIconCheckBox.setGroup(bg);
            container_games.addComponent(twoLinesIconCheckBox);
        }

        bg.addActionListener(e -> {
            try {
                LTA.selGame = (Game) games.get( bg.getSelectedIndex() - (LTA.selGame==null?0:1));
            } catch (ArrayIndexOutOfBoundsException cat_err){
                if(LTA.selGame==null) {
                    LTA.selGame = (Game) games.get(1);
                }
            }

            UpdateTabGUI.UpdateTeams(allContainer, games, LTA);
            UpdateTabGUI.UpdateGoals(allContainer, games, LTA);
            UpdateTabGUI.UpdateMutators(allContainer, games, LTA);
            UpdateTabGUI.UpdateScores(allContainer, games, LTA);
            UpdateTabGUI.UpdateGames(allContainer, games, LTA);
            UpdateTabGUI.UpdateGO(allContainer, games, LTA);

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
                Team t = new Team(tname, (0xFF0000 >> (4 * LTA.selGame.getTeams().size())) );
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


    public static void UpdateGO (
            HashMap<GUI_Elements, Container> allContainer, Vector<Game> games, LaserTagAdministrator LTA){
        Container container_go = allContainer.get(GUI_Elements.GO);
        container_go.removeAll();
        container_go.setScrollableY(false);
        container_go.setScrollVisible(false);
        Container b_cont = BoxLayout.encloseY();

        //b_cont.add(LTA.selGame.getIcon().scaled(LTA.mm * 10, LTA.mm * 10));
        //b_cont.add(new SpanLabel("Start:\n\r"+LTA.selGame.getName()));
        Image deSat= desaturate(LTA.selGame.getIcon().scaled(LTA.mm * 10, LTA.mm * 10));
        SpanButton sb = new SpanButton("Start:\n\r"+LTA.selGame.getName());
        sb.setIcon(deSat);
        sb.setLayout(new FlowLayout(CENTER));
        sb.setEnabled(true);
       // sb.add(b_cont);
        container_go.add(sb);

    }

    private static Image desaturate(Image scaled) {
        int[] deSArr = scaled.getRGB();
        for (int w=0;w<deSArr.length;w++){
            if((deSArr[w]&0xff000000)==0x00000000)
                continue;
            int gray = ( ((deSArr[w]>>0)&0xFF) + ((deSArr[w]>>8)&0xFF) + ((deSArr[w]>>16)&0xFF) ) / 3 ;
            deSArr[w] = 0xff000000 + (gray<<16) + (gray<<8) + gray;
        }
        Image deS = Image.createImage(deSArr, scaled.getWidth(), scaled.getHeight() );
        return deS;
    }


    public static void UpdateScores (
                HashMap<GUI_Elements, Container> allContainer, Vector<Game> games, LaserTagAdministrator LTA){
        Container container_scores = allContainer.get(GUI_Elements.SCORES);
        container_scores.removeAll();
        container_scores.setScrollableY(true);
        container_scores.setScrollVisible(true);

        for(Score score:LTA.selGame.getScores()){
            MultiButton threeLinesButton = new MultiButton(score.getName());
            threeLinesButton.setTextLine2("" + score.getValue());
            threeLinesButton.setTextLine3(score.getDescription());
            threeLinesButton.setEnabled(score.isEditable());
            threeLinesButton.setVisible(score.isVisible());
            threeLinesButton.setSelected(score.isUsed());

            if (score.isUsed()==true)
                container_scores.addComponent(threeLinesButton);

            threeLinesButton.addActionListener(
                    eScor -> {
                        String name=((MultiButton)eScor.getActualComponent()).getTextLine1();
                        String desc=((MultiButton)eScor.getActualComponent()).getTextLine3();
                        Score selSc = null;
                        for(Score sc:LTA.selGame.getScores()){
                            if (sc.getName().equals(name) &&
                                    sc.getDescription().equals(desc)){
                                selSc = sc;
                                break;
                            }
                        }

                        if (selSc == null) return;

                        Dialog d =new Dialog();
                        d.setLayout( new BorderLayout() );
                        d.setBlurBackgroundRadius(8);
                        Container mainCont=BoxLayout.encloseY();
                        d.add(new SpanLabel(name));
                        d.add(new SpanLabel(desc));
                        d.add(BorderLayout.CENTER, mainCont);

                        Button close = new Button("Close");
                        close.addActionListener(e2 -> {
                            d.dispose();
                        });
                        d.add(BorderLayout.SOUTH, close);
                        d.show();

                    });
        }

    }


    public static void UpdateGoals (
                HashMap<GUI_Elements, Container> allContainer, Vector<Game> games, LaserTagAdministrator LTA){
        Container container_goals = allContainer.get(GUI_Elements.GOALS);
        container_goals.removeAll();
        container_goals.setScrollableY(true);
        container_goals.setScrollVisible(true);

        boolean condition=false;

        for(Goal goal:LTA.selGame.getGoals()) {
            MultiButton threeLinesButton = new MultiButton(goal.getName());
            threeLinesButton.setCheckBox(true);
            threeLinesButton.setSelected(goal.isUsed());
            threeLinesButton.setVisible(goal.isVisible());
            //threeLinesButton.setEnabled(goal.isEditable());
            threeLinesButton.setTextLine2(goal.getDescription());
            condition=condition||goal.isUsed();

            //if (goal.isUsed())
            container_goals.addComponent(threeLinesButton);

            if (goal.isEditable())
                threeLinesButton.addActionListener(
                        eMutator -> {
                            String name = ((MultiButton) eMutator.getActualComponent()).getTextLine1();
                            String desc = ((MultiButton) eMutator.getActualComponent()).getTextLine2();
                            Goal selGo = null;
                            for (Goal go : LTA.selGame.getGoals()) {
                                if (    go.getName().equals(name) &&
                                        go.getDescription().equals(desc)) {
                                    selGo = go;
                                    break;
                                }
                            }

                            if (selGo == null) return;

                            Dialog d = new Dialog(selGo.getName());
                            d.setLayout( new BorderLayout() );
                            d.setBlurBackgroundRadius(8);


                            Container mainCont= BoxLayout.encloseY();
                            mainCont.add(new SpanLabel(selGo.getDescription()));
                            Container sel=new Container( new BorderLayout());

                            Button minus = new Button("-");
                            TextField value = new TextField(""+selGo.getValue());
                            Button plus = new Button("+");

                            Goal finalSelGo1 = selGo;
                            minus.addActionListener(bMin->{
                                finalSelGo1.setValue(finalSelGo1.getValue()-1);
                                value.setText(""+finalSelGo1.getValue());
                            });
                            plus.addActionListener(bPls->{
                                finalSelGo1.setValue(finalSelGo1.getValue()+1);
                                value.setText(""+finalSelGo1.getValue());
                            });

                            sel.add(BorderLayout.WEST, minus);
                            sel.add(BorderLayout.CENTER, value);
                            sel.add(BorderLayout.EAST, plus);

                            CheckBox ch=new CheckBox("Use?" );
                            ch.setSelected(selGo.isUsed());



                            ch.setEnabled(goal.isEditable());



                            Goal finalSelGo = selGo;
                            ch.addActionListener(eCB->{
                                finalSelGo.setUsed( ((CheckBox)eCB.getActualComponent()).isSelected() );
                            });

                            mainCont.add(sel);
                            mainCont.add(ch);
                            d.add(BorderLayout.CENTER, mainCont);

                            Button close = new Button("Close");
                            close.addActionListener(e2 -> {
                                d.dispose();
                                UpdateGoals(allContainer,games,LTA);
                            });
                            d.add(BorderLayout.SOUTH, close);
                            d.show();

                        });
        }
        if (condition==false) ToastBar.showMessage("You selected no goal" , FontImage.MATERIAL_INFO);

    }


    public static void UpdateMutators (
                HashMap<GUI_Elements, Container> allContainer, Vector<Game> games, LaserTagAdministrator LTA){
        Container container_mutators = allContainer.get(GUI_Elements.MUTATORS);
        container_mutators.removeAll();
        container_mutators.setScrollableY(true);
        container_mutators.setScrollVisible(true);

        for(Mutator mutator:LTA.selGame.getMutators()){
            MultiButton threeLinesButton = new MultiButton(mutator.getName());
            threeLinesButton.setCheckBox(true);
            threeLinesButton.setSelected(mutator.isUsed());
            threeLinesButton.setVisible(mutator.isVisible());
            threeLinesButton.setEnabled(mutator.isEditable());
            threeLinesButton.setTextLine2(mutator.getDescription());

            //if (mutator.isUsed())
            container_mutators.addComponent(threeLinesButton);

            threeLinesButton.addActionListener(
                    eMutator -> {
                        String name=((MultiButton)eMutator.getActualComponent()).getTextLine1();
                        String desc=((MultiButton)eMutator.getActualComponent()).getTextLine2();
                        threeLinesButton.setSelected(!threeLinesButton.isSelected());
                        Mutator selMut = null;
                        for(Mutator sc:LTA.selGame.getMutators()){
                            if (sc.getName().equals(name) &&
                                    sc.getDescription().equals(desc)){
                                selMut = sc;
                                break;
                            }
                        }

                        if (selMut == null) return;

                        Dialog d =new Dialog( selMut.getName() );
                        d.setLayout( new BorderLayout() );
                        d.setBlurBackgroundRadius(8);
                        Container mainCont=BoxLayout.encloseY();
                        mainCont.add(new SpanLabel(selMut.getDescription()));
                        CheckBox ch=new CheckBox("Use?" );
                        ch.setSelected(selMut.isUsed());
                        Mutator finalSelMut = selMut;
                        ch.addActionListener(eCB->{
                            finalSelMut.setUsed( ((CheckBox)eCB.getActualComponent()).isSelected() );
                        });
                        mainCont.add(ch);
                        d.add(BorderLayout.CENTER, mainCont);

                        Button close = new Button("Close");
                        close.addActionListener(e2 -> {
                            UpdateMutators(allContainer,games,LTA);
                            d.dispose();
                        });
                        d.add(BorderLayout.SOUTH, close);
                        d.show();

                    });
        }

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
