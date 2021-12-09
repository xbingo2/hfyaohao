package com.cn.xbingo.handler;

import com.alibaba.excel.EasyExcel;
import com.cn.xbingo.constant.CommonConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * 抽象处理类，所有的处理类都继承此类
 */
public abstract class AbstractHandler<T> {
    public List<T> infoList = new ArrayList<T>();

    public Class<T> clazz;

    public String fileName;

    public String sheetName;


    /**
     * 获取网页数据
     */
    public abstract void getInfo(String id);

    public void hanlder(String id) {
        getInfo(id);
        insetExcel();
    }


    public void setBaseInfo(String fileName, String sheetName, Class<T> clazz) {
        this.fileName = fileName;
        this.sheetName = sheetName;
        this.clazz = clazz;
    }


    /**
     * 添加数据到list中
     * @param t
     */
    public void setInfoList(T t) {
        infoList.add(t);
    }

    /**
     * easyExcel导出数据
     */
    public void insetExcel() {
        try {
            String pathName = this.fileName + System.currentTimeMillis() + CommonConstants.EXCEL_POSTFIX;
            EasyExcel.write(pathName, clazz).sheet(this.sheetName).doWrite(infoList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
