package com.cn.xbingo.handler.impl;

import com.cn.xbingo.constant.CommonConstants;
import com.cn.xbingo.handler.AbstractHandler;
import com.cn.xbingo.model.Jg;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class JgHandler extends AbstractHandler<Jg> {
    public String baseUrl = "http://220.178.124.94:8010/fangjia/ws/Detail2.aspx?Id=";

    @Override
    public void getInfo(String id) {
        //修改viewscheme/后面的数字 摇号网址上具体项目的地址
        final String viewUrl = baseUrl + id;

        Map<String, String> formDataMap = new HashMap<String, String>();
        formDataMap.put("__EVENTTARGET", "AspNetPager1");
        try {
            Document doc = Jsoup.connect(viewUrl).userAgent(CommonConstants.USER_AGENT).get();
            setBaseInfo(doc.title(), "价格信息", Jg.class);
            String pageHref = doc.body().select("#AspNetPager1").select("a").last().attr("href");
            String lastPage = pageHref.replace("javascript:__doPostBack('AspNetPager1','", "").replace("')","");
            formDataMap.put("__VIEWSTATE", doc.body().select("#__VIEWSTATE").attr("value"));
            for (int page = 0; page < Integer.valueOf(lastPage); page ++) {
                System.out.println("正在获取第" + (page + 1) +  "页数据");
                try {
                    if (page == 0) {
                        setPageInfo(doc);
                    }
                    else {
                        formDataMap.put("__EVENTARGUMENT", String.valueOf(page + 1));
                        Document pageDoc = Jsoup.connect(viewUrl).userAgent(CommonConstants.USER_AGENT).data(formDataMap).post();
                        setPageInfo(pageDoc);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setPageInfo(Document doc) {
        Elements trs = doc.body().select("table").get(4).select("tr");
        for (int i = 1; i < trs.size() - 1; i ++) {
            Elements tds = trs.get(i).select("div");
            Jg jg = new Jg();
            jg.setLh(tds.get(0).text());
            jg.setFh(Integer.valueOf(tds.get(1).text()));
            jg.setHx(tds.get(2).text());
            jg.setJzmj(Double.valueOf(tds.get(3).text()));
            jg.setGtmj(Double.valueOf(tds.get(4).text()));
            jg.setTnmj(Double.valueOf(tds.get(5).text()));
            jg.setDj(Double.valueOf(tds.get(6).text()));
            jg.setZj(Double.valueOf(tds.get(7).text()));
            jg.setLpsx(tds.get(8).text());
            jg.setZxzk(tds.get(9).text());
            jg.setBz(tds.get(10).text());
            setInfoList(jg);
        }
    }

}
