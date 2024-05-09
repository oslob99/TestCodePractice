package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import sample.cafekiosk.spring.api.domain.history.MailSendHistory;
import sample.cafekiosk.spring.api.domain.history.MailSendHistoryRepository;
import sample.cafekiosk.spring.api.domain.order.Order;
import sample.cafekiosk.spring.api.domain.order.OrderRepository;
import sample.cafekiosk.spring.api.domain.order.OrderStatus;
import sample.cafekiosk.spring.api.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.api.domain.product.Product;
import sample.cafekiosk.spring.api.domain.product.ProductRepository;
import sample.cafekiosk.spring.api.domain.product.ProductType;
import sample.cafekiosk.spring.client.MailSendClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static sample.cafekiosk.spring.api.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.api.domain.product.ProductType.HANDMADE;

@SpringBootTest
class OrderStatisticsServiceTest {

  @Autowired
  private OrderStatisticsService orderStatisticsService;

  @Autowired
  private OrderProductRepository orderProductRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private MailSendHistoryRepository mailSendHistoryRepository;

  @MockBean
  private MailSendClient mailSendClient;

  @AfterEach
  void tearDown() {
    orderProductRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
    mailSendHistoryRepository.deleteAllInBatch();
  }

  @DisplayName("결제완료 주문들을 조회해 매출 통계 메일을 전송한다.")
  @Test
  void sendOrderStatisticsMail(){
    // given

    LocalDateTime now = LocalDateTime.of(2024, 5, 9, 0, 0);

    Product product1 = createProduct(HANDMADE, "001", 1000);
    Product product2 = createProduct(HANDMADE, "002", 2000);
    Product product3 = createProduct(HANDMADE, "003", 3000);
    List<Product> products = List.of(product1, product2, product3);
    productRepository.saveAll(products);


    Order order1 = createPaymentCompleteOrder(products, LocalDateTime.of(2024, 5, 8, 23, 59,59));
    Order order2 = createPaymentCompleteOrder(products, now);
    Order order4 = createPaymentCompleteOrder(products, LocalDateTime.of(2024, 5, 9, 23, 59,59));
    Order order3 = createPaymentCompleteOrder(products, LocalDateTime.of(2024, 5, 10, 0, 0));

    when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
        .thenReturn(true);

    // when
    boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2024, 5, 9), "test@test.com");

    // then
    assertThat(result).isTrue();

    List<MailSendHistory> histories = mailSendHistoryRepository.findAll();

    System.out.println("aa:"+histories.get(0).getContent());

    assertThat(histories).hasSize(1)
        .extracting("content")
        .contains(
            "총 매출 합계는 18000원 입니다."
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