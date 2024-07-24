package vn.giabaoblog.giabaoblogserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.giabaoblog.giabaoblogserver.data.domains.User;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.FilterUserDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.PermissionKeyDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.RoleNameDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.UserIdDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.request.*;
import vn.giabaoblog.giabaoblogserver.data.dto.response.StandardResponse;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.CreateOrUpdateUserDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.UserDTO;
import vn.giabaoblog.giabaoblogserver.data.repository.RoleRepository;
import vn.giabaoblog.giabaoblogserver.services.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/forgotpassword")
    public StandardResponse forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.forgotPassword(request.getUsername(), request.getEmail());
        return StandardResponse.create("200", "Reset token sent to your email", request.getEmail());
    }

    @PostMapping("/verifyresettoken")
    public boolean verifyResetToken(@RequestBody VerifyResetTokenRequest request) {
        return userService.verifyResetToken(request.getUsername(), request.getResetToken());
    }

    @PostMapping("/resetpassword")
    public StandardResponse resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getResetToken(), request.getNewPassword());
        return StandardResponse.create("200", "Reset password success");
    }

    @PostMapping("/update")
    public StandardResponse updateUser(@RequestBody UpdateProfileRequest request) {
        UserDTO userDTO = userService.updateUser(request);
        return StandardResponse.create("200", "Updated profile", userDTO);
    }

    @DeleteMapping("/delete")
    public StandardResponse deleteUser(@RequestBody UserIdDTO request) {
        userService.deleteUser(request.getUserId());
        return StandardResponse.create("204", "User deleted success", request.getUserId());
    }

    @PostMapping("/changePassword")
    public StandardResponse changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request.getUserId(), request.getPassword(), request.getNewPassword());
        return StandardResponse.create("200", "Change password success", request.getUserId());
    }

    // get permission by access token
    @GetMapping("/permission/getPermissionByAccessToken")
    public Set<String> getPermissionsByAccessToken() {
        Set<String> permissions = userService.getPermissionsByAccessToken();
        return permissions;
    }

    // check permission by access token

    @GetMapping("/checkpermission")
    public boolean checkPermissionByAccessToken(@RequestBody PermissionKeyDTO request) {
        return userService.checkPermissionByAccessToken(request.getPermissionKey());
    }

    // find user with keyword

    @GetMapping("/findUserByUsername")
    public List<UserDTO> filterUserWithUsername(@RequestBody FilterUserDTO request) {
        return userService.filterUserWithUsername(request.getKeyword());
    }

    @GetMapping("/findUserByRole")
    public List<UserDTO> filterUserByRole(@RequestBody RoleNameDTO request) {
        return userService.filterUserWithRole(request.getRole());
    }

    @GetMapping("/findUserByPermission")
    public List<UserDTO> filterUserByPermission(@RequestBody PermissionKeyDTO request) {
        return userService.filterUserWithPermission(request.getPermissionKey());
    }

    @PostMapping("/filterUser")
    public Page<User> filterUser(@RequestBody SearchUserRequest request) {
        return userService.filterUser(request);
    }

    @PostMapping("/follow")
    public StandardResponse followUser(@RequestBody UserIdDTO request) {
        userService.followUser(request.getUserId());
        return StandardResponse.create("200", "Follow success", request.getUserId());
    }

    @PostMapping("/unfollow")
    public StandardResponse unfollowUser(@RequestBody UserIdDTO request) {
        userService.unfollowUser(request.getUserId());
        return StandardResponse.create("200", "Unfollow success", request.getUserId());
    }

    @GetMapping("/getFollow")
    public List<Long> getFollowUsers() {
        return userService.getFollow();
    }

    @PostMapping("/getUsernameById")
    public String getUsernameById(@RequestBody UserIdDTO request) {
        return userService.getUsernameById(request.getUserId());
    }

    @PostMapping("/getProfile")
    public StandardResponse getProfileById() {
        UserDTO userDTO = userService.getProfileById();
        return StandardResponse.create("200", "Get profile by id", userDTO);
    }
}
