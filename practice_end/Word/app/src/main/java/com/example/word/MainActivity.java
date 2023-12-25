package com.example.word;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private TextView showWordTextView;
    private EditText inputWordEditText;
    private DatabaseHelper dbHelper = new DatabaseHelper(this);
    private Cursor cursor;
    private int currentWordIndex = 0; // для отслеживания текущего слова

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper.insertData("Hello", "Привет");
        dbHelper.insertData("Goodbye", "До свидания");
        dbHelper.insertData("Thank you", "Спасибо");

        Button addButton = findViewById(R.id.button2);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем интент для перехода на AddActivity
                Intent intent = new Intent(MainActivity.this, AddWordActivity.class);
                startActivity(intent); // Начинаем новую активити
                dbHelper.close();
            }
        });

        showWordTextView = findViewById(R.id.show_word);
        inputWordEditText = findViewById(R.id.input_word);

        cursor = dbHelper.getAllWords(); // Метод, который возвращает курсор со всеми словами из базы
        if (cursor != null && cursor.moveToFirst()) {
            int wordColumnIndex = cursor.getColumnIndex("word");
            showWordTextView.setText(cursor.getString(wordColumnIndex));
        }
    }


    public void onCheckValue(View v) {
        String inputWord = inputWordEditText.getText().toString().trim();
        String currentWord = showWordTextView.getText().toString();
        String translation = getTranslationForCurrentWordFromDatabase();

        if (inputWord.equalsIgnoreCase(translation)) {
            // Переход к следующему слову в базе данных
            if (cursor.moveToNext()) {
                int wordColumnIndex = cursor.getColumnIndex("word");
                showWordTextView.setText(cursor.getString(wordColumnIndex));
            } else {
                cursor.moveToFirst(); // Если достигнут конец списка, вернуться к началу
                int wordColumnIndex = cursor.getColumnIndex("word");
                showWordTextView.setText(cursor.getString(wordColumnIndex));
            }
            inputWordEditText.getText().clear(); // Очистка поля ввода
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Неправильный перевод")
                    .setPositiveButton("ОК", null); // Просто кнопка "ОК" для закрытия окна

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private String getTranslationForCurrentWordFromDatabase() {
        // Получаем перевод текущего слова из базы данных (замените это на вашу реализацию запроса к базе)
        // Предположим, у вас есть DBHelper dbHelper и cursor, который уже указывает на текущее слово

        if (cursor != null) {
            int translationColumnIndex = cursor.getColumnIndex("translate");
            if (translationColumnIndex != -1) {
                return cursor.getString(translationColumnIndex);
            }
        }
        return ""; // Если что-то пошло не так, вернем пустую строку
    }

    @Override
    protected void onPause() {
        // Сохранение текущего слова или его индекса в SharedPreferences или в базе данных
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("currentWordIndex", cursor.getPosition());
        editor.apply();

        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Восстановление состояния при повторном входе
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        currentWordIndex = prefs.getInt("currentWordIndex", 0);

        // Перемещение курсора к сохраненному слову
        if (cursor != null && cursor.moveToPosition(currentWordIndex)) {
            int wordColumnIndex = cursor.getColumnIndex("word");
            showWordTextView.setText(cursor.getString(wordColumnIndex));
        }
    }

    @Override
    protected void onDestroy() {
        // Закрытие курсора и базы данных при уничтожении активности
        if (cursor != null) {
            cursor.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}