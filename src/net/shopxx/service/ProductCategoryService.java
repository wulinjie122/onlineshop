package net.shopxx.service;

import java.util.List;
import net.shopxx.entity.ProductCategory;

public abstract interface ProductCategoryService
  extends BaseService<ProductCategory, Long>
{
  public abstract List<ProductCategory> findRoots();
  
  public abstract List<ProductCategory> findRoots(Integer paramInteger);
  
  public abstract List<ProductCategory> findRoots(Integer paramInteger, String paramString);
  
  public abstract List<ProductCategory> findParents(ProductCategory paramProductCategory);
  
  public abstract List<ProductCategory> findParents(ProductCategory paramProductCategory, Integer paramInteger);
  
  public abstract List<ProductCategory> findParents(ProductCategory paramProductCategory, Integer paramInteger, String paramString);
  
  public abstract List<ProductCategory> findTree();
  
  public abstract List<ProductCategory> findChildren(ProductCategory paramProductCategory);
  
  public abstract List<ProductCategory> findChildren(ProductCategory paramProductCategory, Integer paramInteger);
  
  public abstract List<ProductCategory> findChildren(ProductCategory paramProductCategory, Integer paramInteger, String paramString);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.ProductCategoryService
 * JD-Core Version:    0.7.0.1
 */