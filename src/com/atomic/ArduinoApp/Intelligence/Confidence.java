package com.atomic.ArduinoApp.Intelligence;

import android.util.Log;
import com.atomic.ArduinoApp.BaseActivity;
import com.atomic.ArduinoApp.Handlers.DatabaseHandler;
import com.atomic.ArduinoApp.Handlers.DatabaseHelper;
import com.atomic.ArduinoApp.Values.Action;

import java.util.*;


/**
 * ArduinoApp for SRS Copyright (C) 2015 - Atomic Development
 */
public class Confidence {

    String action = null;
    float score = -1;
    static int sampleSize = 10;

    public Confidence(String action, float score) {
        this.action = action;
        this.score = score;
    }

    public float getScore() {
        return score;
    }

    public String getAction() {
        return action;
    }

    /**
     * Confidence to create a runnable
     * @param action
     * @return
     */
    public static Confidence getConfidence(Action action) {

        DatabaseHandler db = BaseActivity.getDatabaseHandler();
        List<String> values = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        List<Integer> index = Arrays.asList(new Integer[] {1,2,3,4,5,6,7,8,9,0});
        boolean DEBUG = true;
        Random rand = new Random();

        /**
         * Get random values, not just 1 through 10!
         */

        for(int i = 0; i < sampleSize; i++) {
            int j = rand.nextInt(db.getIdentifcationNumber(DatabaseHelper.Type.TIMESTAMP));
            values.add(db.get(DatabaseHelper.Type.TIMESTAMP, "id", String.valueOf(j), "timestamp"));
            actions.add(db.get(DatabaseHelper.Type.TIMESTAMP, "id", String.valueOf(j), "action"));
        }

        Collections.shuffle(values);
        Collections.shuffle(actions);
        Collections.shuffle(index);


        Integer[] indexNumber = Arrays.copyOf(index.toArray(), index.toArray().length, Integer[].class);

        float score1 = score(values.get(indexNumber[0]), values.get(indexNumber[5]), actions.get(indexNumber[0]), actions.get(indexNumber[5]), action.toString());

        float score2 = score(values.get(indexNumber[1]), values.get(indexNumber[6]), actions.get(indexNumber[1]), actions.get(indexNumber[6]), action.toString());

        float score3 = score(values.get(indexNumber[2]), values.get(indexNumber[7]), actions.get(indexNumber[2]), actions.get(indexNumber[7]), action.toString());

        if(DEBUG) Log.d("SCORE","Score (1): " + score1 + "%");
        if(DEBUG) Log.d("SCORE","Score (2): " + score2 + "%");
        if(DEBUG) Log.d("SCORE","Score (3): " + score3 + "%");


        float finalScore = Math.round(score1 + score2 + score3) / 3;

        return new Confidence(action.toString(), finalScore);

    }

    private static float score(String f1, String f2, String action1, String action2, String mainAction) {
        String[] part1 = f1.split(":\\[").toString().split("");
        String[] part2 = f2.split(":\\[").toString().split("");
        double c = 0;
        double magic = 29;

        for(int i = 0; i < part1.length; i++) {
            if (part1[i].equalsIgnoreCase(part2[i])) {
                c++;
            }
        }

        // returns number between 1 and 50
        c = Math.round(((c / magic) * 100) / 2);

        if(action1.equalsIgnoreCase(action2)) {
            c += 16;
        }

        if(action1.equalsIgnoreCase(mainAction)) {
            c += 17;
        }

        if(action2.equalsIgnoreCase(mainAction)) {
            c += 17;
        }

        Log.d("SCORE", "Score Final: " + c);

        return (float) c;
    }
}
