package com.modular.web.controllers;

import com.listing.common.dto.UserDto;
import com.listing.common.dto.UserSaveDto;
import com.listing.common.model.User;
import com.listing.common.servicies.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper mapper;

    @GetMapping("/users")
    public String users(ModelMap modelMap) {
        List<User> users = userService.findAll();
        List<UserDto> allUsers = new LinkedList<>();
        for (User user : users) {
            UserDto userDto = mapper.map(user, UserDto.class);
            allUsers.add(userDto);
        }
        modelMap.addAttribute("allUsers", allUsers);
        return "users";
    }

    @GetMapping("/users/{id}")
    public String user(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<User> byId = userService.findById(id);
        if (byId.isEmpty()) {
            return "redirect:/";
        }
        UserDto userDto = mapper.map(byId.get(), UserDto.class);
        modelMap.addAttribute("userDto", userDto);
        return "singleUser";
    }

    @PostMapping("/users")
    public String addUser(@RequestBody UserSaveDto userSaveDto) {
        userService.save(mapper.map(userSaveDto, User.class));
        return "redirect:/users";
    }

    @PutMapping("/users/{id}")
    public String changeUser(@PathVariable("id") int id, @RequestBody UserSaveDto userSaveDto) {
        userService.updateUser(mapper.map(userSaveDto, User.class), id);
        return "redirect:/users";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

}
