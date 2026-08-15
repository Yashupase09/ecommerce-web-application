package com.univ.serviceImpl;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.univ.model.Cart;
import com.univ.model.OrderAddress;
import com.univ.model.OrderRequest;
import com.univ.model.ProductOrder;
import com.univ.repository.CartRepository;
import com.univ.repository.ProductOrderRepository;
import com.univ.service.OrderService;
import com.univ.util.CommonUtil;
import com.univ.util.OrderStatus;


@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ProductOrderRepository orderRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private com.univ.service.PaymentService paymentService;

	
	@Override
	public void saveOrder(Integer userId, OrderRequest orderRequest) throws Exception {

		boolean isOnlinePayment = "ONLINE".equalsIgnoreCase(orderRequest.getPaymentType());

		if (isOnlinePayment) {
			boolean verified = paymentService.verifyPayment(
					orderRequest.getRazorpayOrderId(),
					orderRequest.getRazorpayPaymentId(),
					orderRequest.getRazorpaySignature());

			if (!verified) {
				throw new Exception("Payment verification failed. Order not placed.");
			}
		}
		
		List<Cart> carts = cartRepository.findByUserId(userId);
		
		for(Cart cart:carts)
		{
			ProductOrder order = new ProductOrder();
			
			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(LocalDate.now());
			
			order.setProduct(cart.getProduct());
			order.setPrice(cart.getProduct().getDiscountPrice());
			
			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());
			
			order.setStatus(OrderStatus.IN_PROGRESS.getName());
			order.setPaymentType(orderRequest.getPaymentType());

			if (isOnlinePayment) {
				order.setPaymentStatus("PAID");
				order.setTransactionId(orderRequest.getRazorpayPaymentId());
			} else {
				order.setPaymentStatus("PENDING (COD)");
			}
			
			OrderAddress address = new OrderAddress();
			address.setFirstName(orderRequest.getFirstName());
			address.setLastName(orderRequest.getLastName());
			address.setEmail(orderRequest.getEmail());
			address.setMobileNumber(orderRequest.getMobileNumber());
			address.setAddress(orderRequest.getAddress());
			address.setCity(orderRequest.getCity());
			address.setState(orderRequest.getState());
			address.setPincode(orderRequest.getPincode());
			
			order.setOrderAddress(address);
			
			 ProductOrder saveOrder =orderRepository.save(order);
			commonUtil.sendMailForProductOrder(saveOrder, "success");
		}
		
	}
	
	@Override
	public List<ProductOrder> getOrderByUser(Integer userId) {
		List<ProductOrder> orders = orderRepository.findByUserId(userId);
		return orders;
	}
	
	@Override
	public ProductOrder updateOrderStatus(Integer id, String status) {
		Optional<ProductOrder> findById =  orderRepository.findById(id);
		if(findById.isPresent())
		{
			ProductOrder productOrder = findById.get();
			productOrder.setStatus(status);
			ProductOrder updateOrder  = orderRepository.save(productOrder);
			return updateOrder;
		}
		return null;
	}
	
	
	@Override
	public List<ProductOrder> getAllOrders() {
		
		return orderRepository.findAll();
	}
	
	
	@Override
	public ProductOrder getOrdersByOrderId(String orderId) {
		
		return orderRepository.findByOrderId(orderId);
	}
	
	
	@Override
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
		PageRequest pageable = PageRequest.of(pageNo, pageSize);
		return orderRepository.findAll(pageable);
	}
}
