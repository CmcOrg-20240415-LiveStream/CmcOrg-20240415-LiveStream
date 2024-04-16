package com.cmcorg20240415.livestream.douyu.model.bo;

import java.util.List;

import cn.hutool.core.text.StrBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeleniumOperationHandlerBO {

    private SeleniumOperationBO bo;

    private StrBuilder strBuilder;

    private List<String> inputList;

}
