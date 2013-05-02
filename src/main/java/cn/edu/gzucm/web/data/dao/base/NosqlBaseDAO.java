package cn.edu.gzucm.web.data.dao.base;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.gzucm.web.data.model.base.Entity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

public abstract class NosqlBaseDAO<T extends Entity> extends BaseDAO {

    @Autowired
    protected MongoTemplate mongoTemplate;

    public Page<T> getPageOfData(final Pageable page) {

        Query query = new Query();
        final List<T> list = mongoTemplate.find(query.skip((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize()), getEntityClass());
        return new PageImpl<T>(list, page, page.getPageSize());
    }

    public List<T> findFirstPage() {

        return getPageOfData(new PageRequest(1, 10)).getContent();
    }

    public T add(final T entity) {

        initialBaseFields(entity);
        mongoTemplate.save(entity);
        return entity;
    }

    public T update(final T entity) {

        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        entity.setUpdated(new Date());
        if (entity.getCreated() == null) {
            entity.setCreated(entity.getUpdated());
        }
        mongoTemplate.save(entity);
        return entity;
    }

    public void removeHistoryData(Date date) {

        Criteria criteria = Criteria.where("cdt").lt(date);
        Query query = new Query(criteria);
        remove(query);
    }

    public void remove(final Query query) {

        this.mongoTemplate.remove(query, getEntityClass());
    }

    public void remove(final T entity) {

        mongoTemplate.remove(entity);
    }

    public void removeAll() {

        mongoTemplate.dropCollection(getEntityClass());
    }

    public T findById(final String entityId) {

        return mongoTemplate.findById(entityId, getEntityClass());
    }

    public void removeById(final String entityId) {

        Query query = new Query(Criteria.where("_id").is(entityId));

        mongoTemplate.remove(query, getEntityClass());
    }

    public void removeByIds(final Object[] entityIds) {

        final Criteria criteria = Criteria.where("id").in(entityIds);
        final Query query = new Query(criteria);
        mongoTemplate.remove(query, getEntityClass());
    }

    public int countByField(final String fieldName, Object fieldValue) {

        BasicDBObject query = new BasicDBObject();
        query.put(fieldName, fieldValue);
        return getDBCollection().find(query).count();
    }

    public long countAll() {

        return getDBCollection().count();
    }

    /**
     * 返回查询的第一条记录
     * @param query
     * @return
     */
    public T findFirst(final Query query) {

        return mongoTemplate.findOne(query.limit(1), getEntityClass());
    }

    public T findLatestCreated(final Query query) {

        query.sort().on("cdt", Order.DESCENDING);
        return findFirst(query);

    }

    public T findLatestUpdated(final Query query) {

        query.sort().on("upd", Order.DESCENDING);
        return findFirst(query);

    }

    public void save(final Entity entity) {

        this.mongoTemplate.save(entity);
    }

    /**
     * 批量修改
     * */
    public int update(final Query query, final Update update) {

        WriteResult reslut = mongoTemplate.updateMulti(query, update, getEntityClass());
        return reslut.getN();
    }

    /**
     * 更新单字段值
     */
    public void update(final String queryKey, final String queryValue, final String updateKey, final Object updateValue) {

        this.update(new Query(Criteria.where(queryKey).is(queryValue)), new Update().set(updateKey, updateValue));
    }

    public void update(final Query query, final String updateKey, final Object updateValue) {

        this.update(query, new Update().set(updateKey, updateValue));
    }

    /**
     *  自增
     * @param queryKey 
     * @param queryValue
     * @param updateKey  需要更新的字段名称
     * @param len   步长
     */
    public void increase(final String queryKey, final String queryValue, final String updateKey, final Number len) {

        this.update(new Query(Criteria.where(queryKey).is(queryValue)), new Update().inc(updateKey, len));
    }

    public void increase(final Query query, final String updateKey, final Number len) {

        this.update(query, new Update().inc(updateKey, len));
    }

    public void increase(final String queryKey, final String queryValue, final Update update) {

        this.update(new Query(Criteria.where(queryKey).is(queryValue)), update);
    }

    public T findOne(final Query query) {

        return mongoTemplate.findOne(query, getEntityClass());
    }

    public List<T> findAll() {

        return mongoTemplate.findAll(getEntityClass());
    }

    public Page<T> findByQuery(final Query query, final Pageable pageInfo) {

        return findByQuery(query, pageInfo, getEntityClass());
    }

    public Page<T> findByQuery(final Query query, final Pageable pageInfo, final Class<T> clazz) {

        final List<T> list = mongoTemplate.find(query.skip((pageInfo.getPageNumber() - 1) * pageInfo.getPageSize()).limit(pageInfo.getPageSize()), clazz);
        return new PageImpl<T>(list, pageInfo, pageInfo.getPageSize());
    }

    public List<T> findAllByQuery(final Query query) {

        final List<T> list = mongoTemplate.find(query, getEntityClass());
        return list;
    }

    //查找分页后的List
    public List<T> findPagingByQuery(final Query query, final Pageable pageInfo) {

        final List<T> list = mongoTemplate.find(query.skip((pageInfo.getPageNumber() - 1) * pageInfo.getPageSize()).limit(pageInfo.getPageSize()), getEntityClass());
        return list;
    }

    /**
     * 返回查询记录数
     * @param query
     * @return
     */
    public long findCountByQuery(final Query query) {

        return mongoTemplate.count(query, getEntityClass());
    }

    public DBCollection getDBCollection() {

        final String collectionName = getEntityClass().getAnnotation(Document.class).collection();
        final DBCollection db = mongoTemplate.getDb().getCollection(collectionName);
        return db;
    }

    public String getCollectionName() {

        return getEntityClass().getAnnotation(Document.class).collection();
    }

    public PagingAndSortingRepository<T, String> getRepository() {

        return null;
    }

    public abstract Class<T> getEntityClass();

    public T newEntity() {

        try {
            return getEntityClass().newInstance();
        } catch (final InstantiationException e) {
            //            LoggerUtil.error(_logger, e);
        } catch (final IllegalAccessException e) {
            //            LoggerUtil.error(_logger, e);
        }
        return null;
    }

    /**
     * 删除所有指定日期前的记录
     * @param date
     */
    public void deleteExpireData(final Date date) {

        final Criteria criteria = Criteria.where("cdt").lte(date);
        final Query query = new Query(criteria);
        mongoTemplate.remove(query, getEntityClass());
    }

    public void insertBatch(final Collection<? extends Entity> batchToSave) {

        for (Entity entity : batchToSave) {
            initialBaseFields(entity);
        }
        mongoTemplate.insert(batchToSave, getEntityClass());
    }

    public void initialBaseFields(final Entity entity) {

        if (entity.getCreated() == null) {
            entity.setCreated(new Date());
        }
        entity.setUpdated(new Date());

        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
    }

    public List<T> findByIds(String[] ids) {

        Query query = new Query(Criteria.where("_id").in(ids));
        return findAllByQuery(query);
    }

    public List<T> findByIds(Collection<String> ids) {

        Query query = new Query(Criteria.where("_id").in(ids));
        return findAllByQuery(query);
    }
}
