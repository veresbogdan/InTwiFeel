package intwifeel.dao;

import intwifeel.model.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

public class BaseDao {

    @Autowired
    protected MongoTemplate mongoTemplate;

    public <T extends BaseEntity> List<T> findAll(Class<T> entityClass) {
        return mongoTemplate.findAll(entityClass);
    }

    public <T extends BaseEntity> List<T> findByCriteria(Map<String, Object> criteriaMap, Class<T> entityClass) {
        Query query = new Query();

        for (String key: criteriaMap.keySet()) {
            query.addCriteria(Criteria.where(key).is(criteriaMap.get(key)));
        }

        return mongoTemplate.find(query, entityClass);
    }

    public void saveOrUpdate(BaseEntity entity) {
        mongoTemplate.save(entity);
    }

    public void insert(BaseEntity entity) {
        mongoTemplate.insert(entity);
    }

    public void delete(BaseEntity entity) {
        mongoTemplate.remove(entity);
    }
}
