package Juicesyo.github.manga;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import java.io.IOException;
import java.io.InputStream;

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
            //e.printStackTrace();
        }
    return content;
    }
    public static InputStream inputStream;
    public static InputStream inputStream(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        inputStream = response.body().byteStream();
        return inputStream;
    }
}
