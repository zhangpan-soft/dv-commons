package com.dv.commons.script.base;

import com.dv.commons.script.glue.GlueFactory;
import com.dv.commons.script.glue.GlueTypeEnum;
import com.dv.commons.script.utils.ScriptUtil;
import com.dv.commons.script.utils.SpringContextHolder;
import lombok.SneakyThrows;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseExecutor implements IExecutor {
    private static final String glueSrcPath = new File("").getAbsolutePath();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final String logPath = glueSrcPath;

    @Override
    @SneakyThrows
    public void execute(ExecutorParam params) {
        if (params == null) {
            throw new IllegalArgumentException("ExecutorParam must not null");
        }

        if (params.getGlueType() == null) {
            throw new IllegalArgumentException("ExecutorParam#glueType must not null");
        }
        IHandler handler = null;
        if (params.getGlueType() == GlueTypeEnum.BEAN) {
            if (params.getHandleName() == null || params.getHandleName().isBlank()) {
                throw new IllegalArgumentException("ExecutorParam#executorHandler must not empty");
            }
            handler = SpringContextHolder.getApplicationContext().getBean(params.getHandleName(), IHandler.class);
        } else if (params.getGlueType() == GlueTypeEnum.GLUE_GROOVY) {
            if (params.getGlueSource() == null || params.getGlueSource().isBlank()) {
                throw new IllegalArgumentException("ExecutorParam#glueSource must not empty");
            }
            try {
                handler = GlueFactory.Instance.loadNewInstance(params.getGlueSource());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (params.getGlueType().isScript()) {

            // cmd
            String cmd = params.getGlueType().getCmd();
            byte[] md5 = MessageDigest.getInstance("MD5").digest(params.getGlueSource().getBytes(StandardCharsets.UTF_8));
            String md5Str = new BigInteger(1, md5).toString(16);
            // make script file
            String scriptFileName = glueSrcPath
                    .concat(File.separator)
                    .concat(String.valueOf(md5Str))
                    .concat("_")
                    .concat(params.getGlueType().getSuffix());
            File scriptFile = new File(scriptFileName);
            if (!scriptFile.exists()) {
                ScriptUtil.markScriptFile(scriptFileName, params.getGlueSource());
            }

            // log file

            // script params：0=param、1=分片序号、2=分片总数
            String[] scriptParams = new String[3];
            scriptParams[0] = String.valueOf(params.getExecutorParams());

            String logFileName = logPath + File.separator + "_" + md5Str + "_" + LocalDateTime.now().format(FORMATTER) + ".log";

            // invoke
            int exitValue = ScriptUtil.execToFile(cmd, scriptFileName, logFileName, scriptParams);

            if (exitValue == 0) {
                return;
            } else {
                System.err.println("script exit value(" + exitValue + ") is failed");
                return;
            }
        } else {
            System.err.println("glueType[" + params.getGlueType() + "] is not valid.");
        }

        if (handler == null) {
            throw new RuntimeException("handler is null");
        }

        handler.handle(params.getExecutorParams());
    }
}
