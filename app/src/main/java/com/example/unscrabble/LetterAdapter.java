package com.example.unscrabble;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class LetterAdapter extends BaseAdapter {
    private ArrayList<String> letters;
    private LayoutInflater letterInflater;

    public LetterAdapter(Context c, ArrayList<String> objs) {
        letters = objs;
        letterInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create a button for the letter at this position in the alphabet
        Button letterBtn;
        letterBtn = (Button) letterInflater.inflate(R.layout.letter, parent, false);

        letterBtn.setTextColor(Color.WHITE);
        // set the text to this letter
        letterBtn.setText(letters.get(position));
        return letterBtn;
    }
}
