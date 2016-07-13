package net.shopxx.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="xx_product_category")
public class ProductCategory
  extends OrderEntity
{
  private static final long serialVersionUID = 5095521437302782717L;
  public static final String TREE_PATH_SEPARATOR = ",";
  private static final String SUFFIX_STRING = "/product/list";
  private static final String PREFIX_STRING = ".jhtml";
  private String name;
  private String seoTitle;
  private String seoKeywords;
  private String IIIlllll;
  private String IIlIIIII;
  private Integer IIlIIIIl;
  private ProductCategory IIlIIIlI;
  private Set<ProductCategory> IIlIIIll = new HashSet();
  private Set<Product> IIlIIlII = new HashSet();
  private Set<Brand> IIlIIlIl = new HashSet();
  private Set<ParameterGroup> IIlIIllI = new HashSet();
  private Set<Attribute> productAttributes = new HashSet();
  private Set<Promotion> IIlIlIII = new HashSet();
  
  @NotEmpty
  @Length(max=200)
  @Column(nullable=false)
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Length(max=200)
  public String getSeoTitle()
  {
    return this.seoTitle;
  }
  
  public void setSeoTitle(String seoTitle)
  {
    this.seoTitle = seoTitle;
  }
  
  @Length(max=200)
  public String getSeoKeywords()
  {
    return this.seoKeywords;
  }
  
  public void setSeoKeywords(String seoKeywords)
  {
    this.seoKeywords = seoKeywords;
  }
  
  @Length(max=200)
  public String getSeoDescription()
  {
    return this.IIIlllll;
  }
  
  public void setSeoDescription(String seoDescription)
  {
    this.IIIlllll = seoDescription;
  }
  
  @Column(nullable=false)
  public String getTreePath()
  {
    return this.IIlIIIII;
  }
  
  public void setTreePath(String treePath)
  {
    this.IIlIIIII = treePath;
  }
  
  @Column(nullable=false)
  public Integer getGrade()
  {
    return this.IIlIIIIl;
  }
  
  public void setGrade(Integer grade)
  {
    this.IIlIIIIl = grade;
  }
  
  @ManyToOne(fetch=FetchType.LAZY)
  public ProductCategory getParent()
  {
    return this.IIlIIIlI;
  }
  
  public void setParent(ProductCategory parent)
  {
    this.IIlIIIlI = parent;
  }
  
  @OneToMany(mappedBy="parent", fetch=FetchType.LAZY)
  @OrderBy("order asc")
  public Set<ProductCategory> getChildren()
  {
    return this.IIlIIIll;
  }
  
  public void setChildren(Set<ProductCategory> children)
  {
    this.IIlIIIll = children;
  }
  
  @OneToMany(mappedBy="productCategory", fetch=FetchType.LAZY)
  public Set<Product> getProducts()
  {
    return this.IIlIIlII;
  }
  
  public void setProducts(Set<Product> products)
  {
    this.IIlIIlII = products;
  }
  
  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="xx_product_category_brand")
  @OrderBy("order asc")
  public Set<Brand> getBrands()
  {
    return this.IIlIIlIl;
  }
  
  public void setBrands(Set<Brand> brands)
  {
    this.IIlIIlIl = brands;
  }
  
  @OneToMany(mappedBy="productCategory", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.REMOVE})
  @OrderBy("order asc")
  public Set<ParameterGroup> getParameterGroups()
  {
    return this.IIlIIllI;
  }
  
  public void setParameterGroups(Set<ParameterGroup> parameterGroups)
  {
    this.IIlIIllI = parameterGroups;
  }
  
  @OneToMany(mappedBy="productCategory", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.REMOVE})
  @OrderBy("order asc")
  public Set<Attribute> getAttributes()
  {
    return this.productAttributes;
  }
  
  public void setAttributes(Set<Attribute> attributes)
  {
    this.productAttributes = attributes;
  }
  
  @ManyToMany(mappedBy="productCategories", fetch=FetchType.LAZY)
  public Set<Promotion> getPromotions()
  {
    return this.IIlIlIII;
  }
  
  public void setPromotions(Set<Promotion> promotions)
  {
    this.IIlIlIII = promotions;
  }
  
  @Transient
  public List<Long> getTreePaths()
  {
    ArrayList localArrayList = new ArrayList();
    String[] arrayOfString1 = StringUtils.split(getTreePath(), ",");
    if (arrayOfString1 != null) {
      for (String str : arrayOfString1) {
        localArrayList.add(Long.valueOf(str));
      }
    }
    return localArrayList;
  }
  
  @Transient
  public String getPath()
  {
    if (getId() != null) {
      return "/product/list/" + getId() + ".jhtml";
    }
    return null;
  }
  
  @PreRemove
  public void preRemove()
  {
    Set localSet = getPromotions();
    if (localSet != null)
    {
      Iterator localIterator = localSet.iterator();
      while (localIterator.hasNext())
      {
        Promotion localPromotion = (Promotion)localIterator.next();
        localPromotion.getProductCategories().remove(this);
      }
    }
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.entity.ProductCategory
 * JD-Core Version:    0.7.0.1
 */