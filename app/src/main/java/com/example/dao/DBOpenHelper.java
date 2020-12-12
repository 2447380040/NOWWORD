package com.example.dao;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = DBOpenHelper.class.getSimpleName();

    private final Context context;
    private final String assetPath="now_word.db";
    private final String dbPath;
    private static final String dbName="now_word.db";

    public DBOpenHelper(Context context) {
        super(context, dbName, null, 1);
        this.context = context;
        this.dbPath = "/data/data/"
                + context.getApplicationContext().getPackageName() + "/databases/"
                + dbName;
        checkExists();
    }

    /**
     * Checks if the database asset needs to be copied and if so copies it to the
     * default location.
     *
     * @throws IOException
     */
    private void checkExists() {
        try {
            Log.i(TAG, "checkExists()");

            File dbFile = new File(dbPath);

            if (!dbFile.exists()) {

                Log.i(TAG, "creating database..");

                dbFile.getParentFile().mkdirs();
                copyStream(context.getAssets().open(assetPath), new FileOutputStream(dbFile));
                Log.i(TAG, assetPath + " has been copied to " + dbFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void copyStream(InputStream is, OutputStream os)  {
        try {
            byte buf[] = new byte[1024];
            int c = 0;
            while (true) {
                c = is.read(buf);
                if (c == -1)  break;
                os.write(buf, 0, c);
            }
            is.close();
            os.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
