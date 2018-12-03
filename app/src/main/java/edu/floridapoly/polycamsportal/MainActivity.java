package edu.floridapoly.polycamsportal;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
<<<<<<< Updated upstream
=======
import edu.floridapoly.polycamsportal.Database.CourseItem;
import edu.floridapoly.polycamsportal.Database.DatabaseHelper;
import edu.floridapoly.polycamsportal.Database.ScheduleItem;
import edu.floridapoly.polycamsportal.Database.UserItem;
>>>>>>> Stashed changes

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    public static final String USERNAME = "USERNAME";
    private String username = "Username";
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            username = extras.getString(USERNAME);
        }


        //Add filler data to the database
        ArrayList<CourseItem> courses = new ArrayList<CourseItem>();
        courses.add(new CourseItem("Mobile Device Applications", "08:00", "08:50", "1026", "Dr. Topsakal"));
        courses.add(new CourseItem("Computer Security", "09:00", "09:50","1032", "Dr. Al-Nashif"));
        courses.add(new CourseItem("Machine Learning", "10:00", "10:50","1002", "Dr. Samarah"));
        courses.add(new CourseItem("Network Security", "11:00", "11:50","1006", "Dr. Akbas"));
        courses.add(new CourseItem("Ethical Hacking", "12:00", "12:50","1014", "Dr. Patel"));
        for (CourseItem c: courses) {
            myDb.insertCourse(c);
        }

        UserItem user;
        //Add user
        if (!myDb.userExists(username))
        {
            user = new UserItem(username, null);
            myDb.insertUser(user);
            ScheduleItem sample_schedule = new ScheduleItem("Sample Schedule", user.getName());
            myDb.insertSchedule(sample_schedule);
            user = new UserItem(user.getName(), sample_schedule.getName());
            myDb.updateUser(user);
            for (CourseItem c: courses.subList(0, 5))
            {
                myDb.insertCourseList(user, sample_schedule, c);
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username_header);
        navUsername.setText(username);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new CourseListFragment();
        Bundle bundle = new Bundle();
        String favSchedule = "";
        if (myDb.getUser(username) != null)
        {
            favSchedule = myDb.getUser(username).getFavoriteSchedule();
        }
        bundle.putString(CourseListFragment.USERNAME, username);
        bundle.putString(CourseListFragment.SCHEDULENAME, favSchedule);
        fragment.setArguments(bundle);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
            navigationView.setCheckedItem(R.id.nav_schedule);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        Bundle bundle;
        switch (item.getItemId())
        {
            case R.id.nav_schedules:
                fragment = new SchedulesFragment();
                bundle = new Bundle();
                bundle.putString(SchedulesFragment.USERNAME, username);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
                break;
            case R.id.nav_schedule:
                fragment = new CourseListFragment();
                bundle = new Bundle();
                // TODO: Modify to be favorite schedule
                String favSchedule = "";
                if (myDb.getUser(username) != null)
                {
                    favSchedule = myDb.getUser(username).getFavoriteSchedule();
                }
                bundle.putString(CourseListFragment.USERNAME, username);
                bundle.putString(CourseListFragment.SCHEDULENAME, favSchedule);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new SettingsFragment())
                    .commit();
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra(LoginActivity.ACTION, LoginActivity.LOGOUT);
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }  else {
            super.onBackPressed();
        }
    }
}
