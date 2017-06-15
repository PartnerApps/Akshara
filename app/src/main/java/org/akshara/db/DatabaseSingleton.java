package org.akshara.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseSingleton {
    private static volatile DatabaseSingleton instance = null;
    private SQLiteDatabase mReadabledb;
    private SQLiteDatabase mWritabledb;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseSingleton(Context context) {
        mWritabledb = new DatabaseHandler(context).getWritableDatabase();
        mReadabledb = new DatabaseHandler(context).getReadableDatabase();
    }

    /**
     * Returns instance of DatabaseSingleton class
     *
     * @param context
     * @return Instance of the DatabaseSingleton
     */
    public static DatabaseSingleton getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseSingleton.class) {
                if (instance == null) {
                    instance = new DatabaseSingleton(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * Get current instance of SQLiteDatabase
     *
     * @return SQLiteDatabase
     */
    public SQLiteDatabase getWriteableDB() {
        return this.mWritabledb;
    }

    public SQLiteDatabase getReadableDb() {
        return this.mReadabledb;
    }
//	public static void close() {
//		if (singleton != null) {
//			singleton.databaseHandler.close();
//		}
//	}

}
