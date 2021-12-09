package com.cn.xbingo.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.StringUtils;
import lombok.Data;

import java.io.Serializable;

@Data
public class Jg implements Serializable {
    private static final long serialVersionUID = 2131321500629905052L;

    @ExcelProperty("楼号")
    private String lh;

    @ExcelProperty("房号")
    private Integer fh;

    @ExcelProperty("户型")
    private String hx;

    @ExcelProperty("建筑面积(㎡)")
    private Double jzmj;

    @ExcelProperty("公摊面积(㎡)")
    private Double gtmj;

    @ExcelProperty("套内面积(㎡)")
    private Double tnmj;

    @ExcelProperty("备案单价(元/㎡)")
    private Double dj;

    @ExcelProperty("备案总价(元)")
    private Double zj;

    @ExcelProperty("楼盘属性")
    private String lpsx;

    @ExcelProperty("装修状况")
    private String zxzk;

    @ExcelProperty("备注")
    private String bz;

}
