package com.ecommerce.admin.user;

import com.ecommerce.admin.paging.PagingAndSortingHelper;
import com.ecommerce.common.entity.Role;
import com.ecommerce.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    public static final int USER_PER_PAGE = 5;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User getByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public List<User> listAll(){
        return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
    }

    public void listByPage(int pageNum, PagingAndSortingHelper helper){

       helper.listEntities(pageNum,USER_PER_PAGE, userRepository);

    }

    public List<Role> listRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser){
            User existingUser = userRepository.findById(user.getId()).get();

            if (user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }else{
                encodePassword(user);
            }
        }else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    public User updateAccount(User userInForm){
        User userInDb = userRepository.findById(userInForm.getId()).get();

        System.out.println(userInForm.getPassword());
        if (!userInForm.getPassword().isEmpty()){
            userInDb.setPassword(userInDb.getPassword());
            System.out.println(userInDb.getPassword());
            encodePassword(userInDb);
            System.out.println(userInDb.getPassword());
        }

        if (userInForm.getPhotos() != null){
            userInDb.setPhotos(userInForm.getPhotos());
        }

        userInDb.setFirstName(userInForm.getFirstName());
        userInDb.setLastName(userInForm.getLastName());

        return userRepository.save(userInDb);

    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email){
        User userByEmail = userRepository.getUserByEmail(email);

        if (userByEmail == null) return true;

        boolean isCreatingNew = (id == null);

        if(isCreatingNew) {
            if (userByEmail != null) return false;
        }else{
            if (userByEmail.getId() != id){
                return false;
            }
        }

        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        }catch (NoSuchElementException e){
            throw new UserNotFoundException("Could not find user with ID: "+id);
        }

    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);

        if (countById == null || countById == 0){
            throw new UserNotFoundException("Could not find user with ID: "+id);
        }
        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled){
        userRepository.updateEnabledStatus(id, enabled);
    }
}
