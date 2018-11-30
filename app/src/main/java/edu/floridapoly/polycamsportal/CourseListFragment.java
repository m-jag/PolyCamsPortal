package edu.floridapoly.polycamsportal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import edu.floridapoly.polycamsportal.Database.CourseItem;

import java.util.ArrayList;

public class CourseListFragment extends Fragment {
    private RecyclerView mCourseRecyclerView;
    private CourseAdapter mAdapter;
    public static final String USERNAME = "USERNAME";
    private String username = "Username";
    public static final String SCHEDULENAME = "SCHEDULENAME";
    private String schedulename = "ScheduleName";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        Bundle extras = this.getArguments();

        if (extras != null) {
            username = extras.getString(USERNAME);
            schedulename = extras.getString(SCHEDULENAME);
        }

        // TODO: Pull User Schedules from DB

        TextView title = (TextView) view.findViewById(R.id.schedule_title);
        title.setText("Schedule");

        mCourseRecyclerView = (RecyclerView) view.findViewById(R.id.courses_recycler_view);
        mCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        ArrayList<CourseItem> courses = new ArrayList<>();
        courses.add(new CourseItem("Mobile Device Applications", "08:00", "08:00", "1026", "Dr. Topsakal"));
        courses.add(new CourseItem("Computer Security", "02:00", "08:00","1032", "Dr. Al-Nashif"));
        courses.add(new CourseItem("Machine Learning", "03:00", "08:00","1002", "Dr. Samarah"));
        courses.add(new CourseItem("Network Security", "04:00", "08:00","1006", "Dr. Akbas"));
        courses.add(new CourseItem("Ethical Hacking", "05:00", "08:00","1014", "Dr. Patel"));

        mAdapter = new CourseAdapter(courses);
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

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {

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
    }
}
