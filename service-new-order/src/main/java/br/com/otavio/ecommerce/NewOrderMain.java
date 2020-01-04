package br.com.otavio.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try (var orderDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailDispatcher = new KafkaDispatcher<Email>()) {

                var email = Math.random() + "@email.com";
                for (int i = 0; i < 2; i++) {
                    var orderId = UUID.randomUUID().toString();
                    var amount = BigDecimal.valueOf(Math.random() * 5000);

                    var order = new Order(orderId, amount, email);

                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);

                    var body = "Email: Thanks for your order! We are processing your order!";
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, new Email("otavio", body));
                }
            }
        }
    }

}
