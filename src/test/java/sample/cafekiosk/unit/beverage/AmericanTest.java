package sample.cafekiosk.unit.beverage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AmericanTest {

    @Test
    void getName() {
        Ameriacano ameriacano = new Ameriacano();

//        assertEquals(ameriacano.getName(), "아메리카노");
        assertThat(ameriacano.getName()).isEqualTo("아메리카노");
    }

    @Test
    void getPrice() {

        Ameriacano ameriacano = new Ameriacano();

        assertThat(ameriacano.getPrice()).isEqualTo(4000);

    }
}