package net.bellrise.android.shop.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PasswdDatabase extends SQLiteOpenHelper
{
    /* Database info. */
    private static final String DB_NAME = "passwd.sqlite";
    private static final int DB_VER = 1;

    /* Store data in the passwd database as */
    public static final String TABLE_NAME = "passwd";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWD = "passwd";

    public PasswdDatabase(Context context)
    {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_USERNAME + " TEXT NOT NULL,"
                + COL_PASSWD + " TEXT NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int old_ver, int new_ver)
    {
        onUpgrade(db, old_ver, new_ver);
    }
}
