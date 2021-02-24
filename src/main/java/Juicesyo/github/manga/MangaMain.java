package Juicesyo.github.manga;

import java.net.*;
import java.io.*;
import java.util.logging.Logger;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MangaMain{
    public static void main(String[] args) throws IOException {
/*
        //第一部分，用户输入漫画名获取其id （可与第二部合并）
        String key = "名剑冢";
        //String id = Request.main("https://www.kanman.com/api/getsortlist/?search_key="+key);

        //第二部分，通过用户输入的id解析相应漫画html
        String id = "108243";
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
        //拼凑url，并替换为用户预观看漫画页数
        ChapterRule = "https://mhpic.dm300.com" + ChapterRule.replace("$$","5")+"-kmh.middle.webp";

        System.out.println(ChapterRule);

        //图片链接需将其中中文编码
       //java.net.URLEncoder.encode("名剑冢", "UTF-8");
*/
    }
    public static InputStream inputStream = null;
    public static String MangaId (String key){
        String Id_Json =  Request.main("https://www.kanman.com/api/getsortlist/?search_key="+key);
        String Id_Object = JSONObject.parseObject(Id_Json).getString("data").replace("[","").replace("]","");
        String id = JSONObject.parseObject(Id_Object).get("comic_id").toString();
        return id;
    }
    public static String MangaImage(String MangaName,int Chapter,int page) {
        Document doc = Jsoup.parse(Request.main("https://www.kanman.com/"+MangaId(MangaName)));
        Elements ChapterList = doc.select(".bd").select("ol[id=j_chapter_list]");
        Elements item=ChapterList.select(".item");
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
        String ChapterRule = ChapterData_Json.get("rule").toString();
        String ChapterDomain = ChapterData_Json.get("chapter_domain").toString();
        ChapterRule = "https://mhpic." + ChapterDomain + ChapterRule.replace("$$",String.valueOf(page))+"-kmh.middle.webp";

        return ChapterRule;
    }
    public static InputStream MangaImageInputStream(String MangaName,int Chapter,int page){
        String ChapterRule=MangaImage(MangaName,Chapter,page);
        try {
            ChapterRule = ChapterRule.replace(MangaName,java.net.URLEncoder.encode(MangaName, "UTF-8")).replace("话","%E8%AF%9D").replace("第","%e7%ac%ac");
            URL url=new URL(ChapterRule);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
        }catch (IOException e){
            Logger.getGlobal().info(e.getMessage());
        }
        return inputStream;
    }
    public static String auto_MangaImage(String MangaName,int Chapter,int page){
        String ImageUrl = new String();
        //MangaImage();

        return ImageUrl;
    }
    /*
    public static String next_MangaImage(){
        String ImageUrl=new String();

        return ImageUrl;
    }
     */
}
