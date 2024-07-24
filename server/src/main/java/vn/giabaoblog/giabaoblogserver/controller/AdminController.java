package vn.giabaoblog.giabaoblogserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.RoleIdDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.RoleUserDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.UserIdDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.UserListDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.response.StandardResponse;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.UserDTO;
import vn.giabaoblog.giabaoblogserver.data.repository.RoleRepository;
import vn.giabaoblog.giabaoblogserver.services.UserService;


import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/admin")
@PreAuthorize("hasAuthority('ADMINISTRATOR')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/allUsers")
    public ResponseEntity<UserListDTO> getUserPresentationList() {
        List<UserDTO> list = userService.getUserPresentationList();
        UserListDTO userListDTO = new UserListDTO();
        list.stream().forEach(e -> userListDTO.getUserList().add(e));
        return ResponseEntity.ok(userListDTO);
    }

    @PostMapping("/addRole")
    public StandardResponse addRole(@RequestBody RoleUserDTO request) throws RoleNotFoundException {
        return StandardResponse.create("200", "Role added success",
                new UserDTO(userService.addRole(request.getRoleId(), request.getUserId())));
    }

    @DeleteMapping("/removeRole")
    public StandardResponse removeRole(@RequestBody RoleUserDTO request) throws RoleNotFoundException {
        return StandardResponse.create("204", "Role deleted success",
                new UserDTO(userService.removeRole(request.getRoleId(), request.getUserId())));
    }

    @GetMapping("/disableUser")
    public StandardResponse disableUser(@RequestBody UserIdDTO request) {
        userService.disableUser(request.getUserId());
        return StandardResponse.create("200", "User disabled success", request.getUserId());
    }

    @GetMapping("/enableUser")
    public StandardResponse enableUser(@RequestBody UserIdDTO request) {
        userService.enableUser(request.getUserId());
        return StandardResponse.create("200", "User enabled success", request.getUserId());
    }

    // get Permission by userId
    @GetMapping("/permissionsByUserId")
    public Set<String> getPermissionByUserId(@RequestBody UserIdDTO request) {
        Set<String> permissions = userService.getPermissionByUserId(request.getUserId());
        return permissions;
    }

    // get Role by userId
    @GetMapping("/rolesByUserId")
    public Set<String> getRoleByUserId(@RequestBody UserIdDTO request) {
        Set<String> roles = userService.getRoleByUserId(request.getUserId());
        return roles;
    }

    // count users has role
    @GetMapping("/countUserWithRole")
    public Long countUserWithRole(@RequestBody RoleIdDTO request) throws RoleNotFoundException {
        return userService.countUserWithRole(request.getRoleId());
    }

}
