package net.shopxx.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.shopxx.Filter;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.Setting;
import net.shopxx.Setting.StockAllocationTime;
import net.shopxx.dao.CartDao;
import net.shopxx.dao.CouponCodeDao;
import net.shopxx.dao.DepositDao;
import net.shopxx.dao.MemberDao;
import net.shopxx.dao.MemberRankDao;
import net.shopxx.dao.OrderDao;
import net.shopxx.dao.OrderItemDao;
import net.shopxx.dao.OrderLogDao;
import net.shopxx.dao.PaymentDao;
import net.shopxx.dao.ProductDao;
import net.shopxx.dao.RefundsDao;
import net.shopxx.dao.ReturnsDao;
import net.shopxx.dao.ShippingDao;
import net.shopxx.dao.SnDao;
import net.shopxx.entity.Admin;
import net.shopxx.entity.Cart;
import net.shopxx.entity.CartItem;
import net.shopxx.entity.Coupon;
import net.shopxx.entity.CouponCode;
import net.shopxx.entity.Deposit;
import net.shopxx.entity.Deposit.Type;
import net.shopxx.entity.GiftItem;
import net.shopxx.entity.Member;
import net.shopxx.entity.MemberRank;
import net.shopxx.entity.Order;
import net.shopxx.entity.Order.OrderStatus;
import net.shopxx.entity.Order.PaymentStatus;
import net.shopxx.entity.Order.ShippingStatus;
import net.shopxx.entity.OrderItem;
import net.shopxx.entity.OrderLog;
import net.shopxx.entity.Payment;
import net.shopxx.entity.PaymentMethod;
import net.shopxx.entity.Product;
import net.shopxx.entity.Promotion;
import net.shopxx.entity.Receiver;
import net.shopxx.entity.Refunds;
import net.shopxx.entity.Returns;
import net.shopxx.entity.ReturnsItem;
import net.shopxx.entity.Shipping;
import net.shopxx.entity.ShippingItem;
import net.shopxx.entity.ShippingMethod;
import net.shopxx.entity.Sn;
import net.shopxx.service.OrderService;
import net.shopxx.service.StaticService;
import net.shopxx.util.SettingUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {
    @Resource(name = "orderDaoImpl")
    private OrderDao orderDao;
    @Resource(name = "orderItemDaoImpl")
    private OrderItemDao orderItemDao;
    @Resource(name = "orderLogDaoImpl")
    private OrderLogDao orderLogDao;
    @Resource(name = "cartDaoImpl")
    private CartDao cartDao;
    @Resource(name = "couponCodeDaoImpl")
    private CouponCodeDao couponCodeDao;
    @Resource(name = "snDaoImpl")
    private SnDao snDao;
    @Resource(name = "memberDaoImpl")
    private MemberDao memberDao;
    @Resource(name = "memberRankDaoImpl")
    private MemberRankDao memberRankDao;
    @Resource(name = "productDaoImpl")
    private ProductDao productDao;
    @Resource(name = "depositDaoImpl")
    private DepositDao disDepositDao;
    @Resource(name = "paymentDaoImpl")
    private PaymentDao paymentDao;
    @Resource(name = "refundsDaoImpl")
    private RefundsDao refundsDao;
    @Resource(name = "shippingDaoImpl")
    private ShippingDao shippingDao;
    @Resource(name = "returnsDaoImpl")
    private ReturnsDao returnsDao;
    @Resource(name = "staticServiceImpl")
    private StaticService staticService;

    @Resource(name = "orderDaoImpl")
    public void setBaseDao(OrderDao orderDao) {
        super.setBaseDao(orderDao);
    }

    @Transactional(readOnly = true)
    public Order findBySn(String sn) {
        return this.orderDao.findBySn(sn);
    }

    @Transactional(readOnly = true)
    public List<Order> findList(Member member, Integer count, List<Filter> filters, List<net.shopxx.Order> orders) {
        return this.orderDao.findList(member, count, filters, orders);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPage(Member member, Pageable pageable) {
        return this.orderDao.findPage(member, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPage(Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Order.ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
        return this.orderDao.findPage(orderStatus, paymentStatus, shippingStatus, hasExpired, pageable);
    }

    @Transactional(readOnly = true)
    public Long count(Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Order.ShippingStatus shippingStatus, Boolean hasExpired) {
        return this.orderDao.count(orderStatus, paymentStatus, shippingStatus, hasExpired);
    }

    @Transactional(readOnly = true)
    public Long waitingPaymentCount(Member member) {
        return this.orderDao.waitingPaymentCount(member);
    }

    @Transactional(readOnly = true)
    public Long waitingShippingCount(Member member) {
        return this.orderDao.waitingShippingCount(member);
    }

    @Transactional(readOnly = true)
    public BigDecimal getSalesAmount(Date beginDate, Date endDate) {
        return this.orderDao.getSalesAmount(beginDate, endDate);
    }

    @Transactional(readOnly = true)
    public Integer getSalesVolume(Date beginDate, Date endDate) {
        return this.orderDao.getSalesVolume(beginDate, endDate);
    }

    public void releaseStock() {
        this.orderDao.releaseStock();
    }

    @Transactional(readOnly = true)
    public Order build(Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, boolean isInvoice, String invoiceTitle, boolean useBalance, String memo) {
        Assert.notNull(cart);
        Assert.notNull(cart.getMember());
        Assert.notEmpty(cart.getCartItems());
        Order order = new Order();
        order.setShippingStatus(Order.ShippingStatus.unshipped);
        order.setFee(new BigDecimal(0));
        order.setDiscount(cart.getDiscount());
        order.setPoint(Integer.valueOf(cart.getPoint()));
        order.setMemo(memo);
        order.setMember(cart.getMember());
        if (receiver != null) {
            order.setConsignee(receiver.getConsignee());
            order.setAreaName(receiver.getAreaName());
            order.setAddress(receiver.getAddress());
            order.setZipCode(receiver.getZipCode());
            order.setPhone(receiver.getPhone());
            order.setArea(receiver.getArea());
        }
        if (!cart.getPromotions().isEmpty()) {
            StringBuffer stringbuffer = new StringBuffer();
            Iterator localObject3 = cart.getPromotions().iterator();
            while (((Iterator) localObject3).hasNext()) {
                Promotion promotion = (Promotion) ((Iterator) localObject3).next();
                if ((promotion != null) && (((Promotion) promotion).getName() != null)) {
                    ((StringBuffer) stringbuffer).append(" " + ((Promotion) promotion).getName());
                }
            }
            if (((StringBuffer) stringbuffer).length() > 0) {
                ((StringBuffer) stringbuffer).deleteCharAt(0);
            }
            order.setPromotion(((StringBuffer) stringbuffer).toString());
        }
        order.setPaymentMethod(paymentMethod);
        if ((shippingMethod != null) && (paymentMethod != null) && (paymentMethod.getShippingMethods().contains(shippingMethod))) {
            BigDecimal bigdecimal = shippingMethod.calculateFreight(Integer.valueOf(cart.getWeight()));
            Iterator localObject3 = cart.getPromotions().iterator();
            while (((Iterator) localObject3).hasNext()) {
                Promotion localObject2 = (Promotion) ((Iterator) localObject3).next();
                if (((Promotion) localObject2).getIsFreeShipping().booleanValue()) {
                    bigdecimal = new BigDecimal(0);
                    break;
                }
            }
            order.setFreight((BigDecimal) bigdecimal);
            order.setShippingMethod(shippingMethod);
        } else {
            order.setFreight(new BigDecimal(0));
        }
        if ((couponCode != null) && (cart.isCouponAllowed())) {
            this.couponCodeDao.lock(couponCode, LockModeType.PESSIMISTIC_READ);
            if ((!couponCode.getIsUsed().booleanValue()) && (couponCode.getCoupon() != null) && (cart.isValid(couponCode.getCoupon()))) {
                BigDecimal localObject1 = couponCode.getCoupon().calculatePrice(cart.getAmount());
                BigDecimal localObject2 = cart.getAmount().subtract((BigDecimal) localObject1);
                if (((BigDecimal) localObject2).compareTo(new BigDecimal(0)) > 0) {
                    order.setDiscount(cart.getDiscount().add((BigDecimal) localObject2));
                }
                order.setCouponCode(couponCode);
            }
        }
        Object localObject1 = order.getOrderItems();
        Object localObject3 = cart.getCartItems().iterator();
        Product localProduct;
        OrderItem localOrderItem;
        while (((Iterator) localObject3).hasNext()) {
            CartItem localObject2 = (CartItem) ((Iterator) localObject3).next();
            if ((localObject2 != null) && (((CartItem) localObject2).getProduct() != null)) {
                localProduct = ((CartItem) localObject2).getProduct();
                localOrderItem = new OrderItem();
                localOrderItem.setSn(localProduct.getSn());
                localOrderItem.setName(localProduct.getName());
                localOrderItem.setFullName(localProduct.getFullName());
                localOrderItem.setPrice(((CartItem) localObject2).getUnitPrice());
                localOrderItem.setWeight(localProduct.getWeight());
                localOrderItem.setThumbnail(localProduct.getThumbnail());
                localOrderItem.setIsGift(Boolean.valueOf(false));
                localOrderItem.setQuantity(((CartItem) localObject2).getQuantity());
                localOrderItem.setShippedQuantity(Integer.valueOf(0));
                localOrderItem.setReturnQuantity(Integer.valueOf(0));
                localOrderItem.setProduct(localProduct);
                localOrderItem.setOrder(order);
                ((List) localObject1).add(localOrderItem);
            }
        }
        localObject3 = cart.getGiftItems().iterator();
        while (((Iterator) localObject3).hasNext()) {
            GiftItem localObject2 = (GiftItem) ((Iterator) localObject3).next();
            if ((localObject2 != null) && (((GiftItem) localObject2).getGift() != null)) {
                localProduct = ((GiftItem) localObject2).getGift();
                localOrderItem = new OrderItem();
                localOrderItem.setSn(localProduct.getSn());
                localOrderItem.setName(localProduct.getName());
                localOrderItem.setFullName(localProduct.getFullName());
                localOrderItem.setPrice(new BigDecimal(0));
                localOrderItem.setWeight(localProduct.getWeight());
                localOrderItem.setThumbnail(localProduct.getThumbnail());
                localOrderItem.setIsGift(Boolean.valueOf(true));
                localOrderItem.setQuantity(((GiftItem) localObject2).getQuantity());
                localOrderItem.setShippedQuantity(Integer.valueOf(0));
                localOrderItem.setReturnQuantity(Integer.valueOf(0));
                localOrderItem.setProduct(localProduct);
                localOrderItem.setOrder(order);
                ((List) localObject1).add(localOrderItem);
            }
        }
        Object localObject2 = SettingUtils.get();
        if ((((Setting) localObject2).getIsInvoiceEnabled().booleanValue()) && (isInvoice) && (StringUtils.isNotEmpty(invoiceTitle))) {
            order.setIsInvoice(Boolean.valueOf(true));
            order.setInvoiceTitle(invoiceTitle);
            order.setTax(order.calculateTax());
        } else {
            order.setIsInvoice(Boolean.valueOf(false));
            order.setTax(new BigDecimal(0));
        }
        if (useBalance) {
            localObject3 = cart.getMember();
            if (((Member) localObject3).getBalance().compareTo(order.getAmount()) >= 0) {
                order.setAmountPaid(order.getAmount());
            } else {
                order.setAmountPaid(((Member) localObject3).getBalance());
            }
        } else {
            order.setAmountPaid(new BigDecimal(0));
        }
        if (order.getAmountPayable().compareTo(new BigDecimal(0)) == 0) {
            order.setOrderStatus(Order.OrderStatus.confirmed);
            order.setPaymentStatus(Order.PaymentStatus.paid);
        } else if ((order.getAmountPayable().compareTo(new BigDecimal(0)) > 0) && (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0)) {
            order.setOrderStatus(Order.OrderStatus.confirmed);
            order.setPaymentStatus(Order.PaymentStatus.partialPayment);
        } else {
            order.setOrderStatus(Order.OrderStatus.unconfirmed);
            order.setPaymentStatus(Order.PaymentStatus.unpaid);
        }
        if ((paymentMethod != null) && (paymentMethod.getTimeout() != null) && (order.getPaymentStatus() == Order.PaymentStatus.unpaid)) {
            order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout().intValue()));
        }
        return order;
    }

    public Order create(Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, boolean isInvoice, String invoiceTitle, boolean useBalance, String memo, Admin operator) {
        Assert.notNull(cart);
        Assert.notNull(cart.getMember());
        Assert.notEmpty(cart.getCartItems());
        Assert.notNull(receiver);
        Assert.notNull(paymentMethod);
        Assert.notNull(shippingMethod);
        Order order = build(cart, receiver, paymentMethod, shippingMethod, couponCode, isInvoice, invoiceTitle, useBalance, memo);
        order.setSn(this.snDao.generate(Sn.Type.order));
        if (paymentMethod.getType() == PaymentMethod.Type.online) {
            order.setLockExpire(DateUtils.addSeconds(new Date(), 10));
            order.setOperator(operator);
        }
        if (order.getCouponCode() != null) {
            couponCode.setIsUsed(Boolean.valueOf(true));
            couponCode.setUsedDate(new Date());
            this.couponCodeDao.merge(couponCode);
        }
        Object localObject2 = cart.getPromotions().iterator();
        Object localObject4;
        while (((Iterator) localObject2).hasNext()) {
            Promotion localObject1 = (Promotion) ((Iterator) localObject2).next();
            localObject4 = ((Promotion) localObject1).getCoupons().iterator();
            while (((Iterator) localObject4).hasNext()) {
                Coupon localObject3 = (Coupon) ((Iterator) localObject4).next();
                order.getCoupons().add(localObject3);
            }
        }
        Object localObject1 = SettingUtils.get();
        if ((((Setting) localObject1).getStockAllocationTime() == Setting.StockAllocationTime.order) || ((((Setting) localObject1).getStockAllocationTime() == Setting.StockAllocationTime.payment) && ((order.getPaymentStatus() == Order.PaymentStatus.partialPayment) || (order.getPaymentStatus() == Order.PaymentStatus.paid)))) {
            order.setIsAllocatedStock(Boolean.valueOf(true));
        } else {
            order.setIsAllocatedStock(Boolean.valueOf(false));
        }
        this.orderDao.persist(order);
        OrderLog orderlog = new OrderLog();
        orderlog.setType(OrderLog.Type.create);
        orderlog.setOperator(operator != null ? operator.getUsername() : null);
        orderlog.setOrder(order);
        this.orderLogDao.persist(orderlog);
        Member member = cart.getMember();
        if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
            this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
            member.setBalance(member.getBalance().subtract(order.getAmountPaid()));
            this.memberDao.merge(member);
            Deposit deposit = new Deposit();
            deposit.setType(operator != null ? Deposit.Type.adminPayment : Deposit.Type.memberPayment);
            deposit.setCredit(new BigDecimal(0));
            deposit.setDebit(order.getAmountPaid());
            deposit.setBalance(member.getBalance());
            deposit.setOperator(operator != null ? operator.getUsername() : null);
            deposit.setMember(member);
            deposit.setOrder(order);
            this.disDepositDao.persist(deposit);
        }
        if ((((Setting) localObject1).getStockAllocationTime() == Setting.StockAllocationTime.order) || ((((Setting) localObject1).getStockAllocationTime() == Setting.StockAllocationTime.payment) && ((order.getPaymentStatus() == Order.PaymentStatus.partialPayment) || (order.getPaymentStatus() == Order.PaymentStatus.paid)))) {
            Iterator localIterator = order.getOrderItems().iterator();
            while (localIterator.hasNext()) {
                localObject4 = (OrderItem) localIterator.next();
                if (localObject4 != null) {
                    Product localProduct = ((OrderItem) localObject4).getProduct();
                    this.productDao.lock(localProduct, LockModeType.PESSIMISTIC_WRITE);
                    if ((localProduct != null) && (localProduct.getStock() != null)) {
                        localProduct.setAllocatedStock(Integer.valueOf(localProduct.getAllocatedStock().intValue() + (((OrderItem) localObject4).getQuantity().intValue() - ((OrderItem) localObject4).getShippedQuantity().intValue())));
                        this.productDao.merge(localProduct);
                        this.orderDao.flush();
                        this.staticService.build(localProduct);
                    }
                }
            }
        }
        this.cartDao.remove(cart);
        return order;
    }

    public void update(Order order, Admin operator) {
        Assert.notNull(order);
        Order localOrder = (Order) this.orderDao.find(order.getId());
        if (localOrder.getIsAllocatedStock().booleanValue()) {
            Iterator localIterator = localOrder.getOrderItems().iterator();
            Product localProduct;
            while (localIterator.hasNext()) {
                OrderItem orderitem = (OrderItem) localIterator.next();
                if (orderitem != null) {
                    localProduct = ((OrderItem) orderitem).getProduct();
                    this.productDao.lock(localProduct, LockModeType.PESSIMISTIC_WRITE);
                    if ((localProduct != null) && (localProduct.getStock() != null)) {
                        localProduct.setAllocatedStock(Integer.valueOf(localProduct.getAllocatedStock().intValue() - (((OrderItem) orderitem).getQuantity().intValue() - ((OrderItem) orderitem).getShippedQuantity().intValue())));
                        this.productDao.merge(localProduct);
                        this.orderDao.flush();
                        this.staticService.build(localProduct);
                    }
                }
            }
            localIterator = order.getOrderItems().iterator();
            while (localIterator.hasNext()) {
                OrderItem orderitem1 = (OrderItem) localIterator.next();
                if (orderitem1 != null) {
                    localProduct = ((OrderItem) orderitem1).getProduct();
                    this.productDao.lock(localProduct, LockModeType.PESSIMISTIC_WRITE);
                    if ((localProduct != null) && (localProduct.getStock() != null)) {
                        localProduct.setAllocatedStock(Integer.valueOf(localProduct.getAllocatedStock().intValue() + (((OrderItem) orderitem1).getQuantity().intValue() - ((OrderItem) orderitem1).getShippedQuantity().intValue())));
                        this.productDao.merge(localProduct);
                        this.productDao.flush();
                        this.staticService.build(localProduct);
                    }
                }
            }
        }
        this.orderDao.merge(order);
        OrderLog orderlog = new OrderLog();
        orderlog.setType(OrderLog.Type.modify);
        orderlog.setOperator(operator != null ? operator.getUsername() : null);
        orderlog.setOrder(order);
        this.orderLogDao.persist(orderlog);
    }

    public void confirm(Order order, Admin operator) {
        Assert.notNull(order);
        order.setOrderStatus(Order.OrderStatus.confirmed);
        this.orderDao.merge(order);
        OrderLog localOrderLog = new OrderLog();
        localOrderLog.setType(OrderLog.Type.confirm);
        localOrderLog.setOperator(operator != null ? operator.getUsername() : null);
        localOrderLog.setOrder(order);
        this.orderLogDao.persist(localOrderLog);
    }

    public void complete(Order order, Admin operator) {
        Assert.notNull(order);
        Member member = order.getMember();
        this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);

        if ((order.getShippingStatus() == Order.ShippingStatus.partialShipment) || (order.getShippingStatus() == Order.ShippingStatus.shipped)) {
            member.setPoint(Long.valueOf(member.getPoint().longValue() + order.getPoint().intValue()));
            Iterator localIterator = order.getCoupons().iterator();
            Coupon coupon;
            while (localIterator.hasNext()) {
                coupon = (Coupon) localIterator.next();
                this.couponCodeDao.build(coupon, member);
            }
        }

        if ((order.getShippingStatus() == Order.ShippingStatus.unshipped) ||
                (order.getShippingStatus() == Order.ShippingStatus.returned)) {
            CouponCode localObject = order.getCouponCode();
            if (localObject != null) {
                ((CouponCode) localObject).setIsUsed(Boolean.valueOf(false));
                ((CouponCode) localObject).setUsedDate(null);
                this.couponCodeDao.merge(localObject);
                order.setCouponCode(null);
                this.orderDao.merge(order);
            }
        }

        member.setAmount(member.getAmount().add(order.getAmountPaid()));


        if (!member.getMemberRank().getIsSpecial().booleanValue()) {
            MemberRank memberrank = this.memberRankDao.findByAmount(member.getAmount());
            if (memberrank != null && memberrank.getAmount().compareTo(member.getMemberRank().getAmount()) > 0) {
                member.setMemberRank(memberrank);
            }
        }
        this.memberDao.merge(member);

        Product localProduct;
        if (order.getIsAllocatedStock().booleanValue()) {
            Iterator localIterator = order.getOrderItems().iterator();
            while (localIterator.hasNext()) {
                OrderItem orderitem = (OrderItem) localIterator.next();
                if (orderitem != null) {
                    localProduct = ((OrderItem) orderitem).getProduct();
                    this.productDao.lock(localProduct, LockModeType.PESSIMISTIC_WRITE);
                    if ((localProduct != null) && (localProduct.getStock() != null)) {
                        localProduct.setAllocatedStock(Integer.valueOf(localProduct.getAllocatedStock().intValue() - (((OrderItem) orderitem).getQuantity().intValue() - ((OrderItem) orderitem).getShippedQuantity().intValue())));
                        this.productDao.merge(localProduct);
                        this.orderDao.flush();
                        this.staticService.build(localProduct);
                    }
                }
            }
            order.setIsAllocatedStock(Boolean.valueOf(false));
        }

        Iterator localIterator = order.getOrderItems().iterator();
        while (localIterator.hasNext()) {
            OrderItem localObject = (OrderItem) localIterator.next();
            if (localObject != null) {
                localProduct = ((OrderItem) localObject).getProduct();
                this.productDao.lock(localProduct, LockModeType.PESSIMISTIC_WRITE);
                if (localProduct != null) {
                    Integer localInteger = ((OrderItem) localObject).getQuantity();
                    Calendar localCalendar1 = Calendar.getInstance();
                    Calendar localCalendar2 = DateUtils.toCalendar(localProduct.getWeekSalesDate());
                    Calendar localCalendar3 = DateUtils.toCalendar(localProduct.getMonthSalesDate());
                    if ((localCalendar1.get(1) != localCalendar2.get(1)) || (localCalendar1.get(3) > localCalendar2.get(3))) {
                        localProduct.setWeekSales(Long.valueOf(localInteger.intValue()));
                    } else {
                        localProduct.setWeekSales(Long.valueOf(localProduct.getWeekSales().longValue() + localInteger.intValue()));
                    }
                    if ((localCalendar1.get(1) != localCalendar3.get(1)) || (localCalendar1.get(2) > localCalendar3.get(2))) {
                        localProduct.setMonthSales(Long.valueOf(localInteger.intValue()));
                    } else {
                        localProduct.setMonthSales(Long.valueOf(localProduct.getMonthSales().longValue() + localInteger.intValue()));
                    }
                    localProduct.setSales(Long.valueOf(localProduct.getSales().longValue() + localInteger.intValue()));
                    localProduct.setWeekSalesDate(new Date());
                    localProduct.setMonthSalesDate(new Date());
                    this.productDao.merge(localProduct);
                    this.orderDao.flush();
                    this.staticService.build(localProduct);
                }
            }
        }
        order.setOrderStatus(Order.OrderStatus.completed);
        order.setExpire(null);
        this.orderDao.merge(order);
        OrderLog orderlog = new OrderLog();
        orderlog.setType(OrderLog.Type.complete);
        orderlog.setOperator(operator != null ? operator.getUsername() : null);
        orderlog.setOrder(order);
        this.orderLogDao.persist(orderlog);
    }

    public void cancel(Order order, Admin operator) {
        Assert.notNull(order);
        CouponCode localCouponCode = order.getCouponCode();
        if (localCouponCode != null) {
            localCouponCode.setIsUsed(Boolean.valueOf(false));
            localCouponCode.setUsedDate(null);
            this.couponCodeDao.merge(localCouponCode);
            order.setCouponCode(null);
            this.orderDao.merge(order);
        }
        if (order.getIsAllocatedStock().booleanValue()) {
            Iterator localIterator = order.getOrderItems().iterator();
            while (localIterator.hasNext()) {
                OrderItem orderitem = (OrderItem) localIterator.next();
                if (orderitem != null) {
                    Product localProduct = ((OrderItem) orderitem).getProduct();
                    this.productDao.lock(localProduct, LockModeType.PESSIMISTIC_WRITE);
                    if ((localProduct != null) && (localProduct.getStock() != null)) {
                        localProduct.setAllocatedStock(Integer.valueOf(localProduct.getAllocatedStock().intValue() - (((OrderItem) orderitem).getQuantity().intValue() - ((OrderItem) orderitem).getShippedQuantity().intValue())));
                        this.productDao.merge(localProduct);
                        this.orderDao.flush();
                        this.staticService.build(localProduct);
                    }
                }
            }
            order.setIsAllocatedStock(Boolean.valueOf(false));
        }
        order.setOrderStatus(Order.OrderStatus.cancelled);
        order.setExpire(null);
        this.orderDao.merge(order);
        OrderLog orderlog = new OrderLog();
        orderlog.setType(OrderLog.Type.cancel);
        orderlog.setOperator(operator != null ? operator.getUsername() : null);
        orderlog.setOrder(order);
        this.orderLogDao.persist(orderlog);
    }

    public void payment(Order order, Payment payment, Admin operator) {
        Assert.notNull(order);
        Assert.notNull(payment);
        this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        payment.setOrder(order);
        this.paymentDao.merge(payment);
        if (payment.getType() == Payment.Type.deposit) {
            // 更新会员账户余额
            Member member = order.getMember();
            this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
            member.setBalance(member.getBalance().subtract(payment.getAmount()));
            this.memberDao.merge(member);
            // 创建交易记录
            Deposit deposit = new Deposit();
            deposit.setType(operator != null ? Deposit.Type.adminPayment : Deposit.Type.memberPayment);
            deposit.setCredit(new BigDecimal(0));
            deposit.setDebit(payment.getAmount());
            deposit.setBalance(member.getBalance());
            deposit.setOperator(operator != null ? operator.getUsername() : null);
            deposit.setMember(member);
            deposit.setOrder(order);
            this.disDepositDao.persist(deposit);

        }
        Setting setting = SettingUtils.get();
        if ( !order.getIsAllocatedStock().booleanValue() && setting.getStockAllocationTime() == Setting.StockAllocationTime.payment ) {
            Iterator<OrderItem> itemIterator = order.getOrderItems().iterator();
            while (itemIterator.hasNext()) {
                OrderItem orderItem = itemIterator.next();
                if (orderItem != null) {
                    Product product = orderItem.getProduct();
                    this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
                    if ( product != null && product.getStock() != null ) {
                        // 更新产品库存
                        product.setAllocatedStock(product.getAllocatedStock() +  orderItem.getQuantity() - orderItem.getShippedQuantity() );
                        this.productDao.merge(product);
                        this.orderDao.flush();
                        this.staticService.build(product);
                    }
                }
            }
            order.setIsAllocatedStock(Boolean.valueOf(true));
        }
        order.setAmountPaid(order.getAmountPaid().add(payment.getAmount()));
        order.setFee(payment.getFee());
        order.setExpire(null);
        if (order.getAmountPaid().compareTo(order.getAmount()) >= 0) {        // 全额支付
            order.setOrderStatus(Order.OrderStatus.confirmed);
            order.setPaymentStatus(Order.PaymentStatus.paid);
        } else if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {  // 部分支付
            order.setOrderStatus(Order.OrderStatus.confirmed);
            order.setPaymentStatus(Order.PaymentStatus.partialPayment);
        }
        this.orderDao.merge(order);
        // 创建订单日志
        OrderLog orderlog = new OrderLog();
        orderlog.setType(OrderLog.Type.payment);
        orderlog.setOperator(operator != null ? operator.getUsername() : null);
        orderlog.setOrder(order);
        this.orderLogDao.persist(orderlog);
    }

    public void refunds(Order order, Refunds refunds, Admin operator) {
        Assert.notNull(order);
        Assert.notNull(refunds);
        this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        refunds.setOrder(order);
        this.refundsDao.persist(refunds);

        if (refunds.getType() == Refunds.Type.deposit) {
            Member member = order.getMember();
            this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);

            member.setBalance(member.getBalance().add(refunds.getAmount()));
            this.memberDao.merge(member);

            Deposit deposit = new Deposit();
            deposit.setType(Deposit.Type.adminRefunds);
            deposit.setCredit(refunds.getAmount());
            deposit.setDebit(new BigDecimal(0));
            deposit.setBalance(member.getBalance());
            deposit.setOperator(operator != null ? operator.getUsername() : null);
            deposit.setMember(member);
            deposit.setOrder(order);
            this.disDepositDao.persist(deposit);
        }
        order.setAmountPaid(order.getAmountPaid().subtract(refunds.getAmount()));
        order.setExpire(null);
        if (order.getAmountPaid().compareTo(new BigDecimal(0)) == 0) {
            order.setPaymentStatus(Order.PaymentStatus.refunded);
        } else if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
            order.setPaymentStatus(Order.PaymentStatus.partialRefunds);
        }
        this.orderDao.merge(order);
        OrderLog orderlog = new OrderLog();
        orderlog.setType(OrderLog.Type.refunds);
        orderlog.setOperator(operator != null ? operator.getUsername() : null);
        orderlog.setOrder(order);
        this.orderLogDao.persist(orderlog);
    }

    public void shipping(Order order, Shipping shipping, Admin operator) {
        Assert.notNull(order);
        Assert.notNull(shipping);
        Assert.notEmpty(shipping.getShippingItems());
        this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        Setting localSetting = SettingUtils.get();
        Object localObject2;
        if ((!order.getIsAllocatedStock().booleanValue()) &&
                (localSetting.getStockAllocationTime() == Setting.StockAllocationTime.ship)) {
            Iterator iterator = order.getOrderItems().iterator();
            while (iterator.hasNext()) {
                OrderItem orderItem = (OrderItem) iterator.next();
                if (orderItem != null) {
                    Product product = orderItem.getProduct();
                    this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
                    if (product != null && product.getStock() != null) {
                        product.setAllocatedStock(Integer.valueOf(product.getAllocatedStock().intValue() +
                                (orderItem.getQuantity().intValue() -
                                        orderItem.getShippedQuantity().intValue())));
                        this.productDao.merge(product);
                        this.orderDao.flush();
                        this.staticService.build(product);
                    }
                }
            }
            order.setIsAllocatedStock(Boolean.valueOf(true));
        }
        shipping.setOrder(order);
        this.shippingDao.persist(shipping);
        Iterator localIterator = shipping.getShippingItems().iterator();
        while (localIterator.hasNext()) {
            ShippingItem shippingitem = (ShippingItem) localIterator.next();
            OrderItem orderitem1 = order.getOrderItem(((ShippingItem) shippingitem).getSn());
            if (orderitem1 != null) {
                Product product1 = orderitem1.getProduct();
                this.productDao.lock(product1, LockModeType.PESSIMISTIC_WRITE);
                if (product1 != null) {
                    if (product1.getStock() != null) {
                        product1.setStock(Integer.valueOf(product1.getStock().intValue() -
                                shippingitem.getQuantity().intValue()));
                        if (order.getIsAllocatedStock().booleanValue()) {
                            product1.setAllocatedStock(Integer.valueOf(product1.getAllocatedStock().intValue() - shippingitem.getQuantity().intValue()));
                        }
                    }
                    this.productDao.merge(product1);
                    this.orderDao.flush();
                    this.staticService.build(product1);
                }
                this.orderItemDao.lock(orderitem1, LockModeType.PESSIMISTIC_WRITE);
                orderitem1.setShippedQuantity(Integer.valueOf(orderitem1.getShippedQuantity().intValue() + shippingitem.getQuantity().intValue()));
            }
        }
        if (order.getShippedQuantity() >= order.getQuantity()) {
            order.setShippingStatus(Order.ShippingStatus.shipped);
            order.setIsAllocatedStock(Boolean.valueOf(false));
        } else if (order.getShippedQuantity() > 0) {
            order.setShippingStatus(Order.ShippingStatus.partialShipment);
        }
        order.setExpire(null);
        this.orderDao.merge(order);
        OrderLog orderlog = new OrderLog();
        orderlog.setType(OrderLog.Type.shipping);
        orderlog.setOperator(operator != null ? operator.getUsername() : null);
        orderlog.setOrder(order);
        this.orderLogDao.persist(orderlog);
    }

    public void returns(Order order, Returns returns, Admin operator) {
        Assert.notNull(order);
        Assert.notNull(returns);
        Assert.notEmpty(returns.getReturnsItems());
        this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        returns.setOrder(order);
        this.returnsDao.persist(returns);
        Iterator localIterator = returns.getReturnsItems().iterator();
        while (localIterator.hasNext()) {
            ReturnsItem returnsitem = (ReturnsItem) localIterator.next();
            OrderItem localOrderItem = order.getOrderItem(((ReturnsItem) returnsitem).getSn());
            if (localOrderItem != null) {
                this.orderItemDao.lock(localOrderItem, LockModeType.PESSIMISTIC_WRITE);
                localOrderItem.setReturnQuantity(Integer.valueOf(localOrderItem.getReturnQuantity().intValue() + ((ReturnsItem) returnsitem).getQuantity().intValue()));
            }
        }
        if (order.getReturnQuantity() >= order.getShippedQuantity()) {
            order.setShippingStatus(Order.ShippingStatus.returned);
        } else if (order.getReturnQuantity() > 0) {
            order.setShippingStatus(Order.ShippingStatus.partialReturns);
        }
        order.setExpire(null);
        this.orderDao.merge(order);
        OrderLog orderlog = new OrderLog();
        orderlog.setType(OrderLog.Type.returns);
        orderlog.setOperator(operator != null ? operator.getUsername() : null);
        orderlog.setOrder(order);
        this.orderLogDao.persist(orderlog);
    }

    public void delete(Order order) {
        if (order.getIsAllocatedStock().booleanValue()) {
            Iterator localIterator = order.getOrderItems().iterator();
            while (localIterator.hasNext()) {
                OrderItem localOrderItem = (OrderItem) localIterator.next();
                if (localOrderItem != null) {
                    Product localProduct = localOrderItem.getProduct();
                    this.productDao.lock(localProduct, LockModeType.PESSIMISTIC_WRITE);
                    if ((localProduct != null) && (localProduct.getStock() != null)) {
                        localProduct.setAllocatedStock(Integer.valueOf(localProduct.getAllocatedStock().intValue() - (localOrderItem.getQuantity().intValue() - localOrderItem.getShippedQuantity().intValue())));
                        this.productDao.merge(localProduct);
                        this.orderDao.flush();
                        this.staticService.build(localProduct);
                    }
                }
            }
        }
        super.delete(order);
    }
}