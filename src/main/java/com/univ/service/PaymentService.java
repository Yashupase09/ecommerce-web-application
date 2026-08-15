package com.univ.service;

import org.json.JSONObject;

public interface PaymentService {

	/**
	 * Creates a Razorpay order for the given amount (in rupees) and returns
	 * the raw Razorpay order JSON (contains "id", "amount", "currency" etc).
	 */
	JSONObject createOrder(Double amountInRupees, String receiptId) throws Exception;

	/**
	 * Verifies that a payment callback actually came from Razorpay for the
	 * given order, using HMAC-SHA256 signature verification with the key secret.
	 */
	boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature);

}
