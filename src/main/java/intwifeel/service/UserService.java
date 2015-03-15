package intwifeel.service;

import intwifeel.dao.UserDao;
import intwifeel.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService extends BaseService {

    @Autowired
    private UserDao userDao;

    public User createUser(User user) {

        userDao.saveOrUpdate(user);

        return user;
    }

    public User findByName(String name) {
        Map<String, Object> criteria = new HashMap<>();

        criteria.put("username", name);

        List<User> users = userDao.findByCriteria(criteria, User.class);

        if (users != null && !users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }
}
