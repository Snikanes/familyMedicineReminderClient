package com.example.sondrehj.familymedicinereminderclient.adapters;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sondrehj.familymedicinereminderclient.MainActivity;
import com.example.sondrehj.familymedicinereminderclient.R;
import com.example.sondrehj.familymedicinereminderclient.database.MySQLiteHelper;
import com.example.sondrehj.familymedicinereminderclient.fragments.DashboardListFragment.OnDashboardListFragmentInteractionListener;
import com.example.sondrehj.familymedicinereminderclient.models.Reminder;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<DashboardRecyclerViewAdapter.ViewHolder> {

    private final List<Reminder> mValues;
    private final OnDashboardListFragmentInteractionListener mListener;
    private final Context context;

    public DashboardRecyclerViewAdapter(
            Context context,
            List<Reminder> mValues,
            OnDashboardListFragmentInteractionListener mListener) {
        this.mValues = mValues;
        this.mListener = mListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_dashboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mReminder = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());

        GregorianCalendar cal = mValues.get(position).getDate();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        String timeString = String.format("%02d:%02d", hour, min);
        holder.mDateTimeView.setText(timeString);

        //set medicineicon if medicine is attached
        if (holder.mReminder.getMedicine() != null) {
            // Set the icon
            switch (holder.mReminder.getMedicine().getUnit()) {
                case "mg":
                case "pill":
                case "mcg":
                case "g":
                    holder.mDashboardIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.android_pill));
                    break;
                case "inhalation":
                    holder.mDashboardIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.android_lungs));
                    break;
                case "ml":
                case "unit":
                    holder.mDashboardIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.android_bottle));
                    break;
            }
        } else {
            holder.mDashboardIcon.setImageDrawable(
                    context.getResources().getDrawable(R.drawable.android_appointment_reminder));
        }

        Account account = MainActivity.getAccount(context);

        if (account.name.equals(holder.mReminder.getOwnerId())) { //this account owns the reminders.
            if (holder.mReminder.getTimeTaken() == null) {
                if (holder.mReminder.getMedicine() == null) {
                    holder.mButton.setText("Mark as \n done");
                    holder.mButton.setBackgroundResource(R.drawable.mark_as_taken_button_shape);
                } else {
                    holder.mButton.setText("Mark as taken");
                    holder.mButton.setBackgroundResource(R.drawable.mark_as_taken_button_shape);
                }
            } else {
                System.out.println("taken");
                Calendar calendar = holder.mReminder.getTimeTaken();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                String time = String.format("%02d:%02d", hours, minutes);
                holder.mButton.setText("✓ Taken \n" + time);
                holder.mButton.setBackgroundResource(R.drawable.taken_button_shape);
            }

            holder.mButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    holder.mReminder.setTimeTaken(gregorianCalendar);
                    int hour = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
                    int minute = gregorianCalendar.get(Calendar.MINUTE);
                    String time = String.format("%02d:%02d", hour, minute);
                    if (holder.mReminder.getMedicine() == null) {
                        holder.mButton.setText("✓ Done \n" + time);
                    }
                    holder.mButton.setText("✓ Taken \n" + time);
                    holder.mButton.setBackgroundResource(R.drawable.taken_button_shape);
                    MySQLiteHelper db = new MySQLiteHelper(context);
                    db.setReminderTimeTaken(holder.mReminder);
                }
            });
        } else { //this account does not own the reminders.
            if (holder.mReminder.getTimeTaken() == null) {
                if (holder.mReminder.getMedicine() == null) {
                    holder.mButton.setText("Not done");
                    holder.mButton.setBackgroundResource(R.drawable.mark_as_taken_button_shape);
                } else {
                    holder.mButton.setText("Not taken");
                    holder.mButton.setBackgroundResource(R.drawable.mark_as_taken_button_shape);
                }
            } else {
                Calendar calendar = holder.mReminder.getTimeTaken();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                String time = String.format("%02d:%02d", hours, minutes);
                holder.mButton.setText("✓ Taken \n" + time);
                holder.mButton.setBackgroundResource(R.drawable.taken_button_shape);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void remove(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mDateTimeView;
        public final ImageView mDashboardIcon;
        public final Button mButton;
        public Reminder mReminder;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name_text);
            mDateTimeView = (TextView) view.findViewById(R.id.datetime_text);
            mDashboardIcon = (ImageView) view.findViewById(R.id.dashboard_icon);
            mButton = (Button) view.findViewById(R.id.taken_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
