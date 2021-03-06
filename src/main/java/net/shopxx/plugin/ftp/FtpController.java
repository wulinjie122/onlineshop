package net.shopxx.plugin.ftp;

import javax.annotation.Resource;
import net.shopxx.controller.admin.BaseController;
import net.shopxx.entity.PluginConfig;
import net.shopxx.service.PluginConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminPluginFtpController")
@RequestMapping({"/admin/storage_plugin/ftp"})
public class FtpController
  extends BaseController
{
  @Resource(name="ftpPlugin")
  private FtpPlugin IIIlllIl;
  @Resource(name="pluginConfigServiceImpl")
  private PluginConfigService IIIllllI;
  
  @RequestMapping(value={"/install"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String install(RedirectAttributes redirectAttributes)
  {
    if (!this.IIIlllIl.getIsInstalled())
    {
      PluginConfig localPluginConfig = new PluginConfig();
      localPluginConfig.setPluginId(this.IIIlllIl.getId());
      localPluginConfig.setIsEnabled(Boolean.valueOf(false));
      this.IIIllllI.save(localPluginConfig);
    }
    IIIllIlI(redirectAttributes, SUCCESS);
    return "redirect:/admin/storage_plugin/list.jhtml";
  }
  
  @RequestMapping(value={"/uninstall"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String uninstall(RedirectAttributes redirectAttributes)
  {
    if (this.IIIlllIl.getIsInstalled())
    {
      PluginConfig localPluginConfig = this.IIIlllIl.getPluginConfig();
      this.IIIllllI.delete(localPluginConfig);
    }
    IIIllIlI(redirectAttributes, SUCCESS);
    return "redirect:/admin/storage_plugin/list.jhtml";
  }
  
  @RequestMapping(value={"/setting"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String setting(ModelMap model)
  {
    PluginConfig localPluginConfig = this.IIIlllIl.getPluginConfig();
    model.addAttribute("pluginConfig", localPluginConfig);
    return "/net/shopxx/plugin/ftp/setting";
  }
  
  @RequestMapping(value={"/update"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String update(String host, Integer port, String username, String password, String urlPrefix, @RequestParam(defaultValue="false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes)
  {
    PluginConfig localPluginConfig = this.IIIlllIl.getPluginConfig();
    localPluginConfig.setAttribute("host", host);
    localPluginConfig.setAttribute("port", port.toString());
    localPluginConfig.setAttribute("username", username);
    localPluginConfig.setAttribute("password", password);
    localPluginConfig.setAttribute("urlPrefix", StringUtils.removeEnd(urlPrefix, "/"));
    localPluginConfig.setIsEnabled(isEnabled);
    localPluginConfig.setOrder(order);
    this.IIIllllI.update(localPluginConfig);
    IIIllIlI(redirectAttributes, SUCCESS);
    return "redirect:/admin/storage_plugin/list.jhtml";
  }
}



/* Location:           D:\workspace\shopxx\WEB-INF\classes\

 * Qualified Name:     net.shopxx.plugin.ftp.FtpController

 * JD-Core Version:    0.7.0.1

 */