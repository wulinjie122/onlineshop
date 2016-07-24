package net.shopxx.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.GiftItem;
import net.shopxx.entity.Product;
import net.shopxx.entity.Promotion;
import net.shopxx.entity.Promotion.Operator;
import net.shopxx.service.BrandService;
import net.shopxx.service.CouponService;
import net.shopxx.service.MemberRankService;
import net.shopxx.service.ProductCategoryService;
import net.shopxx.service.ProductService;
import net.shopxx.service.PromotionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminPromotionController")
@RequestMapping({"/admin/promotion"})
public class PromotionController extends BaseController
{

  @Resource(name="promotionServiceImpl")
  private PromotionService IIIlllIl;

  @Resource(name="memberRankServiceImpl")
  private MemberRankService IIIllllI;

  @Resource(name="productCategoryServiceImpl")
  private ProductCategoryService IIIlllll;

  @Resource(name="productServiceImpl")
  private ProductService IIlIIIII;

  @Resource(name="brandServiceImpl")
  private BrandService IIlIIIIl;

  @Resource(name="couponServiceImpl")
  private CouponService IIlIIIlI;

  @RequestMapping(value={"/product_select"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public List<Map<String, Object>> productSelect(String q)
  {
    ArrayList localArrayList = new ArrayList();
    if (StringUtils.isNotEmpty(q))
    {
      List localList = this.IIlIIIII.search(q, Boolean.valueOf(false), Integer.valueOf(20));
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        Product localProduct = (Product)localIterator.next();
        HashMap localHashMap = new HashMap();
        localHashMap.put("id", localProduct.getId());
        localHashMap.put("sn", localProduct.getSn());
        localHashMap.put("fullName", localProduct.getFullName());
        localHashMap.put("path", localProduct.getPath());
        localArrayList.add(localHashMap);
      }
    }
    return localArrayList;
  }

  @RequestMapping(value={"/gift_select"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public List<Map<String, Object>> giftSelect(String q)
  {
    ArrayList localArrayList = new ArrayList();
    if (StringUtils.isNotEmpty(q))
    {
      List localList = this.IIlIIIII.search(q, Boolean.valueOf(true), Integer.valueOf(20));
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        Product localProduct = (Product)localIterator.next();
        HashMap localHashMap = new HashMap();
        localHashMap.put("id", localProduct.getId());
        localHashMap.put("sn", localProduct.getSn());
        localHashMap.put("fullName", localProduct.getFullName());
        localHashMap.put("path", localProduct.getPath());
        localArrayList.add(localHashMap);
      }
    }
    return localArrayList;
  }

  @RequestMapping(value={"/add"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String add(ModelMap model)
  {
    model.addAttribute("operators", Promotion.Operator.values());
    model.addAttribute("memberRanks", this.IIIllllI.findAll());
    model.addAttribute("productCategories", this.IIIlllll.findAll());
    model.addAttribute("brands", this.IIlIIIIl.findAll());
    model.addAttribute("coupons", this.IIlIIIlI.findAll());
    return "/admin/promotion/add";
  }

  @RequestMapping(value={"/save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String save(Promotion promotion, Long memberRankIds[], Long productCategoryIds[], Long brandIds[], Long couponIds[], Long productIds[], RedirectAttributes redirectAttributes)
  {
      promotion.setMemberRanks(new HashSet(IIIllllI.findList(memberRankIds)));
      promotion.setProductCategories(new HashSet(IIIlllll.findList(productCategoryIds)));
      promotion.setBrands(new HashSet(IIlIIIIl.findList(brandIds)));
      promotion.setCoupons(new HashSet(IIlIIIlI.findList(couponIds)));
      for(Iterator iterator1 = IIlIIIII.findList(productIds).iterator(); iterator1.hasNext();)
      {
          Product product = (Product)iterator1.next();
          if(!product.getIsGift().booleanValue())
              promotion.getProducts().add(product);
      }

      for(Iterator iterator = promotion.getGiftItems().iterator(); iterator.hasNext();)
      {
          GiftItem giftitem = (GiftItem)iterator.next();
          if(giftitem == null || giftitem.getGift() == null || giftitem.getGift().getId() == null)
          {
              iterator.remove();
          } else
          {
              giftitem.setGift((Product)IIlIIIII.find(giftitem.getGift().getId()));
              giftitem.setPromotion(promotion);
          }
      }

      if(!IIIllIlI(promotion, new Class[0]))
          return "/admin/common/error";
      if(promotion.getBeginDate() != null && promotion.getEndDate() != null && promotion.getBeginDate().after(promotion.getEndDate()))
          return "/admin/common/error";
      if(promotion.getStartPrice() != null && promotion.getEndPrice() != null && promotion.getStartPrice().compareTo(promotion.getEndPrice()) > 0)
          return "/admin/common/error";
      if(promotion.getPriceOperator() == net.shopxx.entity.Promotion.Operator.divide && promotion.getPriceValue() != null && promotion.getPriceValue().compareTo(new BigDecimal(0)) == 0)
          return "/admin/common/error";
      if(promotion.getPointOperator() == net.shopxx.entity.Promotion.Operator.divide && promotion.getPointValue() != null && promotion.getPointValue().compareTo(new BigDecimal(0)) == 0)
      {
          return "/admin/common/error";
      } else
      {
          IIIlllIl.save(promotion);
          IIIllIlI(redirectAttributes, IIIlllII);
          return "redirect:list.jhtml";
      }
  }

  @RequestMapping(value={"/edit"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("promotion", this.IIIlllIl.find(id));
    model.addAttribute("operators", Promotion.Operator.values());
    model.addAttribute("memberRanks", this.IIIllllI.findAll());
    model.addAttribute("productCategories", this.IIIlllll.findAll());
    model.addAttribute("brands", this.IIlIIIIl.findAll());
    model.addAttribute("coupons", this.IIlIIIlI.findAll());
    return "/admin/promotion/edit";
  }

  @RequestMapping(value={"/update"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String update(Promotion promotion, Long memberRankIds[], Long productCategoryIds[], Long brandIds[], Long couponIds[], Long productIds[], RedirectAttributes redirectAttributes)
  {
      promotion.setMemberRanks(new HashSet(IIIllllI.findList(memberRankIds)));
      promotion.setProductCategories(new HashSet(IIIlllll.findList(productCategoryIds)));
      promotion.setBrands(new HashSet(IIlIIIIl.findList(brandIds)));
      promotion.setCoupons(new HashSet(IIlIIIlI.findList(couponIds)));
      for(Iterator iterator1 = IIlIIIII.findList(productIds).iterator(); iterator1.hasNext();)
      {
          Product product = (Product)iterator1.next();
          if(!product.getIsGift().booleanValue())
              promotion.getProducts().add(product);
      }

      for(Iterator iterator = promotion.getGiftItems().iterator(); iterator.hasNext();)
      {
          GiftItem giftitem = (GiftItem)iterator.next();
          if(giftitem == null || giftitem.getGift() == null || giftitem.getGift().getId() == null)
          {
              iterator.remove();
          } else
          {
              giftitem.setGift((Product)IIlIIIII.find(giftitem.getGift().getId()));
              giftitem.setPromotion(promotion);
          }
      }

      if(!IIIllIlI(promotion, new Class[0]))
          return "/admin/common/error";
      if(promotion.getBeginDate() != null && promotion.getEndDate() != null && promotion.getBeginDate().after(promotion.getEndDate()))
          return "/admin/common/error";
      if(promotion.getPriceOperator() == net.shopxx.entity.Promotion.Operator.divide && promotion.getPriceValue() != null && promotion.getPriceValue().compareTo(new BigDecimal(0)) == 0)
          return "/admin/common/error";
      if(promotion.getPointOperator() == net.shopxx.entity.Promotion.Operator.divide && promotion.getPointValue() != null && promotion.getPointValue().compareTo(new BigDecimal(0)) == 0)
      {
          return "/admin/common/error";
      } else
      {
          IIIlllIl.update(promotion);
          IIIllIlI(redirectAttributes, IIIlllII);
          return "redirect:list.jhtml";
      }
  }

  @RequestMapping(value={"/list"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.IIIlllIl.findPage(pageable));
    return "/admin/promotion/list";
  }

  @RequestMapping(value={"/delete"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.IIIlllIl.delete(ids);
    return IIIlllII;
  }
}