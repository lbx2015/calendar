package com.riking.calendar.view;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.riking.calendar.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 自定义view
 */
public class SpinnerView {

    //the position which popup window will showing
    public ViewGroup viewGroup;
    public PopupWindow mWindow;
    View spinnerLinearLayout;
    private ListAdapter mAdapter;
    private OnItemClickListener mListener;
    private ListView mContentView;

    public SpinnerView(ViewGroup position) {
        viewGroup = position;
    }

    public void setAdapter(ListAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void clickArrow() {
        // 点击箭头，需要弹出显示list数据的层
        if (mAdapter == null) {
            Log.d("zzw", "请调用setAdapter()去设置数据");
        }
        if (mWindow == null) {
            View v = View.inflate(viewGroup.getContext(), R.layout.spinner, null);
            spinnerLinearLayout = v.findViewById(R.id.custom_spinner);
            spinnerLinearLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d("zzw", "touch action:" + event.getAction());
                    mWindow.dismiss();
                    return false;
                }
            });
            mContentView = (ListView) v.findViewById(R.id.list_view);// 显示的view
            // 设置数据
            mContentView.setAdapter(mAdapter);// ---》adapter---》List《数据》
            // popup的宽和高
            mWindow = new PopupWindow(v, MATCH_PARENT, MATCH_PARENT);
            mWindow.setFocusable(true);// 设置获取焦点
            mWindow.setOutsideTouchable(true);// 设置边缘点击收起
//            mWindow.setBackgroundDrawable(new ColorDrawable(viewGroup.getContext().getResources().getColor(android.R.color.white)));
        }
        // 设置item的点击事件
        mContentView.setOnItemClickListener(mListener);
        mWindow.showAsDropDown(viewGroup);//设置popup显示的位置
    }

    public void toggle() {
        Log.d("zzw", "toggle method is called and mWindow is  " + mWindow);
        if (mWindow != null && mWindow.isShowing()) {
            mWindow.dismiss();
        } else {
            clickArrow();
        }
    }
}

