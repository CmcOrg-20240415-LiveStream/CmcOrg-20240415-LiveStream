package com.cmcorg20240415.livestream.douyu.model.enums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.cmcorg20240415.livestream.douyu.model.bo.SeleniumOperationBO;
import com.cmcorg20240415.livestream.douyu.model.bo.SeleniumOperationHandlerBO;
import com.cmcorg20240415.livestream.douyu.util.LiveStreamDouYuSeleniumUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.func.VoidFunc1;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Getter
@Slf4j
public enum SeleniumTypeEnum {

    CLICK(101, (handlerBo) -> {

        SeleniumOperationBO bo = handlerBo.getBo();

        String xpath = bo.getXpath();

        WebElement webElement = LiveStreamDouYuSeleniumUtil.getWebElement(null, By.xpath(xpath), null);

        if (webElement == null) {

            log.info("点击-no：{}", xpath);
            return;

        }

        JavascriptExecutor js = (JavascriptExecutor)LiveStreamDouYuSeleniumUtil.webDriver;

        js.executeScript("arguments[0].click();", webElement);

        log.info("点击：{}", xpath);

    }), // 点击

    INPUT(201, (handlerBo) -> {

        SeleniumOperationBO bo = handlerBo.getBo();

        String xpath = bo.getXpath();

        WebElement webElement = LiveStreamDouYuSeleniumUtil.getWebElement(null, By.xpath(xpath), null);

        if (webElement == null) {

            log.info("输入-no：{}", xpath);
            return;

        }

        if (CollUtil.isNotEmpty(handlerBo.getInputList())) {

            String inputStr = handlerBo.getInputList().remove(0);

            if (StrUtil.isNotBlank(inputStr)) {

                JavascriptExecutor js = (JavascriptExecutor)LiveStreamDouYuSeleniumUtil.webDriver;

                js.executeScript("arguments[0].value = \"" + inputStr + "\";", webElement);

                log.info("输入：{}", xpath);

            }

        }

    }), // 输入

    JUMP_URL(301, (handlerBo) -> {

        SeleniumOperationBO bo = handlerBo.getBo();

        String xpath = bo.getXpath();

        WebElement webElement = LiveStreamDouYuSeleniumUtil.getWebElement(null, By.xpath(xpath), null);

        if (webElement == null) {

            log.info("跳转到新的 url-no：{}", xpath);
            return;

        }

        String href = webElement.getAttribute("href");

        if (StrUtil.isNotBlank(href)) {

            // 跳转到新的 url
            LiveStreamDouYuSeleniumUtil.getCrawlerResult(href, handlerBo.getInputList(), handlerBo.getStrBuilder());

            log.info("跳转到新的 url：{}", xpath);

        }

    }), // 跳转到该 url

    JUMP_USABLE_URL(401, (handlerBo) -> {

        SeleniumOperationBO bo = handlerBo.getBo();

        for (String xpath : bo.getXpathList()) {

            WebElement webElement = LiveStreamDouYuSeleniumUtil.getWebElement(null, By.xpath(xpath), null);

            if (webElement == null) {

                log.info("跳转到可用的 url-no：{}", xpath);
                continue;

            }

            String href = webElement.getAttribute("href");

            if (StrUtil.isNotBlank(href)) {

                log.info("跳转到可用的 url：{}", xpath);

                // 跳转到新的 url
                LiveStreamDouYuSeleniumUtil.getCrawlerResult(href, handlerBo.getInputList(), handlerBo.getStrBuilder());

                break;

            }

        }

    }), // 跳转到可用的 url

    IF_FIND(501, (handlerBo) -> {

        SeleniumOperationBO bo = handlerBo.getBo();

        String xpath = bo.getXpath();

        WebElement webElement = LiveStreamDouYuSeleniumUtil.getWebElement(null, By.xpath(xpath), null);

        if (webElement == null) {

            log.info("存在元素-no：{}", xpath);
            return;

        }

        List<SeleniumOperationBO> subList = bo.getSubList();

        if (CollUtil.isEmpty(subList)) {
            return;
        }

        log.info("存在元素：{}", xpath);

        for (SeleniumOperationBO item : subList) {

            SeleniumOperationHandlerBO handlerBO = new SeleniumOperationHandlerBO();

            handlerBO.setBo(item);
            handlerBO.setStrBuilder(handlerBo.getStrBuilder());
            handlerBO.setInputList(handlerBo.getInputList());

            item.getType().getVoidFunc1().call(handlerBO);

        }

    }), // 如果存在该元素

    TEXT(601, (handlerBo) -> {

        SeleniumOperationBO bo = handlerBo.getBo();

        String xpath = bo.getXpath();

        WebElement webElement = LiveStreamDouYuSeleniumUtil.getWebElement(null, By.xpath(xpath), null);

        if (webElement == null) {

            log.info("获取文字-no：{}", xpath);
            return;

        }

        handlerBo.getStrBuilder().append(webElement.getText()).append("\n");

        log.info("获取文字：{}", xpath);

    }), // 获取元素里面的文字内容

    PRINT_CANVAS(701, (handlerBo) -> {

        SeleniumOperationBO bo = handlerBo.getBo();

        String xpath = bo.getXpath();

        WebElement webElement = LiveStreamDouYuSeleniumUtil.getWebElement(null, By.xpath(xpath), null);

        if (webElement == null) {

            log.info("获取canvas内容-no：{}", xpath);
            return;

        }

        JavascriptExecutor js = (JavascriptExecutor)LiveStreamDouYuSeleniumUtil.webDriver;

        log.info("获取canvas内容：{}", xpath);

        String canvasBase64 =
            (String)js.executeScript("return arguments[0].toDataURL('image/png').substring(21);", webElement);

        log.info("canvasBase64内容：{}", canvasBase64);

    }), // 打印 canvas的内容

    SWITCH_FRAME(801, (handlerBo) -> {

        SeleniumOperationBO bo = handlerBo.getBo();

        String xpath = bo.getXpath();

        if (StrUtil.isBlank(xpath)) {

            log.info("切换 frame：根节点");

            LiveStreamDouYuSeleniumUtil.webDriver.switchTo().defaultContent(); // 切回来
            return;

        }

        WebElement webElement = LiveStreamDouYuSeleniumUtil.getWebElement(null, By.xpath(xpath), null);

        if (webElement == null) {

            log.info("切换 frame-no：{}", xpath);
            return;

        }

        log.info("切换 frame：{}", xpath);

        LiveStreamDouYuSeleniumUtil.webDriver.switchTo().frame(webElement);

    }), // 切换：frame

    ;

    private final int code;

    private final VoidFunc1<SeleniumOperationHandlerBO> voidFunc1;

    public static final Map<Integer, SeleniumTypeEnum> MAP = new HashMap<>();

    static {

        for (SeleniumTypeEnum item : values()) {

            MAP.put(item.getCode(), item);

        }

    }

    /**
     * 通过：code获取类型
     */
    public static SeleniumTypeEnum getByCode(Integer code) {

        if (code == null) {
            return TEXT;
        }

        return MAP.getOrDefault(code, TEXT);

    }

}
