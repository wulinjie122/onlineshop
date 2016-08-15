package net.shopxx.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.BaseDao;
import net.shopxx.entity.OrderEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

public abstract class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {
    private Class<T> clazz;
    private static volatile long ID = 0L;

    @PersistenceContext
    protected EntityManager entityManager;

    public BaseDaoImpl() {
        Type type = getClass().getGenericSuperclass();
        Type[] arrayOfType = ((ParameterizedType) type).getActualTypeArguments();
        this.clazz = ((Class) arrayOfType[0]);
    }

    public T find(ID id) {
        if (id != null) {
            return this.entityManager.find(this.clazz, id);
        }
        return null;
    }

    public List<T> findList(Integer first, Integer count, List<Filter> filters, List<net.shopxx.Order> orders) {
        CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery localCriteriaQuery = localCriteriaBuilder.createQuery(this.clazz);
        localCriteriaQuery.select(localCriteriaQuery.from(this.clazz));
        return findList(localCriteriaQuery, first, count, filters, orders);
    }

    public Page<T> findPage(Pageable pageable) {
        CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery localCriteriaQuery = localCriteriaBuilder.createQuery(this.clazz);
        localCriteriaQuery.select(localCriteriaQuery.from(this.clazz));
        return findList(localCriteriaQuery, pageable);
    }

    public long count(Filter[] filters) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = builder.createQuery(this.clazz);
        criteriaQuery.select(criteriaQuery.from(this.clazz));
        return findList(criteriaQuery, filters != null ? Arrays.asList(filters) : null).longValue();
    }

    public void persist(T entity) {
        Assert.notNull(entity);
        this.entityManager.persist(entity);
    }

    public T merge(T entity) {
        Assert.notNull(entity);
        return this.entityManager.merge(entity);
    }

    public void remove(T entity) {
        if (entity != null)
            this.entityManager.remove(entity);
    }

    public void refresh(T entity) {
        Assert.notNull(entity);
        this.entityManager.refresh(entity);
    }

    public ID getIdentifier(T entity) {
        Assert.notNull(entity);
        return (ID) this.entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
    }

    public boolean isManaged(T entity) {
        return this.entityManager.contains(entity);
    }

    public void detach(T entity) {
        this.entityManager.detach(entity);
    }

    public void lock(T entity, LockModeType lockModeType) {
        if ((entity != null) && (lockModeType != null))
            this.entityManager.lock(entity, lockModeType);
    }

    public void clear() {
        this.entityManager.clear();
    }

    public void flush() {
        this.entityManager.flush();
    }

    protected List<T> findList(CriteriaQuery<T> paramCriteriaQuery, Integer paramInteger1, Integer paramInteger2, List<Filter> paramList, List<net.shopxx.Order> paramList1) {
        Assert.notNull(paramCriteriaQuery);
        Assert.notNull(paramCriteriaQuery.getSelection());
        Assert.notEmpty(paramCriteriaQuery.getRoots());
        CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
        Root localRoot = findList(paramCriteriaQuery);
        IIIllIll(paramCriteriaQuery, paramList);
        IIIlllII(paramCriteriaQuery, paramList1);
        if (paramCriteriaQuery.getOrderList().isEmpty())
            if (OrderEntity.class.isAssignableFrom(this.clazz))
                paramCriteriaQuery.orderBy(new javax.persistence.criteria.Order[]{localCriteriaBuilder.asc(localRoot.get("order"))});
            else
                paramCriteriaQuery.orderBy(new javax.persistence.criteria.Order[]{localCriteriaBuilder.desc(localRoot.get("createDate"))});
        TypedQuery localTypedQuery = this.entityManager.createQuery(paramCriteriaQuery).setFlushMode(FlushModeType.COMMIT);
        if (paramInteger1 != null)
            localTypedQuery.setFirstResult(paramInteger1.intValue());
        if (paramInteger2 != null)
            localTypedQuery.setMaxResults(paramInteger2.intValue());
        return localTypedQuery.getResultList();
    }

    protected Page<T> findList(CriteriaQuery<T> paramCriteriaQuery, Pageable paramPageable) {
        Assert.notNull(paramCriteriaQuery);
        Assert.notNull(paramCriteriaQuery.getSelection());
        Assert.notEmpty(paramCriteriaQuery.getRoots());
        if (paramPageable == null)
            paramPageable = new Pageable();
        CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
        Root localRoot = findList(paramCriteriaQuery);
        IIIllIll(paramCriteriaQuery, paramPageable);
        IIIlllII(paramCriteriaQuery, paramPageable);
        if (paramCriteriaQuery.getOrderList().isEmpty())
            if (OrderEntity.class.isAssignableFrom(this.clazz))
                paramCriteriaQuery.orderBy(new javax.persistence.criteria.Order[]{localCriteriaBuilder.asc(localRoot.get("order"))});
            else
                paramCriteriaQuery.orderBy(new javax.persistence.criteria.Order[]{localCriteriaBuilder.desc(localRoot.get("createDate"))});
        long l = findList(paramCriteriaQuery, (List) (null)).longValue();
        int i = (int) Math.ceil(l / paramPageable.getPageSize());
        if (i < paramPageable.getPageNumber())
            paramPageable.setPageNumber(i);
        TypedQuery localTypedQuery = this.entityManager.createQuery(paramCriteriaQuery).setFlushMode(FlushModeType.COMMIT);
        localTypedQuery.setFirstResult((paramPageable.getPageNumber() - 1) * paramPageable.getPageSize());
        localTypedQuery.setMaxResults(paramPageable.getPageSize());
        return new Page(localTypedQuery.getResultList(), l, paramPageable);
    }

    protected Long findList(CriteriaQuery<T> criteriaQuery, List<Filter> filters) {
        Assert.notNull(criteriaQuery);
        Assert.notNull(criteriaQuery.getSelection());
        Assert.notEmpty(criteriaQuery.getRoots());
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        IIIllIll(criteriaQuery, filters);
        CriteriaQuery localCriteriaQuery = builder.createQuery(Long.class);
        Iterator localIterator = criteriaQuery.getRoots().iterator();
        while (localIterator.hasNext()) {
            Root localRoot1 = (Root) localIterator.next();
            Root localRoot2 = localCriteriaQuery.from(localRoot1.getJavaType());
            localRoot2.alias(findList(localRoot1));
            findList(localRoot1, localRoot2);
        }
        Root localRoot1 = findList(localCriteriaQuery, criteriaQuery.getResultType());
        localCriteriaQuery.select(builder.count(localRoot1));
        if (criteriaQuery.getGroupList() != null)
            localCriteriaQuery.groupBy(criteriaQuery.getGroupList());
        if (criteriaQuery.getGroupRestriction() != null)
            localCriteriaQuery.having(criteriaQuery.getGroupRestriction());
        if (criteriaQuery.getRestriction() != null)
            localCriteriaQuery.where(criteriaQuery.getRestriction());
        return (Long) this.entityManager.createQuery(localCriteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult();
    }

    private synchronized String findList(Selection<?> paramSelection) {
        if (paramSelection != null) {
            String str = paramSelection.getAlias();
            if (str == null) {
                if (ID >= 1000L)
                    ID = 0L;
                str = "shopxxGeneratedAlias" + ID++;
                paramSelection.alias(str);
            }
            return str;
        }
        return null;
    }

    private Root<T> findList(CriteriaQuery<T> paramCriteriaQuery) {
        if (paramCriteriaQuery != null){
            return findList(paramCriteriaQuery, paramCriteriaQuery.getResultType());
        }
        return null;
    }

    private Root<T> findList(CriteriaQuery<?> paramCriteriaQuery, Class<T> paramClass) {
        if ((paramCriteriaQuery != null) && (paramCriteriaQuery.getRoots() != null) && (paramClass != null)) {
            Iterator localIterator = paramCriteriaQuery.getRoots().iterator();
            while (localIterator.hasNext()) {
                Root localRoot = (Root) localIterator.next();
                if (paramClass.equals(localRoot.getJavaType()))
                    return (Root) localRoot.as(paramClass);
            }
        }
        return null;
    }

    private void findList(From<?, ?> paramFrom1, From<?, ?> paramFrom2) {
        Iterator localIterator = paramFrom1.getJoins().iterator();
        Object localObject1;
        Object localObject2;
        while (localIterator.hasNext()) {
            localObject1 = (Join) localIterator.next();
            localObject2 = paramFrom2.join(((Join) localObject1).getAttribute().getName(), ((Join) localObject1).getJoinType());
            ((Join) localObject2).alias(findList((Selection) localObject1));
            findList((From) localObject1, (From) localObject2);
        }
        localIterator = paramFrom1.getFetches().iterator();
        while (localIterator.hasNext()) {
            localObject1 = (Fetch) localIterator.next();
            localObject2 = paramFrom2.fetch(((Fetch) localObject1).getAttribute().getName());
            findList((Fetch) localObject1, (Fetch) localObject2);
        }
    }

    private void findList(Fetch<?, ?> paramFetch1, Fetch<?, ?> paramFetch2) {
        Iterator iterator = paramFetch1.getFetches().iterator();
        while (iterator.hasNext()) {
            Fetch fetch1 = (Fetch) iterator.next();
            Fetch fetch2 = paramFetch2.fetch(fetch1.getAttribute().getName());
            findList(fetch1, fetch2);
        }
    }

    private void IIIllIll(CriteriaQuery<T> paramCriteriaQuery, List<Filter> filters) {
        if ((paramCriteriaQuery == null) || (filters == null) || (filters.isEmpty())){
            return;
        }
        Root root = findList(paramCriteriaQuery);
        if (root == null){
            return;
        }
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        Predicate localPredicate = paramCriteriaQuery.getRestriction() != null ? paramCriteriaQuery.getRestriction() : builder.conjunction();
        Iterator iterator = filters.iterator();
        while (iterator.hasNext()) {
            Filter filter = (Filter) iterator.next();
            if ((filter == null) || (StringUtils.isEmpty(filter.getProperty()))){
                continue;
            }

            if ((filter.getOperator() == Filter.Operator.eq) && (filter.getValue() != null)) {
                if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && ((filter.getValue() instanceof String))){
                    localPredicate = builder.and(localPredicate, builder.equal(builder.lower(root.get(filter.getProperty())), ((String) filter.getValue()).toLowerCase()));
                }
                else{
                    localPredicate = builder.and(localPredicate, builder.equal(root.get(filter.getProperty()), filter.getValue()));
                }
            } else if ((filter.getOperator() == Filter.Operator.ne) && (filter.getValue() != null)) {
                if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && ((filter.getValue() instanceof String)))
                    localPredicate = builder.and(localPredicate, builder.notEqual(builder.lower(root.get(filter.getProperty())), ((String) filter.getValue()).toLowerCase()));
                else
                    localPredicate = builder.and(localPredicate, builder.notEqual(root.get(filter.getProperty()), filter.getValue()));
            } else if ((filter.getOperator() == Filter.Operator.gt) && (filter.getValue() != null)) {
                localPredicate = builder.and(localPredicate, builder.gt(root.get(filter.getProperty()), (Number) filter.getValue()));
            } else if ((filter.getOperator() == Filter.Operator.lt) && (filter.getValue() != null)) {
                localPredicate = builder.and(localPredicate, builder.lt(root.get(filter.getProperty()), (Number) filter.getValue()));
            } else if ((filter.getOperator() == Filter.Operator.ge) && (filter.getValue() != null)) {
                localPredicate = builder.and(localPredicate, builder.ge(root.get(filter.getProperty()), (Number) filter.getValue()));
            } else if ((filter.getOperator() == Filter.Operator.le) && (filter.getValue() != null)) {
                localPredicate = builder.and(localPredicate, builder.le(root.get(filter.getProperty()), (Number) filter.getValue()));
            } else if ((filter.getOperator() == Filter.Operator.like) && (filter.getValue() != null) && ((filter.getValue() instanceof String))) {
                localPredicate = builder.and(localPredicate, builder.like(root.get(filter.getProperty()), (String) filter.getValue()));
            } else if ((filter.getOperator() == Filter.Operator.in) && (filter.getValue() != null)) {
                localPredicate = builder.and(localPredicate, root.get(filter.getProperty()).in(new Object[]{filter.getValue()}));
            } else if (filter.getOperator() == Filter.Operator.isNull) {
                localPredicate = builder.and(localPredicate, root.get(filter.getProperty()).isNull());
            } else {
                if (filter.getOperator() != Filter.Operator.isNotNull)
                    continue;
                localPredicate = builder.and(localPredicate, root.get(filter.getProperty()).isNotNull());
            }
        }
        paramCriteriaQuery.where(localPredicate);
    }

    private void IIIllIll(CriteriaQuery<T> paramCriteriaQuery, Pageable pageable) {

        if ((paramCriteriaQuery == null) || (pageable == null))
            return;
        Root root = findList(paramCriteriaQuery);
        if (root == null)
            return;
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        Predicate predicate = paramCriteriaQuery.getRestriction() != null ? paramCriteriaQuery.getRestriction() : builder.conjunction();
        if ((StringUtils.isNotEmpty(pageable.getSearchProperty())) && (StringUtils.isNotEmpty(pageable.getSearchValue()))) {
            predicate = builder.and(predicate, builder.like(root.get(pageable.getSearchProperty()), "%" + pageable.getSearchValue() + "%"));
        }
        if (pageable.getFilters() != null) {
            Iterator localIterator = pageable.getFilters().iterator();
            while (localIterator.hasNext()) {
                Filter filter = (Filter) localIterator.next();
                if ((filter == null) || (StringUtils.isEmpty(filter.getProperty())))
                    continue;
                if ((filter.getOperator() == Filter.Operator.eq) && (filter.getValue() != null)) {
                    if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && ((filter.getValue() instanceof String)))
                        predicate = builder.and(predicate, builder.equal(builder.lower(root.get(filter.getProperty())), ((String) filter.getValue()).toLowerCase()));
                    else
                        predicate = builder.and(predicate, builder.equal(root.get(filter.getProperty()), filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.ne) && (filter.getValue() != null)) {
                    if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && ((filter.getValue() instanceof String)))
                        predicate = builder.and(predicate, builder.notEqual(builder.lower(root.get(filter.getProperty())), ((String) filter.getValue()).toLowerCase()));
                    else
                        predicate = builder.and(predicate, builder.notEqual(root.get(filter.getProperty()), filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.gt) && (filter.getValue() != null)) {
                    predicate = builder.and(predicate, builder.gt(root.get(filter.getProperty()), (Number) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.lt) && (filter.getValue() != null)) {
                    predicate = builder.and(predicate, builder.lt(root.get(filter.getProperty()), (Number) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.ge) && (filter.getValue() != null)) {
                    predicate = builder.and(predicate, builder.ge(root.get(filter.getProperty()), (Number) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.le) && (filter.getValue() != null)) {
                    predicate = builder.and(predicate, builder.le(root.get(filter.getProperty()), (Number) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.like) && (filter.getValue() != null) && ((filter.getValue() instanceof String))) {
                    predicate = builder.and(predicate, builder.like(root.get(filter.getProperty()), (String) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.in) && (filter.getValue() != null)) {
                    predicate = builder.and(predicate, root.get(filter.getProperty()).in(new Object[]{filter.getValue()}));
                } else if (filter.getOperator() == Filter.Operator.isNull) {
                    predicate = builder.and(predicate, root.get(filter.getProperty()).isNull());
                } else {
                    if (filter.getOperator() != Filter.Operator.isNotNull)
                        continue;
                    predicate = builder.and(predicate, root.get(filter.getProperty()).isNotNull());
                }
            }
        }
        paramCriteriaQuery.where(predicate);
    }

    private void IIIlllII(CriteriaQuery<T> paramCriteriaQuery, List<net.shopxx.Order> paramList) {
        if ((paramCriteriaQuery == null) || (paramList == null) || (paramList.isEmpty()))
            return;
        Root localRoot = findList(paramCriteriaQuery);
        if (localRoot == null)
            return;
        CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
        ArrayList localArrayList = new ArrayList();
        if (!paramCriteriaQuery.getOrderList().isEmpty())
            localArrayList.addAll(paramCriteriaQuery.getOrderList());
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            net.shopxx.Order localOrder = (net.shopxx.Order) localIterator.next();
            if (localOrder.getDirection() == Order.Direction.asc) {
                localArrayList.add(localCriteriaBuilder.asc(localRoot.get(localOrder.getProperty())));
            } else {
                if (localOrder.getDirection() != Order.Direction.desc)
                    continue;
                localArrayList.add(localCriteriaBuilder.desc(localRoot.get(localOrder.getProperty())));
            }
        }
        paramCriteriaQuery.orderBy(localArrayList);
    }

    private void IIIlllII(CriteriaQuery<T> paramCriteriaQuery, Pageable paramPageable) {
        if ((paramCriteriaQuery == null) || (paramPageable == null))
            return;
        Root localRoot = findList(paramCriteriaQuery);
        if (localRoot == null)
            return;
        CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
        ArrayList localArrayList = new ArrayList();
        if (!paramCriteriaQuery.getOrderList().isEmpty())
            localArrayList.addAll(paramCriteriaQuery.getOrderList());
        if ((StringUtils.isNotEmpty(paramPageable.getOrderProperty())) && (paramPageable.getOrderDirection() != null))
            if (paramPageable.getOrderDirection() == Order.Direction.asc)
                localArrayList.add(localCriteriaBuilder.asc(localRoot.get(paramPageable.getOrderProperty())));
            else if (paramPageable.getOrderDirection() == Order.Direction.desc)
                localArrayList.add(localCriteriaBuilder.desc(localRoot.get(paramPageable.getOrderProperty())));
        if (paramPageable.getOrders() != null) {
            Iterator localIterator = paramPageable.getOrders().iterator();
            while (localIterator.hasNext()) {
                net.shopxx.Order localOrder = (net.shopxx.Order) localIterator.next();
                if (localOrder.getDirection() == Order.Direction.asc) {
                    localArrayList.add(localCriteriaBuilder.asc(localRoot.get(localOrder.getProperty())));
                } else {
                    if (localOrder.getDirection() != Order.Direction.desc)
                        continue;
                    localArrayList.add(localCriteriaBuilder.desc(localRoot.get(localOrder.getProperty())));
                }
            }
        }
        paramCriteriaQuery.orderBy(localArrayList);
    }
}