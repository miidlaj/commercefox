package com.ecommerce.setting;


import com.ecommerce.common.entity.setting.Setting;
import com.ecommerce.common.entity.setting.SettingBag;

import java.util.List;

public class MenusSettingBag extends SettingBag {


    public MenusSettingBag(List<Setting> listSettings) {
        super(listSettings);
    }

    public String getValueOfTheDay(){
        return super.getValue("VALUE_OF_DAY");

    }


}