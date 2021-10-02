package com.modular.rest.endpoints;

import com.listing.common.dto.UserAuthDto;
import com.listing.common.dto.UserAuthResponseDto;
import com.listing.common.dto.UserDto;
import com.listing.common.dto.UserSaveDto;
import com.listing.common.model.User;
import com.listing.common.servicies.UserService;
import com.modular.rest.util.JWTUtilToken;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserEndpoint {
    private final UserService userService;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtilToken jwtUtilToken;

    @PostMapping
    public ResponseEntity auth(@RequestBody UserAuthDto userAuthDto){
        User user = userService.findByEmail(userAuthDto.getEmail());
        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (passwordEncoder.matches(user.getPassword(), userAuthDto.getPassword())) {
            return ResponseEntity.ok(UserAuthResponseDto.builder()
                    .userDto(mapper.map(user, UserDto.class))
                    .token(jwtUtilToken.generateToken(user.getEmail()))
                    .build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


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
        userSaveDto.setPassword(passwordEncoder.encode(userSaveDto.getPassword()));
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
