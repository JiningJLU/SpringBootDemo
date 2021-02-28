package hello.service;

import hello.entity.User;
import hello.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder mockEncoder;
    @Mock
    UserMapper mockMapper;
    @InjectMocks
    UserService userService;

    @Test
    public void testSave(){
        //given
        Mockito.when(mockEncoder.encode("myPassword")).thenReturn("myEncodedPassword");
        //when
        userService.save("myUser","myPassword");
        //result
        Mockito.verify(mockMapper).save("myUser","myEncodedPassword");
    }

    @Test
    public void getUserByUsername(){
        userService.getUserByUsername("myUser");
        Mockito.verify(mockMapper).findUserByUsername("myUser");
    }

    @Test
    public void throwExceptionWhenUsernameNotFound(){
        //have to throw this exception
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("myUser"));
    }

    @Test
    public void returnUserDetailsWhenUserFound(){
        Mockito.when(mockMapper.findUserByUsername("myUser")).thenReturn(new User(123,"myUser","myEncodedPassword"));
        UserDetails userDetails = userService.loadUserByUsername("myUser");
        Assertions.assertEquals("myUser",userDetails.getUsername());
        Assertions.assertEquals("myEncodedPassword",userDetails.getPassword());
    }
}