package sample.cafekiosk.unit;

import sample.cafekiosk.unit.beverage.Ameriacano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

public class CafeKioskRunner {

    public static void main(String[] args) throws IllegalAccessException {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Ameriacano());
        System.out.println(">>> 아메리카노 추가");

        cafeKiosk.add(new Latte());
        System.out.println(">>> 라떼 추가");

        int total = cafeKiosk.calculateTotalPrice();
        System.out.println("총 주문가격 : " + total);

        Order order = cafeKiosk.createOrder(LocalDateTime.now());
    }
}
