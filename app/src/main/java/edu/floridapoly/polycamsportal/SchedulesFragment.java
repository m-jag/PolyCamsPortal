package edu.floridapoly.polycamsportal;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import edu.floridapoly.polycamsportal.Database.CourseItem;
import edu.floridapoly.polycamsportal.Database.ScheduleItem;
import edu.floridapoly.polycamsportal.Database.UserItem;

import java.util.ArrayList;

public class SchedulesFragment extends Fragment {

    private static final String TAG = "SchedulesFragment";
    public static final String USERNAME = "USERNAME";
    private String username = "Username";

    private RecyclerView mScheduleRecyclerView;
    private SchedulesFragment.ScheduleAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedules, container, false);

        Bundle extras = this.getArguments();
        UserItem currentUser = null;

        if (extras != null) {
            username = extras.getString(USERNAME);
            currentUser = ((MainActivity) getActivity()).myDb.getUser(username);
        }

        Cursor scheduleCursor = ((MainActivity) getActivity()).myDb.getSchedulesUser(currentUser.getName());
        if (scheduleCursor.getCount() == 0) {
            //Pull schedule
            //Else no schedule for user ¯\_(ツ)_/¯
            //Registering is still being worked on screen?
            Log.e(TAG, currentUser.getName());

            showMessage("Error", "Nothing found");
        } else {
            ArrayList<ScheduleItem> schedules = new ArrayList<>();
            StringBuffer buffer = new StringBuffer();
            while (scheduleCursor.moveToNext()) {
                schedules.add(new ScheduleItem(
                    scheduleCursor.getString(2), // title
                    scheduleCursor.getString(1))); // user
            }
            mAdapter = new SchedulesFragment.ScheduleAdapter(schedules);
        }

        mScheduleRecyclerView = (RecyclerView) view.findViewById(R.id.schedules_recycler_view);
        mScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
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
            String selectedSchedule = mSchedule.getTitle();
            bundle.putString(CourseListFragment.USERNAME, username);
            bundle.putString(CourseListFragment.SCHEDULENAME, selectedSchedule);
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

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
