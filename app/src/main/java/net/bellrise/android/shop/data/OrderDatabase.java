package net.bellrise.android.shop.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OrderDatabase extends SQLiteOpenHelper
{
    /* Database info. */
    private static final String DB_NAME = "orders.sqlite";
    private static final int DB_VER = 2;

    /* This database only has 1 table, which contains the orders. Each row
       has a unique incremented ID, a user name, a phone number and all
       data about the order itself, which is conveniently stored as a JSON
       string (as an object) with an "items" array with all the stuff. */
    public static final String TABLE_NAME = "orders";
    public static final String COL_ID = "id";
    public static final String COL_USER = "user";
    public static final String COL_PHONE = "phone";
    public static final String COL_DATE = "date";
    public static final String COL_DATA = "data";

    public OrderDatabase(Context context)
    {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_USER + " TEXT NOT NULL,"
                + COL_PHONE + " TEXT NOT NULL,"
                + COL_DATE + " INTEGER NOT NULL,"
                + COL_DATA + " TEXT NOT NULL)"
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
