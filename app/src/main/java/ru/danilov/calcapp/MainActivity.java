package ru.danilov.calcapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.inputs)
    EditText inputs;
    @Bind(R.id.answer)
    TextView answer;
    @Bind(R.id.go_button)
    Button goButton;
    MatchParser parser;

    @OnClick(R.id.go_button)
    void calculate() {
        String exp = inputs.getText().toString();
        try {
            if (parser.isDirectNote(exp)) {
                try {
                    answer.setText(String.valueOf(parser.Parse(exp)));
                } catch (Exception e) {
                    answer.setText(e.getMessage());
                }
            }
            else {
                try {
                    answer.setText(String.valueOf(parser.ParseRPN(exp)));
                } catch (Exception e) {
                    answer.setText(e.getMessage());
                }
            }
        } catch (Exception e) {
            answer.setText(e.getMessage());
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parser = new MatchParser();
        ButterKnife.bind(this);
    }

}
