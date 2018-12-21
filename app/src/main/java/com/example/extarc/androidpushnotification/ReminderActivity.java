package com.example.extarc.androidpushnotification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderActivity extends AppCompatActivity {

    SQLiteDatabase SQLdb;
    ReminderDataBase RDBHelper;
    private final String TAG = "ReminderActivity";
    EditText TitleR, DescriptionR;
    Button addreminder;
    FloatingActionButton fab;
    ListView listView;
    ArrayList<ReminderHelper> reminderArray = new ArrayList<ReminderHelper>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        getSupportActionBar().setTitle("Task Reminder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TitleR = findViewById(R.id.titlereminder);
        DescriptionR = findViewById(R.id.Desrreminder);
        addreminder = findViewById(R.id.addreminderbtn);
        fab = findViewById(R.id.addRemFab);
        listView = findViewById(R.id.commentlist);

        RDBHelper = new ReminderDataBase(this);
        SQLdb = RDBHelper.getWritableDatabase();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this);
                builder.setTitle("Add Reminder");
                LayoutInflater inflater = ReminderActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.addreminder, null);
                builder.setView(dialogView);

                final EditText etTitle = (EditText) dialogView.findViewById(R.id.titlereminder);
                final EditText etDes = (EditText) dialogView.findViewById(R.id.Desrreminder);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePickerReminder);
                final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePickerReminder);

                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.GONE);

                final CheckBox remindCheck = (CheckBox) dialogView.findViewById(R.id.Checkbox);
                remindCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            datePicker.setVisibility(View.VISIBLE);
                            timePicker.setVisibility(View.VISIBLE);
                        } else {
                            datePicker.setVisibility(View.GONE);
                            timePicker.setVisibility(View.GONE);
                        }
                    }

                });

                Button btnADD = (Button) dialogView.findViewById(R.id.addreminderbtn);
                btnADD.setText("ADD REMINDER");
                btnADD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String title = etTitle.getText().toString();
                        String description = etDes.getText().toString();

                        ContentValues cv = new ContentValues();
                        cv.put(ReminderDataBase.COL_TITLE, title);
                        cv.put(ReminderDataBase.COL_DETAIL, description);
                        cv.put(ReminderDataBase.COL_DATE, getString(R.string.DefaultDate));
                        cv.put(ReminderDataBase.COL_TIME, getString(R.string.DefaultTime));

                        if (remindCheck.isChecked()){
                            Calendar calender = Calendar.getInstance();
                            calender.clear();
                            calender.set(Calendar.MONTH, datePicker.getMonth());
                            calender.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                            calender.set(Calendar.YEAR, datePicker.getYear());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                calender.set(Calendar.HOUR, timePicker.getHour());
                                calender.set(Calendar.MINUTE, timePicker.getMinute());
                            } else {
                                calender.set(Calendar.HOUR, timePicker.getCurrentHour());
                                calender.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                            }
                            calender.set(Calendar.SECOND, 00);

                            SimpleDateFormat formatter = new SimpleDateFormat("H:mm a");
                            String timeString = formatter.format(new Date(calender.getTimeInMillis()));
                            SimpleDateFormat dateformatter = new SimpleDateFormat("dd-MM-yyyy");
                            String dateString = dateformatter.format(new Date(calender.getTimeInMillis()));

                            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(ReminderActivity.this, ReminderNotification.class);
                            String alertTitle = etTitle.getText().toString();
                            String alertContent = etDes.getText().toString();
                            intent.putExtra(getString(R.string.TitleReminder), alertTitle);
                            intent.putExtra(getString(R.string.ContentReminder), alertContent);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderActivity.this, 0, intent, 0);

                            if (alarmMgr != null) {
                                alarmMgr.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                            } else {
                                Toast.makeText(getApplicationContext(), "AlaramMgr is null", Toast.LENGTH_SHORT).show();
                            }
                            cv.put(ReminderDataBase.COL_TIME, timeString);
                            cv.put(ReminderDataBase.COL_DATE, dateString);

                        }else {
                            Date date = new Date();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            calendar.set(Calendar.HOUR_OF_DAY, 1);
                            calendar.set(Calendar.MINUTE, 30);
                            calendar.set(Calendar.SECOND, 0);

                            Calendar alarmStartTime = Calendar.getInstance();
                            Calendar now = Calendar.getInstance();
                            alarmStartTime.set(Calendar.HOUR_OF_DAY, 1);
                            alarmStartTime.set(Calendar.MINUTE, 35);
                            alarmStartTime.set(Calendar.SECOND, 0);
                            if (now.after(alarmStartTime)) {
                                Log.d("Hey","Added a day");
                                alarmStartTime.add(Calendar.DATE, 1);
                            }

                            SimpleDateFormat formatter = new SimpleDateFormat("H:mm a");
                            String timeString = formatter.format(calendar.getTime());

                            String dateString = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(ReminderActivity.this, ReminderNotification.class);
                            String alertTitle = etTitle.getText().toString();
                            String alertContent = etDes.getText().toString();
                            intent.putExtra(getString(R.string.TitleReminder), alertTitle);
                            intent.putExtra(getString(R.string.ContentReminder), alertContent);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderActivity.this, 0, intent, 0);
                            if (alarmMgr != null) {
                                alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            } else {
                                Toast.makeText(getApplicationContext(), "AlaramMgr is null", Toast.LENGTH_SHORT).show();
                            }
                            cv.put(ReminderDataBase.COL_TIME, timeString);
                            cv.put(ReminderDataBase.COL_DATE, dateString);
                        }

                        SQLdb.insert(ReminderDataBase.TABLE_NAME, null, cv);
                        Log.d(TAG, "CV" + cv);

                        finish();
                        startActivity(getIntent());
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        loadToDosFromDatabase();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadToDosFromDatabase() {
        //we are here using the DatabaseManager instance to get all employees
        Cursor cursor = RDBHelper.getAllToDolists();

        if (cursor.moveToFirst()) {
            do {
                reminderArray.add(new ReminderHelper(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());

            //passing the databasemanager instance this time to the adapter
            ToDoAdapter adapter = new ToDoAdapter(this, R.layout.listview_reminder, reminderArray, RDBHelper);
            listView.setAdapter(adapter);
        }
    }

    class ToDoAdapter extends ArrayAdapter<ReminderHelper> {

        Context mCtx;
        int layoutRes;
        List<ReminderHelper> reminderArray;
        ReminderDataBase ReminderDataBase;

        public ToDoAdapter(Context mCtx, int layoutRes, List<ReminderHelper> reminderArray, ReminderDataBase ReminderDataBase) {
            super(mCtx, layoutRes, reminderArray);

            this.mCtx = mCtx;
            this.layoutRes = layoutRes;
            this.reminderArray = reminderArray;
            this.ReminderDataBase = ReminderDataBase;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);

            View view = inflater.inflate(R.layout.listview_reminder, null);

            TextView textViewTitle = view.findViewById(R.id.title);
            TextView textViewDes = view.findViewById(R.id.Detail);
            TextView textViewDate = view.findViewById(R.id.date);
            TextView textViewTime = view.findViewById(R.id.time);
            ImageButton deleteREminder = view.findViewById(R.id.DeleteReminder);
//            final Button updateREminder = view.findViewById(R.id.UpdateReminder);

            final ReminderHelper ToDoreminder = reminderArray.get(position);

            textViewTitle.setText(ToDoreminder.getTitle());
            textViewDes.setText(ToDoreminder.getDescription());
            textViewDate.setText(ToDoreminder.getDate());
            textViewTime.setText(ToDoreminder.getTime());

            deleteREminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteToDo(ToDoreminder);
                }
            });

//            updateREminder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    updateToDo(ToDoreminder);
//                }
//            });


            return view;
        }

        private void updateToDo(final ReminderHelper ToDo) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.editreminder, null);
            builder.setView(view);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            final EditText etTitle = (EditText) findViewById(R.id.edittitlereminder);
            final EditText etDes = (EditText) findViewById(R.id.editDesrreminder);
            final EditText etDate = (EditText) findViewById(R.id.editdatereminder);
            final EditText etTime = (EditText) findViewById(R.id.edittimereminder);
            Button btnEdit = (Button) findViewById(R.id.editreminderbtn);

            etTitle.setText(ToDo.getTitle());
            etDes.setText(ToDo.getDescription());
            etDate.setText(ToDo.getDate());
            etTime.setText(ToDo.getTime());
            btnEdit.setText("Update REMINDER");

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = etTitle.getText().toString().trim();
                    String description = etDes.getText().toString().trim();
                    String date = etDate.getText().toString();
                    String time = etTime.getText().toString();

                    if (title.isEmpty()) {
                        etTitle.setError("Title can't be empty");
                        etTitle.requestFocus();
                    }

                    if (description.isEmpty()) {
                        etDes.setError("Description can't be empty");
                        etDes.requestFocus();
                    }

                    //calling the update method from database manager instance
                    if (RDBHelper.updateToDolist(ToDo.getToDoid(), title, description, date, time)) {
                        Toast.makeText(mCtx, "ToDo Updated", Toast.LENGTH_SHORT).show();
                        loadToDolistAgain();
                    }
                    alertDialog.dismiss();

                }
            });
        }

        private void deleteToDo(final ReminderHelper ToDo) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
            builder.setTitle("Are you sure?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    //calling the delete method from the database manager instance
                    if (RDBHelper.deleteToDolist(ToDo.getToDoid()))
                        loadToDolistAgain();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        private void loadToDolistAgain() {
            //calling the read method from database instance
            Cursor cursor = RDBHelper.getAllToDolists();

            reminderArray.clear();
            if (cursor.moveToFirst()) {
                do {
                    reminderArray.add(new ReminderHelper(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4)
                    ));
                } while (cursor.moveToNext());
            }
            notifyDataSetChanged();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MasterActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }


}