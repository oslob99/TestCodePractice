package sample.cafekiosk.spring.api.service.order.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.api.domain.order.Order;
import sample.cafekiosk.spring.api.dto.response.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime registerDateTime;
    private List<ProductResponse> products;

    @Builder
    private OrderResponse(Long id, int totalPrice, LocalDateTime registerDateTime, List<ProductResponse> products) {
      this.id = id;
      this.totalPrice = totalPrice;
      this.registerDateTime = registerDateTime;
      this.products = products;
    }

  public static OrderResponse of(Order order) {
    return OrderResponse.builder()
        .id(order.getId())
        .totalPrice(order.getTotalPrice())
        .registerDateTime(order.getRegisterDateTime())
        .products(order.getOrderProducts().stream().map(orderProduct -> ProductResponse.of(orderProduct.getProduct())).toList())
        .build();
  }
}
