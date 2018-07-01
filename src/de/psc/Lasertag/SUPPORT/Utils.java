package de.psc.Lasertag.SUPPORT;

import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.ui.Display;
import com.codename1.ui.Image;
import de.psc.Lasertag.Game.Game;
import de.psc.Lasertag.Game.Goal;
import de.psc.Lasertag.Game.Mutator;
import de.psc.Lasertag.Game.Score;
import de.psc.Lasertag.LaserTagAdministrator;
import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Vector;

public class Utils {
    public static void parseGames(Class<? extends LaserTagAdministrator> aClass, Vector<Game> games){
        games.clear();

        JSONParser json = new JSONParser();
        try(Reader r = new InputStreamReader(Display.getInstance().getResourceAsStream(aClass, "/Mods.json"), "UTF-8")) {
            Map<String, Object> data = json.parseJSON(r);
            //Array
            java.util.List< Map<String, Object> > content = (java.util.List< Map<String, Object> >)data.get("root");
            for(Map<String, Object> obj : content) {
                String gameName = ((String) obj.get("Name"))==null?"":((String) obj.get("Name"));
                String gameDescription = ((String) obj.get("Description"))==null?"":((String) obj.get("Description"));

                Game g = new Game(gameName,gameDescription);
                Image img = Image.createImage(
                        Display.getInstance().getResourceAsStream(aClass,
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
    }
}
