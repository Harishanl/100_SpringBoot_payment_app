package CodewithHarry.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import CodewithHarry.dto.StudentOrder;

public interface StudentOrderRepo extends JpaRepository<StudentOrder, Integer> {

	public StudentOrder findByRazorpayOrderId(String orderId);
}
