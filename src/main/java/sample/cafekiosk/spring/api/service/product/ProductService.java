package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.domain.product.Product;
import sample.cafekiosk.spring.api.domain.product.ProductRepository;
import sample.cafekiosk.spring.api.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.api.dto.response.ProductResponse;

import java.util.List;


/**
 *  readOnly = true : 읽기전용
 *  CRUD 에서 CUD 동작 X / Only Read
 *  JPA :  CUD 스냅샷 저장, 변경감지 X (성능 향상)
 *
 *  CQRDS - Command / Query
 *
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  @Transactional
  public List<ProductResponse> getSellingProducts(){

    List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

    return products.stream()
        .map(ProductResponse::of)
        .toList();
  }

  // 동시성 이슈
  // UUID
  public ProductResponse createProduct(ProductCreateRequest request) {
    // productNumber
    // 001 002 003 004
    // DB에서 마지막 저장된 Product의 상품 번호를 읽어와서  + 1

    String nextProductNumber = createNextProductNumber();

    Product product = request.toEntity(nextProductNumber);
    Product saved = productRepository.save(product);


    return ProductResponse.of(saved);
  }

  private String createNextProductNumber() {
    String latestProductNumber = productRepository.findLatestProductNumber();
    if (latestProductNumber == null) {
      return "001";
    }

    int latestProductNumberInt = Integer.parseInt(latestProductNumber);
    int nextProductNumberInt = latestProductNumberInt + 1;

    return String.format("%03d", nextProductNumberInt);
  }
}
