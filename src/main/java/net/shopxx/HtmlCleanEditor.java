package net.shopxx;

import java.beans.PropertyEditorSupport;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class HtmlCleanEditor
  extends PropertyEditorSupport
{
  private boolean trim;
  private boolean emptyAsNull;
  private Whitelist whitelist = Whitelist.none();
  
  public HtmlCleanEditor(boolean trim, boolean emptyAsNull)
  {
    this.trim = trim;
    this.emptyAsNull = emptyAsNull;
  }
  
  public HtmlCleanEditor(boolean trim, boolean emptyAsNull, Whitelist whitelist)
  {
    this.trim = trim;
    this.emptyAsNull = emptyAsNull;
    this.whitelist = whitelist;
  }
  
  public String getAsText()
  {
    Object localObject = getValue();
    return localObject != null ? localObject.toString() : "";
  }
  
  public void setAsText(String text)
  {
    if (text != null)
    {
      String str = this.trim ? text.trim() : text;
      str = Jsoup.clean(str, this.whitelist);
      if ((this.emptyAsNull) && ("".equals(str))) {
        str = null;
      }
      setValue(str);
    }
    else
    {
      setValue(null);
    }
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.HtmlCleanEditor
 * JD-Core Version:    0.7.0.1
 */