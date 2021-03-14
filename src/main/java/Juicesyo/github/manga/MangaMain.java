package Juicesyo.github.manga;

import java.io.*;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MangaMain{
    public static void main(String[] args) throws IOException {
        /*
        //第一部分，用户输入漫画名获取其id
        String name = "皇帝的独生女";
        //String id = Request.main("https://www.kanman.com/api/getsortlist/?search_key="+name);
        //id=new String(id.getBytes("UTF-8"), "GBK");
        //{"data":[{"comic_id":108243,"comic_name":"����ڣ","last_chapter_name":"��25��1 ��ת","comic_type":"xuanhuan,����|gufeng,�ŷ�|lianai,����","comic_newid":"mjz","comic_author":"�쪶���","renqi":876457319,"update_time":1614047251366,"shoucang":526884,"cover_img":"http://image.yqmh.com/mh/108243_3_4.jpg-noresize.webp"}],"status":0,"message":"ok"}
        String Id_Json =  Request.main("https://www.kanman.com/api/getsortlist/?search_key="+name);
        //String Id_Object = JSONObject.parseObject(Id_Json).getString("data").replace("[","").replace("]","");

        //第二部分，通过用户输入的id解析相应漫画html（可与第一部分合并）
        String id = "100144";
        String html = Request.main("https://www.kanman.com/"+id);

        Document doc = Jsoup.parse(html);
        Elements ChapterList = doc.select(".bd").select("ol[id=j_chapter_list]");
        Elements item=ChapterList.select(".item");
        String Chapter_Link = new String();
        String Chapter_Title = new String();
        for (Element S:item) {
            String ChapterLink = S.select("a").attr("href");
            String ChapterTitle = S.select("a").attr("title");
            //Chapter=Chapter+"\n"+ChapterTitle+" https://www.kanman.com"+ChapterLink;
            //拼凑url，得到正序
            Chapter_Link = Chapter_Link+"\n"+"https://www.kanman.com"+ChapterLink;
            Chapter_Title = Chapter_Title+"\n"+ChapterTitle;
        }
        Chapter_Link = Chapter_Link.trim();
        Chapter_Title = Chapter_Title.trim();
        //System.out.println(new String(Chapter.getBytes("UTF-8"), "GBK"));
        String[] ChapterSpilt = Chapter_Link.split("\n");
        String ChapterUrl = ChapterSpilt[5]; //这里假设赋值5，相当于用户预观看章数为4，按数组规则从0开始

        ChapterSpilt = Chapter_Title.split("\n");
        String ChapterTitle = ChapterSpilt[5]; //同上
        //System.out.println(ChapterUrl+"\n\n"+ChapterTitle);

        //第三部分，请求从第二部分得到的章节url(ChapterUrl)并解析html
        //Document ChapterHtml = Jsoup.connect(ChapterUrl).get();
        Document ChapterDoc = Jsoup.parse(Request.main(ChapterUrl));
        String ChapterData = ChapterDoc.select("body").select("script").toArray()[1].toString().replace("<script>","").replace("</script>","");
        ChapterData = ChapterData.subSequence(ChapterData.indexOf("current_chapter:{"),ChapterData.indexOf("}",ChapterData.indexOf("current_chapter"))).toString()+"}";
        ChapterData = ChapterData.replace("current_chapter:","");
        JSONObject ChapterData_Json = JSONObject.parseObject(ChapterData);
        //String ChapterNewid = ChapterData_Json.get("chapter_newid").toString();
        String ChapterRule = ChapterData_Json.get("rule").toString();
        String Domain = ChapterData_Json.get("chapter_domain").toString();
        //拼凑url，并替换为用户预观看漫画页数
        ChapterRule = "https://mhpic."+ Domain + ChapterRule.replace("$$","5")+"-kmh.middle.webp";

        //图片链接需将其中中文编码
        //java.net.URLEncoder.encode("名剑冢", "UTF-8");
        System.out.println(encode(ChapterRule));

         */
    }
    public static String MangaName;
    public static String MangaId_Json;
    public static String MangaId (String key){ //同时提供id对应的漫画名
        MangaId_Json =  Request.main("https://www.kanman.com/api/getsortlist/?search_key="+key);
        //暴力获取
        String id = MangaId_Json.substring(MangaId_Json.indexOf("\"comic_id\":"),MangaId_Json.indexOf(",")).replace("\"comic_id\":","");

        //这里有个坑，部分漫画在匹配到多个漫画时，最匹配的漫画名不在最前
        //String Id_Object = JSONObject.parseObject(Id_Json).getString("data").replace("[","").replace("]","");
        //String id = JSONObject.parseObject(Id_Object).getString("comic_id");

        MangaName = MangaId_Json.substring(MangaId_Json.indexOf(id+",\"comic_name\":\""),MangaId_Json.indexOf("\",")).replace(id+",\"comic_name\":\"","");
        return id;
    }

    //主要函数
    public static String MangaImage(String MangaName,int Chapter,int Page) throws IOException {
        String key;
        if (MySetting.INSTANCE.getId()=="true" || MySetting.INSTANCE.getId()=="True") {
            key = MangaId(MangaName);
        }else {
            key = MangaName;
        }
        Document doc = Jsoup.parse(Request.main("https://www.kanman.com/"+key));
        Elements ChapterList = doc.select(".bd").select("ol[id=j_chapter_list]");
        Elements item = ChapterList.select(".item");
        String Chapter_Link = new String();
        for (Element S:item) {
            String ChapterLink = S.select("a").attr("href");
            Chapter_Link = Chapter_Link+"\n"+"https://www.kanman.com"+ChapterLink;
        }
        Chapter_Link = Chapter_Link.trim();
        String[] ChapterSpilt = Chapter_Link.split("\n");
        String ChapterUrl = ChapterSpilt[Chapter-1];

        Document ChapterDoc = Jsoup.parse(Request.main(ChapterUrl));
        String ChapterData = ChapterDoc.select("body").select("script").toArray()[1].toString().replace("<script>","").replace("</script>","");
        ChapterData = ChapterData.subSequence(ChapterData.indexOf("current_chapter:{"),ChapterData.indexOf("}",ChapterData.indexOf("current_chapter"))).toString()+"}";
        ChapterData = ChapterData.replace("current_chapter:","");
        JSONObject ChapterData_Json = JSONObject.parseObject(ChapterData);
        String Rule = ChapterData_Json.get("rule").toString();
        String Domain = ChapterData_Json.get("chapter_domain").toString();
        String ChapterRule = "https://mhpic." + Domain + Rule.replace("$$",String.valueOf(Page))+"-kmh.middle.webp";

        ChapterRule = encode(ChapterRule);
        if (Request.main(ChapterRule).split("\n")[1].equals("<Error>")){ //判断图片链接是否有效，并自动跳到下一章
            Page = 1;
            ImageUpload.Page = 0;
            Chapter = Chapter+1;
            ImageUpload.Chapter = Chapter;
            ChapterRule = MangaImage(MangaName,Chapter,Page);
        }
        return ChapterRule;
    }
    //中文编码
    public static String encode(String ChapterRule) throws IOException {
        String MangaName = ChapterRule.split("/")[5];
        String MangaNameEncode = java.net.URLEncoder.encode(MangaName, "UTF-8");
        String MangaChapter = ChapterRule.split("/")[6];
        String MangaChapterEncode = java.net.URLEncoder.encode(MangaChapter, "UTF-8");
        ChapterRule = ChapterRule.replace(MangaName,MangaNameEncode).replace(MangaChapter,MangaChapterEncode);
        return ChapterRule;
    }
    /*
    public static String auto_MangaImage(){
        String ImageUrl=new String();

        return ImageUrl;
    }
     */
}
