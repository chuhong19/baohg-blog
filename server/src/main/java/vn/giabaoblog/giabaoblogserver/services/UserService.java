package vn.giabaoblog.giabaoblogserver.services;

import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.config.exception.*;
import vn.giabaoblog.giabaoblogserver.data.domains.*;
import vn.giabaoblog.giabaoblogserver.data.dto.request.SearchUserRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.request.UpdateProfileRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.CreateOrUpdateUserDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.UserDTO;
import vn.giabaoblog.giabaoblogserver.data.repository.PermissionRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.RoleRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.UserFollowRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.UserRepository;
import vn.giabaoblog.giabaoblogserver.services.support.EmailService;
import vn.giabaoblog.giabaoblogserver.services.validation.PasswordValidatorService;

import javax.management.relation.RoleNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@CommonsLog
@Service
public class UserService {

    @Autowired
    private final UserFollowRepository userFollowRepository;

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final PermissionRepository permissionRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final RoleService roleService;

    private final PasswordValidatorService passwordValidatorService;

    public UserService(UserFollowRepository userFollowRepository, UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder, EmailService emailService, RoleService roleService) {
        this.userFollowRepository = userFollowRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.roleService = roleService;
        passwordValidatorService = new PasswordValidatorService();
    }

    private String salt = "jS9nJFFbhuIYnkpLg47PQZHD4ylhtBOP";

    public Iterable<User> getUserList() {
        return userRepository.findAll();
    }

    public List<UserDTO> getUserPresentationList() {
        ArrayList<UserDTO> listDto = new ArrayList<>();
        Iterable<User> list = getUserList();
        list.forEach(user -> listDto.add(new UserDTO(user)));
        return listDto;
    }

    public User loadUserById(final Long id) throws NotFoundException {
        Optional<User> opUser = userRepository.findById(id);
        final User user = opUser.orElseThrow(() -> new NotFoundException("ID not found"));
        return user;
    }

    public User loadUserByUsernameOrEmail(String username) throws NotFoundException {
        final User user = userRepository.findByUsernameOrEmail(username).orElseThrow(() -> new NotFoundException("Username not found"));
        return user;
    }

    public boolean emailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            return true;
        }
        return false;
    }

    public boolean userExists(String username) {
        if (userRepository.existsByUsername(username)) {
            return true;
        }
        return false;
    }

    public User getUserByUsername(String username) {
        if (username == null) {
            throw new InvalidUsernameException("Username cannot be null");
        }
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email) {
        if (email == null) {
            throw new InvalidEmailException("email cannot be null");
        }
        return userRepository.findByEmail(email);
    }

    @Transactional
    public UserDTO updateUser(UpdateProfileRequest updateUserDTO) {
        Long id = updateUserDTO.getUserId();
        if (id == null) {
            throw new InvalidUserDataException("Id cannot be null");
        }
        if (updateUserDTO == null) {
            throw new InvalidUserDataException("User account data cannot be null");
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("The user with Id = %s doesn't exists", id));
        }
        User user = userOpt.get();

        user.setFirstname(updateUserDTO.getFirstname());
        user.setLastname(updateUserDTO.getLastname());
        user.setGender(updateUserDTO.getGender());

        userRepository.save(user);
        log.info(String.format("User %s has been updated.", user.getId()));
        UserDTO userDTO = new UserDTO(user);
        return userDTO;
    }

    @Transactional
    public User addRole(Long roleId, Long userId) throws RoleNotFoundException {
        // check user
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", userId));
        }
        User user = userOpt.get();

        // check role
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }

        Role role = roleOpt.get();

        user.getRoles().add(role);

        userRepository.save(user);
        log.info(String.format("Added role %s on user id = %s", role.getRole(), user.getId()));

        return user;
    }

    @Transactional
    public User removeRole(Long roleId, Long userId) throws RoleNotFoundException {
        // check user
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", userId));
        }
        User user = userOpt.get();

        // check role
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }

        Role role = roleOpt.get();

        user.getRoles().remove(role);

        userRepository.save(user);
        log.info(String.format("Removed role %s on user id = %s", role.getRole(), user.getId()));

        return user;
    }

    public Set<String> getRoleByUserId(Long userId) {
        // check user
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", userId));
        }

        Set<String> roles = new HashSet<>();
        User user = userOpt.get();
        for (Role role : user.getRoles()) {
            roles.add(role.getRole());
        }
        return roles;
    }

    public Set<String> getPermissionByUserId(Long userId) {
        // check user
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", userId));
        }

        Set<String> permissions = new HashSet<>();
        User user = userOpt.get();
        // ...
        for (Role role : user.getRoles()) {
            // Lấy danh sách quyền từ mỗi Role và thêm vào danh sách permissions
            permissions.addAll(role.getPermissions().stream()
                    .map(Permission::getPermission)
                    .collect(Collectors.toSet()));
        }
        return permissions;
    }

    public Set<String> getPermissionsByAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            Long userId = user.getId();
            return getPermissionByUserId(userId);
        } else {
            // Xử lý khi không thể lấy thông tin User từ SecurityContextHolder
            return Collections.emptySet();
        }
    }

    public Set<String> getPermissionByRoleId(Long roleId) throws RoleNotFoundException {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }
        Role role = roleOpt.get();
        Set<String> permissions = new HashSet<>();
        for (Permission permission : role.getPermissions()) {
            permissions.add(permission.getPermission());
        }
        return permissions;
    }

    public boolean checkPermissionByAccessToken(String permissionToCheck) {
        Set<String> acceptedPermissions = getPermissionsByAccessToken();
        return acceptedPermissions.contains(permissionToCheck);
    }

    public void forgotPassword(String username, String email) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(username);
        if (!userOpt.isPresent()) {
            throw new InvalidUsernameException(String.format("User not found with username = %s", username));
        }
        User user = userOpt.get();
        if (!user.getEmail().equals(email)) {
            throw new InvalidUserDataException(String.format("Email not found with email = %s", email));
        }
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.save(user);

        emailService.sendResetPasswordEmail(user.getEmail(), resetToken);
    }

    public boolean verifyResetToken(String username, String resetToken) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(username);
        if (!userOpt.isPresent()) {
            throw new InvalidUsernameException(String.format("User not found with username = %s", username));
        }
        User user = userOpt.get();
        if (!user.getResetToken().equals(resetToken)) {
            throw new InvalidUserDataException(String.format("Reset token not match with reset token = %s", resetToken));
        }
        return true;
    }

    public void resetPassword(String resetToken, String newPassword) {
        User user = userRepository.findByResetToken(resetToken)
                .orElseThrow(() -> new InvalidUserDataException("Invalid reset token"));
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new InvalidUserDataException("Please don't use your old password!");
        }
        passwordValidatorService.checkPassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
    }

    public void changePassword(Long userId, String password, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new InvalidUsernameException(String.format("User not found with id = %s", userId));
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidUserDataException("Wrong password");
        }
        passwordValidatorService.checkPassword(newPassword);
        if (newPassword.equals(password)) {
            throw new InvalidUserDataException("Please don't use your old password!");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new InvalidUsernameException(String.format("User not found with id = %s", userId));
        }
        User user = userOpt.get();
        userRepository.delete(user);
    }

    public void disableUser(Long userId) {
        System.out.println("...");
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new InvalidUsernameException(String.format("User not found with id = %s", userId));
        }
        User user = userOpt.get();
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void enableUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new InvalidUsernameException(String.format("User not found with id = %s", userId));
        }
        User user = userOpt.get();
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void addSpecialPermission(Long permissionId, Long userId) {
        // check user
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", userId));
        }
        User user = userOpt.get();

        // check permission
        Optional<Permission> permissionOpt = permissionRepository.findById(permissionId);
        if (!permissionOpt.isPresent()) {
            throw new PermissionNotFoundException(String.format("Permission not found with Id = %s", permissionId));
        }

        Permission permission = permissionOpt.get();

        user.getSpecial_permissions().add(permission);

        userRepository.save(user);
        log.info(String.format("Added special permission %s on user id = %s", permission.getPermission(), user.getId()));
    }

    public void removeSpecialPermission(Long permissionId, Long userId) {
        // check user
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", userId));
        }
        User user = userOpt.get();

        // check permission
        Optional<Permission> permissionOpt = permissionRepository.findById(permissionId);
        if (!permissionOpt.isPresent()) {
            throw new PermissionNotFoundException(String.format("Permission not found with Id = %s", permissionId));
        }

        Permission permission = permissionOpt.get();

        user.getSpecial_permissions().remove(permission);

        userRepository.save(user);
        log.info(String.format("Removed special permission %s on user id = %s", permission.getPermission(), user.getId()));
    }

    public Long countUserWithRole(Long roleId) throws RoleNotFoundException {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }
        Role role = roleOpt.get();
        return roleRepository.countRoleUsage(roleId);
    }

    public List<UserDTO> filterUserWithUsername(String keyword) {
        List<UserDTO> allUsers = getUserPresentationList();

        return allUsers.stream()
                .filter(user -> user.getUsername().contains(keyword))
                .collect(Collectors.toList());
    }

    public List<UserDTO> filterUserWithRole(String role) {
        List<UserDTO> allUsers = getUserPresentationList();

        return allUsers.stream()
                .filter(user -> user.getRoles().contains(role))
                .collect(Collectors.toList());
    }

    public List<UserDTO> filterUserWithPermission(String permissionKey) {
        List<UserDTO> allUsers = getUserPresentationList();
        Iterable<Role> allRoles = roleService.getRoleList();
        List<String> rolesWithPermission = new ArrayList<>();
        for (Role role : allRoles) {
            for (Permission permission : role.getPermissions()) {
                if (permissionKey.equals(permission.getPermission())) {
                    rolesWithPermission.add(role.getRole());
                    break;
                }
            }
        }
        List<UserDTO> filteredUsers = new ArrayList<>();
        for (UserDTO user : allUsers) {
            for (String userRole : user.getRoles()) {
                if (rolesWithPermission.contains(userRole)) {
                    filteredUsers.add(user);
                    break;
                }
            }
        }
        return filteredUsers;
    }

    public Page<User> filterUser(SearchUserRequest request) {

        List<String> usernames = request.getUsernames();
        List<String> emails = request.getEmails();
        List<String> roles = request.getRoles();
        List<String> permissions = request.getPermissions();
        Integer page = request.getPage();
        Integer limit = request.getLimit();

        Specification<User> conditions = (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (usernames != null && !usernames.isEmpty()) {
                predicates.add(root.get(User_.USERNAME).in(usernames));
            }
            System.out.println("Predicates 1: " + predicates);

            if (emails != null && !emails.isEmpty()) {
                predicates.add(root.get(User_.EMAIL).in(emails));
            }
            System.out.println("Predicates 2: " + predicates);
            if (roles != null && !roles.isEmpty()) {
                predicates.add(root.join("roles", JoinType.INNER).get("role").in(roles));
            }

            if (permissions != null && !permissions.isEmpty()) {
                predicates.add(root.join("roles").join("permissions").get("permission").in(permissions));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(page, limit, Sort.by(User_.ID).descending());

        return userRepository.findAll(conditions, pageable);
    }

    public List<Long> getFollow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long myId = principal.getId();

        List<UserFollow> userFollowList = userFollowRepository.findByUserId(myId);

        List<Long> followIdList = userFollowList.stream()
                .map(UserFollow::getFollowId)
                .collect(Collectors.toList());

        return followIdList;
    }

    public void followUser(Long followId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long myId = principal.getId();
        if (myId.equals(followId)) {
            throw new DuplicateException(String.format("You can't follow yourself with Id = %s", followId));
        }
        // check exist
        List<Long> myFollowUser = getFollow();
        boolean checkExist = myFollowUser.contains(followId);
        if (checkExist) {
            throw new DuplicateException(String.format("User followed already with Id = %s", followId));
        }
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(myId);
        userFollow.setFollowId(followId);
        userFollowRepository.save(userFollow);
    }

    public void unfollowUser(Long followId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long myId = principal.getId();
        if (myId.equals(followId)) {
            throw new DuplicateException(String.format("You can't unfollow yourself with Id = %s", followId));
        }
        // check exist
        List<Long> myFollowUser = getFollow();
        boolean checkExist = myFollowUser.contains(followId);
        if (!checkExist) {
            throw new DuplicateException(String.format("You haven't followed this user with Id = %s", followId));
        }
        Optional<UserFollow> recordOpt = userFollowRepository.findByUserIdAndFollowId(myId, followId);
        if (recordOpt.isPresent()) {
            UserFollow record = recordOpt.get();
            userFollowRepository.delete(record);
        } else {
            throw new NotFoundException(String.format("You haven't followed this user with Id = %s", followId));
        }
    }

    public String getUsernameById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", userId));
        }
        User user = userOpt.get();
        return user.getUsername();
    }

    public UserDTO getProfileById() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User principal = (User) authentication.getPrincipal();
            Long userId = principal.getId();
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                throw new UserNotFoundException("Please login first");
            }
            User user = userOpt.get();
            return new UserDTO(user);
        } catch (Exception e) {
            throw new InvalidPermissionDataException("Please login first");
        }
    }
}
