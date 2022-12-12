package com.ecommerce.customer;

import com.ecommerce.Utility;
import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.security.oauth.CustomerOAuth2User;
import com.ecommerce.setting.EmailSettingBag;
import com.ecommerce.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;


    @GetMapping("/register")
    public String showRegisterForm(Model model){
        List<Country> listCountries = customerService.listAllCountries();

        model.addAttribute("listCountries", listCountries);
        model.addAttribute("pageTitle", "Register");
        model.addAttribute("customer", new Customer());

        return "register";
    }

    @PostMapping("/create_customer")
    public String createCustomer(Customer customer, Model model, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);

        sendVerificationEmail(request, customer);

        model.addAttribute("pageTitle","Registration Succeeded");

        return "customer/register_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSetting = settingService.getEmailSettings();

        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSetting);

        String toAddress = customer.getEmail();
        String subject = emailSetting.getCustomerVerifySubject();
        String content = emailSetting.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper((message));

        helper.setFrom(emailSetting.getFromAddress(), emailSetting.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", customer.getFullName());

        String verfiyURL = Utility.getSiteUrl(request) + "/verify?code=" + customer.getVerificationCode();

        content = content.replace("[[URL]]", verfiyURL);

        helper.setText(content, true);

        mailSender.send(message);

        System.out.println(toAddress);
        System.out.println(verfiyURL);
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model){
        boolean verified = customerService.verify(code);

        if (verified){
            model.addAttribute("pageTitle", "Verified Successfully");
        }else{
            model.addAttribute("pageTitle", "Verification Failed");
        }

        return "customer/register/" + (verified ? "verify_success" : "verify_fail");
    }

    @GetMapping("/account_details")
    public String viewAccountDetails(Model model, HttpServletRequest request){

        String email = Utility.getEmailAuthenticatedCustomer(request);
        Customer customer = customerService.getCustomerByEmail(email);
        List<Country> listCountries = customerService.listAllCountries();

        model.addAttribute("listCountries",listCountries);
        model.addAttribute("customer", customer);

        return "account/account";
    }


    @PostMapping("/update_account_details")
    public String updateAccountDetails(Model model, Customer customer,
                                       HttpServletRequest request,
                                       RedirectAttributes redirectAttributes){
        customerService.update(customer);

        redirectAttributes.addFlashAttribute("message", "You account details has been updated.");
        updateNameForAuthenticatedCustomer(request,customer);

        String redirectOption = request.getParameter("redirect");
        String redirectURL = "redirect:/account_details";

        if ("address_book".equals(redirectOption)){
            redirectURL = "redirect:/address_book";
        }else if("cart".equals(redirectOption)){
            redirectURL = "redirect:/cart";
        }else if ("checkout".equals(redirectOption)){
            redirectURL = "redirect:/address_book?redirect=checkout";
        }

        return redirectURL;
    }

    private void updateNameForAuthenticatedCustomer(HttpServletRequest request, Customer customer) {
        Object principal = request.getUserPrincipal();
        if (principal instanceof UsernamePasswordAuthenticationToken
                || principal instanceof RememberMeAuthenticationToken){

            CustomerUserDetails userDetails = getCustomerUserDetailObject(principal);
            Customer authenticatedCustomer = userDetails.getCustomer();
            authenticatedCustomer.setFirstName(customer.getFirstName());
            authenticatedCustomer.setLastName(customer.getLastName());
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            String fullName = customer.getFirstName() + " " + customer.getLastName();
            oAuth2User.setFullName(fullName);
        }

    }

    private CustomerUserDetails getCustomerUserDetailObject(Object principle){
        CustomerUserDetails userDetails = null;
        if (principle instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principle;
            userDetails = (CustomerUserDetails) token.getPrincipal();
        } else if (principle instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principle;
            userDetails = (CustomerUserDetails) token.getPrincipal();
        }
        return userDetails;
    }

}
