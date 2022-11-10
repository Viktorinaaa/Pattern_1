package ru.netology;

import static com.codeborne.selenide.Selenide.$x;
import static org.junit.jupiter.api.Assertions.*;

import com.codeborne.selenide.Configuration;
import org.checkerframework.checker.units.qual.K;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.DataGenerator;
import ru.netology.DataGenerator.*;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


class DataGeneratorTest {
    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
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
        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $x("//input[@pattern='[0-9.]*']").doubleClick().sendKeys(firstMeetingDate);
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $(By.className("checkbox__box")).click();
        $$(By.className("button__text")).get(0).click();
        $x("//*[contains(@data-test-id,'success-notification')]").should(appear, Duration.ofSeconds(15));
        $$(".notification__title").get(0).shouldHave(exactText("Успешно!"));
        $$(".notification__content").
                get(0).
                should(exactText("Встреча успешно запланирована на " +
                        firstMeetingDate));

        //$$(".icon-button__content").get(1).click();
        $x("//input[@pattern='[0-9.]*']").doubleClick().sendKeys(secondMeetingDate);

        $$(By.className("button__text")).filter(visible).first().click();
        $x("//*[contains(@data-test-id,'replan-notification')]").should(appear, Duration.ofSeconds(15));
        $$(".notification__title").get(1).shouldHave(exactText("Необходимо подтверждение"));
        $$(By.className("button__text")).get(1).click();

        $x("//*[contains(@data-test-id,'success-notification')]").should(appear, Duration.ofSeconds(15));
        $$(".notification__title").filter(visible).get(0).shouldHave(exactText("Успешно!"));

        $$(".notification__content").filter(visible).get(0).
                should(exactText("Встреча успешно запланирована на " +
                        secondMeetingDate));

    }
}
