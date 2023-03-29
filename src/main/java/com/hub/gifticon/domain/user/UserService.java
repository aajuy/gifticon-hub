package com.hub.gifticon.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User addUser(String userId, String password) {
        // 아이디 중복 체크
        if (checkDuplicateId(userId)) {
            return null;
        }

        /*
            TODO: Password Encryption
         */
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);

        return userRepository.add(user);
    }

    public User login(String userId, String password) {
        User user = userRepository.findById(userId);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }

    public Boolean checkDuplicateId(String userId) {
        return userRepository.findById(userId) != null;
    }
}
