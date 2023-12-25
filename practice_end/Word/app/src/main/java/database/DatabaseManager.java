package database;

import android.content.Context;

public class DatabaseManager {
    private static DatabaseHelper dbHelper;

    public static synchronized DatabaseHelper getDbHelper(Context context) {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context.getApplicationContext());
        }
        return dbHelper;
    }
}
