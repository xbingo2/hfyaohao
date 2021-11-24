package com.cn.xbingo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xbingo.model.Yh;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class YaoHao {


    public static void main(String[] args) {

        System.out.print("请输入项目url：");
        Scanner sc = new Scanner(System.in);
        String id = sc.nextLine();
        if (StringUtils.isBlank(id)) {
            return;
        }

        long sleepTime = 2000;
        final String baseUrl = "http://60.173.254.126:8888";
        final String rsaUrl = baseUrl + "/details/getrsa/";
        final String detailUrl = baseUrl + "/details/house/";
        final String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0";

        //修改viewscheme/后面的数字 摇号网址上具体项目的地址
        final String viewUrl = baseUrl + "/detail/viewscheme/" + id;

        try {
            List<Yh> yhList = new ArrayList<Yh>();

            Document doc = Jsoup.connect(viewUrl).userAgent(userAgent).get();
            Elements as = doc.body().select("div.beihui a");

            String fileName = doc.body().select("dd").get(1).text().split("：")[1] + doc.body().select("dd").get(4).text().split("：")[1] ;
            System.out.println(fileName);
            for (int k = 0; k < as.size(); k ++) {
                try {
                    //延时是因为摇号公式网址对请求做了限制，速度太快ip会直接被禁短时间内无法访问
                    Thread.sleep(sleepTime);
                    Document liDocument = Jsoup.connect(baseUrl + as.get(k).attr("href")).userAgent(userAgent).get();
                    Elements liElements = liDocument.body().select("td");

                    for (int i = 0; i < liElements.size(); i ++) {
                        try {
                            String s = liElements.get(i).attr("onclick");
                            if(!"s('0')".equals(s) && s.contains("'") && s.split("'").length > 1) {

                                String scalerStr = nScaler(s.split("'")[1]);
                                Thread.sleep(sleepTime);
                                Document rsa = Jsoup.connect(rsaUrl + scalerStr).userAgent(userAgent)
                                        .ignoreContentType(true).get();
                                String detailStr = JSON.parseObject(rsa.body().text()).getString("id");

                                Document detail = Jsoup.connect(detailUrl + detailStr).userAgent(userAgent)
                                        .ignoreContentType(true).get();
                                JSONObject object = JSON.parseObject(detail.body().text());
                                System.out.println(object);
                                if ("0".equals(object.getJSONObject("data").getString("sellflag"))) {
                                    yhList.add(JSON.parseObject(object.getJSONObject("data").toJSONString(), Yh.class));
                                }
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                            continue;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            insetExcel(fileName, yhList);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void insetExcel(String fileName, List<Yh> yhList) {
        try {
            String pathName = fileName + System.currentTimeMillis() + ".xlsx";
            EasyExcel.write(pathName, Yh.class).sheet("房屋信息").doWrite(yhList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String nScaler(String s) {
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
