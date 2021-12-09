package com.cn.xbingo;

import com.alibaba.excel.util.StringUtils;
import com.cn.xbingo.constant.CommonConstants;
import com.cn.xbingo.handler.AbstractHandler;
import com.cn.xbingo.handler.impl.JgHandler;
import com.cn.xbingo.handler.impl.YaoHaoHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Map<String, AbstractHandler> handlerMap = new HashMap<String, AbstractHandler>();
        handlerMap.put("yh", new YaoHaoHandler());
        handlerMap.put("jg", new JgHandler());
        select(handlerMap);
    }

    public static void select(Map<String, AbstractHandler> handlerMap) {
        System.out.println("请输入'功能-项目id'，例如yh-373,jg-8142");

        Scanner sc = new Scanner(System.in);
        String type = sc.nextLine();
        if (StringUtils.isNotBlank(type) && type.contains(CommonConstants.INPUT_SPLIT)) {
            if (handlerMap.containsKey(type.split(CommonConstants.INPUT_SPLIT)[0])) {
                handlerMap.get(type.split(CommonConstants.INPUT_SPLIT)[0]).hanlder(type.split(CommonConstants.INPUT_SPLIT)[1]);
            }
        }
        select(handlerMap);
    }
}
