package com.cmcorg20240415.livestream.ai.model.dto;

import com.cmcorg20240415.livestream.ai.model.enums.AIMessageItemContentTypeEnum;
import com.cmcorg20240415.livestream.ai.model.enums.AIMessageItemRoleEnum;

import cn.hutool.core.lang.PatternPool;
import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AIMessageItemDTO {

    @Schema(description = "角色")
    private String role;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "内容类型")
    private AIMessageItemContentTypeEnum contentType;

    @Schema(description = "名称")
    private String name;

    private JSONObject function_call;

    private AIMessageItemDTO() {}

    public AIMessageItemDTO toText() {

        if (AIMessageItemContentTypeEnum.TEXT.equals(contentType)) {
            return this;
        }

        if (AIMessageItemContentTypeEnum.IMAGE_URL.equals(contentType)) {
            return text(AIMessageItemRoleEnum.getByName(getRole()), "图片链接：" + getContent());
        }

        return this;

    }

    public static AIMessageItemDTO text(AIMessageItemRoleEnum role, String content) {

        AIMessageItemDTO lxAssistantMessageItemDTO = new AIMessageItemDTO();

        lxAssistantMessageItemDTO.setRole(role.getName());
        lxAssistantMessageItemDTO.setContent(content);

        return lxAssistantMessageItemDTO;

    }

    public static AIMessageItemDTO imageUrl(AIMessageItemRoleEnum role, String content) {

        AIMessageItemDTO lxAssistantMessageItemDTO = new AIMessageItemDTO();

        lxAssistantMessageItemDTO.setRole(role.getName());

        if (PatternPool.URL.matcher(content).matches()) {

            lxAssistantMessageItemDTO.setContent(content);
            lxAssistantMessageItemDTO.setContentType(AIMessageItemContentTypeEnum.IMAGE_URL);

        } else {

            lxAssistantMessageItemDTO.setContent(content);

        }

        return lxAssistantMessageItemDTO;

    }

    /**
     * @param lxSystemFunctionCallNameEnum 函数名称
     * @param content 函数调用的结果
     */
    public static AIMessageItemDTO functionCall(AIMessageItemRoleEnum lxSystemFunctionCallNameEnum, String content) {

        AIMessageItemDTO lxAssistantMessageItemDTO = new AIMessageItemDTO();

        lxAssistantMessageItemDTO.setRole(AIMessageItemRoleEnum.FUNCTION.getName());
        lxAssistantMessageItemDTO.setContent(content);
        lxAssistantMessageItemDTO.setName(lxSystemFunctionCallNameEnum.getName());

        return lxAssistantMessageItemDTO;

    }

    /**
     * 助手回复的 function_call，格式： "function_call": { "name": "getCurrentWeather", "arguments": "{\n \"city\": \"上海市\"\n}" }
     */
    public static AIMessageItemDTO resultFunctionCall(JSONObject functionCall) {

        AIMessageItemDTO lxAssistantMessageItemDTO = new AIMessageItemDTO();

        lxAssistantMessageItemDTO.setRole(AIMessageItemRoleEnum.ASSISTANT.getName());
        lxAssistantMessageItemDTO.setContent("");
        lxAssistantMessageItemDTO.setFunction_call(functionCall);

        return lxAssistantMessageItemDTO;

    }

}
