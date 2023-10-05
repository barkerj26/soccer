package cs301.Soccer;

import android.annotation.SuppressLint;
import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private final Map<String, SoccerPlayer> database = new LinkedHashMap<>();

    /**
     * add a player;
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        return database.putIfAbsent(firstName + ";" + lastName,
                new SoccerPlayer(firstName, lastName, uniformNumber, teamName)) == null;
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        return database.remove(firstName + ";" + lastName) != null;
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        return database.get(firstName + ";" + lastName);
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        SoccerPlayer ply = getPlayer(firstName, lastName);
        if (ply == null) {
            return false;
        }

        ply.bumpGoals();
        return true;
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        SoccerPlayer ply = getPlayer(firstName, lastName);
        if (ply == null) {
            return false;
        }

        ply.bumpYellowCards();
        return true;
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        SoccerPlayer ply = getPlayer(firstName, lastName);
        if (ply == null) {
            return false;
        }

        ply.bumpRedCards();
        return true;
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        if (teamName == null || teamName.equals("")) {
            return database.size();
        }

        int total = 0;
        for (SoccerPlayer player : database.values()) {
            if (player.getTeamName().equals(teamName)) {
                total++;
            }
        }

        return total;
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        if (idx < 0 || idx > numPlayers(teamName)) {
            return null;
        }

        SoccerPlayer output = null;
        int i = 0;
        if (teamName == null || teamName.equals("")) {
            for (SoccerPlayer player : database.values()) {
                if (i == idx) {
                    output = player;
                    break;
                }
                i++;
            }
        } else {
            for (SoccerPlayer player : database.values()) {
                if (!player.getTeamName().equals(teamName)) {
                    continue;
                }
                if (i == idx) {
                    output = player;
                    break;
                }
                i++;
            }
        }

        return output;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @SuppressLint("DefaultLocale")
    @Override
    public boolean readData(File file) {
        try {
            if (!file.exists()) {
                return false;
            }
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(";");
                SoccerPlayer player = new SoccerPlayer(data[0], data[1],
                        Integer.parseInt(data[3]), data[2]);
                player.setScoring(Integer.parseInt(data[4]), Integer.parseInt(data[5]),
                        Integer.parseInt(data[6]));
                // database.remove(data[0] + ";" + data[1]);
                database.put(data[0] + ";" + data[1], player);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @SuppressLint("DefaultLocale")
    @Override
    public boolean writeData(File file) {
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileWriter writer = new FileWriter(file.getName());
            for (SoccerPlayer player : database.values()) {
                writer.write(String.format("%s;%s;%s;%d;%d;%d;%d",
                        player.getFirstName(),
                        player.getLastName(),
                        player.getTeamName(),
                        player.getUniform(),
                        player.getGoals(),
                        player.getYellowCards(),
                        player.getRedCards()
                ));
            }
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        return new HashSet<String>();
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
