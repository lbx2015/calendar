package com.riking.calendar.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.widget.Toast;

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
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
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

    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            getContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(getContext(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Read the name of all the contacts.
     *
     * @return a list of names.
     */
    private void getContaactNames() {
        // Get the ContentResolver
        ContentResolver cr = getActivity().getContentResolver();
        // Get the Cursor of all the contacts
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        // Move the cursor to first. Also check whether the cursor is empty or not.
        if (cursor.moveToFirst()) {
            // Iterate through the cursor
            do {
                AppUserResult userResult = new AppUserResult();//实例化一个user
                // Get the contacts name
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                int phoneType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                switch (phoneType) {
                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                        // do something with the Home number here...
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                        // do something with the Mobile number here...
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                        // do something with the Work number here...
                        break;
                }
                userResult.userName = name;
                userResult.phone = StringUtil.getPhoneNumber(phone);

                if (StringUtil.isEmpty(userResult.userName) || !StringUtil.isMobileNO(userResult.phone)) {

                } else {
                    contacts.add(userResult);//将map加入list集合中}
                }
            } while (cursor.moveToNext());
        }
        // Close the curosor
        cursor.close();

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
        showContacts();
        if (contacts.size() == 0) {
            return;
        }
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
