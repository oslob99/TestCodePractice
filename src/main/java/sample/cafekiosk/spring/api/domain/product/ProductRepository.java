package sample.cafekiosk.spring.api.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

  @Query(value = "select p.product_number from product p order by id desc limit 1", nativeQuery = true)
  String findLatestProductNumber();
}
