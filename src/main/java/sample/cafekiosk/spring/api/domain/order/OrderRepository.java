package sample.cafekiosk.spring.api.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


  @Query("select o from Order o where o.registerDateTime >= :startDateTime and o.registerDateTime < :endDateTime and o.orderStatus = :orderStatus")
  List<Order> findOrdersBy(LocalDateTime startDateTime, LocalDateTime endDateTime, OrderStatus orderStatus);
}
