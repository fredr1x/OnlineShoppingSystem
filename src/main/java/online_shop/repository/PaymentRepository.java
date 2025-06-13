package online_shop.repository;

import online_shop.dto.PaymentDto;
import online_shop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p " +
           "from Payment p " +
           "join Order o on p.order.id = o.id " +
           "join User u on o.user.id = u.id " +
           "where u.id = :userId")
    List<Payment> findAllByUserId(@Param("userId") Long userId);
}
