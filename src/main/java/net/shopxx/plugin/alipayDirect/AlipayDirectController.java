package net.shopxx.plugin.alipayDirect;

import java.math.BigDecimal;
import javax.annotation.Resource;

import net.shopxx.controller.admin.BaseController;
import net.shopxx.entity.PluginConfig;
import net.shopxx.plugin.PaymentPlugin;
import net.shopxx.service.PluginConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminAlipayDirectController")
@RequestMapping({"/admin/payment_plugin/alipay_direct"})
public class AlipayDirectController extends BaseController {
    @Resource(name = "alipayDirectPlugin")
    private AlipayDirectPlugin alipayDirectPlugin;
    @Resource(name = "pluginConfigServiceImpl")
    private PluginConfigService pluginConfigService;

    @RequestMapping(value = {"/install"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public String install(RedirectAttributes redirectAttributes) {
        if (!this.alipayDirectPlugin.getIsInstalled()) {
            PluginConfig pluginConfig = new PluginConfig();
            pluginConfig.setPluginId(this.alipayDirectPlugin.getId());
            pluginConfig.setIsEnabled(Boolean.valueOf(false));
            this.pluginConfigService.save(pluginConfig);
        }
        IIIllIlI(redirectAttributes, SUCCESS);
        return "redirect:/admin/payment_plugin/list.jhtml";
    }

    @RequestMapping(value = {"/uninstall"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public String uninstall(RedirectAttributes redirectAttributes) {
        if (this.alipayDirectPlugin.getIsInstalled()) {
            PluginConfig localPluginConfig = this.alipayDirectPlugin.getPluginConfig();
            this.pluginConfigService.delete(localPluginConfig);
        }
        IIIllIlI(redirectAttributes, SUCCESS);
        return "redirect:/admin/payment_plugin/list.jhtml";
    }

    @RequestMapping(value = {"/setting"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public String setting(ModelMap model) {
        PluginConfig localPluginConfig = this.alipayDirectPlugin.getPluginConfig();
        model.addAttribute("feeTypes", PaymentPlugin.FeeType.values());
        model.addAttribute("pluginConfig", localPluginConfig);
        return "/net/shopxx/plugin/alipayDirect/setting";
    }

    @RequestMapping(value = {"/update"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String update(String paymentName, String partner, String key, PaymentPlugin.FeeType feeType, BigDecimal fee, String logo, String description, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes) {
        PluginConfig localPluginConfig = this.alipayDirectPlugin.getPluginConfig();
        localPluginConfig.setAttribute("paymentName", paymentName);
        localPluginConfig.setAttribute("partner", partner);
        localPluginConfig.setAttribute("key", key);
        localPluginConfig.setAttribute("feeType", feeType.toString());
        localPluginConfig.setAttribute("fee", fee.toString());
        localPluginConfig.setAttribute("logo", logo);
        localPluginConfig.setAttribute("description", description);
        localPluginConfig.setIsEnabled(isEnabled);
        localPluginConfig.setOrder(order);
        this.pluginConfigService.update(localPluginConfig);
        IIIllIlI(redirectAttributes, SUCCESS);
        return "redirect:/admin/payment_plugin/list.jhtml";
    }
}