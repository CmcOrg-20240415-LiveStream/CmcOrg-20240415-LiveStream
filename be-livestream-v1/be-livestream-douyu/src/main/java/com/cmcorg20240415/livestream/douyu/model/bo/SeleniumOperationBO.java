package com.cmcorg20240415.livestream.douyu.model.bo;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.cmcorg20240415.livestream.douyu.model.enums.SeleniumTypeEnum;

import lombok.Data;

@Data
public class SeleniumOperationBO {

    /**
     * 类型
     */
    private SeleniumTypeEnum type;

    /**
     * xpath
     */
    private String xpath;

    /**
     * xpath集合
     */
    private List<String> xpathList;

    /**
     * 执行操作集合
     */
    private List<SeleniumOperationBO> subList;

    /**
     * 获取：元素文字信息
     */
    public static SeleniumOperationBO text(String xpath) {

        SeleniumOperationBO seleniumOperationBO = new SeleniumOperationBO();

        seleniumOperationBO.setType(SeleniumTypeEnum.TEXT);
        seleniumOperationBO.setXpath(xpath);

        return seleniumOperationBO;

    }

    /**
     * 点击
     */
    public static SeleniumOperationBO click(String xpath) {

        SeleniumOperationBO seleniumOperationBO = new SeleniumOperationBO();

        seleniumOperationBO.setType(SeleniumTypeEnum.CLICK);
        seleniumOperationBO.setXpath(xpath);

        return seleniumOperationBO;

    }

    /**
     * 输入
     */
    public static SeleniumOperationBO input(String xpath) {

        SeleniumOperationBO seleniumOperationBO = new SeleniumOperationBO();

        seleniumOperationBO.setType(SeleniumTypeEnum.INPUT);
        seleniumOperationBO.setXpath(xpath);

        return seleniumOperationBO;

    }

    /**
     * 跳转到该 url
     */
    public static SeleniumOperationBO jumpUrl(String xpath) {

        SeleniumOperationBO seleniumOperationBO = new SeleniumOperationBO();

        seleniumOperationBO.setType(SeleniumTypeEnum.JUMP_URL);
        seleniumOperationBO.setXpath(xpath);

        return seleniumOperationBO;

    }

    /**
     * 跳转到可用的 url
     */
    public static SeleniumOperationBO jumpUsableUrl(List<String> xpathList) {

        SeleniumOperationBO seleniumOperationBO = new SeleniumOperationBO();

        seleniumOperationBO.setType(SeleniumTypeEnum.JUMP_USABLE_URL);
        seleniumOperationBO.setXpathList(xpathList);

        return seleniumOperationBO;

    }

    /**
     * 如果存在该元素
     */
    public static SeleniumOperationBO ifFind(String xpath, List<SeleniumOperationBO> subList) {

        SeleniumOperationBO seleniumOperationBO = new SeleniumOperationBO();

        seleniumOperationBO.setType(SeleniumTypeEnum.IF_FIND);
        seleniumOperationBO.setXpath(xpath);

        seleniumOperationBO.setSubList(subList);

        return seleniumOperationBO;

    }

    /**
     * 打印 canvas的内容
     */
    public static SeleniumOperationBO printCanvas(String xpath) {

        SeleniumOperationBO seleniumOperationBO = new SeleniumOperationBO();

        seleniumOperationBO.setType(SeleniumTypeEnum.PRINT_CANVAS);
        seleniumOperationBO.setXpath(xpath);

        return seleniumOperationBO;

    }

    /**
     * 切换：frame
     * 
     * @param xpath 为空则表示：切回来
     * 
     */
    public static SeleniumOperationBO switchFrame(@Nullable String xpath) {

        SeleniumOperationBO seleniumOperationBO = new SeleniumOperationBO();

        seleniumOperationBO.setType(SeleniumTypeEnum.SWITCH_FRAME);
        seleniumOperationBO.setXpath(xpath);

        return seleniumOperationBO;

    }

}
