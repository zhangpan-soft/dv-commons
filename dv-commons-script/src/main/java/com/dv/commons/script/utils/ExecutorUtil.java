package com.dv.commons.script.utils;

import com.dv.commons.script.base.BaseExecutor;
import com.dv.commons.script.base.ExecutorParam;
import com.dv.commons.script.base.IExecutor;

import java.util.List;
import java.util.ServiceLoader;

public enum ExecutorUtil {
    Instance;
    IExecutor executor = null;
    ExecutorUtil(){
        ServiceLoader<IExecutor> executors = ServiceLoader.load(IExecutor.class);
        List<ServiceLoader.Provider<IExecutor>> collect = executors.stream().filter(iExecutorProvider -> !(iExecutorProvider.get() instanceof BaseExecutor)).toList();
        if (collect.isEmpty()){
            executor = new BaseExecutor();
        }else if (collect.size()>1){
            throw new IllegalArgumentException("more than one instance find");
        }else {
            executor = collect.get(0).get();
        }
    }

    public void execute(ExecutorParam param){
        executor.execute(param);
    }
}
