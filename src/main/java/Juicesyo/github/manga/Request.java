package Juicesyo.github.manga;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import java.io.IOException;
import java.util.logging.Logger;

public class Request {
    public static String main(String url){
        String content=new String();
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            content = response.body().string();
        } catch (IOException e) {
            Logger.getGlobal().info(e.getMessage());
        }
    return content;
    }
}
