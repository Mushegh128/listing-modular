package com.listing.common.servicies;

import com.listing.common.model.Role;
import com.listing.common.model.User;
import com.listing.common.repositories.ListingRepository;
import com.listing.common.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail.isPresent()) {
            log.info("User by email: " + user.getEmail() + " don't saved, because it repeating");
            return null;
        }
        return userRepository.save(user);
    }

    public User updateUser(User user, int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            log.info("Don't update: wrong ID");
            return null;
        }
        if (user.getRole() != Role.USER && user.getRole() != Role.ADMIN) {
            log.info("wrong Role of update's User");
            user.setRole(byId.get().getRole());
        }
        user.setId(id);
        return userRepository.save(user);
    }

    public boolean deleteById(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            log.info("Don't delete: wrong ID");
            return false;
        }
        listingRepository.changeAllListingsUser(id, null);
        userRepository.deleteById(id);
        return true;
    }

    public User findByEmail(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            return null;
        }
        return byEmail.get();
    }
}
