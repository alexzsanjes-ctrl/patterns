package ru.netology.patterns.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.patterns.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ReplanDeliveryTest {

    @BeforeAll
    static void setupAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        Configuration.browser = "firefox";
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDown() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        SelenideElement form = $("fieldset");
        Allure.step("Выбор места и времени", () -> {
            form.$("[data-test-id=city] input").setValue(validUser.getCity());
            form.$("[data-test-id=date] input").
                    press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE).setValue(firstMeetingDate);
        });
        Allure.step("Заполнение контактных данных", () -> {
            form.$("[data-test-id=name] input").setValue(validUser.getName());
            form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        });
        Allure.step("Соглашение с условиями и планирование", () -> {
            form.$("[data-test-id=agreement]").click();
            form.$$("button").find(exactText("Запланировать")).click();
        });
        Allure.step("Уведовление об успешном планировнаии", () -> {
            $("[data-test-id=success-notification] .notification__content").
                    shouldBe(visible, Duration.ofSeconds(15)).
                    shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate));
        });
        Allure.step("Перепланирование даты", () -> {
            form.$("[data-test-id=date] input").
                    press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE).setValue(secondMeetingDate);
            form.$$("button").find(exactText("Запланировать")).click();
            $("[data-test-id=replan-notification] .button__content .button__text").
                    shouldBe(visible).click();
        });
        Allure.step("Уведомление об успешном перепланировании", () -> {
            $("[data-test-id=success-notification] .notification__content").
                    shouldBe(visible, Duration.ofSeconds(15)).
                    shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate));
        });
    }
}
