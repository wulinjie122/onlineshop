package net.shopxx.plugin;

import java.math.BigDecimal;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.shopxx.Setting;
import net.shopxx.entity.PluginConfig;
import net.shopxx.service.PluginConfigService;
import net.shopxx.util.SettingUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.stereotype.Component;

public abstract class PaymentPlugin implements Comparable<PaymentPlugin> {

    public static final String PAYMENT_NAME_ATTRIBUTE_NAME = "paymentName";
    public static final String FEE_TYPE_ATTRIBUTE_NAME = "feeType";
    public static final String FEE_ATTRIBUTE_NAME = "fee";
    public static final String LOGO_ATTRIBUTE_NAME = "logo";
    public static final String DESCRIPTION_ATTRIBUTE_NAME = "description";

    @Resource(name = "pluginConfigServiceImpl")
    private PluginConfigService pluginConfigService;

    public final String getId() {
        return ((Component) getClass().getAnnotation(Component.class)).value();
    }

    public abstract String getName();

    public abstract String getVersion();

    public abstract String getAuthor();

    public abstract String getSiteUrl();

    public abstract String getInstallUrl();

    public abstract String getUninstallUrl();

    public abstract String getSettingUrl();

    public boolean getIsInstalled() {
        return this.pluginConfigService.pluginIdExists(getId());
    }

    public PluginConfig getPluginConfig() {
        return this.pluginConfigService.findByPluginId(getId());
    }

    public boolean getIsEnabled() {
        PluginConfig localPluginConfig = getPluginConfig();
        return localPluginConfig != null ? localPluginConfig.getIsEnabled().booleanValue() : false;
    }

    public String getAttribute(String name) {
        PluginConfig localPluginConfig = getPluginConfig();
        return localPluginConfig != null ? localPluginConfig.getAttribute(name) : null;
    }

    public Integer getOrder() {
        PluginConfig localPluginConfig = getPluginConfig();
        return localPluginConfig != null ? localPluginConfig.getOrder() : null;
    }

    public String getPaymentName() {
        PluginConfig localPluginConfig = getPluginConfig();
        return localPluginConfig != null ? localPluginConfig.getAttribute("paymentName") : null;
    }

    public PaymentPlugin.FeeType getFeeType() {
        PluginConfig pluginConfig = getPluginConfig();
        return pluginConfig != null ? PaymentPlugin.FeeType.valueOf(pluginConfig.getAttribute("feeType")) : null;
    }

    public BigDecimal getFee() {
        PluginConfig localPluginConfig = getPluginConfig();
        return localPluginConfig != null ? new BigDecimal(localPluginConfig.getAttribute("fee")) : null;
    }

    public String getLogo() {
        PluginConfig localPluginConfig = getPluginConfig();
        return localPluginConfig != null ? localPluginConfig.getAttribute("logo") : null;
    }

    public String getDescription() {
        PluginConfig localPluginConfig = getPluginConfig();
        return localPluginConfig != null ? localPluginConfig.getAttribute("description") : null;
    }

    public abstract String getUrl();

    public abstract PaymentPlugin.Method getMethod();

    public abstract Integer getTimeout();

    public abstract Map<String, String> getParameterMap(String sn, BigDecimal amount, String paramString2, HttpServletRequest paramHttpServletRequest);

    public abstract boolean verify(String paramString, HttpServletRequest request);

    public abstract BigDecimal getAmount(String paramString, HttpServletRequest paramHttpServletRequest);

    public abstract String getNotifyContext(String paramString, HttpServletRequest paramHttpServletRequest);

    protected String getReturnUrl(String paramString) {
        Setting localSetting = SettingUtils.get();
        return localSetting.getSiteUrl() + "/payment/return/" + paramString + ".jhtml";
    }

    protected String getNotifyUrl(String paramString) {
        Setting localSetting = SettingUtils.get();
        return localSetting.getSiteUrl() + "/payment/getNotifyUrl/" + paramString + ".jhtml";
    }

    public final BigDecimal getFee(BigDecimal amount) {
        Setting setting = SettingUtils.get();
        BigDecimal decimal;
        if (getFeeType() == PaymentPlugin.FeeType.scale) {
            decimal = amount.multiply(getFee());
        } else {
            decimal = getFee();
        }
        return setting.setScale(decimal);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        PaymentPlugin localPaymentPlugin = (PaymentPlugin) obj;
        return new EqualsBuilder().append(getId(), localPaymentPlugin.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
    }

    public int compareTo(PaymentPlugin paymentPlugin) {
        return new CompareToBuilder().append(getOrder(), paymentPlugin.getOrder()).append(getId(), paymentPlugin.getId()).toComparison();
    }

    public enum FeeType {
        scale, fixed;
    }

    public enum Method {
        post, get;
    }
}