package sample.cafekiosk.spring.api.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(OrderController.class)
class OrderControllerTest {


  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderService orderService;

  @Autowired
  private ObjectMapper objectMapper;

  @DisplayName("신규 주문을 등록한다.")
  @Test
  void createOrder() throws Exception {
    // given
    OrderCreateRequest request = OrderCreateRequest.builder()
        .productNumbers(List.of("1", "2", "3"))
        .build();

    // when // then
    mockMvc.perform(post("/api/v1/orders/new")
            .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON)
    )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("200"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
    ;

   }

  @DisplayName("신규 주문을 등록할 때 상품번호는 1개 이상이어야 한다.")
  @Test
  void createOrderWithEmptyProductNumbers() throws Exception {
    // given
    OrderCreateRequest request = OrderCreateRequest.builder()
        .productNumbers(List.of())
        .build();

    // when // then
    mockMvc.perform(post("/api/v1/orders/new")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("주문 리스트는 공백이면 안됩니다."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
    ;

  }
}