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
        String name = "名剑冢";
        //String id = Request.main("https://www.kanman.com/api/getsortlist/?search_key="+name);
        //id=new String(id.getBytes("UTF-8"), "GBK");
        //{"data":[{"comic_id":108243,"comic_name":"����ڣ","last_chapter_name":"��25��1 ��ת","comic_type":"xuanhuan,����|gufeng,�ŷ�|lianai,����","comic_newid":"mjz","comic_author":"�쪶���","renqi":876457319,"update_time":1614047251366,"shoucang":526884,"cover_img":"http://image.yqmh.com/mh/108243_3_4.jpg-noresize.webp"}],"status":0,"message":"ok"}

        //第二部分，通过用户输入的id解析相应漫画html
        //注意代码可能有些紧凑，请区分Chapter_*与Chapter*，L与l
        String id = "17745";
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

/*
第25话1 逆转
window.$definitions={low:"-kmh.low",middle:"-kmh.middle",high:"-kmh.high"},window.pageType="read",
window.comicInfo={comic_id:108243,comic_newid:"mjz",comic_name:"名剑冢",last_chapter_id:"1768034",last_chapter_newid:"di25hua1-1613805800016",
last_chapter_name:"第25话1 逆转"...current_chapter:{chapter_name:"第25话1 逆转",chapter_newid:"di25hua1-1613805800016",chapter_id:1768034,chapter_domain_suffix:"",chapter_domain:"dm300.com",start_num:1,
end_num:23,price:24,chapter_image_addr:"/mh/108243_2_1.jpg",create_date:16140384e5,rule:"/comic/M/名剑冢/第25话1F1_304200/$$.jpg"},
prev_chapter:{chapter_id:1766985,chapter_newid:"di24hua3-1612669180047",chapter_name:"第24话3 转机",price:24,chapter_image_addr:"/mh/108243_2_1.jpg",create_time:"2021-02-19T00:00:00.000Z",topic_copyright:"",chapter_domain:"dm300.com",rule:"/comic/M/名剑冢/第24话3F0_303260/$$.jpg",start_num:1,end_num:28,create_date:16140384e5,chapter_domain_suffix:""},
next_chapter:null,seoTitleTemplate:{default:"{0}{1} {0}漫画{1}",title:"{0}{1}话 {2} {0}漫画{1}话 {2} 看漫画"}}
 */
/*
第23话3 危机
current_chapter:{chapter_name:"第23话3 危机",chapter_newid:"di23hua3-1612669180016",chapter_id:1766982,
chapter_domain_suffix:"",chapter_domain:"dm300.com",start_num:1,end_num:26,price:24,
chapter_image_addr:"/mh/108243_2_1.jpg",create_date:16128288e5,rule:"/comic/M/名剑冢/第23话3F0_303256/$$.jpg"},
prev_chapter:{chapter_id:1767035,chapter_newid:"1yuedacallhuojiangmingdan-1612684720015",chapter_name:"1月打call获奖名单 ",
price:0,chapter_image_addr:"/mh/108243_2_1.jpg",create_time:"2021-02-09T05:40:15.000Z",topic_copyright:"",
chapter_domain:"dm300.com",rule:"/comic/M/名剑冢/1月打call获奖名单F0_303305/$$.jpg",start_num:1,end_num:1,
create_date:16128288e5,chapter_domain_suffix:""},
next_chapter:{chapter_id:1766983,chapter_newid:"di24hua1-1612669180031",chapter_name:"第24话1 转机",price:24,chapter_image_addr:"/mh/108243_2_1.jpg",create_time:"2021-02-12T00:00:00.000Z",topic_copyright:"",chapter_domain:"dm300.com",rule:"/comic/M/名剑冢/第24话1F0_303257/$$.jpg",start_num:1,end_num:27,create_date:16128288e5,chapter_domain_suffix:""},seoTitleTemplate:{default:"{0}{1} {0}漫画{1}",title:"{0}{1}话 {2} {0}漫画{1}话 {2} 看漫画"}}
*/