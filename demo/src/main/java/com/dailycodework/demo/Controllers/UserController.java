package com.dailycodework.demo.Controllers;

import com.dailycodework.demo.Response.ApiResponse;
import com.dailycodework.demo.model.User;
import com.dailycodework.demo.service.User.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(  @PathVariable Long userId){
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse("Success" , user));
        } catch (ResourceAccessException e) {
          return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }
    }


}
