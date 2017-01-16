package ch.dss.gadgeothek;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ch.dss.gadgeothek.service.Callback;
import ch.dss.gadgeothek.service.ItemSelectionListener;
import ch.dss.gadgeothek.service.LibraryService;

public class MainTabActivity extends AppCompatActivity implements ItemSelectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Toast toast = Toast.makeText(MainTabActivity.this, "Login erfolgreich!", Toast.LENGTH_SHORT);
        toast.show();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GadgetListFragment(), "Gadgets");
        adapter.addFragment(new LoanListFragment(), "Ausleihe");
        adapter.addFragment(new ReservationListFragment(), "Reservation");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                LibraryService.logout(new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        if (input) {
                            Intent intent = new Intent(MainTabActivity.this, MainActivity.class);
                            finish(); //beendet
                        } else {
                            Toast toast = Toast.makeText(MainTabActivity.this, "Logout fehlgeschlagen!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Toast toast = Toast.makeText(MainTabActivity.this, "Logout fehlgeschlagen!!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(int position) {
        Intent detailIntent = new Intent(this, SingleViewActivity.class);
        detailIntent.putExtra(SingleViewFragment.ARG_ITEM, position);
        startActivity(detailIntent);
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