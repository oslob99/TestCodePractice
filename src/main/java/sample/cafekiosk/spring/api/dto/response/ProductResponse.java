package sample.cafekiosk.spring.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.api.domain.product.Product;
import sample.cafekiosk.spring.api.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.api.domain.product.ProductType;

@Getter
public class ProductResponse {

  private Long id;
  private String productName;
  private ProductType type;
  private ProductSellingStatus sellingStatus;
  private String name;
  private int price;

  @Builder
  private ProductResponse(Long id, String productName, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
    this.id = id;
    this.productName = productName;
    this.type = type;
    this.sellingStatus = sellingStatus;
    this.name = name;
    this.price = price;
  }

  public static ProductResponse of(Product product) {
    return ProductResponse.builder()
        .id(product.getId())
        .productName(product.getProductNumber())
        .type(product.getType())
        .sellingStatus(product.getSellingStatus())
        .name(product.getName())
        .price(product.getPrice())
        .build();
  }
}
