package com.cn.xbingo.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xbingo.constant.CommonConstants;
import com.cn.xbingo.handler.AbstractHandler;
import com.cn.xbingo.model.Yh;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class YaoHaoHandler extends AbstractHandler<Yh> {
    public String baseUrl = "http://60.173.254.126:8888";

    public String rsaUrl = baseUrl + "/details/getrsa/";
    public String detailUrl = baseUrl + "/details/house/";

    @Override
    public void getInfo(String id) {
        String viewUrl = baseUrl + "/detail/viewscheme/" + id;
        try {
            Document doc = Jsoup.connect(viewUrl).userAgent(CommonConstants.USER_AGENT).get();
            Elements as = doc.body().select("div.beihui a");

            String fileName = doc.body().select("dd").get(1).text().split("：")[1] + doc.body().select("dd").get(4).text().split("：")[1];
            System.out.println(fileName);
            setBaseInfo(fileName, "房屋信息", Yh.class);

            for (int k = 0; k < as.size(); k++) {
                try {
                    //延时是因为摇号公式网址对请求做了限制，速度太快ip会直接被禁短时间内无法访问
                    Thread.sleep(CommonConstants.SLEEP_TIME);
                    Document liDocument = Jsoup.connect(baseUrl + as.get(k).attr("href")).userAgent(CommonConstants.USER_AGENT).get();
                    Elements liElements = liDocument.body().select("td");

                    for (int i = 0; i < liElements.size(); i++) {
                        try {
                            String s = liElements.get(i).attr("onclick");
                            if (!"s('0')".equals(s) && s.contains("'") && s.split("'").length > 1) {

                                String scalerStr = nScaler(s.split("'")[1]);
                                Thread.sleep(CommonConstants.SLEEP_TIME);
                                Document rsa = Jsoup.connect(rsaUrl + scalerStr).userAgent(CommonConstants.USER_AGENT)
                                        .ignoreContentType(true).get();
                                String detailStr = JSON.parseObject(rsa.body().text()).getString("id");

                                Document detail = Jsoup.connect(detailUrl + detailStr).userAgent(CommonConstants.USER_AGENT)
                                        .ignoreContentType(true).get();
                                JSONObject object = JSON.parseObject(detail.body().text());
                                System.out.println(object);
                                if ("0".equals(object.getJSONObject("data").getString("sellflag"))) {
                                    setInfoList(JSON.parseObject(object.getJSONObject("data").toJSONString(), Yh.class));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String nScaler(String s) {
        StringBuilder b = new StringBuilder();
        for(int i= 0; i < s.length(); i ++) {
            switch (s.charAt(i)) {
                case '0':
                    b.append("0");
                    break;
                case '1':
                    b.append("2");
                    break;
                case '2':
                    b.append("5");
                    break;
                case '3':
                    b.append("8");
                    break;
                case '4':
                    b.append("6");
                    break;
                case '5':
                    b.append("1");
                    break;
                case '6':
                    b.append("3");
                    break;
                case '7':
                    b.append("4");
                    break;
                case '8':
                    b.append("9");
                    break;
                case '9':
                    b.append("7");
                    break;
            }
        }
        return b.toString();
    }


}
