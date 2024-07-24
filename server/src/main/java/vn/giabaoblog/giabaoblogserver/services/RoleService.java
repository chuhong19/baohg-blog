package vn.giabaoblog.giabaoblogserver.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.config.exception.*;
import vn.giabaoblog.giabaoblogserver.data.domains.Permission;
import vn.giabaoblog.giabaoblogserver.data.domains.Role;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.RoleDTO;
import vn.giabaoblog.giabaoblogserver.data.repository.PermissionRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class RoleService {
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Iterable<Role> getRoleList() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        if (id == null) {
            throw new InvalidRoleDataException("Id role cannot be null");
        }
        Optional<Role> roleOpt = roleRepository.findById(id);
        if (roleOpt.isPresent()) {
            return roleOpt.get();
        }
        throw new NotFoundException(String.format("Role not found for Id = %s", id));
    }

    public static void validateRoleName(String roleName) {
        if (Objects.isNull(roleName) || roleName.trim().isEmpty()) {
            throw new InvalidRoleDataException(String.format("Invalid role name: %s", roleName));
        }
    }

    @Transactional
    public Role createRole(String roleStr) {
        validateRoleName(roleStr);
        Optional<Role> roleOptional = roleRepository.findByRole(roleStr);
        if (!roleOptional.isEmpty()) {
            String errMsg = String.format("The role %s already exists", roleStr);
            log.error(errMsg);
            throw new RoleInUseException(errMsg);
        }
        Role role = new Role();
        role.setRole(roleStr);
        roleRepository.save(role);
        log.info(String.format("Role %s %s has been created.", role.getId(), role.getRole()));
        return role;
    }

    @Transactional
    public void deleteRole(Long id) {
        Optional<Role> roleOpt = roleRepository.findById(id);
        if (!roleOpt.isPresent()) {
            String errMsg = String.format("Role not found with Id = %s", id);
            log.error(errMsg);
            throw new NotFoundException(errMsg);
        }

        Role role = roleOpt.get();

        // check if the role is in use
        Long countUsages = roleRepository.countRoleUsage(id);
        if (countUsages > 0) {
            String errMsg = String.format("The role %s %s is in use (%s users_roles configuration rows)" +
                    " and cannot be deleted", role.getId(), role.getRole(), countUsages);
            log.error(errMsg);
            throw new RoleInUseException(errMsg);
        }

        roleRepository.deleteById(id);
        log.info(String.format("Role %s has been deleted.", id));
    }

    public static void validatePermissionKey(String permissionKey) {
        if (permissionKey == null || permissionKey.isBlank()) {
            throw new InvalidPermissionDataException("Permission key cannot be null or empty");
        }
    }

    @Transactional
    public Role addPermissionOnRole(String permissionKey, Long roleId) {
        // Check null
        // Check tồn tại role & permissionKey
        // Lấy ra 2 id, Check tồn tại permission on role từ trước trong bảng permission_role
        // Add vào list
        validatePermissionKey(permissionKey);

        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new MyRoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }
        Role role = roleOpt.get();

        Permission permission;

        Optional<Permission> permissionOpt = permissionRepository.findByPermission(permissionKey);
        if (permissionOpt.isPresent()) {
            permission = permissionOpt.get();
        } else {
            permission = new Permission();
            permission.setPermission(permissionKey);
            permissionRepository.save(permission);
        }

        if (role.getPermissions().contains(permission)) {
            throw new InvalidPermissionDataException(String.format("The permission %s has been already" +
                    " associated on the role %s", permission.getPermission(), role.getRole() ));
        }

        role.getPermissions().add(permission);
        roleRepository.save(role);

        log.info(String.format("Added permission %s on role id = %s", permissionKey, roleId));
        return roleRepository.findById(roleId).get();
    }

    @Transactional
    public Role removePermissionOnRole(String permissionKey, Long roleId) {
        validatePermissionKey(permissionKey);

        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new MyRoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }
        Role role = roleOpt.get();

        Optional<Permission> permissionOpt = permissionRepository.findByPermission(permissionKey);
        if (!permissionOpt.isPresent()) {
            throw new PermissionNotFoundException(String.format("Permission not found with Id = %s on role %s", permissionKey, roleId));
        }
        Permission permission = permissionOpt.get();

        role.getPermissions().remove(permission);
        roleRepository.save(role);

        log.info(String.format("Removed permission %s from role id = %s", permissionKey, roleId));
        return roleRepository.findById(roleId).get();
    }

    public List<RoleDTO> findRoleByName(String keyword) {
        Iterable<Role> allRoles = roleRepository.findAll();
        List<RoleDTO> filteredRoles = new ArrayList<>();
        for (Role role : allRoles) {
            if (role.getRole().equals(keyword)) {
                filteredRoles.add(new RoleDTO(role));
            }
        }
        return filteredRoles;
    }

    public List<RoleDTO> findRoleByPermission(String permissionKey) {
        Iterable<Role> allRoles = roleRepository.findAll();
        List<RoleDTO> filteredRoles = new ArrayList<>();
        for (Role role : allRoles) {
            for (Permission permission : role.getPermissions()) {
                if (permission.getPermission().equals(permissionKey)) {
                    filteredRoles.add(new RoleDTO(role));
                }
            }
        }
        return filteredRoles;
    }
}
