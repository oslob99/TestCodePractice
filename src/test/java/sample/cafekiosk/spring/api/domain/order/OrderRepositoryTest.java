package sample.cafekiosk.spring.api.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sample.cafekiosk.spring.api.domain.product.Product;
import sample.cafekiosk.spring.api.domain.product.ProductRepository;
import sample.cafekiosk.spring.api.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.api.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.api.domain.product.ProductType.HANDMADE;

@DataJpaTest
class OrderRepositoryTest {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private ProductRepository productRepository;

  @DisplayName("오늘 기준으로 24시간동안 주문 완료 상태인 주문들을 조회한다.")
  @Test
  void findOrdersBy(){
    // given
    LocalDateTime now = LocalDateTime.of(2024, 5, 9, 0, 0);

    Product product1 = createProduct(HANDMADE, "001", 1000);
    Product product2 = createProduct(HANDMADE, "002", 2000);
    Product product3 = createProduct(HANDMADE, "003", 3000);
    List<Product> products = List.of(product1, product2, product3);
    productRepository.saveAll(products);

    createPaymentCompleteOrder(products, LocalDateTime.of(2024, 5, 8, 23, 59,59));
    createPaymentCompleteOrder(products, now);
    createPaymentCompleteOrder(products, LocalDateTime.of(2024, 5, 9, 23, 59,59));
    createPaymentCompleteOrder(products, LocalDateTime.of(2024, 5, 10, 0, 0));

    // when
    List<Order> orders = orderRepository.findOrdersBy(now.toLocalDate().atStartOfDay(), now.plusDays(1).toLocalDate().atStartOfDay(), OrderStatus.PAYMENT_COMPLETED);

    // then
    assertThat(orders).hasSize(2)
        .extracting("orderStatus","totalPrice")
        .contains(
            tuple(OrderStatus.PAYMENT_COMPLETED,6000),
            tuple(OrderStatus.PAYMENT_COMPLETED,6000)
        )
    ;

   }

  private Order createPaymentCompleteOrder(List<Product> products, LocalDateTime now) {
    Order order = Order.builder()
        .products(products)
        .orderStatus(OrderStatus.PAYMENT_COMPLETED)
        .registerDateTime(now)
        .build();
    return orderRepository.save(order);
  }

  private Product createProduct(ProductType type, String productNumber, int price){
    return Product.builder()
        .type(type)
        .productNumber(productNumber)
        .price(price)
        .sellingStatus(SELLING)
        .name("메뉴 이름")
        .build();
  }
}