package me.tsblock.Blinky.HTTPRequest;

import com.google.gson.Gson;
import me.tsblock.Blinky.HTTPRequest.APIJSONFormat.EightBall;
import me.tsblock.Blinky.HTTPRequest.APIJSONFormat.RandomCat;
import me.tsblock.Blinky.HTTPRequest.APIJSONFormat.ksoft.reddit;
import me.tsblock.Blinky.Settings.Settings;
import me.tsblock.Blinky.Settings.SettingsManager;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class GetBody {
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .hostnameVerifier((s, sslSession) -> true).build(); //sorry but im forced to do this
    private static Settings settings = SettingsManager.getInstance().getSettings();

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

    public static String getDadJokes() throws IOException {
        String result = null;
        Request request = new Request.Builder()
                .url("https://icanhazdadjoke.com/")
                .header("Accept", "text/plain")
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    public static reddit getMeme() throws IOException {
        //TODO: fix it
        Response response = httpClient.newCall(new Request.Builder()
                .url("https://api.ksoft.si/meme/random-meme")
                .header("Authorization", "Token " + settings.getAPI().getKsoftsi())
                .addHeader("Accept", "application/json")
                .build()).execute();
        Gson gson = new Gson();
        String result = response.body().string();
        reddit reddit = gson.fromJson(result, reddit.class);
        return reddit;
    }

    public static JSONObject getDefine(String word) throws IOException {
        String result = GetBody.run("http://api.urbandictionary.com/v0/define?term=" + URLEncoder.encode(word, "UTF-8"));
        return new JSONObject(result);
    }
}
