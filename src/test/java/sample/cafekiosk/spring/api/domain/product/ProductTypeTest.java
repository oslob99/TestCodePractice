package sample.cafekiosk.spring.api.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeTest {

  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @Test
  void containsStockType(){
      // given
    ProductType givenProductType = ProductType.HANDMADE;

    // when
    boolean result = ProductType.containsStockType(givenProductType);

    // then
    assertThat(result).isFalse();

   }

  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @Test
  void containsStockType2(){
    // given
    ProductType givenProductType = ProductType.BOTTLE;

    // when
    boolean result = ProductType.containsStockType(givenProductType);

    // then
    assertThat(result).isTrue();

  }
}