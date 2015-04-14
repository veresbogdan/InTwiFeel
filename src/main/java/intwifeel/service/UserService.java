package intwifeel.service;

import intwifeel.dao.UserDao;
import intwifeel.model.ProductEntity;
import intwifeel.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService extends BaseService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductService productService;

    public UserEntity createUser(UserEntity userEntity) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(hashedPassword);

        productService.saveUserProducts(userEntity);
        userDao.saveOrUpdate(userEntity);
        productService.updateUserProducts(userEntity);

        userEntity.setPassword(null);

        return userEntity;
    }

    public UserEntity findByName(String name) {
        Map<String, Object> criteria = new HashMap<>();

        criteria.put("username", name);

        List<UserEntity> userEntities = userDao.findByCriteria(criteria, UserEntity.class);

        if (userEntities != null && !userEntities.isEmpty()) {
            return userEntities.get(0);
        } else {
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = findByName(s);

        if (userEntity != null) {
            return createAccount(userEntity);
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }

    public void loginUser(UserEntity userEntity) {
        if (userEntity.getUserName() != null && userEntity.getPassword() != null) {
            SecurityContextHolder.getContext().setAuthentication(authenticate(userEntity));
        } else {
            throw new UsernameNotFoundException("username or password is missing");
        }
    }

    private Authentication authenticate(UserEntity userEntity) {
        return new UsernamePasswordAuthenticationToken(createAccount(userEntity), null, Collections.singleton(createAuthority(userEntity)));
    }

    private User createAccount(UserEntity userEntity) {
        return new User(userEntity.getUserName(), userEntity.getPassword(), Collections.singleton(createAuthority(userEntity)));
    }

    private GrantedAuthority createAuthority(UserEntity userEntity) {
        return new SimpleGrantedAuthority(userEntity.getRole());
    }

    public void updateUser(UserEntity userEntity, ProductEntity productEntity) {
        if (userEntity.getProducts() != null) {
            userEntity.getProducts().add(productEntity);
        } else {
            List<ProductEntity> productEntities = new ArrayList<>();
            productEntities.add(productEntity);

            userEntity.setProducts(productEntities);
        }

        userDao.saveOrUpdate(userEntity);
    }
}
