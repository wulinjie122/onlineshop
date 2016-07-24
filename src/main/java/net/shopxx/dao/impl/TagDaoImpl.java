package net.shopxx.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import net.shopxx.dao.TagDao;
import net.shopxx.entity.Tag;
import net.shopxx.entity.Tag.Type;
import org.springframework.stereotype.Repository;

@Repository("tagDaoImpl")
public class TagDaoImpl
  extends BaseDaoImpl<Tag, Long>
  implements TagDao
{
  public List<Tag> findList(Tag.Type type)
  {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Tag.class);
    Root root = criteriaQuery.from(Tag.class);
    criteriaQuery.select(root);
    if (type != null) {
      criteriaQuery.where(criteriaBuilder.equal(root.get("type"), type));
    }
    criteriaQuery.orderBy(new Order[] { criteriaBuilder.asc(root.get("order")) });
    return this.entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
  }
}