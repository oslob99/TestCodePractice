package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Ameriacano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {

    @Test
    void add_manual_test(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Ameriacano());

        System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료 : " + cafeKiosk.getBeverages().get(0).getName());
    }

    @Test
    void add(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Ameriacano());

        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void addSeveralBeverage() throws IllegalAccessException {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Ameriacano ameriacano = new Ameriacano();

        cafeKiosk.add(ameriacano, 2);

        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(ameriacano);
        assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(ameriacano);
    }

    @Test
    void addZeroBeverage() throws IllegalAccessException {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Ameriacano ameriacano = new Ameriacano();

        cafeKiosk.add(ameriacano, 0);

        assertThatThrownBy(() -> cafeKiosk.add(ameriacano, 0))
                .isInstanceOf(IllegalAccessError.class)
                .hasMessage("음료는 1잔 이상으로 주문해야 합니다.");

    }

    @Test
    void remove(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Ameriacano ameriacano = new Ameriacano();

        cafeKiosk.add(ameriacano);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);

        cafeKiosk.remove(ameriacano);
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void clear(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Ameriacano ameriacano = new Ameriacano();
        Latte latte = new Latte();

        cafeKiosk.add(latte);
        cafeKiosk.add(ameriacano);
        assertThat(cafeKiosk.getBeverages()).hasSize(2);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void createOrder() throws IllegalAccessException {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Ameriacano ameriacano = new Ameriacano();

        cafeKiosk.add(ameriacano);

        Order order = cafeKiosk.createOrder();

        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");

    }

    @Test
    void createOrderWithCurrentTime() throws IllegalAccessException {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Ameriacano ameriacano = new Ameriacano();

        cafeKiosk.add(ameriacano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2024,1,17,10,0));

        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");

    }

    @Test
    void createOrderOutsideOpenTime() throws IllegalAccessException {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Ameriacano ameriacano = new Ameriacano();

        cafeKiosk.add(ameriacano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2024,1,17,9,59));

        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");

    }

    @Test
    void calculateTotalPrice() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Ameriacano ameriacano = new Ameriacano();
        Latte latte = new Latte();

        cafeKiosk.add(ameriacano);
        cafeKiosk.add(latte);

        int totalPrice = cafeKiosk.calculateTotalPrice();

        assertThat(totalPrice).isEqualTo(8500);
    }
}