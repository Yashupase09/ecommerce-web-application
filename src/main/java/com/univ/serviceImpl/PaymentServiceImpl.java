package com.univ.serviceImpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.univ.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Value("${razorpay.key_id}")
	private String keyId;

	@Value("${razorpay.key_secret}")
	private String keySecret;

	@Override
	public JSONObject createOrder(Double amountInRupees, String receiptId) throws Exception {

		RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);

		// Razorpay expects amount in the smallest currency unit (paise for INR)
		int amountInPaise = (int) Math.round(amountInRupees * 100);

		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", amountInPaise);
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", receiptId);

		com.razorpay.Order order = razorpayClient.orders.create(orderRequest);

		return new JSONObject(order.toString());
	}

	@Override
	public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
		try {
			JSONObject options = new JSONObject();
			options.put("razorpay_order_id", razorpayOrderId);
			options.put("razorpay_payment_id", razorpayPaymentId);
			options.put("razorpay_signature", razorpaySignature);

			return Utils.verifyPaymentSignature(options, keySecret);
		} catch (Exception e) {
			return false;
		}
	}
}
