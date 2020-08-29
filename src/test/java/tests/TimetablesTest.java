package tests;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import model.TripData;
import org.junit.Assert;
import org.junit.Assume;
import pages.YandexTimetablesPage;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.get;
import static io.restassured.path.json.JsonPath.from;

public class TimetablesTest {

    private final Map<String, Integer> months = new HashMap<>();
    private final YandexTimetablesPage timetablesPage;
    private final TripData trip = new TripData();

    private final Calendar departureDate = new GregorianCalendar();

    private String inputtedDayOfWeek;
    private String inputtedDeparture;
    private String inputtedDestination;

    public TimetablesTest() {
        months.put("понедельник", Calendar.MONDAY);
        months.put("вторник", Calendar.TUESDAY);
        months.put("среда", Calendar.WEDNESDAY);
        months.put("четверг", Calendar.THURSDAY);
        months.put("пятница", Calendar.FRIDAY);
        months.put("суббота", Calendar.SATURDAY);
        months.put("воскресенье", Calendar.SUNDAY);

        timetablesPage = new YandexTimetablesPage();
    }

    @Given("пользователь открывает главную страницу Яндекса")
    public void openYandexHomepage() {
        open("https://yandex.ru");
    }

    @Then("нажимает кнопку Ещё после иконок с различными сервисами")
    public void clickMoreServicesButton() {
        timetablesPage.clickMainPageMoreButton();
    }

    @And("в открывшемся окне выбирает сервис Расписания")
    public void clickTimetablesServiceButton() {
        timetablesPage.clickMainPageServicesPopupTimetables();
        Selenide.switchTo().window(1);
    }

    @And("указывает {string} и {string}")
    public void setValueDepartureInputAndDestinationInput(String departure, String destination) {
        inputtedDeparture = departure;
        inputtedDestination = destination;

        timetablesPage.setValueTimetablesPageDepartureInput(departure);
        timetablesPage.setValueTimetablesPageDestinationInput(destination);
    }

    @And("нажимает на поле ввода даты отправления")
    public void clickDateInput() {
        timetablesPage.clickTimetablesPageDepartureDateInput();
    }

    @And("в открывшемся окне выбирает дату отправления равному ближайшему {string}")
    public void clickDatePickerDay(String day) {
        inputtedDayOfWeek = day.toLowerCase();

        int difference = months.get(inputtedDayOfWeek) - departureDate.get(Calendar.DAY_OF_WEEK);
        departureDate.add(Calendar.DAY_OF_MONTH, (difference < 0 ? 7 + difference : difference));

        timetablesPage.clickTimetablesPageCalendarDays(departureDate);
    }

    @And("нажимает на кнопку Найти")
    public void clickSearchButton() {
        timetablesPage.clickTimetablesPageSearchButton();
    }

    @And("в верхней части страницы выбирает вид транспорта {string}")
    public void clickTransportTypeValue(String transport) {
        timetablesPage.clickTimetablesPageTransportTypes(transport);
    }

    @And("проверяет, что результат удовлетворяет поисковому запросу")
    public void checkSearchResultIsCorrect() {
        String title = timetablesPage.getValueTimetablesPageSearchTitle();

        Assert.assertTrue(
                title.contains(inputtedDeparture) &&
                title.contains(inputtedDestination) &&
                title.contains(inputtedDayOfWeek) &&
                title.contains(String.valueOf(departureDate.get(Calendar.DAY_OF_MONTH)))
        );
    }

    @And("запоминает рейс, который отправляется не раньше {string} со стоимостью билета не больше {float}")
    public void saveTripData(String departureTime, float maxPrice) {
        int tripsNumber = timetablesPage.countTimetablesPageItems();
        int inputtedDepartureTime = Integer.parseInt(departureTime.replaceAll(":", ""));

        for (int i = 0; i < tripsNumber; i++) {
            String itemDepartureTime = timetablesPage.getValueTimetablesPageItemsDepartureTime(i);
            String itemArrivalTime = timetablesPage.getValueTimetablesPageItemsArrivalTime(i);
            String itemTravelTime = timetablesPage.getValueTimetablesPageItemsTravelTime(i);

            float price = Float.parseFloat(timetablesPage.getValueTimetablesPageItemsMinPrice(i)
                    .replaceAll("[^0-9\\.,]+", "").replaceAll(",", "."));

            if (Integer.parseInt(itemDepartureTime.replaceAll(":", "")) > inputtedDepartureTime && price < maxPrice) {
                Calendar departure = new GregorianCalendar();

                departure.set(Calendar.HOUR_OF_DAY, Integer.parseInt(itemDepartureTime.split(":")[0]));
                departure.set(Calendar.MINUTE, Integer.parseInt(itemDepartureTime.split(":")[1]));
                
                Calendar arrival = new GregorianCalendar();

                arrival.set(Calendar.HOUR_OF_DAY, Integer.parseInt(itemArrivalTime.split(":")[0]));
                arrival.set(Calendar.MINUTE, Integer.parseInt(itemArrivalTime.split(":")[1]));
                
                String[] splittedTravelTime = itemTravelTime.replaceAll("[^0-9\\s]+", "").split("\\s\\s");
                int travelTime = Integer.parseInt(splittedTravelTime[0].trim()) * 60 + Integer.parseInt(splittedTravelTime[1].trim());

                trip.withDepature(inputtedDeparture)
                        .withDestination(inputtedDestination)
                        .withTitle(timetablesPage.getValueTimetablesPageItemsLink(i).replaceAll("[^А-Яа-яA-Za-z0-9]", ""))
                        .withDepartureTime(departure)
                        .withArrivalTime(arrival)
                        .withTravelTime(travelTime)
                        .withPrice(price)
                        .withId(i);

                break;
            }
        }
    }

    @And("сообщает о найденном рейсе")
    public void printTrip() {
        Assume.assumeTrue("Не найдено ни одного рейса!", !trip.getTitle().equals(""));

        String rates = get("https://www.cbr-xml-daily.ru/daily_json.js").asString();
        float rate = from(rates).get("Valute.USD.Value");

        System.out.printf("Найден рейс %s, стоимостью %.2f руб. ($%.2f)! Отправляется в %s.\r\n",
                trip.getTitle(),
                trip.getPrice(),
                trip.getPrice() / rate,
                String.format("%d:%d",
                        trip.getDepartureTime().get(Calendar.HOUR_OF_DAY),
                        trip.getDepartureTime().get(Calendar.MINUTE)
                ));
    }

    @And("нажимает на название подходящего рейса")
    public void clickSaveTripLink() {
        if (trip.getTitle().equals("")) return;
        timetablesPage.clickTimetablesPageItemsLink(trip.getId());
    }

    @And("проверяет, что рейс удовлетворяет ранее заданным условиям")
    public void checkTripIsCorrect() {
        if (trip.getTitle().equals("")) return;

        String itemDepartureTime = timetablesPage.getValueTimetablesPageTripDepartureTime();
        String itemArrivalTime = timetablesPage.getValueTimetablesPageTripArrivalTime();
        String itemTravelTime = timetablesPage.getValueTimetablesPageTripTravelTime();

        Calendar departure = new GregorianCalendar();

        departure.set(Calendar.HOUR_OF_DAY, Integer.parseInt(itemDepartureTime.split(":")[0]));
        departure.set(Calendar.MINUTE, Integer.parseInt(itemDepartureTime.split(":")[1]));

        Calendar arrival = new GregorianCalendar();

        arrival.set(Calendar.HOUR_OF_DAY, Integer.parseInt(itemArrivalTime.split(":")[0]));
        arrival.set(Calendar.MINUTE, Integer.parseInt(itemArrivalTime.split(":")[1]));

        String[] splittedTravelTime = itemTravelTime.replaceAll("[^0-9\\s]+", "").split("\\s\\s");
        int travelTime = Integer.parseInt(splittedTravelTime[0].trim()) * 60 + Integer.parseInt(splittedTravelTime[1].trim());

        Assert.assertTrue(
                timetablesPage.getValueTimetablesPageTripTitle().replaceAll("[^А-Яа-яA-Za-z0-9]", "").contains(trip.getTitle()) &&
                        travelTime == trip.getTravelTime() &&
                        departure.get(Calendar.HOUR_OF_DAY) == trip.getDepartureTime().get(Calendar.HOUR_OF_DAY) &&
                        departure.get(Calendar.MINUTE) == trip.getDepartureTime().get(Calendar.MINUTE) &&
                        arrival.get(Calendar.HOUR_OF_DAY) == trip.getArrivalTime().get(Calendar.HOUR_OF_DAY) &&
                        arrival.get(Calendar.MINUTE) == trip.getArrivalTime().get(Calendar.MINUTE) &&
                        timetablesPage.getValueTimetablesPageTripDeparture().contains(trip.getDepature()) &&
                        timetablesPage.getValueTimetablesPageTripDestination().contains(trip.getDestination())
        );
    }

}