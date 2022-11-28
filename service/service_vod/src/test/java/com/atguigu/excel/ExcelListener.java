package com.atguigu.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * @author WH
 * @version 1.0
 * @date 2022/11/25 14:46
 */
public class ExcelListener extends AnalysisEventListener<User> {
    //一行一行读取excel内容,把每行内容封装到user对象
    //从excel第二行读取(默认第一行是表头)
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        System.out.println(user);
    }

    //读取表头中的内容
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头:"+headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
