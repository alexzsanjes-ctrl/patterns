import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ReplanDeliveryTest {

    @BeforeAll
    static void setupAll() {
//        Configuration.browser = "firefox";
//        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
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
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").
                press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE).setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=success-notification] .notification__content").
                shouldBe(visible, Duration.ofSeconds(15)).
                shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate));
        form.$("[data-test-id=date] input").
                press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE).setValue(secondMeetingDate);
        form.$$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=replan-notification] .button__content .button__text").
                shouldBe(visible).click();
        $("[data-test-id=success-notification] .notification__content").
                shouldBe(visible, Duration.ofSeconds(15)).
                shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }
}
