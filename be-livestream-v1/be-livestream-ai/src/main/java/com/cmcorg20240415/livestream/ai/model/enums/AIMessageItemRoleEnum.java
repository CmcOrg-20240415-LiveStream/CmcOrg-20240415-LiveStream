package com.cmcorg20240415.livestream.ai.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AIMessageItemRoleEnum {

    USER("user"), // 用户

    ASSISTANT("assistant"), // 助手

    SYSTEM("system"), // 系统

    FUNCTION("function"), // 函数

    ;

    @JsonValue
    private final String name;

    public static AIMessageItemRoleEnum getByName(String name) {

        if (name == null) {
            return USER;
        }

        for (AIMessageItemRoleEnum item : values()) {

            if (item.getName().equals(name)) {
                return item;
            }

        }

        return USER;

    }

}
