package sample.cafekiosk.spring.api.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


  /**
   * select *
   * from product
   * where type in ('SELLING','HOLD');
   */
  List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingType);

  List<Product> findAllByProductNumberIn(List<String> productNumber);

}
