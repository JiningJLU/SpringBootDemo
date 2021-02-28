package hello.controller;

import hello.entity.Result;
import hello.entity.User;
import hello.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(AuthenticationManager authenticationManager,UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @GetMapping("/auth")
    @ResponseBody
    public Object auth(){
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication == null?null:authentication.getName());
        if (loggedInUser == null){
            return Result.failure("用户没有登录");
        }else {
            return new Result("ok", null, true, loggedInUser);
        }
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Object login(@RequestBody Map<String,Object> userNameAndPassWord){
        System.out.println(userNameAndPassWord);
        String username = userNameAndPassWord.get("username").toString();
        String password = userNameAndPassWord.get("password").toString();
        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(username);
        }catch (UsernameNotFoundException ue){
            return Result.failure("用户不存在");
        }
        //去数据库拿到真正的用户名和密码
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return new Result("ok","登录成功",true,
                    userService.getUserByUsername(username));
        }catch (BadCredentialsException be){
            be.printStackTrace();
            return Result.failure("密码不正确");
        }
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String,Object> userNameAndPassWord){
        String username = userNameAndPassWord.get("username").toString();
        String password = userNameAndPassWord.get("password").toString();
        if(username == null ||password == null){
            return Result.failure("用户名或密码为空");
        }
        if (username.length()<1 ||username.length() > 15){
            return Result.failure("无效用户名");
        }
        if (password.length()<6 ||password.length() > 16){
            return Result.failure("无效密码");
        }
        User user = userService.getUserByUsername(username);
        try {
            userService.save(username,password);
        }catch (DuplicateKeyException e){
            e.printStackTrace();
            return Result.failure("用户已经存在");
        }
        return Result.success("success");
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        if (user == null){
            return Result.failure("用户没有登录");
        }else {
            SecurityContextHolder.clearContext();
            return Result.success("注销成功");
        }
    }

}
