package com.univ.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.univ.model.Cart;
import com.univ.model.Product;
import com.univ.model.UserDtls;
import com.univ.repository.CartRepository;
import com.univ.repository.ProductRepository;
import com.univ.repository.UserRepository;

import com.univ.service.CartService;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	
	@Override
	public Cart saveCart(Integer productId, Integer userId) {
		
		UserDtls userDtls = userRepository.findById(userId).get();
		Product product = productRepository.findById(productId).get();
		
		Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);
		Cart cart = null;
		
		if(ObjectUtils.isEmpty(cartStatus))
		{
			cart =new Cart();
			cart.setProduct(product);
			cart.setUser(userDtls);	
			cart.setQuantity(1);
			cart.setTotalPrice(1*product.getDiscountPrice());
		}
		else
		{
			cart=cartStatus;
			cart.setQuantity(cart.getQuantity()+1);
			cart.setTotalPrice(cart.getQuantity()*cart.getProduct().getDiscountPrice());
		}
		
		Cart saveCart =  cartRepository.save(cart);
		
		return saveCart;
	}
	
	@Override
	public List<Cart> getCartByUser(Integer userId) {
		List<Cart> carts = cartRepository.findByUserId(userId);
		
		Double totalOrderPrice = 0.0;
		List<Cart> updteCarts = new ArrayList<Cart>();
		for(Cart c:carts)
		{
		  Double	totalPrice = (c.getProduct().getDiscountPrice()*c.getQuantity());
			c.setTotalPrice(totalPrice);
			
			totalOrderPrice = 	totalOrderPrice + totalPrice;
			c.setTotalOrderPrice(totalOrderPrice);
			updteCarts.add(c);
		}
		
		
		
		return updteCarts;
	}
	
	
	@Override
	public Integer getCountCart(Integer userId) {
		
		Integer countByuserId = cartRepository.countByuserId(userId);
		return countByuserId;
	}
	
	
	@Override
	public void updateQuantity(String sy, Integer cid) {
		
		Cart cart = cartRepository.findById(cid).get();
		int updateQuantity;
		if(sy.equalsIgnoreCase("de"))
		{
			updateQuantity =  cart.getQuantity()-1;
			
			if(updateQuantity <=0 )
			{
				//cartRepository.deleteById(cid);
				cartRepository.delete(cart);		
			}
			else
			{
				cart.setQuantity(updateQuantity);	
				cartRepository.save(cart);
			}
		}else
		{
			updateQuantity = cart.getQuantity()+1;
			cart.setQuantity(updateQuantity);	
			cartRepository.save(cart);
		}
		
	
	}
}


