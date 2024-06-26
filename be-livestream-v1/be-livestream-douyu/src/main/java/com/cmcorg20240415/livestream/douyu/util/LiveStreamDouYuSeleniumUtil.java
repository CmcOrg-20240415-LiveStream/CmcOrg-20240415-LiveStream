package com.cmcorg20240415.livestream.douyu.util;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import com.cmcorg20230301.be.engine.model.model.constant.BaseConstant;
import com.cmcorg20240415.livestream.douyu.model.bo.SeleniumOperationBO;
import com.cmcorg20240415.livestream.douyu.model.bo.SeleniumOperationHandlerBO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LiveStreamDouYuSeleniumUtil {

    public static WebDriver webDriver;

    /**
     * 初始：WebDriver
     */
    public static void initWebDriver(boolean headless) {

        if (webDriver != null) {
            return;
        }

        String driverPath = "/home/browserDriver/chromedriver";

        // 设置：浏览器驱动的位置
        if (Platform.getCurrent().is(Platform.WINDOWS)) {

            driverPath = driverPath + ".exe";

        }

        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverPath);

        ChromeOptions chromeOptions = new ChromeOptions();

        // 通用处理：浏览器参数
        handleChromiumOptions(chromeOptions, headless);

        webDriver = new ChromeDriver(chromeOptions);

        log.info("浏览器初始化完成");

    }

    /**
     * 通用处理：浏览器参数
     */
    private static void handleChromiumOptions(ChromiumOptions<?> chromiumOptions, boolean headless) {

        chromiumOptions.setHeadless(headless);

        chromiumOptions.addArguments("--remote-allow-origins=*");

        chromiumOptions.addArguments("--no-sandbox");

        chromiumOptions.addArguments("--disable-gpu");

        chromiumOptions.addArguments("--disable-dev-shm-usage");

        chromiumOptions.addArguments("--start-maximized");

        chromiumOptions.addArguments("--disable-infobars");

        chromiumOptions.addArguments("--disable-software-rasterizer");

        chromiumOptions.addArguments("--disable-plugins");

        chromiumOptions.addArguments("--disable-extensions");

        chromiumOptions.addArguments("--disable-images");

        chromiumOptions.addArguments("blink-settings=imagesEnabled=false");

        chromiumOptions.addArguments("--disable-blink-features=AutomationControlled");

        // chromiumOptions.addArguments("--user-data-dir=/home/browserUserDataDir");

        // chromiumOptions.setExperimentalOption("detach", true);

        chromiumOptions.setExperimentalOption("useAutomationExtension", false);

        chromiumOptions.setExperimentalOption("excludeSwitches", CollUtil.newArrayList("enable-automation"));

    }

    @PreDestroy
    public void preDestroy() {

        // 关闭：WebDriver
        quitWebDriver();

    }

    /**
     * 关闭：WebDriver
     */
    private static void quitWebDriver() {

        if (webDriver != null) {

            try {

                webDriver.quit();

            } catch (Exception ignored) {

            } finally {

                webDriver = null;

            }

        }

    }

    public static final String BAIDU = "https://baidu.com";

    public static final String BING = "https://cn.bing.com";

    public static final String DOUYU = "https://douyu.com";

    // key：网站的 host，value：操作集合
    public static final Map<String, List<SeleniumOperationBO>> CRAWLER_OPERATION_MAP = MapUtil.newHashMap();

    static {

        CRAWLER_OPERATION_MAP.put("baijiahao.baidu.com",
            CollUtil.newArrayList(SeleniumOperationBO.text("//*[@id=\"header\"]/div[1]"),
                SeleniumOperationBO.text("//*[@id=\"ssr-content\"]/div[2]/div[1]/div[1]/div[3]/div[1]")));

        CRAWLER_OPERATION_MAP.put("zhuanlan.zhihu.com",
            CollUtil.newArrayList(SeleniumOperationBO.click("/html/body/div[4]/div/div/div/div[2]/button"),
                SeleniumOperationBO.text("//*[@id=\"root\"]/div/main/div/article/header/h1"),
                SeleniumOperationBO.text("//*[@id=\"root\"]/div/main/div/article/div[1]/div/div/div")));

        CRAWLER_OPERATION_MAP.put("mp.weixin.qq.com",
            CollUtil.newArrayList(SeleniumOperationBO.text("//*[@id=\"activity-name\"]"),
                SeleniumOperationBO.text("//*[@id=\"js_content\"]")));

        CRAWLER_OPERATION_MAP.put("baike.baidu.com",
            CollUtil.newArrayList(SeleniumOperationBO.text("//*[@id=\"J-lemma-main-wrapper\"]/div[2]/div/div[1]/div")));

        CRAWLER_OPERATION_MAP.put("baidu.com", CollUtil.newArrayList(SeleniumOperationBO.input("//*[@id=\"kw\"]"),
            SeleniumOperationBO.click("//*[@id=\"su\"]"),
            SeleniumOperationBO.jumpUsableUrl(CollUtil.newArrayList("//*[@id=\"1\"]/div/h3/a",
                "//*[@id=\"2\"]/div/h3/a", "//*[@id=\"3\"]/div/h3/a", "//*[@id=\"4\"]/div/h3/a",
                "//*[@id=\"5\"]/div/h3/a", "//*[@id=\"6\"]/div/h3/a", "//*[@id=\"1\"]/div/div[1]/h3/a",
                "//*[@id=\"2\"]/div/div[1]/h3/a", "//*[@id=\"3\"]/div/div[1]/h3/a", "//*[@id=\"4\"]/div/div[1]/h3/a",
                "//*[@id=\"5\"]/div/div[1]/h3/a", "//*[@id=\"6\"]/div/div[1]/h3/a"))));

        CRAWLER_OPERATION_MAP.put("cn.bing.com",
            CollUtil.newArrayList(SeleniumOperationBO.input("//*[@id=\"sb_form_q\"]"),
                SeleniumOperationBO.click("//*[@id=\"search_icon\"]"),
                SeleniumOperationBO.jumpUsableUrl(CollUtil.newArrayList("//*[@id=\"b_results\"]/li[1]/div[1]/h2/a",
                    "//*[@id=\"b_results\"]/li[2]/div[1]/h2/a", "//*[@id=\"b_results\"]/li[3]/div[1]/h2/a",
                    "//*[@id=\"b_results\"]/li[4]/div[1]/h2/a", "//*[@id=\"b_results\"]/li[5]/div[1]/h2/a",
                    "//*[@id=\"b_results\"]/li[6]/div[1]/h2/a", "//*[@id=\"b_results\"]/li[1]/h2/a",
                    "//*[@id=\"b_results\"]/li[2]/h2/a", "//*[@id=\"b_results\"]/li[3]/h2/a",
                    "//*[@id=\"b_results\"]/li[4]/h2/a", "//*[@id=\"b_results\"]/li[5]/h2/a",
                    "//*[@id=\"b_results\"]/li[6]/h2/a"))));

        CRAWLER_OPERATION_MAP.put("douyu.com",
            CollUtil.newArrayList(SeleniumOperationBO.ifFind(
                "//*[@id=\"js-header\"]/div/div[1]/div[3]/div[7]/div/div/a/span", //
                    CollUtil.newArrayList(
                        SeleniumOperationBO.click("//*[@id=\"js-header\"]/div/div[1]/div[3]/div[7]/div/div/a/span"),
                        SeleniumOperationBO.switchFrame("//*[@id=\"login-passport-frame\"]"), //
                        SeleniumOperationBO.printCanvas(
                            "//*[@id=\"loginbox\"]/div[2]/div[2]/div[5]/div/div[1]/div/div[1]/div/div[1]/div/canvas"), //
                        SeleniumOperationBO.switchFrame(null))) //
            ));

    }

    /**
     * 发送弹幕
     */
    public synchronized static void sendDanMu(String value) {

        if (!LiveStreamDouYuUtil.SIGN_IN_FLAG) {
            return;
        }

        String textarea = "//*[@id=\"layout-Player-aside\"]/div[2]/div/div[2]/div[2]/textarea";

        WebElement webElement = LiveStreamDouYuSeleniumUtil.getWebElement(null, By.xpath(textarea), null);

        if (webElement == null) {

            webElement = LiveStreamDouYuSeleniumUtil.getWebElement(null, By.xpath("/html"), null);

            log.info("当前页面：url：{}\n页面：{}", LiveStreamDouYuSeleniumUtil.webDriver.getCurrentUrl(),
                webElement.getAttribute("innerHTML"));
            //
            // // 刷新页面
            // LiveStreamDouYuSeleniumUtil.webDriver.navigate().refresh();

            return;

        }

        List<SeleniumOperationBO> operationList = CollUtil.newArrayList(SeleniumOperationBO.input(textarea),
            SeleniumOperationBO.click("//*[@id=\"layout-Player-aside\"]/div[2]/div/div[2]/div[2]/div[2]"));

        // 发送弹幕
        doExecGetCrawlerResult(CollUtil.newArrayList(value), null, operationList);

    }

    public static void main(String[] args) {

        // var a =
        // document.evaluate('//*[@id="loginbox"]/div[2]/div[2]/div[5]/div/div[1]/div/div[1]/div/div[1]/div/canvas',
        // document).iterateNext()

        // 初始：WebDriver
        initWebDriver(false);

        StrBuilder strBuilder = StrUtil.strBuilder();

        getCrawlerResult(BAIDU, CollUtil.newArrayList("java"), strBuilder);

        System.out.println(strBuilder);

    }

    /**
     * 获取：默认的操作集合
     */
    @Nullable
    public static List<SeleniumOperationBO> getHostDefaultOperationList(String urlStr) {

        String hostStr = getHostStr(urlStr);

        return CRAWLER_OPERATION_MAP.get(hostStr);

    }

    /**
     * 例如：https://baijiahao.baidu.com/s，返回：baijiahao.baidu.com
     */
    @Nullable
    public static String getHostStr(String urlStr) {

        URL url = URLUtil.url(urlStr);

        if (url == null) {
            return null;
        }

        return url.getHost().replaceAll("www.", "");

    }

    /**
     * 获取：爬取的内容
     */
    public synchronized static void getCrawlerResult(String urlStr, @Nullable List<String> inputList,
        @Nullable StrBuilder strBuilder) {

        try {

            log.info("打开页面-开始：{}", urlStr);

            webDriver.get(urlStr);

            log.info("打开页面-完毕：{}", urlStr);

        } catch (Exception e) {

            e.printStackTrace();

            quitWebDriver();

            initWebDriver(true);

            if (webDriver == null) {
                return;
            }

            webDriver.get(urlStr);

        }

        // 获取：爬取的内容
        execGetCrawlerResult(urlStr, inputList, strBuilder);

    }

    /**
     * 获取：爬取的内容
     */
    @SneakyThrows
    private static void execGetCrawlerResult(String urlStr, @Nullable List<String> inputList,
        @Nullable StrBuilder strBuilder) {

        // 获取：操作集合
        List<SeleniumOperationBO> operationList = getOperationList(urlStr);

        doExecGetCrawlerResult(inputList, strBuilder, operationList);

    }

    /**
     * 执行：获取：爬取的内容
     */
    @SneakyThrows
    private static void doExecGetCrawlerResult(@Nullable List<String> inputList, @Nullable StrBuilder strBuilder,
        List<SeleniumOperationBO> operationList) {

        for (SeleniumOperationBO item : operationList) {

            SeleniumOperationHandlerBO handlerBO = new SeleniumOperationHandlerBO();

            handlerBO.setBo(item);
            handlerBO.setStrBuilder(strBuilder);
            handlerBO.setInputList(inputList);

            item.getType().getVoidFunc1().call(handlerBO);

        }

    }

    /**
     * 获取：操作集合
     */
    private static List<SeleniumOperationBO> getOperationList(String urlStr) {

        // 获取：默认的操作集合
        List<SeleniumOperationBO> operationList = getHostDefaultOperationList(urlStr);

        if (CollUtil.isEmpty(operationList)) {

            operationList = CollUtil.newArrayList(SeleniumOperationBO.text("/html/body")); // 则爬取全部内容

        }

        return operationList;

    }

    public static final Duration TIMEOUT = Duration.ofMillis(BaseConstant.SECOND_20_EXPIRE_TIME);

    /**
     * 获取：元素
     */
    @Nullable
    public static WebElement getWebElement(@Nullable Duration timeout, By by, @Nullable WebDriver webDriver) {

        if (timeout == null) {
            timeout = TIMEOUT;
        }

        if (webDriver == null) {
            webDriver = LiveStreamDouYuSeleniumUtil.webDriver;
        }

        try {

            log.info("寻找元素：{}", by.toString());

            WebDriverWait webDriverWait = new WebDriverWait(webDriver, timeout);

            WebDriver finalWebDriver = webDriver;

            return webDriverWait.until(new ExpectedCondition<WebElement>() {

                @Override
                public WebElement apply(WebDriver driver) {

                    return finalWebDriver.findElement(by);

                }

            });

        } catch (Exception e) {

            log.info("未找到元素：{}", by.toString());

            return null;

        }

    }

}
