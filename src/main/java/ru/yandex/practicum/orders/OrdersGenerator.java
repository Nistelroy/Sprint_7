package ru.yandex.practicum.orders;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class OrdersGenerator {
    private static Random random = new Random();
    private static final String LOCAL_DATE = String.valueOf(LocalDate.now());
    public static final String RANDOM_RENT_TIME = String.valueOf(random.nextInt(5) + 1);

    public static Orders randomOrdersExceptColor(List<String> color) {
        Faker faker = new Faker();
        return new Orders(faker.name().firstName(),
                faker.name().lastName(),
                faker.address().city(),
                faker.address().streetAddress(),
                faker.phoneNumber().phoneNumber(),
                RANDOM_RENT_TIME,
                LOCAL_DATE,
                faker.name().lastName(),
                List.of(color.toString())
        );
    }
}
