package Juicesyo.github.manga;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.utils.ExternalResource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ImageUpload implements Runnable {
    public static Contact sender;
    public static String MangaName;
    public static int Chapter;
    public static int Page;
    public static ArrayList<String> ImageIdS = new ArrayList<>();
    private Lock lock = new ReentrantLock();
    @Override
    public void run() {
        lock.lock();
        for (int i = 0; i <= 3; i++) {
            try {
                ImageIdS.add(sender.uploadImage(ExternalResource.create(Request.inputStream(MangaMain.MangaImage(MangaName, Chapter, Page)))).getImageId());
                Page = Page+1;
                //int a = 1;
                //ImageIdS.add(MangaMain.MangaImage("风起苍岚", 1, a+=1));
                //System.out.println(ImageIdS+" "+ImageIdS.size());
            } catch (IOException e) {
                sender.sendMessage("上传图片出错/(ㄒoㄒ)/~~");
            }
        }
        lock.unlock();
    }

    public static void main(String[] args) {
        ImageUpload thread=new ImageUpload();

        Thread upload = new Thread(thread);
        upload.start();

        Thread upload2 = new Thread(thread);
        upload2.start();

        Thread upload3 = new Thread(thread);
        upload3.start();
    }
}