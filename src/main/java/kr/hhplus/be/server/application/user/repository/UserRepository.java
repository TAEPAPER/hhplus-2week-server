package kr.hhplus.be.server.application.user.repository;

import kr.hhplus.be.server.domain.user.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(long userId);

    User save(User user);

    void deleteAll();
}
