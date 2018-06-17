package me.tsblock.Blinky.HTTPRequest;

import com.google.gson.Gson;
import me.tsblock.Blinky.HTTPRequest.APIJSONFormat.EightBall;
import me.tsblock.Blinky.HTTPRequest.APIJSONFormat.RandomCat;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;

public class GetBody {
    static OkHttpClient httpClient = new OkHttpClient();

    public static String run(String url) throws IOException {
        Request request =  new Request.Builder()
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String getCatURL() {
        String result = null;
        try {
            result = GetBody.run("http://aws.random.cat/meow");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        RandomCat cat = gson.fromJson(result, RandomCat.class);
        return cat.getFile();
    }

    public static String get8BallQuestion(String question) {
        String result = null;
        try {
            result = GetBody.run("https://8ball.delegator.com/magic/JSON/" + URLEncoder.encode(question, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        EightBall eightBall = gson.fromJson(result, EightBall.class);
        return eightBall.getMagic().getAnswer();
    }
}
