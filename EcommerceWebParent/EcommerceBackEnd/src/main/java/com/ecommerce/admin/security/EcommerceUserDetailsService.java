package com.ecommerce.admin.security;

import com.ecommerce.admin.user.UserRepository;
import com.ecommerce.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EcommerceUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if (user != null){
            return new EcommerceUserDetails(user);
        }

        throw new UsernameNotFoundException("Could Not find user with Email: "+email);
    }
}
