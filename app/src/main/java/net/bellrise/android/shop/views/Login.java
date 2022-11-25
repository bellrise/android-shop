package net.bellrise.android.shop.views;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.bellrise.android.shop.Global;
import net.bellrise.android.shop.R;
import net.bellrise.android.shop.data.PasswdDatabase;

import java.util.Locale;


public class Login extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PasswdDatabase db = new PasswdDatabase(this);
        EditText username, passwd;
        Button login;

        db.newUser("admin", "admin");

        username = findViewById(R.id.login_username);
        passwd = findViewById(R.id.login_passwd);
        login = findViewById(R.id.login_button);

        login.setOnClickListener(view -> {
            if (!areFieldsValid(username, passwd))
                return;

            if (loginUser(username, passwd)) {
                Intent main = new Intent(this, Order.class);
                main.putExtra("username", username.getText().toString());
                startActivity(main);
                finish();
            } else {
                inputSetRed(username);
                inputSetRed(passwd);
                Toast.makeText(this, getString(R.string.wrong_user_or_passwd),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean loginUser(EditText _username, EditText _passwd)
    {
        String username = _username.getText().toString();
        String passwd = _passwd.getText().toString();
        String hash = PasswdDatabase.sha256(passwd);

        Log.i(Global.TAG, String.format(Locale.getDefault(), "sha256('%s') = '%s'",
                passwd, hash));

        /* Check the database for the user. */
        PasswdDatabase db = new PasswdDatabase(this);
        Cursor res = db.getReadableDatabase().query(PasswdDatabase.TABLE_NAME,
                new String[]{"username", "passwd"}, null, null, null, null, null);

        while (res.moveToNext()) {
            if (!username.equals(res.getString(0)))
                continue;

            if (hash.equals(res.getString(1))) {
                res.close();
                return true;
            }
        }

        res.close();
        return false;
    }

    private void inputSetRed(EditText input)
    {
        input.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
    }

    private boolean areFieldsValid(EditText username, EditText passwd)
    {
        if (username.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.missing_name), Toast.LENGTH_SHORT).show();
            inputSetRed(username);
            return false;
        }

        if (passwd.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.missing_passwd), Toast.LENGTH_SHORT).show();
            inputSetRed(passwd);
            return false;
        }

        return true;
    }
}