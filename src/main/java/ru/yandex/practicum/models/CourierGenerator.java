package ru.yandex.practicum.models;

import com.github.javafaker.Faker;

public class CourierGenerator {

    public static Courier randomCourier() {
        Faker faker = new Faker();
        Courier courier = new Courier();

        courier.withLogin(faker.name().username());
        courier.withPassword(faker.name().lastName());
        courier.withName(faker.name().firstName());

        return courier;
    }
}
