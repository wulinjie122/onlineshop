package net.shopxx.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import net.shopxx.dao.ArticleCategoryDao;
import net.shopxx.dao.ArticleDao;
import net.shopxx.dao.TagDao;
import net.shopxx.entity.Article;
import net.shopxx.entity.ArticleCategory;
import net.shopxx.entity.Tag;
import net.shopxx.entity.Tag.Type;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/applicationContext.xml", "classpath*:/applicationContext-mvc.xml"})
@Transactional
public class ArticleDaoImplTest
{
  @Resource(name="articleCategoryDaoImpl")
  private ArticleCategoryDao articleCategoryDao;
  @Resource(name="tagDaoImpl")
  private TagDao IIIllIll;
  @Resource(name="articleDaoImpl")
  private ArticleDao IIIlllII;
  private static final Logger IIIlllIl = LoggerFactory.getLogger(ArticleDaoImplTest.class);
  private static Long[] IIIllllI = new Long[100];
  private static Long[] IIIlllll = new Long[100];
  private static Long[] IIlIIIII = new Long[20];
  
  @Before
  public void prepareTestData()
  {
    String str;
    ArticleCategory articlecategory;
    for (int i = 0; i < IIIllllI.length; i++)
    {
      str = "test" + i;
      articlecategory = new ArticleCategory();
      if (i < 20)
      {
    	  articlecategory.setName(str);
    	  articlecategory.setOrder(Integer.valueOf(i));
        this.articleCategoryDao.persist(articlecategory);
      }
      else
      {
    	  articlecategory.setName(str);
    	  articlecategory.setOrder(Integer.valueOf(i));
    	  articlecategory.setParent((ArticleCategory)this.articleCategoryDao.find(IIIllllI[0]));
        this.articleCategoryDao.persist(articlecategory);
      }
      IIIllllI[i] = ((ArticleCategory)articlecategory).getId();
    }
    this.articleCategoryDao.flush();
    this.articleCategoryDao.clear();
    
    for (int i = 0; i < IIlIIIII.length; i++)
    {
      str = "test" + i;
      Tag tag = new Tag();
      tag.setName(str);
      tag.setOrder(Integer.valueOf(i));
      tag.setType(Tag.Type.article);
      this.IIIllIll.persist(tag);
      IIlIIIII[i] = tag.getId();
    }
    this.IIIllIll.flush();
    this.IIIllIll.clear();
    
    for (int i = 0; i < IIIlllll.length; i++)
    {
      str = "test" + i;
      Article article = new Article();
      article.setTitle(str);
      article.setContent(str);
      article.setIsPublication(Boolean.valueOf(true));
      article.setIsTop(Boolean.valueOf(false));
      article.setHits(Long.valueOf(0L));
      if (i < 20) {
    	  article.setArticleCategory((ArticleCategory)this.articleCategoryDao.find(IIIllllI[0]));
      } else {
    	  article.setArticleCategory((ArticleCategory)this.articleCategoryDao.find(IIIllllI[1]));
      }
      if (i < 20)
      {
        HashSet hashset = new HashSet();
        if (i < 10)
        {
          hashset.add((Tag)this.IIIllIll.find(IIlIIIII[0]));
          hashset.add((Tag)this.IIIllIll.find(IIlIIIII[1]));
        }
        hashset.add((Tag)this.IIIllIll.find(IIlIIIII[2]));
        article.setTags(hashset);
      }
      this.IIIlllII.persist(article);
      IIIlllll[i] = article.getId();
    }
    this.IIIlllII.flush();
    this.IIIlllII.clear();
    IIIlllIl.info("prepare test data");
  }
  
  @Test
  public void testFindList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add((Tag)this.IIIllIll.find(IIlIIIII[0]));
    localArrayList.add((Tag)this.IIIllIll.find(IIlIIIII[2]));
    MatcherAssert.assertThat(Integer.valueOf(this.IIIlllII.findList(null, localArrayList, null, null, null).size()), Matchers.is(Integer.valueOf(80)));
  }
}