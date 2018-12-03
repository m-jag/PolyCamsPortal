package edu.floridapoly.polycamsportal;


import android.content.Intent;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import edu.floridapoly.polycamsportal.Database.CourseItem;
import edu.floridapoly.polycamsportal.Database.DatabaseHelper;
import edu.floridapoly.polycamsportal.Database.ScheduleItem;
import edu.floridapoly.polycamsportal.Database.UserItem;
import edu.floridapoly.polycamsportal.schedule.Course;
import edu.floridapoly.polycamsportal.schedule.CourseFetcher;
import edu.floridapoly.polycamsportal.schedule.CourseSession;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    public static final String USERNAME = "USERNAME";
    private String username = "Username";
    private DrawerLayout drawer;

    public final DatabaseHelper myDb = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            username = extras.getString(USERNAME);
        }

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                List<Course> fetched;
                try {
                    fetched = new CourseFetcher().fetchCourses(28);
                    Log.d(TAG, "Fetched " + fetched.size() + " courses");
                } catch (IOException ex) {
                    Log.e(TAG, "Failed to fetch courses", ex);
                    return null;
                }

                ArrayList<CourseItem> courses = new ArrayList<>();
                for (Course c : fetched) {
                    CourseSession s = c.getSections().get(0).getSessions().get(0);
                    courses.add(new CourseItem(c.getTitle(), s.getStartTime().toString(), s.getEndTime().toString(),
                        s.getInstructor(), s.getRoom()));
                }

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

                return null;
            }
        }.execute();

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
