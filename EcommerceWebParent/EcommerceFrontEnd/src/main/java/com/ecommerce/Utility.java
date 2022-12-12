package com.ecommerce;

import com.ecommerce.security.oauth.CustomerOAuth2User;
import com.ecommerce.setting.CurrencySettingBag;
import com.ecommerce.setting.EmailSettingBag;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

public class Utility {

    public static String getSiteUrl(HttpServletRequest request){
        String siteUrl = request.getRequestURL().toString();

        return siteUrl.replace(request.getServletPath(),"");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(settings.getHost());
        mailSender.setPort(settings.getPort());
        mailSender.setUsername(settings.getUsername());
        mailSender.setPassword(settings.getPassword());

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth",settings.getSmtpAuth());
        mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());

        mailSender.setJavaMailProperties(mailProperties);

        return mailSender;
    }

    public static String getEmailAuthenticatedCustomer(HttpServletRequest request){
        Object principal = request.getUserPrincipal();
        if (principal == null) return null;
        String customerEmail = null;

        if (principal instanceof UsernamePasswordAuthenticationToken
                || principal instanceof RememberMeAuthenticationToken){
            customerEmail = request.getUserPrincipal().getName();
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            customerEmail = oAuth2User.getEmail();
        }

        return customerEmail;
    }

    public static String formatCurrency(float amount, CurrencySettingBag settings){
        String symbol = settings.getSymbol();
        String symbolPosition = settings.getSymbolPosition();
        String decimalPointType = settings.getDecimalPointType();
        String thousandPointType = settings.getThousandPointType();
        int decimalDigits = settings.getDecimalDigit();


        String pattern = symbolPosition.equals("Before Price") ? symbol: "";
        pattern += "###,####";

        if (decimalDigits > 0){
            pattern += ".";
            for (int count=1; count<=decimalDigits; count++) pattern+= "#";
        }
        pattern += symbolPosition.equals("After Price") ? symbol: "";

        char thousandsSeparator = thousandPointType.equals("POINT") ? '.' : ',';
        char decimalSeparator = decimalPointType.equals("POINT") ? '.' : ',';


        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
        decimalFormatSymbols.setGroupingSeparator(thousandsSeparator);


        DecimalFormat formatter = new DecimalFormat(pattern, decimalFormatSymbols);

        return formatter.format(amount);
    }

}
