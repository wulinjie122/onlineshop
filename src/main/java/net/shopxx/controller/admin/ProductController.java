package net.shopxx.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.Setting;
import net.shopxx.entity.Attribute;
import net.shopxx.entity.Brand;
import net.shopxx.entity.Goods;
import net.shopxx.entity.MemberRank;
import net.shopxx.entity.Parameter;
import net.shopxx.entity.ParameterGroup;
import net.shopxx.entity.Product;
import net.shopxx.entity.ProductCategory;
import net.shopxx.entity.ProductImage;
import net.shopxx.entity.Promotion;
import net.shopxx.entity.Specification;
import net.shopxx.entity.SpecificationValue;
import net.shopxx.service.BrandService;
import net.shopxx.service.FileService;
import net.shopxx.service.GoodsService;
import net.shopxx.service.MemberRankService;
import net.shopxx.service.ProductCategoryService;
import net.shopxx.service.ProductImageService;
import net.shopxx.service.ProductService;
import net.shopxx.service.PromotionService;
import net.shopxx.service.SpecificationService;
import net.shopxx.service.SpecificationValueService;
import net.shopxx.service.TagService;
import net.shopxx.util.SettingUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminProductController")
@RequestMapping({"/admin/product"})
public class ProductController extends BaseController
{

  @Resource(name="productServiceImpl")
  private ProductService IIIlllIl;

  @Resource(name="productCategoryServiceImpl")
  private ProductCategoryService IIIllllI;

  @Resource(name="goodsServiceImpl")
  private GoodsService IIIlllll;

  @Resource(name="brandServiceImpl")
  private BrandService IIlIIIII;

  @Resource(name="promotionServiceImpl")
  private PromotionService IIlIIIIl;

  @Resource(name="tagServiceImpl")
  private TagService IIlIIIlI;

  @Resource(name="memberRankServiceImpl")
  private MemberRankService IIlIIIll;

  @Resource(name="productImageServiceImpl")
  private ProductImageService IIlIIlII;

  @Resource(name="specificationServiceImpl")
  private SpecificationService IIlIIlIl;

  @Resource(name="specificationValueServiceImpl")
  private SpecificationValueService IIlIIllI;

  @Resource(name="fileServiceImpl")
  private FileService IIlIIlll;

  @RequestMapping(value={"/check_sn"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public boolean checkSn(String previousSn, String sn)
  {
    if (StringUtils.isEmpty(sn))
      return false;
    return this.IIIlllIl.snUnique(previousSn, sn);
  }

  @RequestMapping(value={"/parameter_groups"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public Set<ParameterGroup> parameterGroups(Long id)
  {
    ProductCategory localProductCategory = (ProductCategory)this.IIIllllI.find(id);
    return localProductCategory.getParameterGroups();
  }

  @RequestMapping(value={"/attributes"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public Set<Attribute> attributes(Long id)
  {
    ProductCategory localProductCategory = (ProductCategory)this.IIIllllI.find(id);
    return localProductCategory.getAttributes();
  }

  @RequestMapping(value={"/add"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String add(ModelMap model)
  {
      model.addAttribute("productCategoryTree", IIIllllI.findTree());
      model.addAttribute("brands", IIlIIIII.findAll());
      model.addAttribute("tags", IIlIIIlI.findList(net.shopxx.entity.Tag.Type.product));
      model.addAttribute("memberRanks", IIlIIIll.findAll());
      model.addAttribute("specifications", IIlIIlIl.findAll());
      return "/admin/product/add";
  }

  @RequestMapping(value={"/save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String save(Product product, Long productCategoryId, Long brandId, Long tagIds[], Long specificationIds[], HttpServletRequest request, RedirectAttributes redirectAttributes)
  {
      for(Iterator iterator = product.getProductImages().iterator(); iterator.hasNext();)
      {
          ProductImage productimage1 = (ProductImage)iterator.next();
          if(productimage1 == null || productimage1.isEmpty())
              iterator.remove();
          else
          if(productimage1.getFile() != null && !productimage1.getFile().isEmpty() && !IIlIIlll.isValid(net.shopxx.FileInfo.FileType.image, productimage1.getFile()))
          {
              IIIllIlI(redirectAttributes, Message.error("admin.upload.invalid", new Object[0]));
              return "redirect:add.jhtml";
          }
      }

      product.setProductCategory((ProductCategory)IIIllllI.find(productCategoryId));
      product.setBrand((Brand)IIlIIIII.find(brandId));
      product.setTags(new HashSet(IIlIIIlI.findList(tagIds)));
      if(!IIIllIlI(product, new Class[0]))
          return "/admin/common/error";
      if(StringUtils.isNotEmpty(product.getSn()) && IIIlllIl.snExists(product.getSn()))
          return "/admin/common/error";
      if(product.getMarketPrice() == null)
      {
          BigDecimal bigdecimal = IIIllIlI(product.getPrice());
          product.setMarketPrice(bigdecimal);
      }
      if(product.getPoint() == null)
      {
          long l = IIIllIll(product.getPrice());
          product.setPoint(Long.valueOf(l));
      }
      product.setFullName(null);
      product.setAllocatedStock(Integer.valueOf(0));
      product.setScore(Float.valueOf(0.0F));
      product.setTotalScore(Long.valueOf(0L));
      product.setScoreCount(Long.valueOf(0L));
      product.setHits(Long.valueOf(0L));
      product.setWeekHits(Long.valueOf(0L));
      product.setMonthHits(Long.valueOf(0L));
      product.setSales(Long.valueOf(0L));
      product.setWeekSales(Long.valueOf(0L));
      product.setMonthSales(Long.valueOf(0L));
      product.setWeekHitsDate(new Date());
      product.setMonthHitsDate(new Date());
      product.setWeekSalesDate(new Date());
      product.setMonthSalesDate(new Date());
      product.setReviews(null);
      product.setConsultations(null);
      product.setFavoriteMembers(null);
      product.setPromotions(null);
      product.setCartItems(null);
      product.setOrderItems(null);
      product.setGiftItems(null);
      product.setProductNotifies(null);
      for(Iterator iterator1 = IIlIIIll.findAll().iterator(); iterator1.hasNext();)
      {
          MemberRank memberrank = (MemberRank)iterator1.next();
          String s = request.getParameter((new StringBuilder("memberPrice_")).append(memberrank.getId()).toString());
          if(StringUtils.isNotEmpty(s) && (new BigDecimal(s)).compareTo(new BigDecimal(0)) >= 0)
              product.getMemberPrice().put(memberrank, new BigDecimal(s));
          else
              product.getMemberPrice().remove(memberrank);
      }

      ProductImage productimage;
      for(Iterator iterator2 = product.getProductImages().iterator(); iterator2.hasNext(); IIlIIlII.build(productimage))
          productimage = (ProductImage)iterator2.next();

      Collections.sort(product.getProductImages());
      if(product.getImage() == null && product.getThumbnail() != null)
          product.setImage(product.getThumbnail());
      for(Iterator iterator3 = product.getProductCategory().getParameterGroups().iterator(); iterator3.hasNext();)
      {
          ParameterGroup parametergroup = (ParameterGroup)iterator3.next();
          for(Iterator iterator5 = parametergroup.getParameters().iterator(); iterator5.hasNext();)
          {
              Parameter parameter = (Parameter)iterator5.next();
              String s2 = request.getParameter((new StringBuilder("parameter_")).append(parameter.getId()).toString());
              if(StringUtils.isNotEmpty(s2))
                  product.getParameterValue().put(parameter, s2);
              else
                  product.getParameterValue().remove(parameter);
          }

      }

      for(Iterator iterator4 = product.getProductCategory().getAttributes().iterator(); iterator4.hasNext();)
      {
          Attribute attribute = (Attribute)iterator4.next();
          String s1 = request.getParameter((new StringBuilder("attribute_")).append(attribute.getId()).toString());
          if(StringUtils.isNotEmpty(s1))
              product.setAttributeValue(attribute, s1);
          else
              product.setAttributeValue(attribute, null);
      }

      Goods goods = new Goods();
      ArrayList arraylist = new ArrayList();
      if(specificationIds != null && specificationIds.length > 0)
      {
          for(int i = 0; i < specificationIds.length; i++)
          {
              Specification specification = (Specification)IIlIIlIl.find(specificationIds[i]);
              String as[] = request.getParameterValues((new StringBuilder("specification_")).append(specification.getId()).toString());
              if(as != null && as.length > 0)
              {
                  for(int j = 0; j < as.length; j++)
                  {
                      if(i == 0)
                          if(j == 0)
                          {
                              product.setGoods(goods);
                              product.setSpecifications(new HashSet());
                              product.setSpecificationValues(new HashSet());
                              arraylist.add(product);
                          } else
                          {
                              Product product1 = new Product();
                              BeanUtils.copyProperties(product, product1);
                              product1.setId(null);
                              product1.setCreateDate(null);
                              product1.setModifyDate(null);
                              product1.setSn(null);
                              product1.setFullName(null);
                              product1.setAllocatedStock(Integer.valueOf(0));
                              product1.setIsList(Boolean.valueOf(false));
                              product1.setScore(Float.valueOf(0.0F));
                              product1.setTotalScore(Long.valueOf(0L));
                              product1.setScoreCount(Long.valueOf(0L));
                              product1.setHits(Long.valueOf(0L));
                              product1.setWeekHits(Long.valueOf(0L));
                              product1.setMonthHits(Long.valueOf(0L));
                              product1.setSales(Long.valueOf(0L));
                              product1.setWeekSales(Long.valueOf(0L));
                              product1.setMonthSales(Long.valueOf(0L));
                              product1.setWeekHitsDate(new Date());
                              product1.setMonthHitsDate(new Date());
                              product1.setWeekSalesDate(new Date());
                              product1.setMonthSalesDate(new Date());
                              product1.setGoods(goods);
                              product1.setReviews(null);
                              product1.setConsultations(null);
                              product1.setFavoriteMembers(null);
                              product1.setSpecifications(new HashSet());
                              product1.setSpecificationValues(new HashSet());
                              product1.setPromotions(null);
                              product1.setCartItems(null);
                              product1.setOrderItems(null);
                              product1.setGiftItems(null);
                              product1.setProductNotifies(null);
                              arraylist.add(product1);
                          }
                      Product product2 = (Product)arraylist.get(j);
                      SpecificationValue specificationvalue = (SpecificationValue)IIlIIllI.find(Long.valueOf(as[j]));
                      product2.getSpecifications().add(specification);
                      product2.getSpecificationValues().add(specificationvalue);
                  }

              }
          }

      } else
      {
          product.setGoods(goods);
          product.setSpecifications(null);
          product.setSpecificationValues(null);
          arraylist.add(product);
      }
      goods.getProducts().clear();
      goods.getProducts().addAll(arraylist);
      IIIlllll.save(goods);
      IIIllIlI(redirectAttributes, SUCCESS);
      return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String edit(Long id, ModelMap model)
  {
      model.addAttribute("productCategoryTree", IIIllllI.findTree());
      model.addAttribute("brands", IIlIIIII.findAll());
      model.addAttribute("tags", IIlIIIlI.findList(net.shopxx.entity.Tag.Type.product));
      model.addAttribute("memberRanks", IIlIIIll.findAll());
      model.addAttribute("specifications", IIlIIlIl.findAll());
      model.addAttribute("product", IIIlllIl.find(id));
      return "/admin/product/edit";
  }

  @RequestMapping(value={"/update"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String update(Product product, Long productCategoryId, Long brandId, Long tagIds[], Long specificationIds[], Long specificationProductIds[], HttpServletRequest request, 
          RedirectAttributes redirectAttributes)
  {
      for(Iterator iterator = product.getProductImages().iterator(); iterator.hasNext();)
      {
          ProductImage productimage = (ProductImage)iterator.next();
          if(productimage == null || productimage.isEmpty())
              iterator.remove();
          else
          if(productimage.getFile() != null && !productimage.getFile().isEmpty() && !IIlIIlll.isValid(net.shopxx.FileInfo.FileType.image, productimage.getFile()))
          {
              IIIllIlI(redirectAttributes, Message.error("admin.upload.invalid", new Object[0]));
              return (new StringBuilder("redirect:edit.jhtml?id=")).append(product.getId()).toString();
          }
      }

      product.setProductCategory((ProductCategory)IIIllllI.find(productCategoryId));
      product.setBrand((Brand)IIlIIIII.find(brandId));
      product.setTags(new HashSet(IIlIIIlI.findList(tagIds)));
      if(!IIIllIlI(product, new Class[0]))
          return "/admin/common/error";
      Product product1 = (Product)IIIlllIl.find(product.getId());
      if(product1 == null)
          return "/admin/common/error";
      if(StringUtils.isNotEmpty(product.getSn()) && !IIIlllIl.snUnique(product1.getSn(), product.getSn()))
          return "/admin/common/error";
      if(product.getMarketPrice() == null)
      {
          BigDecimal bigdecimal = IIIllIlI(product.getPrice());
          product.setMarketPrice(bigdecimal);
      }
      if(product.getPoint() == null)
      {
          long l = IIIllIll(product.getPrice());
          product.setPoint(Long.valueOf(l));
      }
      for(Iterator iterator1 = IIlIIIll.findAll().iterator(); iterator1.hasNext();)
      {
          MemberRank memberrank = (MemberRank)iterator1.next();
          String s = request.getParameter((new StringBuilder("memberPrice_")).append(memberrank.getId()).toString());
          if(StringUtils.isNotEmpty(s) && (new BigDecimal(s)).compareTo(new BigDecimal(0)) >= 0)
              product.getMemberPrice().put(memberrank, new BigDecimal(s));
          else
              product.getMemberPrice().remove(memberrank);
      }

      ProductImage productimage1;
      for(Iterator iterator2 = product.getProductImages().iterator(); iterator2.hasNext(); IIlIIlII.build(productimage1))
          productimage1 = (ProductImage)iterator2.next();

      Collections.sort(product.getProductImages());
      if(product.getImage() == null && product.getThumbnail() != null)
          product.setImage(product.getThumbnail());
      for(Iterator iterator3 = product.getProductCategory().getParameterGroups().iterator(); iterator3.hasNext();)
      {
          ParameterGroup parametergroup = (ParameterGroup)iterator3.next();
          for(Iterator iterator5 = parametergroup.getParameters().iterator(); iterator5.hasNext();)
          {
              Parameter parameter = (Parameter)iterator5.next();
              String s2 = request.getParameter((new StringBuilder("parameter_")).append(parameter.getId()).toString());
              if(StringUtils.isNotEmpty(s2))
                  product.getParameterValue().put(parameter, s2);
              else
                  product.getParameterValue().remove(parameter);
          }

      }

      for(Iterator iterator4 = product.getProductCategory().getAttributes().iterator(); iterator4.hasNext();)
      {
          Attribute attribute = (Attribute)iterator4.next();
          String s1 = request.getParameter((new StringBuilder("attribute_")).append(attribute.getId()).toString());
          if(StringUtils.isNotEmpty(s1))
              product.setAttributeValue(attribute, s1);
          else
              product.setAttributeValue(attribute, null);
      }

      Goods goods = product1.getGoods();
      ArrayList arraylist = new ArrayList();
      if(specificationIds != null && specificationIds.length > 0)
      {
          for(int i = 0; i < specificationIds.length; i++)
          {
              Specification specification = (Specification)IIlIIlIl.find(specificationIds[i]);
              String as[] = request.getParameterValues((new StringBuilder("specification_")).append(specification.getId()).toString());
              if(as != null && as.length > 0)
              {
                  for(int j = 0; j < as.length; j++)
                  {
                      if(i == 0)
                          if(j == 0)
                          {
                              BeanUtils.copyProperties(product, product1, new String[] {
                                  "id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", 
                                  "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", 
                                  "consultations", "favoriteMembers", "specifications", "specificationValues", "promotions", "cartItems", "orderItems", "giftItems", "productNotifies"
                              });
                              product1.setSpecifications(new HashSet());
                              product1.setSpecificationValues(new HashSet());
                              arraylist.add(product1);
                          } else
                          if(specificationProductIds != null && j < specificationProductIds.length)
                          {
                              Product product2 = (Product)IIIlllIl.find(specificationProductIds[j]);
                              if(product2.getGoods() != goods)
                                  return "/admin/common/error";
                              product2.setSpecifications(new HashSet());
                              product2.setSpecificationValues(new HashSet());
                              arraylist.add(product2);
                          } else
                          {
                              Product product3 = new Product();
                              BeanUtils.copyProperties(product, product3);
                              product3.setId(null);
                              product3.setCreateDate(null);
                              product3.setModifyDate(null);
                              product3.setSn(null);
                              product3.setFullName(null);
                              product3.setAllocatedStock(Integer.valueOf(0));
                              product3.setIsList(Boolean.valueOf(false));
                              product3.setScore(Float.valueOf(0.0F));
                              product3.setTotalScore(Long.valueOf(0L));
                              product3.setScoreCount(Long.valueOf(0L));
                              product3.setHits(Long.valueOf(0L));
                              product3.setWeekHits(Long.valueOf(0L));
                              product3.setMonthHits(Long.valueOf(0L));
                              product3.setSales(Long.valueOf(0L));
                              product3.setWeekSales(Long.valueOf(0L));
                              product3.setMonthSales(Long.valueOf(0L));
                              product3.setWeekHitsDate(new Date());
                              product3.setMonthHitsDate(new Date());
                              product3.setWeekSalesDate(new Date());
                              product3.setMonthSalesDate(new Date());
                              product3.setGoods(goods);
                              product3.setReviews(null);
                              product3.setConsultations(null);
                              product3.setFavoriteMembers(null);
                              product3.setSpecifications(new HashSet());
                              product3.setSpecificationValues(new HashSet());
                              product3.setPromotions(null);
                              product3.setCartItems(null);
                              product3.setOrderItems(null);
                              product3.setGiftItems(null);
                              product3.setProductNotifies(null);
                              arraylist.add(product3);
                          }
                      Product product4 = (Product)arraylist.get(j);
                      SpecificationValue specificationvalue = (SpecificationValue)IIlIIllI.find(Long.valueOf(as[j]));
                      product4.getSpecifications().add(specification);
                      product4.getSpecificationValues().add(specificationvalue);
                  }

              }
          }

      } else
      {
          product.setSpecifications(null);
          product.setSpecificationValues(null);
          BeanUtils.copyProperties(product, product1, new String[] {
              "id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", 
              "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", 
              "consultations", "favoriteMembers", "promotions", "cartItems", "orderItems", "giftItems", "productNotifies"
          });
          arraylist.add(product1);
      }
      goods.getProducts().clear();
      goods.getProducts().addAll(arraylist);
      IIIlllll.update(goods);
      IIIllIlI(redirectAttributes, SUCCESS);
      return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String list(Long productCategoryId, Long brandId, Long promotionId, Long tagId, Boolean isMarketable, Boolean isList, Boolean isTop, 
          Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model)
  {
      ProductCategory productcategory = (ProductCategory)IIIllllI.find(productCategoryId);
      Brand brand = (Brand)IIlIIIII.find(brandId);
      Promotion promotion = (Promotion)IIlIIIIl.find(promotionId);
      List list1 = IIlIIIlI.findList(new Long[] {
          tagId
      });
      model.addAttribute("productCategoryTree", IIIllllI.findTree());
      model.addAttribute("brands", IIlIIIII.findAll());
      model.addAttribute("promotions", IIlIIIIl.findAll());
      model.addAttribute("tags", IIlIIIlI.findList(net.shopxx.entity.Tag.Type.product));
      model.addAttribute("productCategoryId", productCategoryId);
      model.addAttribute("brandId", brandId);
      model.addAttribute("promotionId", promotionId);
      model.addAttribute("tagId", tagId);
      model.addAttribute("isMarketable", isMarketable);
      model.addAttribute("isList", isList);
      model.addAttribute("isTop", isTop);
      model.addAttribute("isGift", isGift);
      model.addAttribute("isOutOfStock", isOutOfStock);
      model.addAttribute("isStockAlert", isStockAlert);
      model.addAttribute("page", IIIlllIl.findPage(productcategory, brand, promotion, list1, null, null, null, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, net.shopxx.entity.Product.OrderType.dateDesc, pageable));
      return "/admin/product/list";
  }

  @RequestMapping(value={"/delete"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.IIIlllIl.delete(ids);
    return SUCCESS;
  }

  private BigDecimal IIIllIlI(BigDecimal paramBigDecimal)
  {
    Setting localSetting = SettingUtils.get();
    Double localDouble = localSetting.getDefaultMarketPriceScale();
    return localSetting.setScale(paramBigDecimal.multiply(new BigDecimal(localDouble.toString())));
  }

  private long IIIllIll(BigDecimal paramBigDecimal)
  {
    Setting localSetting = SettingUtils.get();
    Double localDouble = localSetting.getDefaultPointScale();
    return paramBigDecimal.multiply(new BigDecimal(localDouble.toString())).longValue();
  }
}