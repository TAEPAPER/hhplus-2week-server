package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.application.user.repository.UserRepository;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;


    @Override
    public Optional<User> findById(long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public void deleteAll() {
        userJpaRepository.deleteAll();
    }

}
