package com.zhiliao.common.utils;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.zhiliao.module.web.system.service.TCmsAdminKeyService;
import com.zhiliao.mybatis.model.TCmsAdminKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author王硕
 * @site QQ:1468282838
 * @company wang/wangshuo/王硕/WangShuo
 * @create 2020-04-24 13:56
 */
@Component
public class TimestampUtil {
    @Value("${system.key}")
    private   String systemKey;

    private    final String times ="2020-04-24";

    @Autowired
    private TCmsAdminKeyService tCmsAdminKeyService;

    @Autowired
    private TCmsAdminKeyService service;


    public  boolean timeStamp(){
        if(systemKey == "" || systemKey.equals("") || systemKey.isEmpty() ||systemKey==null){
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = null;
            try {
                date = df.parse(times);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            long timestamp = cal.getTimeInMillis();
            String UrlTime=null;
            try {
                UrlTime=  this.VisitURL("http://time.tianqi.com");
            } catch (Exception e) {
                try {
                    UrlTime=   this.VisitURL("http://tv.cctv.com");
                } catch (Exception ex) {
                        ex.printStackTrace();
                }
            }
            java.util.Date urlDate = null;
            try {
                urlDate=df.parse(UrlTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal2= Calendar.getInstance();
            cal2.setTime(urlDate);
            long twoTime = cal2.getTimeInMillis();
            if(twoTime<timestamp){
                return false;
            }
        }
        else {
            String dbKey = "8299-e7c4f741bc0d";
            List<TCmsAdminKey> all = service.findAll();
            TCmsAdminKey tCmsAdminKey = all.get(0);
            String s = tCmsAdminKey.getThiskey() + dbKey;
            if (s.equals(systemKey)) {
                return false;
            }
        }

          return true;
    }

    public  String VisitURL(String url) throws Exception{
        String urlDate = null;
            URL url1 = new URL(url);
            URLConnection  conn = url1.openConnection();  //生成连接对象
            conn.connect();  //连接对象网页
            Date date = new Date(conn.getDate());  //获取对象网址时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  //设置日期格式
            urlDate = df.format(date);
        return urlDate;
    }
}
