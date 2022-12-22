package com.ecommerce.admin.settings;

import com.ecommerce.common.entity.setting.Setting;
import com.ecommerce.common.entity.setting.SettingBag;

import java.util.List;

public class MenusSettingBag extends SettingBag {

    public MenusSettingBag(List<Setting> listSettings) {
        super(listSettings);
    }

    public void updateAdImage(String value){
        super.update("AD_IMAGE",value);
    }
}
