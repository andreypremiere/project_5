package com.example.word;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import database.DatabaseHelper;

public class AddWordActivity extends AppCompatActivity {

    private EditText field_eng_word;
    private EditText field_tr_word;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        Button mainActivity = findViewById(R.id.button3);
        mainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем интент для перехода на AddActivity
                Intent intent = new Intent(AddWordActivity.this, MainActivity.class);
                startActivity(intent); // Начинаем новую активити
                dbHelper.close();
            }
        });

        field_eng_word = findViewById(R.id.field_eng_word);
        field_tr_word = findViewById(R.id.field_tr_word);
        dbHelper = new DatabaseHelper(this);

        Button addWordButton = findViewById(R.id.add_word_2);
        addWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWordToDatabase();
            }
        });
    }

    private void addWordToDatabase() {
        String word = field_eng_word.getText().toString().trim();
        String translation = field_tr_word.getText().toString().trim();

        if (!word.isEmpty() && !translation.isEmpty()) {
            Boolean result = dbHelper.insertData(word, translation);
            if (result == Boolean.TRUE) {
                Toast.makeText(this, "Слово добавлено", Toast.LENGTH_SHORT).show();
                field_eng_word.getText().clear();
                field_tr_word.getText().clear();
            } else {
                Toast.makeText(this, "Ошибка при добавлении слова", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Пожалуйста, введите слово и его перевод", Toast.LENGTH_SHORT).show();
        }
    }

}