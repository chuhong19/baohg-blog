package vn.giabaoblog.giabaoblogserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.giabaoblog.giabaoblogserver.data.domains.Permission;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.PermissionKeyDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.PermissionUserDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.RoleIdDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.RolePermissionDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.response.StandardResponse;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.PermissionDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.RoleDTO;
import vn.giabaoblog.giabaoblogserver.services.PermissionService;
import vn.giabaoblog.giabaoblogserver.services.RoleService;
import vn.giabaoblog.giabaoblogserver.services.UserService;

import javax.management.relation.RoleNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/permission")
@PreAuthorize("hasAuthority('ADMINISTRATOR')")
public class PermissionController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    // retrieve the permission's list
    @GetMapping("/allPermissions")
    public ResponseEntity<List<PermissionDTO>> getPermissionPresentationList() {
        Iterable<Permission> permissionList = permissionService.getPermissionList();
        ArrayList<PermissionDTO> list = new ArrayList<>();
        permissionList.forEach(e -> list.add(new PermissionDTO(e)));
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getPermissionByKey")
    public ResponseEntity<PermissionDTO> getPermissionByKey(@RequestBody PermissionKeyDTO request) {
        PermissionDTO permissionDTO = new PermissionDTO(permissionService.getPermissionByKey(request.getPermissionKey()));
        return ResponseEntity.ok(permissionDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO) {
        return new ResponseEntity(new PermissionDTO(permissionService.createPermission(permissionDTO)), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<PermissionDTO> updatePermission(@RequestBody PermissionDTO permissionDTO) {
        return new ResponseEntity(new PermissionDTO(permissionService.updatePermission(permissionDTO)), HttpStatus.CREATED);
    }

    @DeleteMapping("/deletePermissionByKey")
    public StandardResponse deletePermissionByKey(@RequestBody PermissionKeyDTO request) {
        permissionService.deletePermissionByKey(request.getPermissionKey());
        return StandardResponse.create("204", "Permission deleted successfully", request.getPermissionKey());
    }

    @GetMapping("/addPermissionToRole")
    public ResponseEntity<RoleDTO> addPermissionOnRole(@RequestBody RolePermissionDTO request) {
        return new ResponseEntity(new RoleDTO(
                roleService.addPermissionOnRole(request.getPermissionKey(), request.getRoleId())), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/removePermissionOnRole")
    public ResponseEntity<RoleDTO> removePermissionOnRole(@RequestBody RolePermissionDTO request) {
        return new ResponseEntity(new RoleDTO(
                roleService.removePermissionOnRole(request.getPermissionKey(), request.getRoleId())), null, HttpStatus.OK);
    }

    // Get permission by role id
    @PostMapping("/getPermissionByRoleId")
    public Set<String> getPermissionByRoleId(@RequestBody RoleIdDTO request) throws RoleNotFoundException {
        Set<String> permissions = userService.getPermissionByRoleId(request.getRoleId());
        return permissions;
    }

    @GetMapping("/addSpecialpermission")
    public StandardResponse addSpecialPermission(@RequestBody PermissionUserDTO request) {
        userService.addSpecialPermission(request.getPermissionId(), request.getUserId());
        return StandardResponse.create("200", "Special permission added to user", request.getUserId());
    }

    @GetMapping("/removeSpecialpermission")
    public StandardResponse removeSpecialPermission(@RequestBody PermissionUserDTO request) {
        userService.removeSpecialPermission(request.getPermissionId(), request.getUserId());
        return StandardResponse.create("204", "Special permission removed from user", request.getUserId());
    }

    // get permission by access token
    @GetMapping("/getByAccessToken")
    public Set<String> getPermissionsByAccessToken() {
        Set<String> permissions = userService.getPermissionsByAccessToken();
        return permissions;
    }

    @GetMapping("/check/{permission}")
    public boolean checkPermissionByAccessToken(@PathVariable("permission") String permissionToCheck) {
        boolean res = userService.checkPermissionByAccessToken(permissionToCheck);
        return res;
    }

}
