package com.atguigu.excel;

import com.alibaba.excel.EasyExcel;

/**
 * @author WH
 * @version 1.0
 * @date 2022/11/25 14:50
 */
public class TestRead {
    public static void main(String[] args) {
        //设置文件名称和路径
        String fileName = "D:\\TestData\\atguigu.xlsx";
        //调用方法进行读的操作,sheet()默认读第一个sheet
        EasyExcel.read(fileName,User.class,new ExcelListener()).sheet().doRead();

    }
}
