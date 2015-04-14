package intwifeel.service;

import intwifeel.dao.ProductDao;
import intwifeel.model.ProductEntity;
import intwifeel.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ProductEntity addProduct(ProductEntity productEntity) {
        UserEntity userEntity = userService.getCurrentUser();

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

        return productDao.findByCriteria(criteria, ProductEntity.class);
    }
}
