package com.ecommerce.admin.report;

import com.ecommerce.admin.settings.SettingService;
import com.ecommerce.common.entity.setting.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private SettingService settingService;

    @GetMapping("/reports")
    public String viewSalesReportHome(Model model, HttpServletRequest request){
        loadCurrencySetting(request);

        model.addAttribute("pageTitle", "Sales Report");
        return "reports/reports";
    }

    private void loadCurrencySetting(HttpServletRequest request) {
        List<Setting> currencySettings = settingService.getCurrencySettings();

        for (Setting setting : currencySettings) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }

}
