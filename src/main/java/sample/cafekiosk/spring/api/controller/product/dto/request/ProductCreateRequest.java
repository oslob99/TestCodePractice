package sample.cafekiosk.spring.api.controller.product.dto.request;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.api.domain.product.Product;
import sample.cafekiosk.spring.api.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.api.domain.product.ProductType;

@Getter
public class ProductCreateRequest {

  private ProductType type;
  private ProductSellingStatus sellingStatus;
  private String name;
  private int price;

  @Builder
  public ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
    this.type = type;
    this.sellingStatus = sellingStatus;
    this.name = name;
    this.price = price;
  }

  public Product toEntity(String nextProductNumber) {
    return Product.builder()
        .productNumber(nextProductNumber)
        .type(type)
        .name(name)
        .sellingStatus(sellingStatus)
        .price(price)
        .build();
  }
}
