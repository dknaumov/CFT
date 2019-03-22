package com.cft.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cft.calculator.parser.Parser;


import java.util.HashSet;


public class MainActivity extends AppCompatActivity {

    final static String reserve = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onSaveInstanceState(Bundle outState) {
        TextView Save = (TextView) findViewById(R.id.input);
        outState.putString(reserve,Save.getText().toString());
        super.onSaveInstanceState(outState);
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String Saving = savedInstanceState.getString(reserve);
        TextView nameView = (TextView) findViewById(R.id.input);
        nameView.setText(Saving);
    }

    public void print(View view) {
        HashSet<String> finalStates = new HashSet<>();
        finalStates.add("Infinity");
        finalStates.add("Whoops");

        TextView input = findViewById(R.id.input);
        Button button = (Button) view;
        String text = input.getText().toString();
        if (finalStates.contains(text)) {
            text = "";
        }
        text += button.getText().toString();
        input.setText(text);
    }

    public void clear(View view) {
        TextView input = findViewById(R.id.input);
        String text = input.getText().toString();
        input.setText(text.length() > 0 ? text.substring(0, text.length() - 1) : text);
    }

    public void clearAll(View view) {
        TextView input = findViewById(R.id.input);
        input.setText("");
    }

    public void compute(View view) throws Exception {
        TextView input = findViewById(R.id.input);
        String result = null;
        try {
            Parser parser = new Parser(input.getText().toString());
            result = parser.parse().toString();
            if (result.charAt(result.length() - 2) == '.' &&
                    result.charAt(result.length() - 1) == '0') {
                result = result.substring(0, result.length() - 2);
            }
            input.setText(result);
        }
        catch (Exception e) {
            Toast toast = Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
//            throw e;
        }
    }
}
