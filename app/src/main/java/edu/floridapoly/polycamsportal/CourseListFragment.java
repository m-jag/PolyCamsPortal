package edu.floridapoly.polycamsportal;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import edu.floridapoly.polycamsportal.Database.CourseItem;
import edu.floridapoly.polycamsportal.Database.DatabaseHelper;
import edu.floridapoly.polycamsportal.Database.UserItem;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CourseListFragment extends Fragment {
    private static final String TAG = "CourseListFragment";
    private RecyclerView mCourseRecyclerView;
    private CourseAdapter mAdapter;
    public static final String USERNAME = "USERNAME";
    public static final String SCHEDULENAME = "SCHEDULENAME";
    private String schedulename = "ScheduleName";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        UserItem currentUser = null;
        Bundle extras = this.getArguments();

        String uname = "";
        if (extras != null) {
            uname = extras.getString(USERNAME);
            currentUser = ((MainActivity) getActivity()).myDb.getUser(uname);
            schedulename = extras.getString(SCHEDULENAME);
        }

        // TODO: Pull User Schedules from DB
        if (currentUser == null)
        {
            //showMessage("Error", "No User (" + uname + ") found");
            // Logout
        }
        else {
            if (schedulename == null) {
                schedulename = "Schedule";
            }

            DatabaseHelper myDb = ((MainActivity) getActivity()).myDb;
            if (schedulename != "") {
                Cursor courseCursor = myDb.getCourseList(currentUser.getName(),
                    schedulename);
                if (courseCursor.getCount() == 0) {
                    //Pull schedule
                    //Else no schedule for user ¯\_(ツ)_/¯
                    //Registering is still being worked on screen?
                    Log.e(TAG, currentUser.getName() + " " + (currentUser.getFavoriteSchedule() != null ? currentUser
                        .getFavoriteSchedule(): "None"));

                    //showMessage("Error", "Nothing found");
                } else {
                    ArrayList<CourseItem> courses = new ArrayList<>();
                    while (courseCursor.moveToNext()) {
                        courses.add(new CourseItem(
                            courseCursor.getString(0), // name
                            courseCursor.getString(1),// starttime
                            courseCursor.getString(2), // endtime
                            courseCursor.getString(3), // professor
                            courseCursor.getString(4))); // room
                    }
                    mAdapter = new CourseAdapter(courses);
                }
            }
        }

        TextView title = (TextView) view.findViewById(R.id.schedule_title);
        title.setText(schedulename);

        mCourseRecyclerView = (RecyclerView) view.findViewById(R.id.courses_recycler_view);
        mCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        mCourseRecyclerView.setAdapter(mAdapter);
    }

    private class CourseHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        private CourseItem mCourse;

        private TextView mTimeTextView;
        private TextView mClassNameTextView;
        private TextView mRoomTextView;
        private TextView mProfessorTextView;

        public CourseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_schedule, parent, false));
            itemView.setOnClickListener(this);

            mTimeTextView = (TextView) itemView.findViewById(R.id.time);
            mClassNameTextView = (TextView) itemView.findViewById(R.id.class_name);
            mRoomTextView = (TextView) itemView.findViewById(R.id.room);
            mProfessorTextView = (TextView) itemView.findViewById(R.id.professor);
        }

        public void bind(CourseItem course) {
            mCourse = course;
            mCourse = course;
            mTimeTextView.setText(mCourse.getStartTime());
            mClassNameTextView.setText(mCourse.getTitle());
            mRoomTextView.setText(mCourse.getRoom());
            mProfessorTextView.setText(mCourse.getProfessor());

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),
                mCourse.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                .show();
        }
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> implements Observer {

        private ArrayList<CourseItem> mCourses;

        public CourseAdapter(ArrayList<CourseItem> courses) {
            mCourses = courses;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CourseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position) {
            CourseItem course = mCourses.get(position);
            holder.bind(course);
        }

        @Override
        public int getItemCount() {
            return mCourses.size();
        }

        @Override
        public void update(Observable o, Object courses) {
            mCourses = (ArrayList<CourseItem>) courses;
        }
    }
}
