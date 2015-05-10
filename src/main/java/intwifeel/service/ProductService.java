package intwifeel.service;

import intwifeel.dao.ProductDao;
import intwifeel.dao.RedisDao;
import intwifeel.model.ProductEntity;
import intwifeel.model.ScoreEntity;
import intwifeel.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ProductService extends BaseService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisDao redisDao;

    private static final Integer DEFAULT_SCORE = 0;

    public ProductEntity addProduct(ProductEntity productEntity) {
        UserEntity userEntity = userService.getCurrentUser();

        ScoreEntity scoreEntity = addDefaultScore(productEntity);
        if (userEntity != null) {
            productEntity.setUser(userEntity);
        }

        saveProduct(productEntity);

        userService.updateUser(userEntity, productEntity);
        updateScore(scoreEntity, productEntity);

        return productEntity;
    }

    private void updateScore(ScoreEntity scoreEntity, ProductEntity productEntity) {
        scoreEntity.setProduct(productEntity);
        productDao.saveOrUpdate(scoreEntity);
    }

    private ScoreEntity addDefaultScore(ProductEntity productEntity) {
        List<ScoreEntity> scoreEntities = new ArrayList<>();

        ScoreEntity scoreEntity = new ScoreEntity();
        scoreEntity.setDate(new Date());
        scoreEntity.setScore(DEFAULT_SCORE);
        productDao.saveOrUpdate(scoreEntity);

        scoreEntities.add(scoreEntity);

        productEntity.setScores(scoreEntities);

        return scoreEntity;
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

            if (productEntity.getScores() != null) {
                Integer sum = 0;
                for (ScoreEntity scoreEntity : productEntity.getScores()) {
                    sum += scoreEntity.getScore();
                }
                productEntity.setAverage(sum / productEntity.getScores().size());
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

    public String getExampleForProduct(String name) {
        return redisDao.readValue(name);
    }
}
