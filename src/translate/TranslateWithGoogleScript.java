package translate;
 
import static org.junit.Assert.fail;
 
import java.util.concurrent.TimeUnit;
 
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
 
public class TranslateWithGoogleScript {
        private WebDriver driver;
        private WebDriver driver2;
        private String baseUrl;
        private boolean acceptNextAlert = true;
        private StringBuffer verificationErrors = new StringBuffer();
 
        @Before
        public void setUp() throws Exception {
                System.setProperty("webdriver.chrome.driver",
                                "/home/leonid/Public/chromedriver");
                driver = new ChromeDriver();
                driver2 = new ChromeDriver();
                baseUrl = "http://test2.kovalenko.d.ukrtech.info";
                driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
 
                driver.get(baseUrl + "/admin/translate");
                driver.findElement(By.id("LoginForm_username")).clear();
                driver.findElement(By.id("LoginForm_username")).sendKeys("admin");
                driver.findElement(By.id("LoginForm_password")).clear();
                driver.findElement(By.id("LoginForm_password")).sendKeys("123456");
                driver.findElement(By.name("yt0")).click();
        }
 
        @Test
        public void testTranlateScript() throws Exception {
 
                int i = 1;
                while (true) {
                        int currentTextNumber = 0;
                        // Фильтруем по непереведенному на какой-то язык и нажимаем
                        // первую
                        // ссылку "Перевести". Нужно указывать алиас языка на который
                        // переводим в фильтре.arg0
                        driver.get(baseUrl + "/admin/translate");
                        driver.findElement(By.id("content_filter_header")).click();
                        Thread.sleep(500);
                        new Select(driver.findElement(By.id("filter-lang-type")))
                                        .selectByVisibleText("Не переведено на язык(и)");
                        driver.findElement(By.id("filter-lang-alias-en")).click();
                        driver.findElement(By.name("btn_filter")).click();
                        Thread.sleep(500);
                        //************************************
                       
                       
                        while (currentTextNumber < 50) {
                                int j = 1;
                                driver.findElements(By.linkText("Перевести")).get(currentTextNumber).click();
                                //driver.findElements(By.cssSelector("#langs>tbody>tr>td>a"))
                                //              .get(currentTextNumber).click();
                                // Ищем оригинальный текст и присваеваем его переменной
 
                                driver.findElement(By.id("original-text")).isDisplayed();
                                String text_to_translate = driver.findElement(
                                                By.id("original-text")).getText();
 
                                System.out
                                                .println(i
                                                                + "."
                                                                + +j
                                                                + "  "
                                                                + "текст получен: '"
                                                                + text_to_translate
                                                                + "'     "
                                                                + driver.findElement(By.id("original-text"))
                                                                                .toString());
                                j++;
                                if ((text_to_translate == null)
                                                || (text_to_translate.equals(""))) {
                                        System.out.println(i + "." + j + "  "
                                                        + "Выход по break (пустой текст):" + "'"
                                                        + text_to_translate + "'");
                                        j++;
                                        break;
                                }
 
                                // Идем в гугл и переводим текст из переменной. Тут нужно
                                // указывать
                                // алиасы языков с которого и на который переводим.
                                driver2.get("https://translate.google.com/#ru/en/"
                                                + text_to_translate);
                                // Ждем элемент с переведенным текстом и присваиваем его новой
                                // переменной.
                                driver2.findElement(By.id("source")).isDisplayed();
                                String text_translated = driver2
                                                .findElement(By.id("result_box")).getText();
 
                                // driver.findElement(By.xpath(".//tbody/tr[2]/td[10]/a")).click();
                                // Ищем нужный язык в аккордеоне и нажимаем на
                                // него.
                                // Необходимо корректировать путь в зависимости от положения
                                // языка в
                                // аккордеоне.
                                // driver.findElement(
                                // By.xpath(".//*[@id='ui-accordion-form_translate_accordion-header-4']/a"))
                                // .click();
                                driver.findElement(
                                                By.id("ui-accordion-form_translate_accordion-header-1"))
                                                .click();
                                driver.findElement(By.id("translate-form-en")).clear();
                                driver.findElement(By.id("translate-form-en")).sendKeys(
                                                text_translated);
                                System.out.println(i
                                                + "."
                                                + j
                                                + "  "
                                                + "в нужное поле вписан переведенный текст: '"
                                                + text_translated
                                                + "' "
                                                + driver.findElement(By.id("translate-form-en"))
                                                                .toString());
                                j++;
                                driver.findElement(By.xpath("//button[@type='button']"))
                                                .click();
                                System.out.println(i + "." + j + "  " + text_to_translate
                                                + " ---> " + text_translated);
                                j++;
                                System.out.println();
                                i++;
                                currentTextNumber++;
                        }
                }
        }
 
        @After
        public void tearDown() throws Exception {
                driver.quit();
                driver2.quit();
                String verificationErrorString = verificationErrors.toString();
                if (!"".equals(verificationErrorString)) {
                        fail(verificationErrorString);
                }
        }
 
        private boolean isElementPresent(By by) {
                try {
                        driver.findElement(by);
                        return true;
                } catch (NoSuchElementException e) {
                        return false;
                }
        }
 
        private boolean isAlertPresent() {
                try {
                        driver.switchTo().alert();
                        return true;
                } catch (NoAlertPresentException e) {
                        return false;
                }
        }
 
        private String closeAlertAndGetItsText() {
                try {
                        Alert alert = driver.switchTo().alert();
                        String alertText = alert.getText();
                        if (acceptNextAlert) {
                                alert.accept();
                        } else {
                                alert.dismiss();
                        }
                        return alertText;
                } finally {
                        acceptNextAlert = true;
                }
        }
}