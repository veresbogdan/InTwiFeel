package intwifeel.service;

import intwifeel.dao.ProductDao;
import intwifeel.dao.RedisDao;
import intwifeel.model.ProductEntity;
import intwifeel.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductService extends BaseService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisDao redisDao;

    public ProductEntity addProduct(ProductEntity productEntity) {
        UserEntity userEntity = userService.getCurrentUser();

        productEntity.setScore(0);
        productEntity.setDate(new Date());
        if (userEntity != null) {
            productEntity.setUser(userEntity);
        }

        saveProduct(productEntity);

        userService.updateUser(userEntity, productEntity);

        return productEntity;
    }

    public ProductEntity saveProduct(ProductEntity productEntity) {
        productDao.saveOrUpdate(productEntity);

        return productEntity;
    }

    public void saveUserProducts(UserEntity userEntity) {
        if (userEntity.getProducts() != null && !userEntity.getProducts().isEmpty()) {
            for (ProductEntity productEntity: userEntity.getProducts()) {
                saveProduct(productEntity);
            }
        }
    }

    public void updateUserProducts(UserEntity userEntity) {
        if (userEntity.getProducts() != null && !userEntity.getProducts().isEmpty()) {
            for (ProductEntity productEntity: userEntity.getProducts()) {
                productEntity.setUser(userEntity);

                saveProduct(productEntity);
            }
        }
    }

    public List<ProductEntity> listProductsForUser() {
        UserEntity userEntity = userService.getCurrentUser();

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("user", userEntity);

        List<ProductEntity> productEntities = productDao.findByCriteria(criteria, ProductEntity.class);

        for (ProductEntity productEntity: productEntities) {
            String exampleTwitt = redisDao.readValue(productEntity.getName());

            if (exampleTwitt != null) {
                productEntity.setExample(exampleTwitt);
            }
        }

        return productEntities;
    }

    public void removeProduct(String name) {
        List<ProductEntity> ownProducts = listProductsForUser();

        for (ProductEntity productEntity: ownProducts) {
            if (productEntity.getName().equals(name)) {
                productDao.delete(productEntity);
            }
        }
    }
}
