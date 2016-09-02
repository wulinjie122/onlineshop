package net.shopxx.controller.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.Admin;
import net.shopxx.entity.Area;
import net.shopxx.entity.DeliveryCorp;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.OrderItem;
import net.shopxx.entity.Payment;
import net.shopxx.entity.PaymentMethod;
import net.shopxx.entity.Product;
import net.shopxx.entity.Refunds;
import net.shopxx.entity.Returns;
import net.shopxx.entity.ReturnsItem;
import net.shopxx.entity.Shipping;
import net.shopxx.entity.ShippingItem;
import net.shopxx.entity.ShippingMethod;
import net.shopxx.entity.Sn;
import net.shopxx.service.AdminService;
import net.shopxx.service.AreaService;
import net.shopxx.service.DeliveryCorpService;
import net.shopxx.service.OrderItemService;
import net.shopxx.service.OrderService;
import net.shopxx.service.PaymentMethodService;
import net.shopxx.service.ProductService;
import net.shopxx.service.ShippingMethodService;
import net.shopxx.service.SnService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminOrderController")
@RequestMapping({ "/admin/order" })
public class OrderController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService IIIlllIl;

	@Resource(name = "areaServiceImpl")
	private AreaService IIIllllI;

	@Resource(name = "productServiceImpl")
	private ProductService IIIlllll;

	@Resource(name = "orderServiceImpl")
	private OrderService IIlIIIII;

	@Resource(name = "orderItemServiceImpl")
	private OrderItemService IIlIIIIl;

	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService IIlIIIlI;

	@Resource(name = "deliveryCorpServiceImpl")
	private DeliveryCorpService IIlIIIll;

	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService IIlIIlII;

	@Resource(name = "snServiceImpl")
	private SnService IIlIIlIl;

	@RequestMapping(value = { "/check_lock" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message checkLock(Long id) {
		Order localOrder = (Order) this.IIlIIIII.find(id);
		if (localOrder == null)
			return Message.warn("admin.common.invalid", new Object[0]);
		Admin localAdmin = this.IIIlllIl.getCurrent();
		if (localOrder.isLocked(localAdmin)) {
			if (localOrder.getOperator() != null)
				return Message
						.warn("admin.order.adminLocked",
								new Object[] { localOrder.getOperator()
										.getUsername() });
			return Message.warn("admin.order.memberLocked", new Object[0]);
		}
		localOrder.setLockExpire(DateUtils.addSeconds(new Date(), 60));
		localOrder.setOperator(localAdmin);
		this.IIlIIIII.update(localOrder);
		return SUCCESS;
	}

	@RequestMapping(value = { "/view" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String view(Long id, ModelMap model) {
		model.addAttribute("types", Payment.Type.values());
		model.addAttribute("refundsTypes", Refunds.Type.values());
		model.addAttribute("paymentMethods", this.IIlIIlII.findAll());
		model.addAttribute("shippingMethods", this.IIlIIIlI.findAll());
		model.addAttribute("deliveryCorps", this.IIlIIIll.findAll());
		model.addAttribute("order", this.IIlIIIII.find(id));
		return "/admin/order/view";
	}

	@RequestMapping(value = { "/confirm" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String confirm(Long id, RedirectAttributes redirectAttributes) {
		Order localOrder = (Order) this.IIlIIIII.find(id);
		Admin localAdmin = this.IIIlllIl.getCurrent();
		if ((localOrder != null)
				&& (!localOrder.isExpired())
				&& (localOrder.getOrderStatus() == Order.OrderStatus.unconfirmed)
				&& (!localOrder.isLocked(localAdmin))) {
			this.IIlIIIII.confirm(localOrder, localAdmin);
			IIIllIlI(redirectAttributes, SUCCESS);
		} else {
			IIIllIlI(redirectAttributes,
					Message.warn("admin.common.invalid", new Object[0]));
		}
		return "redirect:view.jhtml?id=" + id;
	}

	@RequestMapping(value = { "/complete" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String complete(Long id, RedirectAttributes redirectAttributes) {
		Order localOrder = (Order) this.IIlIIIII.find(id);
		Admin localAdmin = this.IIIlllIl.getCurrent();
		if ((localOrder != null) && (!localOrder.isExpired())
				&& (localOrder.getOrderStatus() == Order.OrderStatus.confirmed)
				&& (!localOrder.isLocked(localAdmin))) {
			this.IIlIIIII.complete(localOrder, localAdmin);
			IIIllIlI(redirectAttributes, SUCCESS);
		} else {
			IIIllIlI(redirectAttributes,
					Message.warn("admin.common.invalid", new Object[0]));
		}
		return "redirect:view.jhtml?id=" + id;
	}

	@RequestMapping(value = { "/cancel" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String cancel(Long id, RedirectAttributes redirectAttributes) {
		Order localOrder = (Order) this.IIlIIIII.find(id);
		Admin localAdmin = this.IIIlllIl.getCurrent();
		if ((localOrder != null)
				&& (!localOrder.isExpired())
				&& (localOrder.getOrderStatus() == Order.OrderStatus.unconfirmed)
				&& (!localOrder.isLocked(localAdmin))) {
			this.IIlIIIII.cancel(localOrder, localAdmin);
			IIIllIlI(redirectAttributes, SUCCESS);
		} else {
			IIIllIlI(redirectAttributes,
					Message.warn("admin.common.invalid", new Object[0]));
		}
		return "redirect:view.jhtml?id=" + id;
	}

	@RequestMapping(value = { "/payment" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String payment(Long orderId, Long paymentMethodId, Payment payment,
			RedirectAttributes redirectAttributes) {
		Order localOrder = (Order) this.IIlIIIII.find(orderId);
		payment.setOrder(localOrder);
		PaymentMethod localPaymentMethod = (PaymentMethod) this.IIlIIlII
				.find(paymentMethodId);
		payment.setPaymentMethod(localPaymentMethod != null ? localPaymentMethod
				.getName() : null);
		if (!IIIllIlI(payment, new Class[0]))
			return "/admin/common/error";
		if ((localOrder.isExpired())
				|| (localOrder.getOrderStatus() != Order.OrderStatus.confirmed))
			return "/admin/common/error";
		if ((localOrder.getPaymentStatus() != Order.PaymentStatus.unpaid)
				&& (localOrder.getPaymentStatus() != Order.PaymentStatus.partialPayment))
			return "/admin/common/error";
		if ((payment.getAmount().compareTo(new BigDecimal(0)) <= 0)
				|| (payment.getAmount()
						.compareTo(localOrder.getAmountPayable()) > 0))
			return "/admin/common/error";
		Member localMember = localOrder.getMember();
		if ((payment.getType() == Payment.Type.deposit)
				&& (payment.getAmount().compareTo(localMember.getBalance()) > 0))
			return "/admin/common/error";
		Admin localAdmin = this.IIIlllIl.getCurrent();
		if (localOrder.isLocked(localAdmin))
			return "/admin/common/error";
		payment.setSn(this.IIlIIlIl.generate(Sn.Type.payment));
		payment.setStatus(Payment.Status.success);
		payment.setFee(new BigDecimal(0));
		payment.setOperator(localAdmin.getUsername());
		payment.setPaymentDate(new Date());
		payment.setPaymentPluginId(null);
		payment.setExpire(null);
		payment.setDeposit(null);
		payment.setMember(null);
		this.IIlIIIII.payment(localOrder, payment, localAdmin);
		IIIllIlI(redirectAttributes, SUCCESS);
		return "redirect:view.jhtml?id=" + orderId;
	}

	@RequestMapping(value = { "/refunds" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String refunds(Long orderId, Long paymentMethodId, Refunds refunds,
			RedirectAttributes redirectAttributes) {
		Order localOrder = (Order) this.IIlIIIII.find(orderId);
		refunds.setOrder(localOrder);
		PaymentMethod localPaymentMethod = (PaymentMethod) this.IIlIIlII
				.find(paymentMethodId);
		refunds.setPaymentMethod(localPaymentMethod != null ? localPaymentMethod
				.getName() : null);
		if (!IIIllIlI(refunds, new Class[0]))
			return "/admin/common/error";
		if ((localOrder.isExpired())
				|| (localOrder.getOrderStatus() != Order.OrderStatus.confirmed))
			return "/admin/common/error";
		if ((localOrder.getPaymentStatus() != Order.PaymentStatus.paid)
				&& (localOrder.getPaymentStatus() != Order.PaymentStatus.partialPayment)
				&& (localOrder.getPaymentStatus() != Order.PaymentStatus.partialRefunds))
			return "/admin/common/error";
		if ((refunds.getAmount().compareTo(new BigDecimal(0)) <= 0)
				|| (refunds.getAmount().compareTo(localOrder.getAmountPaid()) > 0))
			return "/admin/common/error";
		Admin localAdmin = this.IIIlllIl.getCurrent();
		if (localOrder.isLocked(localAdmin))
			return "/admin/common/error";
		refunds.setSn(this.IIlIIlIl.generate(Sn.Type.refunds));
		refunds.setOperator(localAdmin.getUsername());
		this.IIlIIIII.refunds(localOrder, refunds, localAdmin);
		IIIllIlI(redirectAttributes, SUCCESS);
		return "redirect:view.jhtml?id=" + orderId;
	}

	@RequestMapping(value = { "/shipping" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String shipping(Long orderId, Long shippingMethodId,
			Long deliveryCorpId, Long areaId, Shipping shipping,
			RedirectAttributes redirectAttributes) {
		Order order = (Order) IIlIIIII.find(orderId);
		if (order == null)
			return "/admin/common/error";
		for (Iterator iterator = shipping.getShippingItems().iterator(); iterator
				.hasNext();) {
			ShippingItem shippingitem = (ShippingItem) iterator.next();
			if (shippingitem == null
					|| StringUtils.isEmpty(shippingitem.getSn())
					|| shippingitem.getQuantity() == null
					|| shippingitem.getQuantity().intValue() <= 0) {
				iterator.remove();
			} else {
				OrderItem orderitem = order.getOrderItem(shippingitem.getSn());
				if (orderitem == null
						|| shippingitem.getQuantity().intValue() > orderitem
								.getQuantity().intValue()
								- orderitem.getShippedQuantity().intValue())
					return "/admin/common/error";
				if (orderitem.getProduct() != null
						&& orderitem.getProduct().getStock() != null
						&& shippingitem.getQuantity().intValue() > orderitem
								.getProduct().getStock().intValue())
					return "/admin/common/error";
				shippingitem.setName(orderitem.getFullName());
				shippingitem.setShipping(shipping);
			}
		}

		shipping.setOrder(order);
		ShippingMethod shippingmethod = (ShippingMethod) IIlIIIlI
				.find(shippingMethodId);
		shipping.setShippingMethod(shippingmethod == null ? null
				: shippingmethod.getName());
		DeliveryCorp deliverycorp = (DeliveryCorp) IIlIIIll
				.find(deliveryCorpId);
		shipping.setDeliveryCorp(deliverycorp == null ? null : deliverycorp
				.getName());
		shipping.setDeliveryCorpUrl(deliverycorp == null ? null : deliverycorp
				.getUrl());
		shipping.setDeliveryCorpCode(deliverycorp == null ? null : deliverycorp
				.getCode());
		Area area = (Area) IIIllllI.find(areaId);
		shipping.setArea(area == null ? null : area.getFullName());
		if (!IIIllIlI(shipping, new Class[0]))
			return "/admin/common/error";
		if (order.isExpired()
				|| order.getOrderStatus() != net.shopxx.entity.Order.OrderStatus.confirmed)
			return "/admin/common/error";
		if (order.getShippingStatus() != net.shopxx.entity.Order.ShippingStatus.unshipped
				&& order.getShippingStatus() != net.shopxx.entity.Order.ShippingStatus.partialShipment)
			return "/admin/common/error";
		Admin admin = IIIlllIl.getCurrent();
		if (order.isLocked(admin)) {
			return "/admin/common/error";
		} else {
			shipping.setSn(IIlIIlIl
					.generate(net.shopxx.entity.Sn.Type.shipping));
			shipping.setOperator(admin.getUsername());
			IIlIIIII.shipping(order, shipping, admin);
			IIIllIlI(redirectAttributes, SUCCESS);
			return (new StringBuilder("redirect:view.jhtml?id=")).append(
					orderId).toString();
		}
	}

	@RequestMapping(value = { "/returns" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String returns(Long orderId, Long shippingMethodId,
			Long deliveryCorpId, Long areaId, Returns returns,
			RedirectAttributes redirectAttributes) {
		Order order = (Order) IIlIIIII.find(orderId);
		if (order == null)
			return "/admin/common/error";
		for (Iterator iterator = returns.getReturnsItems().iterator(); iterator
				.hasNext();) {
			ReturnsItem returnsitem = (ReturnsItem) iterator.next();
			if (returnsitem == null || StringUtils.isEmpty(returnsitem.getSn())
					|| returnsitem.getQuantity() == null
					|| returnsitem.getQuantity().intValue() <= 0) {
				iterator.remove();
			} else {
				OrderItem orderitem = order.getOrderItem(returnsitem.getSn());
				if (orderitem == null
						|| returnsitem.getQuantity().intValue() > orderitem
								.getShippedQuantity().intValue()
								- orderitem.getReturnQuantity().intValue())
					return "/admin/common/error";
				returnsitem.setName(orderitem.getFullName());
				returnsitem.setReturns(returns);
			}
		}

		returns.setOrder(order);
		ShippingMethod shippingmethod = (ShippingMethod) IIlIIIlI
				.find(shippingMethodId);
		returns.setShippingMethod(shippingmethod == null ? null
				: shippingmethod.getName());
		DeliveryCorp deliverycorp = (DeliveryCorp) IIlIIIll
				.find(deliveryCorpId);
		returns.setDeliveryCorp(deliverycorp == null ? null : deliverycorp
				.getName());
		Area area = (Area) IIIllllI.find(areaId);
		returns.setArea(area == null ? null : area.getFullName());
		if (!IIIllIlI(returns, new Class[0]))
			return "/admin/common/error";
		if (order.isExpired()
				|| order.getOrderStatus() != net.shopxx.entity.Order.OrderStatus.confirmed)
			return "/admin/common/error";
		if (order.getShippingStatus() != net.shopxx.entity.Order.ShippingStatus.shipped
				&& order.getShippingStatus() != net.shopxx.entity.Order.ShippingStatus.partialShipment
				&& order.getShippingStatus() != net.shopxx.entity.Order.ShippingStatus.partialReturns)
			return "/admin/common/error";
		Admin admin = IIIlllIl.getCurrent();
		if (order.isLocked(admin)) {
			return "/admin/common/error";
		} else {
			returns.setSn(IIlIIlIl.generate(net.shopxx.entity.Sn.Type.returns));
			returns.setOperator(admin.getUsername());
			IIlIIIII.returns(order, returns, admin);
			IIIllIlI(redirectAttributes, SUCCESS);
			return (new StringBuilder("redirect:view.jhtml?id=")).append(
					orderId).toString();
		}
	}

	@RequestMapping(value = { "/edit" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String edit(Long id, ModelMap model) {
		model.addAttribute("paymentMethods", this.IIlIIlII.findAll());
		model.addAttribute("shippingMethods", this.IIlIIIlI.findAll());
		model.addAttribute("order", this.IIlIIIII.find(id));
		return "/admin/order/edit";
	}

	@RequestMapping(value = { "/order_item_add" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> orderItemAdd(String productSn) {
		HashMap localHashMap = new HashMap();
		Product localProduct = this.IIIlllll.findBySn(productSn);
		if (localProduct == null) {
			localHashMap.put("message",
					Message.warn("admin.order.productNotExist", new Object[0]));
			return localHashMap;
		}
		if (!localProduct.getIsMarketable().booleanValue()) {
			localHashMap.put("message", Message.warn(
					"admin.order.productNotMarketable", new Object[0]));
			return localHashMap;
		}
		if (localProduct.getIsOutOfStock().booleanValue()) {
			localHashMap.put("message", Message.warn(
					"admin.order.productOutOfStock", new Object[0]));
			return localHashMap;
		}
		localHashMap.put("sn", localProduct.getSn());
		localHashMap.put("fullName", localProduct.getFullName());
		localHashMap.put("price", localProduct.getPrice());
		localHashMap.put("weight", localProduct.getWeight());
		localHashMap.put("isGift", localProduct.getIsGift());
		localHashMap.put("message", SUCCESS);
		return localHashMap;
	}

	@RequestMapping(value = { "/calculate" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map calculate(Order order, Long areaId, Long paymentMethodId,
			Long shippingMethodId) {
		HashMap hashmap = new HashMap();
		for (Iterator iterator = order.getOrderItems().iterator(); iterator
				.hasNext();) {
			OrderItem orderitem = (OrderItem) iterator.next();
			if (orderitem == null || StringUtils.isEmpty(orderitem.getSn()))
				iterator.remove();
		}

		order.setArea((Area) IIIllllI.find(areaId));
		order.setPaymentMethod((PaymentMethod) IIlIIlII.find(paymentMethodId));
		order.setShippingMethod((ShippingMethod) IIlIIIlI
				.find(shippingMethodId));
		if (!IIIllIlI(order, new Class[0])) {
			hashmap.put("message",
					Message.warn("admin.common.invalid", new Object[0]));
			return hashmap;
		}
		Order order1 = (Order) IIlIIIII.find(order.getId());
		if (order1 == null) {
			hashmap.put("message",
					Message.error("admin.common.invalid", new Object[0]));
			return hashmap;
		}
		for (Iterator iterator1 = order.getOrderItems().iterator(); iterator1
				.hasNext();) {
			OrderItem orderitem1 = (OrderItem) iterator1.next();
			if (orderitem1.getId() != null) {
				OrderItem orderitem3 = (OrderItem) IIlIIIIl.find(orderitem1
						.getId());
				if (orderitem3 == null || !order1.equals(orderitem3.getOrder())) {
					hashmap.put("message", Message.error(
							"admin.common.invalid", new Object[0]));
					return hashmap;
				}
				Product product1 = orderitem3.getProduct();
				if (product1 != null && product1.getStock() != null)
					if (order1.getIsAllocatedStock().booleanValue()) {
						if (orderitem1.getQuantity().intValue() > product1
								.getAvailableStock().intValue()
								+ orderitem3.getQuantity().intValue()) {
							hashmap.put("message", Message.warn(
									"admin.order.lowStock", new Object[0]));
							return hashmap;
						}
					} else if (orderitem1.getQuantity().intValue() > product1
							.getAvailableStock().intValue()) {
						hashmap.put("message", Message.warn(
								"admin.order.lowStock", new Object[0]));
						return hashmap;
					}
			} else {
				Product product = IIIlllll.findBySn(orderitem1.getSn());
				if (product == null) {
					hashmap.put("message", Message.error(
							"admin.common.invalid", new Object[0]));
					return hashmap;
				}
				if (product.getStock() != null
						&& orderitem1.getQuantity().intValue() > product
								.getAvailableStock().intValue()) {
					hashmap.put("message",
							Message.warn("admin.order.lowStock", new Object[0]));
					return hashmap;
				}
			}
		}

		HashMap hashmap1 = new HashMap();
		OrderItem orderitem2;
		for (Iterator iterator2 = order.getOrderItems().iterator(); iterator2
				.hasNext(); hashmap1.put(orderitem2.getSn(), orderitem2))
			orderitem2 = (OrderItem) iterator2.next();

		hashmap.put("weight", Integer.valueOf(order.getWeight()));
		hashmap.put("price", order.getPrice());
		hashmap.put("quantity", Integer.valueOf(order.getQuantity()));
		hashmap.put("amount", order.getAmount());
		hashmap.put("orderItems", hashmap1);
		hashmap.put("message", SUCCESS);
		return hashmap;
	}

	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String update(Order order, Long areaId, Long paymentMethodId,
			Long shippingMethodId, RedirectAttributes redirectAttributes) {
		for (Iterator iterator = order.getOrderItems().iterator(); iterator
				.hasNext();) {
			OrderItem orderitem = (OrderItem) iterator.next();
			if (orderitem == null || StringUtils.isEmpty(orderitem.getSn()))
				iterator.remove();
		}

		order.setArea((Area) IIIllllI.find(areaId));
		order.setPaymentMethod((PaymentMethod) IIlIIlII.find(paymentMethodId));
		order.setShippingMethod((ShippingMethod) IIlIIIlI
				.find(shippingMethodId));
		if (!IIIllIlI(order, new Class[0]))
			return "/admin/common/error";
		Order order1 = (Order) IIlIIIII.find(order.getId());
		if (order1 == null)
			return "/admin/common/error";
		if (order1.isExpired()
				|| order1.getOrderStatus() != net.shopxx.entity.Order.OrderStatus.unconfirmed)
			return "/admin/common/error";
		Admin admin = IIIlllIl.getCurrent();
		if (order1.isLocked(admin))
			return "/admin/common/error";
		if (!order.getIsInvoice().booleanValue()) {
			order.setInvoiceTitle(null);
			order.setTax(new BigDecimal(0));
		}
		for (Iterator iterator1 = order.getOrderItems().iterator(); iterator1
				.hasNext();) {
			OrderItem orderitem1 = (OrderItem) iterator1.next();
			if (orderitem1.getId() != null) {
				OrderItem orderitem2 = (OrderItem) IIlIIIIl.find(orderitem1
						.getId());
				if (orderitem2 == null || !order1.equals(orderitem2.getOrder()))
					return "/admin/common/error";
				Product product1 = orderitem2.getProduct();
				if (product1 != null && product1.getStock() != null)
					if (order1.getIsAllocatedStock().booleanValue()) {
						if (orderitem1.getQuantity().intValue() > product1
								.getAvailableStock().intValue()
								+ orderitem2.getQuantity().intValue())
							return "/admin/common/error";
					} else if (orderitem1.getQuantity().intValue() > product1
							.getAvailableStock().intValue())
						return "/admin/common/error";
				BeanUtils.copyProperties(orderitem2, orderitem1, new String[] {
						"price", "quantity" });
				if (orderitem2.getIsGift().booleanValue())
					orderitem1.setPrice(new BigDecimal(0));
			} else {
				Product product = IIIlllll.findBySn(orderitem1.getSn());
				if (product == null)
					return "/admin/common/error";
				if (product.getStock() != null
						&& orderitem1.getQuantity().intValue() > product
								.getAvailableStock().intValue())
					return "/admin/common/error";
				orderitem1.setName(product.getName());
				orderitem1.setFullName(product.getFullName());
				if (product.getIsGift().booleanValue())
					orderitem1.setPrice(new BigDecimal(0));
				orderitem1.setWeight(product.getWeight());
				orderitem1.setThumbnail(product.getThumbnail());
				orderitem1.setIsGift(product.getIsGift());
				orderitem1.setShippedQuantity(Integer.valueOf(0));
				orderitem1.setReturnQuantity(Integer.valueOf(0));
				orderitem1.setProduct(product);
				orderitem1.setOrder(order1);
			}
		}

		order.setSn(order1.getSn());
		order.setOrderStatus(order1.getOrderStatus());
		order.setPaymentStatus(order1.getPaymentStatus());
		order.setShippingStatus(order1.getShippingStatus());
		order.setFee(order1.getFee());
		order.setAmountPaid(order1.getAmountPaid());
		order.setPromotion(order1.getPromotion());
		order.setExpire(order1.getExpire());
		order.setLockExpire(null);
		order.setIsAllocatedStock(order1.getIsAllocatedStock());
		order.setOperator(null);
		order.setMember(order1.getMember());
		order.setCouponCode(order1.getCouponCode());
		order.setCoupons(order1.getCoupons());
		order.setOrderLogs(order1.getOrderLogs());
		order.setDeposits(order1.getDeposits());
		order.setPayments(order1.getPayments());
		order.setRefunds(order1.getRefunds());
		order.setShippings(order1.getShippings());
		order.setReturns(order1.getReturns());
		IIlIIIII.update(order, admin);
		IIIllIlI(redirectAttributes, SUCCESS);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(Order.OrderStatus orderStatus,
			Order.PaymentStatus paymentStatus,
			Order.ShippingStatus shippingStatus, Boolean hasExpired,
			Pageable pageable, ModelMap model) {
		model.addAttribute("orderStatus", orderStatus);
		model.addAttribute("paymentStatus", paymentStatus);
		model.addAttribute("shippingStatus", shippingStatus);
		model.addAttribute("hasExpired", hasExpired);
		model.addAttribute("page", this.IIlIIIII.findPage(orderStatus,
				paymentStatus, shippingStatus, hasExpired, pageable));
		return "/admin/order/list";
	}

	@RequestMapping(value = { "/delete" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message delete(Long[] ids) {
		if (ids != null) {
			Admin localAdmin = this.IIIlllIl.getCurrent();
			for (Long localLong : ids) {
				Order localOrder = (Order) this.IIlIIIII.find(localLong);
				if ((localOrder != null) && (localOrder.isLocked(localAdmin)))
					return Message.error("admin.order.deleteLockedNotAllowed",
							new Object[] { localOrder.getSn() });
			}
			this.IIlIIIII.delete(ids);
		}
		return SUCCESS;
	}
}