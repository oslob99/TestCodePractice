package sample.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.api.domain.product.ProductType;
import sample.cafekiosk.spring.api.dto.response.ProductResponse;
import sample.cafekiosk.spring.api.service.product.ProductService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ProductService productService;

  @DisplayName("신규 상품을 등록한다.")
  @Test
  void createProduct() throws Exception {
    // given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("아메리카노")
        .price(4000)
        .build();

    // when then
    mockMvc.perform(
              post("/api/v1/products/new")
                  .content(objectMapper.writeValueAsString(request))
                  .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
   }

  @DisplayName("신규 상품을 등록할 때 상품 타입은 필수 값이다..")
  @Test
  void createProductWithoutType() throws Exception {
    // given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("아메리카노")
        .price(4000)
        .build();

    // when then
    mockMvc.perform(
            post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty())
    ;
  }

  @DisplayName("신규 상품을 등록할 때 상품 판매상태는 필수 값이다.")
  @Test
  void createProductWithoutSellingStatus() throws Exception {
    // given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .name("아메리카노")
        .price(4000)
        .build();

    // when then
    mockMvc.perform(
            post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty())
    ;
  }

  @DisplayName("신규 상품을 등록할 때 상품 이름은 필수 값이다.")
  @Test
  void createProductWithoutName() throws Exception {
    // given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .price(4000)
        .build();

    // when then
    mockMvc.perform(
            post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty())
    ;
  }

  @DisplayName("신규 상품을 등록할 때 상품 가격은 양수이다.")
  @Test
  void createProductOverZeroPrice() throws Exception {
    // given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("아메리카노")
        .price(-1)
        .build();

    // when then
    mockMvc.perform(
            post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
        .andExpect(jsonPath("$.data").isEmpty())
    ;
  }

  @DisplayName("현재 팔고있는 상품들을 조회할 수 있다.")
  @Test
  void getSellingProducts() throws Exception {
    // given
    List<ProductResponse> result = List.of();

    when(productService.getSellingProducts()).thenReturn(result);

    // when // then
    mockMvc.perform(get("/api/v1/products/selling")
//            .queryParam("아메리카노")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("200"))
        .andExpect(jsonPath("$.status").value("OK"))
        .andExpect(jsonPath("$.message").value("OK"))
        .andExpect(jsonPath("$.data").isArray())
    ;



   }
}