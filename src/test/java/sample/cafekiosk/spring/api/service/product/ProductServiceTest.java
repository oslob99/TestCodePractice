package sample.cafekiosk.spring.api.service.product;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.domain.product.Product;
import sample.cafekiosk.spring.api.domain.product.ProductRepository;
import sample.cafekiosk.spring.api.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.api.domain.product.ProductType;
import sample.cafekiosk.spring.api.dto.response.ProductResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.api.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.api.domain.product.ProductType.BAKERY;
import static sample.cafekiosk.spring.api.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {


  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  @AfterEach
  void tearDown() {
    productRepository.deleteAllInBatch();
  }

  @DisplayName("")
  @Test
  void getSellingProducts() {
  }

  @DisplayName("신규 상품을 등록한다. 상품 번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
  @Test
  void createProduct() {

    // given
    Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);

    productRepository.save(product1);

    ProductCreateRequest request = ProductCreateRequest.builder()
        .name("카푸치노")
        .sellingStatus(SELLING)
        .type(HANDMADE)
        .price(5000)
        .build();

    // when
    ProductResponse productResponse = productService.createProduct(request);

    // then
    assertThat(productResponse)
        .extracting("productNumber", "type","sellingStatus","name", "price")
        .contains("002", HANDMADE, SELLING, "카푸치노", 5000);

    List<Product> products = productRepository.findAll();

    assertThat(products).hasSize(2)
        .extracting("productNumber", "type", "sellingStatus", "name", "price")
        .containsExactlyInAnyOrder(
            Tuple.tuple("001",HANDMADE, SELLING, "아메리카노", 4000),
            Tuple.tuple("002", HANDMADE, SELLING, "카푸치노", 5000)
        );
  }

  @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
  @Test
  void createProductWhenProductsIsEmpty() {

    // given

    ProductCreateRequest request = ProductCreateRequest.builder()
        .name("카푸치노")
        .sellingStatus(SELLING)
        .type(HANDMADE)
        .price(5000)
        .build();

    // when
    ProductResponse productResponse = productService.createProduct(request);

    // then
    assertEquals(productResponse.getProductNumber(), "001");

    List<Product> products = productRepository.findAll();

    assertThat(products).hasSize(1)
        .extracting("productNumber", "type", "sellingStatus", "name", "price")
        .containsExactlyInAnyOrder(
            Tuple.tuple("001", HANDMADE, SELLING, "카푸치노", 5000)
        );

  }

  private static Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
    return Product.builder()
        .productNumber(productNumber)
        .type(type)
        .sellingStatus(sellingStatus)
        .name(name)
        .price(price)
        .build();
  }
}