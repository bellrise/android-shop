package net.bellrise.android.shop.views;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.bellrise.android.shop.R;
import net.bellrise.android.shop.data.PasswdDatabase;

import java.util.Locale;

public class AddUser extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        EditText username, passwd;
        PasswdDatabase db;
        Button button;

        username = findViewById(R.id.add_user_username);
        passwd = findViewById(R.id.add_user_passwd);
        button = findViewById(R.id.add_user_button);
        db = new PasswdDatabase(this);

        button.setOnClickListener(view -> {
            if (!areFieldsValid(username, passwd))
                return;

            if (db.newUser(username.getText().toString(), passwd.getText().toString())) {
                Toast.makeText(this, getString(R.string.added_user), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, String.format(Locale.getDefault(), "%s %s",
                        username.getText().toString(), getString(R.string.already_exists)),
                        Toast.LENGTH_SHORT).show();
                inputSetRed(username);
            }
        });
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