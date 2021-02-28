package hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authenticationManager,userService)).build();
    }
    @Test
    void returnNotLoggedInByDefault() throws Exception{
        mockMvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));
    }
    @Test
    void testLogin() throws Exception{
        mockMvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));
        Map<String,String> userNamePassword = new HashMap<>();
        userNamePassword.put("username","MyUser");
        userNamePassword.put("password","MyPassword");
        System.out.println(new ObjectMapper().writeValueAsString(userNamePassword));

        Mockito.when(userService.loadUserByUsername("MyUser")).
                thenReturn(new User("MyUser",bCryptPasswordEncoder.encode("MyPassword"), Collections.emptyList()));
        Mockito.when(userService.getUserByUsername("MyUser")).
                thenReturn(new hello.entity.User(123,"MyUser",bCryptPasswordEncoder.encode("MyPassword")));

        MvcResult mvcResult = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(userNamePassword)))
                .andExpect(status().isOk())
                .andExpect(mvcResult1 -> System.out.println(mvcResult1.getResponse().getContentAsString()))
                .andReturn();
        System.out.println(Arrays.toString(mvcResult.getResponse().getCookies()));
        HttpSession session = mvcResult.getRequest().getSession();
        mockMvc.perform(get("/auth").session((MockHttpSession) session)).andExpect(status().isOk())
                .andExpect(Result -> System.out.println(Result.getResponse().getContentAsString()));
    }
}