package org.example.userservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dtos.*;
import org.example.userservice.models.Role;
import org.example.userservice.models.Users;
import org.example.userservice.repositories.RoleRepository;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.requestBodies.*;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public String createAdmin(@RequestBody AdminCreateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = auth.getName();

        Users loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        if (!loggedInUser.getRole().getName().equals("SUPER ADMIN")) {
            throw new RuntimeException("Only Super Admins can create new admins");
        }


        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Role adminRole = roleRepository.findByName("ADMIN");

        Users admin = new Users();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(adminRole);

        userRepository.save(admin);

        return "Admin created";
    }


    public String banAdmin(@RequestBody BanUserRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users superAdmin = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Super Admin not found"));
        Users targetAdmin = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        if (superAdmin.getId() == targetAdmin.getId()) {
            throw new RuntimeException("You cannot ban yourself");
        }

        if (!targetAdmin.getRole().getName().equalsIgnoreCase("ADMIN")) {
            if (targetAdmin.getRole().getName().equalsIgnoreCase("SUPER_ADMIN")) {
                throw new RuntimeException("You are not allowed to ban another Super Admin");
            }
            throw new RuntimeException("User is not an Admin");
        }

        if (targetAdmin.isBanned()) {
            throw new RuntimeException("Admin is already banned");
        }

        targetAdmin.setBanned(true);
        targetAdmin.setBanReason(request.getReason());

        if (request.getDuration() == 0) {
            // Permanent Ban
            targetAdmin.setBanExpiresAt(null);
        } else {
            targetAdmin.setBanExpiresAt(LocalDateTime.now().plusDays(request.getDuration()));
        }

        userRepository.save(targetAdmin);

        return "Admin banned successfully";
    }

    public BulkCreateResponse bulkCreateUsers(List<BulkCreateUserRequest> requests) {
        BulkCreateResponse result = new BulkCreateResponse();
        if (requests == null || requests.isEmpty()) {
            return result;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated");
        }
        String callerEmail = auth.getName();
        Users caller = userRepository.findByEmail(callerEmail)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        String callerRole = caller.getRole() != null ? caller.getRole().getName() : "";
        if (!(callerRole.equalsIgnoreCase("ADMIN") || callerRole.equalsIgnoreCase("SUPER ADMIN"))) {
            throw new RuntimeException("Only Admins or Super Admins can create users");
        }

        Map<String, Integer> emailFirstIndex = new HashMap<>();
        Set<String> duplicateInRequest = new HashSet<>();
        for (int i = 0; i < requests.size(); i++) {
            String email = requests.get(i).getEmail().toLowerCase().trim();
            if (emailFirstIndex.containsKey(email)) {
                duplicateInRequest.add(email);
            } else {
                emailFirstIndex.put(email, i);
            }
        }

        Set<String> emails = requests.stream()
                .map(r -> r.getEmail().toLowerCase().trim())
                .collect(Collectors.toSet());

        List<Users> existingUsers = userRepository.findAllByEmailIn(emails);
        Set<String> emailsExistingInDb = existingUsers.stream()
                .map(u -> u.getEmail().toLowerCase().trim())
                .collect(Collectors.toSet());

        List<Users> toSave = new ArrayList<>();
        List<CreatedUserDto> created = new ArrayList<>();
        List<FailedUserDto> failed = new ArrayList<>();

        Map<String, Role> roleCache = new HashMap<>();

        for (BulkCreateUserRequest req : requests) {
            String email = req.getEmail() == null ? "" : req.getEmail().toLowerCase().trim();

            if (email.isEmpty()) {
                failed.add(new FailedUserDto(req.getEmail(), "Empty or invalid email"));
                continue;
            }
            if (req.getPassword() == null || req.getPassword().isBlank()) {
                failed.add(new FailedUserDto(req.getEmail(), "Password is required"));
                continue;
            }
            if (duplicateInRequest.contains(email) && !emailFirstIndex.get(email).equals(requests.indexOf(req))) {
                failed.add(new FailedUserDto(req.getEmail(), "Duplicate email in request"));
                continue;
            }
            if (emailsExistingInDb.contains(email)) {
                failed.add(new FailedUserDto(req.getEmail(), "Email already exists"));
                continue;
            }

            String requestedRoleName = req.getRole() == null ? "" : req.getRole().trim();
            if (requestedRoleName.isEmpty()) {
                failed.add(new FailedUserDto(req.getEmail(), "Role is required"));
                continue;
            }

            if (requestedRoleName.equalsIgnoreCase("ADMIN") || requestedRoleName.equalsIgnoreCase("SUPER ADMIN")) {
                failed.add(new FailedUserDto(req.getEmail(), "Not allowed to create users with ADMIN or SUPER ADMIN role"));
                continue;
            }

            Role role = roleCache.get(requestedRoleName.toUpperCase());
            if (role == null) {
                Optional<Role> maybeRole = roleRepository.findByNameIgnoreCase(requestedRoleName);
                if (maybeRole.isEmpty()) {
                    failed.add(new FailedUserDto(req.getEmail(), "Role '" + requestedRoleName + "' does not exist"));
                    continue;
                }
                role = maybeRole.get();
                roleCache.put(requestedRoleName.toUpperCase(), role);
            }

            Users user = new Users();
            user.setName(req.getName());
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setRole(role);

            user.setBanned(false);
            user.setBanReason(null);
            user.setBanExpiresAt(null);

            toSave.add(user);
        }

        if (!toSave.isEmpty()) {
            Iterable<Users> saved = userRepository.saveAll(toSave);
            for (Users s : saved) {
                created.add(new CreatedUserDto(s.getId(), s.getEmail(), s.getName()));
            }
        }

        result.setCreatedUsers(created);
        result.setFailedUsers(failed);
        return result;
    }

    @Transactional
    public BulkUpdateResponse bulkUpdateUsers(List<BulkUpdateUsersRequest> requests) {
        BulkUpdateResponse response = new BulkUpdateResponse();
        if (requests == null || requests.isEmpty()) {
            return response;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated");
        }
        String callerEmail = auth.getName();
        Users caller = userRepository.findByEmail(callerEmail)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        String callerRoleName = caller.getRole() != null ? caller.getRole().getName() : "";
        if (!(callerRoleName.equalsIgnoreCase("ADMIN") || callerRoleName.equalsIgnoreCase("SUPER ADMIN"))) {
            throw new RuntimeException("Only Admins or Super Admins can update users");
        }

        List<String> emailsInOrder = requests.stream()
                .map(r -> r.getEmail() == null ? "" : r.getEmail().toLowerCase().trim())
                .collect(Collectors.toList());

        Set<String> uniqueEmails = emailsInOrder.stream()
                .filter(e -> !e.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Users> existing = userRepository.findAllByEmailIn(uniqueEmails);
        Map<String, Users> emailToUser = new HashMap<>();
        for (Users u : existing) {
            emailToUser.put(u.getEmail().toLowerCase().trim(), u);
        }

        Map<String, Role> roleCache = new HashMap<>();

        List<UpdatedUserDto> updated = new ArrayList<>();
        List<FailedUserDto> failed = new ArrayList<>();

        for (BulkUpdateUsersRequest req : requests) {
            String email = req.getEmail() == null ? "" : req.getEmail().toLowerCase().trim();
            if (email.isEmpty()) {
                failed.add(new FailedUserDto(null, "Missing or empty email"));
                continue;
            }

            Users user = emailToUser.get(email);
            if (user == null) {
                failed.add(new FailedUserDto(email, "User with this email does not exist"));
                continue;
            }

            boolean changed = false;

            if (req.getName() != null) {
                user.setName(req.getName());
                changed = true;
            }

            if (req.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(req.getPassword()));
                changed = true;
            }

            if (req.getRole() != null) {
                String requestedRole = req.getRole().trim();
                if (requestedRole.equalsIgnoreCase("ADMIN") || requestedRole.equalsIgnoreCase("SUPER ADMIN") || requestedRole.equalsIgnoreCase("SUPER_ADMIN")) {
                    failed.add(new FailedUserDto(email, "Not allowed to assign ADMIN or SUPER_ADMIN roles"));
                    continue;
                }

                String key = requestedRole.toUpperCase();
                Role role = roleCache.get(key);
                if (role == null) {
                    Optional<Role> maybeRole = roleRepository.findByNameIgnoreCase(requestedRole);
                    if (maybeRole.isEmpty()) {
                        failed.add(new FailedUserDto(email, "Role '" + requestedRole + "' does not exist"));
                        continue;
                    }
                    role = maybeRole.get();
                    roleCache.put(key, role);
                }
                user.setRole(role);
                changed = true;
            }

            emailToUser.put(email, user);

            updated.add(new UpdatedUserDto(user.getId(), user.getEmail(), user.getName()));
        }

        List<Users> toSave = emailToUser.values().stream()
                .filter(Objects::nonNull)
                .filter(u -> {
                    return updated.stream().anyMatch(d -> Objects.equals(d.getId(), u.getId()));
                })
                .collect(Collectors.toList());

        if (!toSave.isEmpty()) {
            userRepository.saveAll(toSave);
        }

        response.setUpdatedUsers(updated);
        response.setFailedUsers(failed);
        return response;
    }

//    public BulkBanResponse bulkBanUsers(List<BulkBanRequest> request) {
//
//    }
}

