package com.riking.calendar.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.adapter.MyContactsAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class MyContactsFragment extends ZFragment<MyContactsAdapter> {
    private ContentResolver cr;
    private List<AppUserResult> contacts = new ArrayList<>();

    @Override
    public MyContactsAdapter getAdapter() {
        return new MyContactsAdapter();
    }

    public void initViews() {
        //拿到内容访问者
        cr = getActivity().getContentResolver();
    }

    public void initEvents() {
    }

    public void getContacts() {
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cs = cr.query(uri, null, null, null, null, null);
        while (cs.moveToNext()) {
            //拿到联系人id 跟name
            int id = cs.getInt(cs.getColumnIndex("_id"));
            String name = cs.getString(cs.getColumnIndex("display_name"));
            //得到这个id的所有数据（data表）
            Uri uri1 = Uri.parse("content://com.android.contacts/raw_contacts/" + id + "/data");
            Cursor cs2 = cr.query(uri1, null, null, null, null, null);
            AppUserResult userResult = new AppUserResult();//实例化一个map
            while (cs2.moveToNext()) {
                //得到data这一列 ，包括很多字段
                String data1 = cs2.getString(cs2.getColumnIndex("data1"));
                //得到data中的类型
                String type = cs2.getString(cs2.getColumnIndex("mimetype"));
                String str = type.substring(type.indexOf("/") + 1, type.length());//截取得到最后的类型
                if ("name".equals(str)) {//匹配是否为联系人名字
                    userResult.userName = data1;
                }
                if ("phone_v2".equals(str)) {//匹配是否为电话
                    userResult.phone = StringUtil.getPhoneNumber(data1);
                }
                if ("photo".equals(str)) {
//                    maps.put("photo", data1);
                    userResult.photoUrl = data1;
                }

                MyLog.d(data1 + "       " + type);
            }
            if (StringUtil.isEmpty(userResult.userName) || !StringUtil.isMobileNO(userResult.phone)) {

            } else {
                contacts.add(userResult);//将map加入list集合中}
            }
//        simpleAdapter.notifyDataSetChanged();//通知适配器发生数据改变

        }
    }

    public void loadData(final int page) {
        getContacts();
        List sb = new ArrayList();

        for (int i = 0; i < contacts.size(); i++) {
            AppUserResult m = contacts.get(i);
            sb.add(m.phone);
        }

        final UserParams params = new UserParams();
        params.phones = sb;
        APIClient.getContacts(params, new ZCallBack<ResponseModel<List<String>>>() {
            @Override
            public void callBack(ResponseModel<List<String>> response) {
                mPullToLoadView.setComplete();

                List<String> phones = response._data;
                MyLog.d("list size: " + phones.size());
                if (phones.size() < params.pcount) {
                    isHasLoadedAll = true;
                }

                for (AppUserResult u : contacts) {
                    if (phones.contains(u.phone)) {
                        //invited
                        u.isInvited = 1;
                    }
                }
                isLoading = false;
                nextPage = page + 1;
                mAdapter.setData(contacts);
            }
        });
    }
}
