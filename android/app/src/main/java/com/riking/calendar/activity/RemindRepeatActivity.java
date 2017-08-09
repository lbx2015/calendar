package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.riking.calendar.R;
import com.riking.calendar.fragment.CreateReminderFragment;

import java.util.ArrayList;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class RemindRepeatActivity extends AppCompatActivity implements View.OnClickListener {
    //1...7
    public String repeatWeek;
    //0,1,2,3
    public byte repeatFlag;

    //image of confirm
    private View noRepeatImage;
    private View mondayImage;
    private View tuesdayImage;
    private View wednesdayImage;
    private View thursdayImge;
    private View fridayImage;
    private View saturdayImage;
    private View sundayImage;
    private View workDayImage;
    private View holidayImage;
    private ArrayList<String> repeatWeekDays = new ArrayList<>();
    private byte repeatWays;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_repeat);
        View notRepeat = findViewById(R.id.not_repeat_item);
        View perMonday = findViewById(R.id.per_monday_item);
        View perTuesday = findViewById(R.id.per_tuesday_item);
        View perWednesday = findViewById(R.id.per_wednesday_item);
        View perThurdsday = findViewById(R.id.per_thursday_item);
        View perFriday = findViewById(R.id.per_friday_item);
        View perSaturday = findViewById(R.id.per_saturday_item);
        View perSunday = findViewById(R.id.per_sunday_item);
        View perWorkdays = findViewById(R.id.repeat_on_working_day_item);
        View perHolidays = findViewById(R.id.repeat_on_holiday_item);

        noRepeatImage = findViewById(R.id.no_repeat_confirm);
        mondayImage = findViewById(R.id.per_monday_confirm);
        tuesdayImage = findViewById(R.id.per_tuesday_confirm);
        wednesdayImage = findViewById(R.id.per_wednesday_confirm);
        thursdayImge = findViewById(R.id.per_thursday_confirm);
        fridayImage = findViewById(R.id.per_friday_confirm);
        saturdayImage = findViewById(R.id.per_saturday_confirm);
        sundayImage = findViewById(R.id.per_sunday_confirm);
        workDayImage = findViewById(R.id.repeat_on_working_day_confirm);
        holidayImage = findViewById(R.id.repeat_on_holiday_item_confirm);

        View backButton = findViewById(R.id.back);

        //set click listeners
        backButton.setOnClickListener(this);
        notRepeat.setOnClickListener(this);
        perMonday.setOnClickListener(this);
        perTuesday.setOnClickListener(this);
        perWednesday.setOnClickListener(this);
        perThurdsday.setOnClickListener(this);
        perFriday.setOnClickListener(this);
        perSaturday.setOnClickListener(this);
        perSunday.setOnClickListener(this);
        perWorkdays.setOnClickListener(this);
        perHolidays.setOnClickListener(this);
        findViewById(R.id.done).setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        repeatFlag = bundle.getByte("repeat_flag");
        repeatWeek = bundle.getString("repeat_week");
        switch (repeatFlag) {
            case 0: {
                noRepeatImage.setVisibility(View.VISIBLE);
                //hide others
                mondayImage.setVisibility(View.GONE);
                tuesdayImage.setVisibility(View.GONE);
                wednesdayImage.setVisibility(View.GONE);
                thursdayImge.setVisibility(View.GONE);
                fridayImage.setVisibility(View.GONE);
                saturdayImage.setVisibility(View.GONE);
                sundayImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case 1: {
                workDayImage.setVisibility(View.VISIBLE);
                mondayImage.setVisibility(View.GONE);
                tuesdayImage.setVisibility(View.GONE);
                wednesdayImage.setVisibility(View.GONE);
                thursdayImge.setVisibility(View.GONE);
                fridayImage.setVisibility(View.GONE);
                saturdayImage.setVisibility(View.GONE);
                sundayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case 2: {
                holidayImage.setVisibility(View.VISIBLE);
                mondayImage.setVisibility(View.GONE);
                tuesdayImage.setVisibility(View.GONE);
                wednesdayImage.setVisibility(View.GONE);
                thursdayImge.setVisibility(View.GONE);
                fridayImage.setVisibility(View.GONE);
                saturdayImage.setVisibility(View.GONE);
                sundayImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                break;
            }
            case 3: {
                if (repeatWeek == null) break;
                noRepeatImage.setVisibility(View.GONE);
                if (repeatWeek.contains("1")) {
                    repeatWeekDays.add("1");
                    mondayImage.setVisibility(View.VISIBLE);
                }
                if (repeatWeek.contains("2")) {
                    repeatWeekDays.add("2");
                    tuesdayImage.setVisibility(View.VISIBLE);
                }
                if (repeatWeek.contains("3")) {
                    repeatWeekDays.add("3");
                    wednesdayImage.setVisibility(View.VISIBLE);
                }
                if (repeatWeek.contains("4")) {
                    repeatWeekDays.add("4");
                    tuesdayImage.setVisibility(View.VISIBLE);
                }
                if (repeatWeek.contains("5")) {
                    repeatWeekDays.add("5");
                    fridayImage.setVisibility(View.VISIBLE);
                }
                if (repeatWeek.contains("6")) {
                    repeatWeekDays.add("6");
                    saturdayImage.setVisibility(View.VISIBLE);
                }
                if (repeatWeek.contains("7")) {
                    repeatWeekDays.add("7");
                    sundayImage.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                onBackPressed();
                break;
            }
            case R.id.not_repeat_item: {
                repeatWays = 0;
                noRepeatImage.setVisibility(View.VISIBLE);
                //hide others
                mondayImage.setVisibility(View.GONE);
                tuesdayImage.setVisibility(View.GONE);
                wednesdayImage.setVisibility(View.GONE);
                thursdayImge.setVisibility(View.GONE);
                fridayImage.setVisibility(View.GONE);
                saturdayImage.setVisibility(View.GONE);
                sundayImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }

            case R.id.per_monday_item: {
                if (mondayImage.getVisibility() == View.VISIBLE) {
                    if (repeatWeekDays.size() == 1) {
                        break;
                    }
                    repeatWeekDays.remove("1");
                    mondayImage.setVisibility(View.GONE);
                } else {
                    mondayImage.setVisibility(View.VISIBLE);
                    repeatWays = 3;
                    repeatWeekDays.add("1");
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.per_tuesday_item: {
                if (tuesdayImage.getVisibility() == View.VISIBLE) {
                    if (repeatWeekDays.size() == 1) {
                        break;
                    }
                    repeatWeekDays.remove("2");
                    tuesdayImage.setVisibility(View.GONE);
                } else {
                    repeatWays = 3;
                    repeatWeekDays.add("2");
                    tuesdayImage.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.per_wednesday_item: {
                if (wednesdayImage.getVisibility() == View.VISIBLE) {
                    if (repeatWeekDays.size() == 1) {
                        break;
                    }
                    repeatWeekDays.remove("3");
                    wednesdayImage.setVisibility(View.GONE);
                } else {
                    repeatWays = 3;
                    repeatWeekDays.add("3");
                    wednesdayImage.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.per_thursday_item: {
                if (thursdayImge.getVisibility() == View.VISIBLE) {
                    if (repeatWeekDays.size() == 1) {
                        break;
                    }
                    repeatWeekDays.remove("4");
                    thursdayImge.setVisibility(View.GONE);
                } else {
                    repeatWays = 3;
                    repeatWeekDays.add("4");
                    thursdayImge.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.per_friday_item: {
                if (fridayImage.getVisibility() == View.VISIBLE) {
                    if (repeatWeekDays.size() == 1) {
                        break;
                    }
                    repeatWeekDays.remove("5");
                    fridayImage.setVisibility(View.GONE);
                } else {
                    repeatWays = 3;
                    repeatWeekDays.add("5");
                    fridayImage.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.per_saturday_item: {
                if (saturdayImage.getVisibility() == View.VISIBLE) {
                    if (repeatWeekDays.size() == 1) {
                        break;
                    }
                    repeatWeekDays.remove("6");
                    saturdayImage.setVisibility(View.GONE);
                } else {
                    repeatWays = 3;
                    repeatWeekDays.add("6");
                    saturdayImage.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.per_sunday_item: {
                if (sundayImage.getVisibility() == View.VISIBLE) {
                    if (repeatWeekDays.size() == 1) {
                        break;
                    }
                    repeatWeekDays.remove("7");
                    sundayImage.setVisibility(View.GONE);
                } else {
                    repeatWays = 3;
                    repeatWeekDays.add("7");
                    sundayImage.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.repeat_on_working_day_item: {
                if (workDayImage.getVisibility() == View.VISIBLE) {
                    //do nothing if the item is selected already.
                    break;
                } else {
                    repeatWays = 1;
                    repeatWeekDays.clear();
                    workDayImage.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                mondayImage.setVisibility(View.GONE);
                tuesdayImage.setVisibility(View.GONE);
                wednesdayImage.setVisibility(View.GONE);
                thursdayImge.setVisibility(View.GONE);
                fridayImage.setVisibility(View.GONE);
                saturdayImage.setVisibility(View.GONE);
                sundayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.repeat_on_holiday_item: {
                if (holidayImage.getVisibility() == View.VISIBLE) {
                    break;
                } else {
                    repeatWays = 2;
                    repeatWeekDays.clear();
                    holidayImage.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                mondayImage.setVisibility(View.GONE);
                tuesdayImage.setVisibility(View.GONE);
                wednesdayImage.setVisibility(View.GONE);
                thursdayImge.setVisibility(View.GONE);
                fridayImage.setVisibility(View.GONE);
                saturdayImage.setVisibility(View.GONE);
                sundayImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.done: {
                Intent i = new Intent();
                i.putExtra("repeatWay", repeatWays);//0,1,2,3
                StringBuilder weeks = new StringBuilder();
                for (int index = 0; index < repeatWeekDays.size(); index++) {
                    weeks.append(repeatWeekDays.get(index));
                }
                i.putExtra("repeatWeekDays", weeks.toString());
                setResult(CreateReminderFragment.REPEAT_ITEMS, i);
                finish();
                break;
            }
        }
    }
}
