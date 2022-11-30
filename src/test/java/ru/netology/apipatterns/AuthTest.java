package ru.netology.apipatterns;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.devtools.v105.network.Network.clearBrowserCookies;
import static ru.netology.apipatterns.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.apipatterns.DataGenerator.getRandomLogin;
import static ru.netology.apipatterns.DataGenerator.getRandomPassword;


class AuthTest {

    @BeforeEach
    void setup() {

        open("http://localhost:9999");
    }

    @AfterEach
    void clear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Test
       void shouldSuccessfullyLoginRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("h2")
                .shouldHave(text("Личный кабинет"))
                .shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWithUnregisteredUser() {
        $("[data-test-id='login'] input").setValue(getRandomLogin());
        $("[data-test-id='password'] input").setValue(getRandomPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWithBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification']")
                .shouldHave(text("Ошибка! Пользователь заблокирован"))
                .shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWithInvalidLogin() {
        var registeredUser = getRegisteredUser("active");
        /*        var wrongLogin = getRandomLogin();*/
        $("[data-test-id='login'] input").setValue(getRandomLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible, Duration.ofSeconds(15));

    }

    @Test
        void shouldFailWithInvalidPassword() {
        var registeredUser = getRegisteredUser("active");
        /*        var wrongPassword = getRandomPassword();*/
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(getRandomPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible, Duration.ofSeconds(15));

    }
}