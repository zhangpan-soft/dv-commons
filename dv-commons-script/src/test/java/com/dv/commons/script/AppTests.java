package com.dv.commons.script;

import com.dv.commons.script.base.ExecutorParam;
import com.dv.commons.script.glue.GlueTypeEnum;
import com.dv.commons.script.utils.ExecutorUtil;

public class AppTests {

    public static void main(String[] args) {
        ExecutorUtil.Instance.execute(new ExecutorParam().setExecutorParams("").setGlueType(GlueTypeEnum.GLUE_PYTHON).setGlueSource("print(123)"));
    }
}
