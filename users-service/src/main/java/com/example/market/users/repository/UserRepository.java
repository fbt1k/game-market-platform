package com.example.market.users.repository;

import com.example.market.users.model.User;
import org.springframework.stereorype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private final Map<Long, User> users = new ConcurrentHashMap<>();// использование конкурент хэш мап дял потокобезопастности 
    private final AtomicLong idGenerator = new AtomicLong(1);// гарантия разных айди 

    public User save(User user){
        if (user.getId() == null){
            user.setId(idGenerator.getAndIncrement());
        }
        users.put(user.getId(), user);
        return user;
    }
    
    public Optional<User> findById(long id){
        return Optional.ofNullable(users.get(id));
    }
}
