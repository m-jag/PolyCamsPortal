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

import java.util.ArrayList;

public class CourseListFragment extends Fragment {
    private RecyclerView mCourseRecyclerView;
    private CourseAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        TextView title = (TextView) view.findViewById(R.id.schedule_title);
        title.setText("Schedule");

        mCourseRecyclerView = (RecyclerView) view.findViewById(R.id.courses_recycler_view);
        mCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        ArrayList<ScheduleItem> courses = new ArrayList<>();
        courses.add(new ScheduleItem("Mobile Device Applications", "08:00", "1026", "Dr. Topsakal"));
        courses.add(new ScheduleItem("Computer Security", "02:00", "1032", "Dr. Al-Nashif"));
        courses.add(new ScheduleItem("Machine Learning", "03:00", "1002", "Dr. Samarah"));
        courses.add(new ScheduleItem("Network Security", "04:00", "1006", "Dr. Akbas"));
        courses.add(new ScheduleItem("Ethical Hacking", "05:00", "1014", "Dr. Patel"));

        mAdapter = new CourseAdapter(courses);
        mCourseRecyclerView.setAdapter(mAdapter);
    }

    private class CourseHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        private ScheduleItem mCourse;

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

        public void bind(ScheduleItem course) {
            mCourse = course;
            mCourse = course;
            mTimeTextView.setText(mCourse.getTime());
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

        private ArrayList<ScheduleItem> mCourses;

        public CourseAdapter(ArrayList<ScheduleItem> courses) {
            mCourses = courses;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CourseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position) {
            ScheduleItem course = mCourses.get(position);
            holder.bind(course);
        }

        @Override
        public int getItemCount() {
            return mCourses.size();
        }
    }
}
