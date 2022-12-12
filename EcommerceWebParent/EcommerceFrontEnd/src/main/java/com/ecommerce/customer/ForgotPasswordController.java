package com.ecommerce.customer;

import com.ecommerce.Utility;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.setting.EmailSettingBag;
import com.ecommerce.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/forgot_password")
    public String showRequestForm(){
        return "customer/reset_password/reset_password";
    }

    @PostMapping("/forgot_password")
    public String processRequestForm(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        try {
            String token = customerService.updateResetPasswordToken(email);
            String link = Utility.getSiteUrl(request) + "/reset_password?token=" + token;
            SendEmail(link,email);

            model.addAttribute("message","Reset link has been sent to your email.");

        }catch (CustomerNotFoundException e){
            model.addAttribute("error",e.getMessage());
        }  catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error","Could not send email");
        }

        return "customer/reset_password/reset_password";
    }

    private void SendEmail(String link, String email) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSetting = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSetting);

        String toAddress = email;
        String subject = "Link to reset password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSetting.getFromAddress(), emailSetting.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content, true);
        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetForm(@Param("token")String token, Model model){
        Customer customer = customerService.getByResetPasswordToken(token);
        if (customer != null){
            model.addAttribute("token", token);
        }else{
            model.addAttribute("message", "Invalid Token");
            model.addAttribute("pageTitle", "Invalid Token");

            return "customer/reset_password/password_reset_failed";
        }

        return "customer/reset_password/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetForm(HttpServletRequest request, Model model){
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        try {
            customerService.updatePassword(token, password);
            model.addAttribute("message", "Password has been changed.");
            model.addAttribute("pageTitle", "Success");

            return "customer/reset_password/password_reset_success";
        } catch (CustomerNotFoundException e) {
            model.addAttribute("message", "Invalid");
            model.addAttribute("pageTitle", e.getMessage());

            return "customer/reset_password/password_reset_failed";
        }
    }


}
