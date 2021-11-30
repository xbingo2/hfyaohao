package com.cn.xbingo.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.StringUtils;
import lombok.Data;

import java.io.Serializable;

@Data
public class Yh implements Serializable {
    private static final long serialVersionUID = 2131321500629905052L;

    @ExcelProperty("套内面积")
    private String lbInsideArea;

    @ExcelProperty("地址")
    private String lbLocation;

    @ExcelProperty("房号")
    private String strTitle;

    @ExcelProperty("建筑面积")
    private String lbBuildArea;

    @ExcelProperty("结构")
    private String lbStructure;

    @ExcelProperty("是否可售")
    private String lbSellFlag;

    @ExcelProperty("标记")
    private String sellflag;

    @ExcelProperty("单价")
    private String iPrice;

    @ExcelProperty("房号1")
    private String lbPartNO;

    @ExcelProperty("户型")
    private String lbHouseType;

    @ExcelProperty("类型")
    private String lbHouseUsefulness;

    @ExcelProperty("公摊面积")
    private String lbJoinArea;

    @ExcelProperty("总价")
    private Double total;

    public Double getTotal() {
        if (StringUtils.isNotBlank(this.iPrice) && this.iPrice.contains("元/㎡")
                && StringUtils.isNotBlank(this.lbBuildArea) && this.lbBuildArea.contains("㎡")) {
            return Double.parseDouble(this.iPrice.replace("元/㎡", "")) * Double.parseDouble(this.lbBuildArea.replace("㎡", ""));
        }
        else {
            return null;
        }
    }
}
