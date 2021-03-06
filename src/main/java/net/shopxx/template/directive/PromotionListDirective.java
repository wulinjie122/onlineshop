package net.shopxx.template.directive;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import net.shopxx.entity.Promotion;
import net.shopxx.service.PromotionService;
import net.shopxx.util.FreemarkerUtils;
import org.springframework.stereotype.Component;

@Component("promotionListDirective")
public class PromotionListDirective
  extends BaseDirective
{
  private static final String IIIllIlI = "hasBegun";
  private static final String IIIllIll = "hasEnded";
  private static final String IIIlllII = "promotions";
  @Resource(name="promotionServiceImpl")
  private PromotionService IIIlllIl;
  
  public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
  {
    Boolean localBoolean1 = (Boolean)FreemarkerUtils.getParameter("hasBegun", Boolean.class, params);
    Boolean localBoolean2 = (Boolean)FreemarkerUtils.getParameter("hasEnded", Boolean.class, params);
    boolean bool = IIIllIlI(env, params);
    String str = IIIllIll(env, params);
    Integer localInteger = IIIllIll(params);
    List localList2 = IIIllIlI(params, Promotion.class, new String[0]);
    List localList3 = IIIllIlI(params, new String[0]);
    List localList1;
    if (bool) {
      localList1 = this.IIIlllIl.findList(localBoolean1, localBoolean2, localInteger, localList2, localList3, str);
    } else {
      localList1 = this.IIIlllIl.findList(localBoolean1, localBoolean2, localInteger, localList2, localList3);
    }
    IIIllIlI("promotions", localList1, env, body);
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.template.directive.PromotionListDirective
 * JD-Core Version:    0.7.0.1
 */