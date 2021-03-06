package net.shopxx.service.impl;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.BaseDao;
import net.shopxx.service.BaseService;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional
public class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

  private static final String[] IIIllIlI = { "id", "createDate", "modifyDate" };
  private BaseDao<T, ID> baseDao;
  
  public void setBaseDao(BaseDao<T, ID> baseDao) {
    this.baseDao = baseDao;
  }
  
  @Transactional(readOnly=true)
  public T find(ID id) {
    return this.baseDao.find(id);
  }
  
  @Transactional(readOnly=true)
  public List<T> findAll()
  {
    return findList(null, null, null, null);
  }
  
  @Transactional(readOnly=true)
  public List<T> findList(ID... ids)
  {
    ArrayList arraylist = new ArrayList();
    if (ids != null) {
      for (ID id : ids) {
        Object obj = find(id);
        if (obj != null) {
          arraylist.add(obj);
        }
      }
    }
    return arraylist;
    
  }
  
  @Transactional(readOnly=true)
  public List<T> findList(Integer count, List<Filter> filters, List<Order> orders)
  {
    return findList(null, count, filters, orders);
  }
  
  @Transactional(readOnly=true)
  public List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders)
  {
    return this.baseDao.findList(first, count, filters, orders);
  }
  
  @Transactional(readOnly=true)
  public Page<T> findPage(Pageable pageable)
  {
    return this.baseDao.findPage(pageable);
  }
  
  @Transactional(readOnly=true)
  public long count()
  {
    return count(new Filter[0]);
  }
  
  @Transactional(readOnly=true)
  public long count(Filter... filters)
  {
    return this.baseDao.count(filters);
  }
  
  @Transactional(readOnly=true)
  public boolean exists(ID id)
  {
    return this.baseDao.find(id) != null;
  }
  
  @Transactional(readOnly=true)
  public boolean exists(Filter... filters)
  {
    return this.baseDao.count(filters) > 0L;
  }
  
  @Transactional
  public void save(T entity)
  {
    this.baseDao.persist(entity);
  }
  
  @Transactional
  public T update(T entity)
  {
    return this.baseDao.merge(entity);
  }
  
  @Transactional
  public T update(T entity, String... ignoreProperties)
  {
	//��ʱ�޸� wulinjie
	/*
    Assert.notNull(entity);
    if (this.getNotifyUrl.isManaged(entity)) {
      throw new IllegalArgumentException("Entity must not be managed");
    }
    Object localObject = this.getNotifyUrl.find(this.getNotifyUrl.getIdentifier(entity));
    if (localObject != null)
    {
      entityManager(entity, localObject, (String[])ArrayUtils.addAll(ignoreProperties, entityManager));
      return update(localObject);
    }
    return update(entity);
    */
	return null;
  }
  
  @Transactional
  public void delete(ID id)
  {
    delete(this.baseDao.find(id));
  }
  
  @Transactional
  public void delete(ID... ids)
  {
	  //��ʱ�޸�
	  /*
    if (ids != null) {
      for (Serializable localSerializable : ids) {
        delete(this.getNotifyUrl.find(localSerializable));
      }
    }
    */
  }
  
  @Transactional
  public void delete(T entity)
  {
    this.baseDao.remove(entity);
  }
  
  private void IIIllIlI(Object paramObject1, Object paramObject2, String[] paramArrayOfString)
  {
	//��ʱ�޸� wulinjie
	/*
    Assert.notNull(paramObject1, "Source must not be null");
    Assert.notNull(paramObject2, "Target must not be null");
    PropertyDescriptor[] arrayOfPropertyDescriptor1 = BeanUtils.getPropertyDescriptors(paramObject2.getClass());
    Object localObject1 = paramArrayOfString != null ? Arrays.asList(paramArrayOfString) : null;
    for (PropertyDescriptor localPropertyDescriptor1 : arrayOfPropertyDescriptor1) {
      if ((localPropertyDescriptor1.getWriteMethod() != null) && ((paramArrayOfString == null) || (!localObject1.contains(localPropertyDescriptor1.getName()))))
      {
        PropertyDescriptor localPropertyDescriptor2 = BeanUtils.getPropertyDescriptor(paramObject1.getClass(), localPropertyDescriptor1.getName());
        if ((localPropertyDescriptor2 != null) && (localPropertyDescriptor2.getReadMethod() != null)) {
          try
          {
            Method localMethod = localPropertyDescriptor2.getReadMethod();
            if (!Modifier.isPublic(localMethod.getDeclaringClass().getModifiers())) {
              localMethod.setAccessible(true);
            }
            Object localObject2 = localMethod.invoke(paramObject1, new Object[0]);
            Object localObject3 = localMethod.invoke(paramObject2, new Object[0]);
            Object localObject4;
            if ((localObject2 != null) && (localObject3 != null) && ((localObject3 instanceof Collection)))
            {
              localObject4 = (Collection)localObject3;
              ((Collection)localObject4).clear();
              ((Collection)localObject4).addAll((Collection)localObject2);
            }
            else
            {
              localObject4 = localPropertyDescriptor1.getWriteMethod();
              if (!Modifier.isPublic(((Method)localObject4).getDeclaringClass().getModifiers())) {
                ((Method)localObject4).setAccessible(true);
              }
              ((Method)localObject4).invoke(paramObject2, new Object[] { localObject2 });
            }
          }
          catch (Throwable localThrowable)
          {
            throw new FatalBeanException("Could not copy properties from source to target", localThrowable);
          }
        }
      }
    }
    */
  }
}