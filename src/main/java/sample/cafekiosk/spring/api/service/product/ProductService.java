package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.domain.product.Product;
import sample.cafekiosk.spring.api.domain.product.ProductRepository;
import sample.cafekiosk.spring.api.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.api.dto.response.ProductResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public List<ProductResponse> getSellingProducts(){

    List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

    return products.stream()
        .map(ProductResponse::of)
        .toList();
  }

}
