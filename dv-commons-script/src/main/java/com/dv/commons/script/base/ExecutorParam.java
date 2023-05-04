package com.dv.commons.script.base;

import com.dv.commons.script.glue.GlueTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExecutorParam {
    private String handleName;
    private Object executorParams;
    private GlueTypeEnum glueType;
    private String glueSource;
}
