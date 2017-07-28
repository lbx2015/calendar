package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class RemindRepeatActivity extends AppCompatActivity implements View.OnClickListener {
    private View backButton;

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

        //set click listeners
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                onBackPressed();
                break;
            }
            case R.id.not_repeat_item: {
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
                    mondayImage.setVisibility(View.GONE);
                } else {
                    mondayImage.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.per_tuesday_item: {
                if (tuesdayImage.getVisibility() == View.VISIBLE) {
                    tuesdayImage.setVisibility(View.GONE);
                } else {
                    tuesdayImage.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.per_wednesday_item: {
                if (wednesdayImage.getVisibility() == View.VISIBLE) {
                    wednesdayImage.setVisibility(View.GONE);
                } else {
                    wednesdayImage.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.per_thursday_item: {
                if (thursdayImge.getVisibility() == View.VISIBLE) {
                    thursdayImge.setVisibility(View.GONE);
                } else {
                    thursdayImge.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.per_friday_item: {
                if (fridayImage.getVisibility() == View.VISIBLE) {
                    fridayImage.setVisibility(View.GONE);
                } else {
                    fridayImage.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.per_saturday_item: {
                if (saturdayImage.getVisibility() == View.VISIBLE) {
                    saturdayImage.setVisibility(View.GONE);
                } else {
                    saturdayImage.setVisibility(View.VISIBLE);
                }
                noRepeatImage.setVisibility(View.GONE);
                workDayImage.setVisibility(View.GONE);
                holidayImage.setVisibility(View.GONE);
                break;
            }
            case R.id.per_sunday_item: {
                if (sundayImage.getVisibility() == View.VISIBLE) {
                    sundayImage.setVisibility(View.GONE);
                } else {
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
                    workDayImage.setVisibility(View.VISIBLE);
                }
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
                    holidayImage.setVisibility(View.VISIBLE);
                }
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
        }
    }
}
