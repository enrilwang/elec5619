package net.guides.springboot2.springboot2webappjsp.controllers;

import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserProfileController {
    @Autowired
    UserRepository userRepo;

    //change password
    @RequestMapping(value = "changeNameAndPassword", method = RequestMethod.POST)
    @CrossOrigin
    public Result changeNameAndPassword(HttpServletRequest request, @RequestBody User user) {
        Result result = new Result();

        String email = JwtUtil.getUserEmailByToken(request);
        User existUser = userRepo.getUserByEmail(email);

        if (existUser == null) {
            result.setMsg("wrong email address!");
            result.setCode(1);
            return result;
        }
        else {

            existUser.setPassword(DigestUtils.md5DigestAsHex(user.getPassword() .getBytes()));
            existUser.setName(user.getUsername());
            userRepo.save(existUser);
            result.setMsg("change name and password successfully");
            result.setCode(0);
            return result;
        }


    }


    //change role
    @RequestMapping(value = "changeRole", method = RequestMethod.POST)
    @CrossOrigin
    public Result changeRole(HttpServletRequest request) {
        Result result = new Result();

            String email = JwtUtil.getUserEmailByToken(request);
            User existUser = userRepo.getUserByEmail(email);

            if (existUser.getIsCreator().equals("user")) existUser.setIsCreator("creator");
            else existUser.setIsCreator("user");

            userRepo.save(existUser);
            result.setMsg("change role successfully");
            result.setCode(0);
            return result;


    }





    //getUserInfo
    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    @CrossOrigin
    public Result getUserInfo(HttpServletRequest request) {
        Result result = new Result();
        String token = request.getHeader("Authorization");



        String email = JwtUtil.getUserEmailByToken(request);
        try{
            User user1 = userRepo.getUserByEmail(email);
            String pass = user1.getPassword();
            boolean ans = JwtUtil.verify(token,email,pass);
            if(ans){

                result.setCode(0);
                HashMap<String,String> map = new HashMap<>();
                map.put("role",user1.getIsCreator());
                map.put("email",user1.getEmail());
                map.put("avatar",user1.getProfilePicStore());
                map.put("username",user1.getUsername());
                result.setData(map);
                return result;

            }
        }catch (Exception e) {
            System.out.println(e.toString());
            result.setCode(-2);
            return result;
        }


        result.setCode(1);
        result.setMsg("token failed");
        return result;


    }





//    //add profile picture
//    @RequestMapping(value = "api/addProfilePicture", method = RequestMethod.POST)
//    @CrossOrigin
//    public Result addProfilePicture(@RequestParam String email,  @RequestParam String newPassword, @RequestParam String name) {
//        Result result = new Result();
//        User existUser = userRepo.getUserByEmail(email);
//
//        if (existUser == null) {
//            result.setMsg("wrong email address!");
//            result.setCode(400);
//            return result;
//        }
//        else {
//            existUser.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
//            existUser.setName(name);
//            userRepo.save(existUser);
//            result.setMsg("change password successfully");
//            result.setCode(201);
//            return result;
//        }
//
//
//    }

    //getAllFavouriteList
    @RequestMapping(value = "getFavouriteList", method = RequestMethod.GET)
    @CrossOrigin
    public Result getFavouriteList(HttpServletRequest request) {
        Result result = new Result();
        String email = JwtUtil.getUserEmailByToken(request);
        User existUser = userRepo.getUserByEmail(email);
        List<User> favouriteList = new ArrayList<>();
        List<User> users = userRepo.findAll();
        String now = existUser.getFavoriteId();
        if (now != null) {
            String[] str = now.split(",");

            List<Integer> ids = new ArrayList<>();
            for (String s : str) ids.add(Integer.valueOf(s));

            for (Integer id : ids) {
                for (User user : users) {
                    if (id == user.getId()) {
                        favouriteList.add(user);
                        break;
                    }
                }
            }
        }

        result.setMsg("OK");
        result.setCode(0);
        result.setData(favouriteList);
        return result;

    }



    //getAllSubscribeList
    @RequestMapping(value = "getSubscribeList", method = RequestMethod.GET)
    @CrossOrigin
    public Result getSubscribeList(HttpServletRequest request) {
        Result result = new Result();
        String email = JwtUtil.getUserEmailByToken(request);
        User existUser = userRepo.getUserByEmail(email);

        if (existUser == null) {
            result.setMsg("wrong email address!");
            result.setCode(1);
            return result;
        }
        else {
            List<User> subscribeList = new ArrayList<>();
            List<User> users = userRepo.findAll();


            String now = existUser.getSubscribeId();
            if (now != null) {
                String[] str = now.split(",");
                List<Integer> ids = new ArrayList<>();
                for (String s : str) ids.add(Integer.valueOf(s));
                for (int i = 0; i < ids.size(); i++) {
                    for (int j = 0; j < users.size(); j++) {
                        if (ids.get(i) == users.get(j).getId()) {
                            subscribeList.add(users.get(j));
                            break;
                        }
                    }
                }
            }



            result.setCode(0);
            result.setData(subscribeList);
            return result;
        }


    }


    //delete favourite
    @RequestMapping(value = "deleteFavouriteList", method = RequestMethod.POST)
    @CrossOrigin
    public Result deleteFavouriteList(HttpServletRequest request,@RequestParam Integer id) {
        Result result = new Result();
        String email = JwtUtil.getUserEmailByToken(request);
        User existUser = userRepo.getUserByEmail(email);

        String now = existUser.getFavoriteId();
        if (now != null) {
            String[] str = now.split(",");
            List<String> favouriteList = Arrays.asList(str);
            favouriteList.remove(String.valueOf(id));
            String[] strs = favouriteList.toArray(new String[favouriteList.size()]);
            existUser.setFavoriteId(Arrays.toString(strs));
        }
        
        result.setMsg("OK");
        result.setCode(0);

        return result;

    }









}
