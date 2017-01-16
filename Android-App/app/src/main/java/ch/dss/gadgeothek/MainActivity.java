package ch.dss.gadgeothek;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Stack;

import ch.dss.gadgeothek.service.Callback;
import ch.dss.gadgeothek.service.LibraryService;


public class MainActivity extends AppCompatActivity implements StepLoginFragment.Login, StepRegFragment.Register {
    SharedPreferences globalSettings;
    private FragmentManager fragmentManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        switchTo(new StepLoginFragment());
        SharedPreferences settings = getPreferences(0);
        String server = settings.getString("serveradress", "http://10.0.2.2:8080/public");
        String mServer = "http://10.0.2.2:8080/public";
        globalSettings = settings;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //This Method allows us easily switch fragments
    public void switchTo(Fragment fragment) {
        Bundle args = new Bundle();
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //This Method allows to set the Server in a SharedPreference (Permanent)
    public void changeServer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Server Adresse");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        input.setText(globalSettings.getString("serveradress", "http://10.0.2.2:8080/public"));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences settings = getPreferences(0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("serveradress", input.getText().toString());
                editor.commit();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //This Method implements the login procedure
    public void login(String mail, String password) {
        LibraryService.setServerAddress(globalSettings.getString("serveradress", "http://10.0.2.2:8080/public"));

        LibraryService.login(mail, password, new Callback<Boolean>() {
            @Override
            public void onCompletion(Boolean input) {
                if (input) {
                    emptyBackStack();
                    Intent intent = new Intent(MainActivity.this, MainTabActivity.class);
                    startActivity(intent);

                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "Login Fehlgeschlagen!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onError(String message) {
                Toast toast = Toast.makeText(MainActivity.this, "Password oder Username falsch!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    //This Method implements the register procedure
    public void register(String mail, String password, String name, String studentenNumber) {
        LibraryService.setServerAddress(globalSettings.getString("serveradress", "http://10.0.2.2:8080/public"));
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email = mail.trim();
        if ((!email.matches(emailPattern)) || mail.length() < 1) {
            Toast toast = Toast.makeText(MainActivity.this, "E-Mail ungültig!", Toast.LENGTH_SHORT);
            toast.show();
        } else if (studentenNumber.length()<1) {
            Toast toast = Toast.makeText(MainActivity.this, "Matrikelnummer ungültig!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            LibraryService.register(mail, password, name, studentenNumber, new Callback<Boolean>() {
                @Override
                public void onCompletion(Boolean input) {
                    if (input) {
                        // Jetzt sind wir eingeloggt, activity aufrufen
                        Toast toast = Toast.makeText(MainActivity.this, "Registration erfolgreich!", Toast.LENGTH_SHORT);
                        toast.show();
                        switchTo(new StepLoginFragment());
                    } else {
                        Toast toast = Toast.makeText(MainActivity.this, "Registration fehlgeschlagen!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

                @Override
                public void onError(String message) {
                    Toast toast = Toast.makeText(MainActivity.this, "Registration fehlgeschlagen!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    private void emptyBackStack() {
        FragmentManager backstack = getSupportFragmentManager();
        while (backstack.getBackStackEntryCount() > 0) {
            backstack.popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack(); // nimmt die letzte Transaction vom Stack
        }
    }
}
