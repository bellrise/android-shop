package net.bellrise.android.shop.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.bellrise.android.shop.Global;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

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

    public boolean newUser(String username, String passwd)
    {
        ContentValues vals = new ContentValues();
        String hash = sha256(passwd);

        Cursor res = getReadableDatabase().query(PasswdDatabase.TABLE_NAME,
                new String[]{"username"}, null, null, null, null, null);

        while (res.moveToNext()) {
            /* If such a user already exists, don't add another one. */
            if (username.equals(res.getString(0)))
                return false;
        }

        res.close();

        Log.i(Global.TAG, String.format(Locale.getDefault(), "new user=%s hash=%s",
                username, hash));

        vals.put(PasswdDatabase.COL_USERNAME, username);
        vals.put(PasswdDatabase.COL_PASSWD, hash);

        getWritableDatabase().insert(PasswdDatabase.TABLE_NAME, null, vals);
        return true;
    }

    public static String sha256(String str)
    {
        MessageDigest sha;
        StringBuilder res;
        byte[] buf;

        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "<failed>";
        }

        buf = sha.digest(str.getBytes(StandardCharsets.UTF_8));
        res = new StringBuilder();
        for (byte b : buf)
            res.append(String.format(Locale.getDefault(), "%02x", b));

        return res.toString();
    }
}
