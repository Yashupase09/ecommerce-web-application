package com.univ.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.univ.model.Cart;
import com.univ.model.UserDtls;
import com.univ.service.CartService;
import com.univ.service.PaymentService;
import com.univ.service.UserService;

@RestController
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private CartService cartService;

	@Autowired
	private UserService userService;

	@Value("${razorpay.key_id}")
	private String razorpayKeyId;

	@PostMapping("/user/create-razorpay-order")
	public Map<String, Object> createRazorpayOrder(Principal p) throws Exception {

		String email = p.getName();
		UserDtls user = userService.getUserByEmail(email);

		List<Cart> carts = cartService.getCartByUser(user.getId());

		Map<String, Object> response = new HashMap<>();

		if (carts.isEmpty()) {
			response.put("error", "Cart is empty");
			return response;
		}

		// Same total calculation as the order page: cart total + delivery fee + tax
		Double cartTotal = carts.get(carts.size() - 1).getTotalOrderPrice();
		Double totalOrderPrice = cartTotal + 250 + 100;

		String receiptId = "rcpt_" + UUID.randomUUID().toString().substring(0, 20);

		JSONObject razorpayOrder = paymentService.createOrder(totalOrderPrice, receiptId);

		response.put("order_id", razorpayOrder.get("id"));
		response.put("amount", razorpayOrder.get("amount"));
		response.put("currency", razorpayOrder.get("currency"));
		response.put("key_id", razorpayKeyId);
		response.put("name", user.getName());
		response.put("email", user.getEmail());
		response.put("contact", user.getMobileNumber());

		return response;
	}
}
