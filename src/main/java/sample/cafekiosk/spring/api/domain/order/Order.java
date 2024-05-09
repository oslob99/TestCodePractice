package sample.cafekiosk.spring.api.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.api.domain.product.BaseEntity;
import sample.cafekiosk.spring.api.domain.product.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    private LocalDateTime registerDateTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    private Order(List<Product> products, OrderStatus orderStatus, LocalDateTime registerDateTime) {
        this.orderStatus = OrderStatus.INIT;
        this.totalPrice = calculateTotalPrice(products);
        this.registerDateTime = registerDateTime;
        this.orderProducts = products.stream()
            .map(product -> new OrderProduct(this,product)).toList();
    }

    public static Order create(List<Product> products, LocalDateTime registerDateTime) {
        return  Order.builder()
            .orderStatus(OrderStatus.INIT)
            .products(products)
            .registerDateTime(registerDateTime)
            .build();
    }

    private int calculateTotalPrice(List<Product> products) {
        return products.stream().mapToInt(Product::getPrice).sum();
    }
}
