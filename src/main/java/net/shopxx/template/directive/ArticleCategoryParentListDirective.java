package net.shopxx.template.directive;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;
import java.util.ArrayList;
import java.util.Map;
import javax.annotation.Resource;
import net.shopxx.entity.ArticleCategory;
import net.shopxx.service.ArticleCategoryService;
import net.shopxx.util.FreemarkerUtils;
import org.springframework.stereotype.Component;

@Component("articleCategoryParentListDirective")
public class ArticleCategoryParentListDirective
  extends BaseDirective
{
  private static final String IIIllIlI = "articleCategoryId";
  private static final String IIIllIll = "articleCategories";
  @Resource(name="articleCategoryServiceImpl")
  private ArticleCategoryService IIIlllII;
  
  public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
  {
    Long localLong = (Long)FreemarkerUtils.getParameter("articleCategoryId", Long.class, params);
    ArticleCategory localArticleCategory = (ArticleCategory)this.IIIlllII.find(localLong);
    Object localObject;
    if ((localLong != null) && (localArticleCategory == null))
    {
      localObject = new ArrayList();
    }
    else
    {
      boolean bool = IIIllIlI(env, params);
      String str = IIIllIll(env, params);
      Integer localInteger = IIIllIll(params);
      if (bool) {
        localObject = this.IIIlllII.findParents(localArticleCategory, localInteger, str);
      } else {
        localObject = this.IIIlllII.findParents(localArticleCategory, localInteger);
      }
    }
    IIIllIlI("articleCategories", localObject, env, body);
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.template.directive.ArticleCategoryParentListDirective
 * JD-Core Version:    0.7.0.1
 */