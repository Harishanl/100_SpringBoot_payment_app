package CodewithHarry.service;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import CodewithHarry.dto.StudentOrder;
import CodewithHarry.repo.StudentOrderRepo;

@Service
public class StudentService {
	
	@Autowired
	private StudentOrderRepo studentOrderRepo;
	
	@Value("${razorpay.key.id}")
	private String razorPayKey;
	
	@Value("${razorpay.key.secret}")
	private String razorPaySecret;
	
	private RazorpayClient client;
	
	public StudentOrder createOrder(StudentOrder stuorder)throws Exception
	{
		JSONObject orderReq= new JSONObject();
		orderReq.put("amount",stuorder.getAmount() * 100 );
		
		orderReq.put("currency", "INR");
		
		orderReq.put("receipt", stuorder.getEmail());
		this.client=new RazorpayClient(razorPayKey,razorPaySecret);
		
		Order razorPayOrder = client.orders.create(orderReq);
		
		System.out.println(razorPayOrder);
		stuorder.setRazorpayOrderId(razorPayOrder.get("id"));
		stuorder.setOrderStatus(razorPayOrder.get("status"));
		studentOrderRepo.save(stuorder);
		
		return stuorder;
		
	}
	
	public StudentOrder updateOrder(Map<String, String> responsepayLoad)
	{
		String razorPayOrderId = responsepayLoad.get("razorpay_order_id");
		StudentOrder order = studentOrderRepo.findByRazorpayOrderId(razorPayOrderId);
		order.setOrderStatus("PAYMENT_COMPLETED");
		StudentOrder updatedOrderd = studentOrderRepo.save(order);
		return updatedOrderd;
		
		
		
	}

}
