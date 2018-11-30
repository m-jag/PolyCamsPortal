package edu.floridapoly.polycamsportal;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import edu.floridapoly.polycamsportal.Database.ScheduleItem;

import java.util.ArrayList;

public class SchedulesFragment extends Fragment {

    private static final String TAG = "MainActivity";
    public static final String USERNAME = "USERNAME";
    private String username = "Username";

    private RecyclerView mScheduleRecyclerView;
    private SchedulesFragment.ScheduleAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedules, container, false);

        Bundle extras = this.getArguments();

        if (extras != null) {
            username = extras.getString(USERNAME);
        }

        // TODO: Pull User Schedules from DB

        mScheduleRecyclerView = (RecyclerView) view.findViewById(R.id.schedules_recycler_view);
        mScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        ArrayList<ScheduleItem> schedules = new ArrayList<ScheduleItem>();
        schedules.add(new ScheduleItem("Schedule 1", "user"));
        schedules.add(new ScheduleItem("Schedule 2", "user"));

        mAdapter = new SchedulesFragment.ScheduleAdapter(schedules);
        mScheduleRecyclerView.setAdapter(mAdapter);
    }

    private class ScheduleHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        private ScheduleItem mSchedule;

        private TextView mScheduleTextView;
        //private ImageButton mEditScheduleBtn;

        public ScheduleHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.schedule_item, parent, false));
            itemView.setOnClickListener(this);

            mScheduleTextView = (TextView) itemView.findViewById(R.id.schedule_item_name);
        }

        public void bind(ScheduleItem schedule) {
            mSchedule = schedule;
            mScheduleTextView.setText(mSchedule.getTitle());

        }

        @Override
        public void onClick(View view) {
            Fragment fragment = new CourseListFragment();
            Bundle bundle = new Bundle();
            // TODO: Modify to be favorite schedule
            String favSchedule = "schedule";
            bundle.putString(CourseListFragment.USERNAME, username);
            bundle.putString(CourseListFragment.SCHEDULENAME, favSchedule);
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
            Toast.makeText(getActivity(),
                mSchedule.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                .show();
        }
    }

    private class ScheduleAdapter extends RecyclerView.Adapter<SchedulesFragment.ScheduleHolder> {

        private ArrayList<ScheduleItem> mSchedules;

        public ScheduleAdapter(ArrayList<ScheduleItem> schedules) {
            mSchedules = schedules;
        }

        @Override
        public SchedulesFragment.ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SchedulesFragment.ScheduleHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SchedulesFragment.ScheduleHolder holder, int position) {
            ScheduleItem schedule = mSchedules.get(position);
            holder.bind(schedule);
        }

        @Override
        public int getItemCount() {
            return mSchedules.size();
        }
    }
}
