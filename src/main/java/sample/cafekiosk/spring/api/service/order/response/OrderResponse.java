package sample.cafekiosk.spring.api.service.order.response;

import lombok.Getter;
import sample.cafekiosk.spring.api.dto.response.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime registerDateTime;
    private List<ProductResponse> products;


}
