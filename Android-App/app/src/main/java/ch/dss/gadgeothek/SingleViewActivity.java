package ch.dss.gadgeothek;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ch.dss.gadgeothek.service.Callback;
import ch.dss.gadgeothek.service.LibraryService;

public class SingleViewActivity extends AppCompatActivity implements SingleViewFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        Bundle arguments = new Bundle();
        int positionToShow = getIntent().getIntExtra(SingleViewFragment.ARG_ITEM, -1);
        arguments.putInt(SingleViewFragment.ARG_ITEM, positionToShow);

        SingleViewFragment fragment = new SingleViewFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.itemDetailContainer, fragment)
                .commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                LibraryService.logout(new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        if (input) {
                            Intent intent = new Intent(SingleViewActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(SingleViewActivity.this, "Logout Fehlgeschlagen!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Toast toast = Toast.makeText(SingleViewActivity.this, "Logout Fehlgeschlagen!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
