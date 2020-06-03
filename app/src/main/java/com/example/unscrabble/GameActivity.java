package com.example.unscrabble;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
    ArrayList<String> wordSplitList, correctAns, testArr;
    TextView tvLives;

    private int currPart;
    private int numChars;
    private int numCorr;
    int marker = 0;
    String strLives = "";
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

        tvLives = findViewById(R.id.textViewLives);
        strLives = "You have 3 lives remaining.";
        tvLives.setText(strLives);

        wordLayout = findViewById(R.id.word);
        letters = findViewById(R.id.letters);

        testArr = new ArrayList<>();
        ltrAdapt = new LetterAdapter(this, testArr);
        letters.setAdapter(ltrAdapt);

        playGame();
    }

    private void playGame() {
        strLives = "You have 3 lives remaining.";
        marker = 0;

        int arrayCount = words.size();
        int randomInt = random.nextInt(arrayCount);
        String newWord = words.get(randomInt).getAnswer();

        while (newWord.equals(currWord)) newWord = words.get(randomInt).getAnswer();
        currWord = newWord;
        imageView.setImageResource(words.get(randomInt).getImage());

        charViews = new TextView[currWord.length()];
        wordLayout.removeAllViews();

        // create the "underscores" for each letters
        for (int c = 0; c < currWord.length(); c++) {
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

        String[] wordSplit = currWord.split("");
        wordSplitList = new ArrayList<>(Arrays.asList(wordSplit));
        System.out.println(wordSplitList);
        correctAns = new ArrayList<>(wordSplitList);
        Collections.shuffle(wordSplitList);

        // use arraylist
        // adapter should be created once
        testArr.clear();
        testArr.addAll(wordSplitList);

        ltrAdapt.notifyDataSetChanged();

        currPart = 0;
        numChars = currWord.length();
        numCorr = 0;
    }

    public void letterPressed(View v) {
        String ltr = ((TextView) v).getText().toString();
        int ltrIndex = wordSplitList.indexOf(ltr);
        System.out.println(ltr);
        System.out.println(ltrIndex);


        boolean correct = false;

        Log.d("debug", ltr + " " + correctAns.get(marker));
        if (ltr.equals(correctAns.get(marker))) {
            correct = true;
            numCorr++;
            charViews[marker].setTextColor(Color.BLACK);

            v.setEnabled(false);
            v.setBackgroundResource(R.drawable.letter_down);

            marker += 1;
        }

        int numParts = 2;
        // add image to show number of lives
        if (correct) {
            if (numCorr == numChars) {
                disableBtns();

                AlertDialog.Builder winAlert = new AlertDialog.Builder(this);
                winAlert.setCancelable(false);
                winAlert.setTitle("YAY");
                winAlert.setMessage("You win!\n\nThe answer was:\n" + currWord);
                winAlert.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity.this.playGame();
                        strLives = "You have 3 tries remaining";
                        tvLives.setText(strLives);
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
        } else if (currPart < numParts) {
            currPart++;
            if(currPart == 1) {
                strLives = "You have 2 lives remaining.";
            } else if(currPart == 2) {
                strLives = "You have 1 life remaining.";
            }
            tvLives.setText(strLives);
        } else {
            disableBtns();

            strLives = "You are out of tries";
            tvLives.setText(strLives);

            AlertDialog.Builder loseAlert = new AlertDialog.Builder(this);
            loseAlert.setCancelable(false);
            loseAlert.setTitle("OOPS");
            loseAlert.setMessage("You lose!\n\nThe answer was:\n" + currWord);
            loseAlert.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity.this.playGame();
                    strLives = "You have 3 lives remaining.";
                    tvLives.setText(strLives);
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
        for (int j = 0; j < numLetters; j++) {
            letters.getChildAt(j);
        }
    }
}