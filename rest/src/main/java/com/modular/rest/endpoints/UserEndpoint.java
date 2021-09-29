package com.modular.rest.endpoints;

import com.listing.common.dto.UserDto;
import com.listing.common.dto.UserSaveDto;
import com.listing.common.model.User;
import com.listing.common.servicies.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserEndpoint {
    private final UserService userService;
    private final ModelMapper mapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> users() {
        List<User> users = userService.findAll();
        List<UserDto> allUsers = new LinkedList<>();
        for (User user : users) {
            UserDto userDto = mapper.map(user, UserDto.class);
            allUsers.add(userDto);
        }

        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> user(@PathVariable("id") int id) {
        Optional<User> byId = userService.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UserDto userDto = mapper.map(byId.get(), UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> addUser(@RequestBody UserSaveDto userSaveDto) {

        User save = userService.save(mapper.map(userSaveDto, User.class));
        if (save == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.map(save, UserDto.class));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> changeUser(@PathVariable("id") int id, @RequestBody UserSaveDto userSaveDto) {
        User updatedUser = userService.updateUser(mapper.map(userSaveDto, User.class), id);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.map(updatedUser, UserDto.class));
    }

    @DeleteMapping("/users/{id}")
    @ApiOperation(notes = "this is for deleting", value = "delete user by ID")
    public ResponseEntity deleteUser(@PathVariable("id") int id) {
        boolean isDeleted = userService.deleteById(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
