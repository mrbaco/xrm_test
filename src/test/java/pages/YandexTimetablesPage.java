package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.Calendar;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class YandexTimetablesPage {

    /* ################################################
        Описание элементов
       ################################################ */

    /** Главная страница Яндекса. Кнопка Ещё */
    private final SelenideElement mainPageMoreButton = $("a.home-link.services-new__item.services-new__more");

    /** Главная страница Яндекса. Всплывающее окно с сервисами. Сервис Расписания */
    private final SelenideElement mainPageServicesPopupTimetables = $("a.home-link.services-new__item.services-new__more-popup-item[data-statlog*='rasp']");

    /** Яндекс.Расписания. Поле ввода Пункт отправления */
    private final SelenideElement timetablesPageDepartureInput = $(".SearchForm #from");

    /** Яндекс.Расписания. Поле ввода Пункт назначения */
    private final SelenideElement timetablesPageDestinationInput = $(".SearchForm #to");

    /** Яндекс.Расписания. Поле ввода Даты отправления */
    private final SelenideElement timetablesPageDepartureDateInput = $(".SearchForm #when");

    /** Яндекс.Расписания. Дни в календаре */
    private final ElementsCollection timetablesPageCalendarDays = $$(".CalendarContent .CalendarDay");

    /** Яндекс.Расписания. Кнопка Найти */
    private final SelenideElement timetablesPageSearchButton = $(".SearchForm .SearchForm__submit");

    /** Яндекс.Расписания. Ссылки выбора вида транспорта */
    private final ElementsCollection timetablesPageTransportTypes = $$("a.TransportSelectorLinks__item");

    /** Яндекс.Расписания. Заголовок поисковых результатов */
    private final SelenideElement timetablesPageSearchTitle = $(".SearchTitle");

    /** Яндекс.Расписания. Элементы таблицы с расписанием */
    private final ElementsCollection timetablesPageItems = $$(".SearchPage article.SearchSegment.SearchSegment_isNotGone");

    /** Яндекс.Расписания. Страница рейса. Заголовок */
    private final SelenideElement timetablesPageTripTitle = $("h1.ThreadPage__title");

    /** Яндекс.Расписания. Страница рейса. Элементы таблицы с расписанием рейса */
    private final ElementsCollection timetablesPageTripItems = $$("table.ThreadTable__table tbody tr.ThreadTable__rowStation");

    /* ################################################
        Методы взаимодействия с элементами
       ################################################ */

    /** Главная страница Яндекса. Кнопка Ещё. Нажать */
    public void clickMainPageMoreButton() {
        mainPageMoreButton.shouldBe(visible).scrollIntoView("{block: 'center'}").click();
    }

    /** Главная страница Яндекса. Всплывающее окно с сервисами. Сервис Расписания. Нажать */
    public void clickMainPageServicesPopupTimetables() {
        mainPageServicesPopupTimetables.shouldBe(visible).scrollIntoView("{block: 'center'}").click();
    }

    /** Яндекс.Расписания. Поле ввода Пункт отправления. Установить значение */
    public void setValueTimetablesPageDepartureInput(String value) {
        timetablesPageDepartureInput.shouldBe(visible).scrollIntoView("{block: 'center'}").setValue(value);
    }

    /** Яндекс.Расписания. Поле ввода Пункт назначения. Установить значение */
    public void setValueTimetablesPageDestinationInput(String value) {
        timetablesPageDestinationInput.shouldBe(visible).scrollIntoView("{block: 'center'}").setValue(value);
    }

    /** Яндекс.Расписания. Поле ввода Даты отправления. Нажать */
    public void clickTimetablesPageDepartureDateInput() {
        timetablesPageDepartureDateInput.shouldBe(visible).scrollIntoView("{block: 'center'}").click();
    }

    /** Яндекс.Расписания. Дни в календаре. Найти по дате и нажать */
    public void clickTimetablesPageCalendarDays(Calendar date) {
        timetablesPageCalendarDays.findBy(attribute("data-date", String.format("%d-%02d-%02d",
                date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH))
        )).shouldBe(visible).scrollIntoView("{block: 'center'}").click();
    }

    /** Яндекс.Расписания. Кнопка Найти. Нажать */
    public void clickTimetablesPageSearchButton() {
        timetablesPageSearchButton.shouldBe(visible).scrollIntoView("{block: 'center'}").click();
    }

    /** Яндекс.Расписания. Ссылки выбора вида транспорта. Нажать */
    public void clickTimetablesPageTransportTypes(String transport) {
        timetablesPageTransportTypes.findBy(matchText(String.format("(?ui)%s", transport)))
                .scrollIntoView("{block: 'center'}").click();
    }

    /** Яндекс.Расписания. Заголовок поисковых результатов. Получить значение */
    public String getValueTimetablesPageSearchTitle() {
        return timetablesPageSearchTitle.shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

    /** Яндекс.Расписания. Страница рейса. Заголовок. Получить значение */
    public String getValueTimetablesPageTripTitle() {
        return timetablesPageTripTitle.shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

    /** Яндекс.Расписания. Страница рейса. Время отправления. Получить значение */
    public String getValueTimetablesPageTripDepartureTime() {
        return timetablesPageTripItems.get(0).find(".ThreadTable__departure").shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

    /** Яндекс.Расписания. Страница рейса. Пункт отправления. Получить значение */
    public String getValueTimetablesPageTripDeparture() {
        return timetablesPageTripItems.get(0).find(".ThreadTable__station").shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

    /** Яндекс.Расписания. Страница рейса. Время прибытия. Получить значение */
    public String getValueTimetablesPageTripArrivalTime() {
        return timetablesPageTripItems.last().find(".ThreadTable__arrival").shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

    /** Яндекс.Расписания. Страница рейса. Пункт прибытия. Получить значение */
    public String getValueTimetablesPageTripDestination() {
        return timetablesPageTripItems.last().find(".ThreadTable__station").shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

    /** Яндекс.Расписания. Страница рейса. Время в пути. Получить значение */
    public String getValueTimetablesPageTripTravelTime() {
        return timetablesPageTripItems.last().find(".ThreadTable__timeInTheWay").shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

    /** Яндекс.Расписания. Элементы таблицы с расписанием. Рейс. Посчитать */
    public int countTimetablesPageItems() {
        return timetablesPageItems.size();
    }

    /** Яндекс.Расписания. Элементы таблицы с расписанием. Рейс. Ссылка рейса. Нажать */
    public void clickTimetablesPageItemsLink(int trip) {
        timetablesPageItems.get(trip).find("a.Link.SegmentTitle__link").shouldBe(visible).scrollIntoView("{block: 'center'}").click();
    }

    /** Яндекс.Расписания. Элементы таблицы с расписанием. Рейс. Ссылка рейса. Получить значение */
    public String getValueTimetablesPageItemsLink(int trip) {
        return timetablesPageItems.get(trip).find("a.Link.SegmentTitle__link").shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }
    
    /** Яндекс.Расписания. Элементы таблицы с расписанием. Рейс. Время отправления. Получить значение */
    public String getValueTimetablesPageItemsDepartureTime(int trip) {
        return timetablesPageItems.get(trip).find(".SearchSegment__timeAndStations .SearchSegment__dateTime.Time_important .SearchSegment__time").shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

    /** Яндекс.Расписания. Элементы таблицы с расписанием. Рейс. Пункт отправления. Получить значение */
    public String getValueTimetablesPageItemsDeparture(int trip) {
        return timetablesPageItems.get(trip).find(".SearchSegment__timeAndStations .SearchSegment__stations .SearchSegment__stationHolder:nth-child(1)").shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

    /** Яндекс.Расписания. Элементы таблицы с расписанием. Рейс. Время прибытия. Получить значение */
    public String getValueTimetablesPageItemsArrivalTime(int trip) {
        return timetablesPageItems.get(trip).find(".SearchSegment__timeAndStations .SearchSegment__dateTime:not(.Time_important) .SearchSegment__time").shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

    /** Яндекс.Расписания. Элементы таблицы с расписанием. Рейс. Пункт прибытия. Получить значение */
    public String getValueTimetablesPageItemsDestination(int trip) {
        return timetablesPageItems.get(trip).find(".SearchSegment__timeAndStations .SearchSegment__stations .SearchSegment__stationHolder:nth-child(2)").shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

    /** Яндекс.Расписания. Элементы таблицы с расписанием. Рейс. Время в пути. Получить значение */
    public String getValueTimetablesPageItemsTravelTime(int trip) {
        return timetablesPageItems.get(trip).find(".SearchSegment__timeAndStations .SearchSegment__times .SearchSegment__duration").shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

    /** Яндекс.Расписания. Элементы таблицы с расписанием. Рейс. Минимальная цена. Получить значение */
    public String getValueTimetablesPageItemsMinPrice(int trip) {
        return timetablesPageItems.get(trip).find(".SearchSegment__scheduleAndPrices .TariffsListItem__price").shouldBe(visible).scrollIntoView("{block: 'center'}").getText();
    }

}
