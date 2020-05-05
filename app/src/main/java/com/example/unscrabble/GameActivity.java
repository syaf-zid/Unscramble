package com.example.unscrabble;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    Random random;
    String currWord;
    LinearLayout wordLayout;
    TextView[] charViews;
    GridView letters;
    LetterAdapter ltrAdapt;
    ImageView imageView;

    private int currPart;
    private int numChars;
    private int numCorr;
    ArrayList<ImageData> words = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        imageView = findViewById(R.id.ivWhiteBg);

        words.add(new ImageData(0, "APPLE", R.drawable.apple));
        words.add(new ImageData(1, "COMPUTER", R.drawable.computer));
        words.add(new ImageData(2, "TABLET", R.drawable.tablet));
        words.add(new ImageData(3, "TELEVISION", R.drawable.television));
        words.add(new ImageData(4, "ANDROID", R.drawable.android));
        words.add(new ImageData(5, "KEYBOARD", R.drawable.keyboard));
        words.add(new ImageData(6, "PLAYSTATION", R.drawable.playstation));

        random = new Random();
        currWord = "";

        wordLayout = findViewById(R.id.word);
        letters = findViewById(R.id.letters);

        playGame();
    }

    private void playGame() {
        int arrayCount = words.size();
        int randomInt = random.nextInt(arrayCount);
        String newWord = words.get(randomInt).getAnswer();

        while(newWord.equals(currWord)) newWord = words.get(randomInt).getAnswer();
        currWord = newWord;
        imageView.setImageResource(words.get(randomInt).getImage());

        charViews = new TextView[currWord.length()];
        wordLayout.removeAllViews();

        // create the "underscores" for each letters
        for(int c = 0; c < currWord.length(); c++) {
            charViews[c] = new TextView(this);
            charViews[c].setText("" + currWord.charAt(c));

            charViews[c].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            charViews[c].setGravity(Gravity.CENTER);
            charViews[c].setTextColor(Color.WHITE);
            charViews[c].setTextSize(20);
            charViews[c].setBackgroundResource(R.drawable.letter_bg);

            // add to layout
            wordLayout.addView(charViews[c]);
        }

        String[] lettersCol = new String[currWord.length()];
        String[] wordSplit = currWord.split("");
        ArrayList<String> wordSplitList = new ArrayList<>(Arrays.asList(wordSplit));
        System.out.println(wordSplitList);
        wordSplitList.remove(0);

        // please use shuffle
        // convert array to arraylist for shuffle
         Collections.shuffle(wordSplitList);

         // To remove duplicate letters in a word
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(wordSplitList);
        ArrayList<String> wordSplitListWD = new ArrayList<>(hashSet);
        String[] wordSplitArr = new String[wordSplitListWD.size()];
        wordSplitArr = wordSplitListWD.toArray(wordSplitArr);
        System.out.println(wordSplitArr);

        System.arraycopy(wordSplitArr, 0, lettersCol, 0, lettersCol.length);
        ltrAdapt = new LetterAdapter(this, lettersCol);
        letters.setAdapter(ltrAdapt);

        currPart = 0;
        numChars = currWord.length();
        numCorr = 0;
    }

    int marker = 0;
    public void letterPressed(View v) {
            String ltr = ((TextView) v).getText().toString();
            char letterChar = ltr.charAt(0);

            boolean correct = false;
            // use manual loop, check using if else
            // increase i if the letter matches correspondingly
            // use marker instead of i
            for(int i = 0; i < currWord.length(); i++) {
                if(currWord.charAt(i) == letterChar) {
                    correct = true;
                    numCorr++;
                    charViews[i].setTextColor(Color.BLACK);

                    v.setEnabled(false);
                    v.setBackgroundResource(R.drawable.letter_down);
                }
            }

        int numParts = 3;
        if(correct) {
            if(numCorr == numChars) {
                disableBtns();

                AlertDialog.Builder winAlert = new AlertDialog.Builder(this);
                winAlert.setTitle("YAY");
                winAlert.setMessage("You win!\n\nThe answer was:\n" + currWord);
                winAlert.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity.this.playGame();
                    }
                });

                winAlert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity.this.finish();
                    }
                });

                winAlert.show();
            }
        } else if(currPart < numParts) {
            currPart++;
        } else {
            disableBtns();

            AlertDialog.Builder loseAlert = new AlertDialog.Builder(this);
            loseAlert.setTitle("OOPS");
            loseAlert.setMessage("You lose!\n\nThe answer was:\n" + currWord);
            loseAlert.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity.this.playGame();
                }
            });

            loseAlert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity.this.finish();
                }
            });

            loseAlert.show();
        }
    }

    public void disableBtns() {
        int numLetters = letters.getChildCount();
        for(int j = 0; j < numLetters; j++) {
            letters.getChildAt(j);
        }
    }
}
