package net.shopxx.controller.shop;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.Payment;
import net.shopxx.entity.PaymentMethod;
import net.shopxx.entity.Sn;
import net.shopxx.plugin.PaymentPlugin;
import net.shopxx.service.MemberService;
import net.shopxx.service.OrderService;
import net.shopxx.service.PaymentService;
import net.shopxx.service.PluginService;
import net.shopxx.service.SnService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("shopPaymentController")
@RequestMapping({"/payment"})
public class PaymentController extends BaseController {
    @Resource(name = "orderServiceImpl")
    private OrderService orderService;
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;
    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;
    @Resource(name = "snServiceImpl")
    private SnService snService;

    @RequestMapping(value = {"/submit"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String submit(String sn, String paymentPluginId, HttpServletRequest request, ModelMap model) {
        Order order = this.orderService.findBySn(sn);       //查找订单
        if (order == null) {
            return "/shop/common/error";
        }
        Member member = this.memberService.getCurrent();
        if ((member == null) || (order.getMember() != member) || (order.isExpired())) { //订单过期
            return "/shop/common/error";
        }
        if ((order.getPaymentMethod() == null) || (order.getPaymentMethod().getType() == PaymentMethod.Type.offline)) { //离线支付
            return "/shop/common/error";
        }
        if ((order.getPaymentStatus() != Order.PaymentStatus.unpaid) && (order.getPaymentStatus() != Order.PaymentStatus.partialPayment)) {
            return "/shop/common/error";
        }
        if (order.getAmountPayable().compareTo(new BigDecimal(0)) <= 0) {
            return "/shop/common/error";
        }
        PaymentPlugin paymentPlugin = this.pluginService.getPaymentPlugin(paymentPluginId);
        if ((paymentPlugin == null) || (!paymentPlugin.getIsEnabled())) {
            return "/shop/common/error";
        }
        BigDecimal fee = paymentPlugin.getFee(order.getAmountPayable());
        BigDecimal amount = order.getAmountPayable().add(fee);
        //新建支付
        Payment payment = new Payment();
        payment.setSn(this.snService.generate(Sn.Type.payment));
        payment.setType(Payment.Type.online);
        payment.setStatus(Payment.Status.wait);
        payment.setPaymentMethod(order.getPaymentMethodName() + "-" + paymentPlugin.getPaymentName());
        payment.setFee(fee);
        payment.setAmount(amount);
        payment.setPaymentPluginId(paymentPluginId);
        payment.setExpire(paymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout().intValue()) : null);
        payment.setMember(null);
        payment.setOrder(order);
        this.paymentService.save(payment);
        model.addAttribute("url", paymentPlugin.getUrl());
        model.addAttribute("method", paymentPlugin.getMethod());
        model.addAttribute("parameterMap", paymentPlugin.getParameterMap(payment.getSn(), amount, order.getProductName(), request));
        return "shop/payment/submit";
    }

    @RequestMapping({"/return/{sn}"})
    public String returns(@PathVariable String sn, HttpServletRequest request, ModelMap model) {
        Payment payment = this.paymentService.findBySn(sn);
        if (payment == null) {
            return "/shop/common/error";
        }
        if (payment.getStatus() == Payment.Status.wait) {
            PaymentPlugin paymentPlugin = this.pluginService.getPaymentPlugin(payment.getPaymentPluginId());
            if ((paymentPlugin != null) && (paymentPlugin.verify(sn, request))) {
                BigDecimal amount = paymentPlugin.getAmount(sn, request);
                if (amount.compareTo(payment.getAmount()) >= 0) {
                    Order order = payment.getOrder();
                    if (order != null) {
                        if (amount.compareTo(order.getAmountPayable()) >= 0) {
                            this.orderService.payment(order, payment, null);
                        }
                    } else {
                        Member member = payment.getMember();
                        if (member != null) {
                            BigDecimal localBigDecimal2 = payment.getAmount().subtract(payment.getFee());
                            this.memberService.update(member, null, localBigDecimal2, getMessage("shop.payment.paymentName", new Object[]{paymentPlugin.getPaymentName()}), null);
                        }
                    }
                }
                payment.setStatus(Payment.Status.success);
                payment.setAmount(amount);
                payment.setPaymentDate(new Date());
            } else {
                payment.setStatus(Payment.Status.failure);
                payment.setPaymentDate(new Date());
            }
            this.paymentService.update(payment);
        }
        model.addAttribute("payment", payment);
        return "shop/payment/return";
    }

    @RequestMapping({"/notify/{sn}"})
    public String notify(@PathVariable String sn, HttpServletRequest request, ModelMap model) {
        Payment payment = this.paymentService.findBySn(sn);
        if (payment != null) {
            PaymentPlugin paymentPlugin = this.pluginService.getPaymentPlugin(payment.getPaymentPluginId());
            if (paymentPlugin != null) {
                if ((payment.getStatus() == Payment.Status.wait) && (paymentPlugin.verify(sn, request))) {
                    BigDecimal amount = paymentPlugin.getAmount(sn, request);
                    if (amount.compareTo(payment.getAmount()) >= 0) {
                        Order order = payment.getOrder();
                        if (order != null) {
                            if (amount.compareTo(order.getAmountPayable()) >= 0) {
                                this.orderService.payment(order, payment, null);
                            }
                        } else {
                            Member member = payment.getMember();
                            if (member != null) {
                                BigDecimal localBigDecimal2 = payment.getAmount().subtract(payment.getFee());
                                this.memberService.update(member, null, localBigDecimal2, getMessage("shop.payment.paymentName", new Object[]{paymentPlugin.getPaymentName()}), null);
                            }
                        }
                    }
                    payment.setStatus(Payment.Status.success);
                    payment.setAmount(amount);
                    payment.setPaymentDate(new Date());
                    this.paymentService.update(payment);
                }
                model.addAttribute("notifyContext", paymentPlugin.getNotifyContext(sn, request));
            }
        }
        return "shop/payment/getNotifyUrl";
    }
}