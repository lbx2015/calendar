package com.riking.calendar.bean;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.riking.calendar.pojo.Dictionary;

/**
 * Created by KyuYi on 2017/3/2.
 * E-Mail:kyu_yi@sina.com
 * 功能：
 */

public class DictionaryBean implements IPickerViewData {
    public byte id;
    public String clazz;
    public String ke;
    public String valu;

    public DictionaryBean(Dictionary dictionary) {
        id = dictionary.id;
        clazz = dictionary.clazz;
        ke = dictionary.ke;
        valu = dictionary.valu;
    }

    @Override
    public String getPickerViewText() {
        return valu;
    }

}

