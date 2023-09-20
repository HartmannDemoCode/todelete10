package dk.cphbusiness;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class ApiReader {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static void main(String[] args) {
        String url = "https://api.chucknorris.io/jokes/random";
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String res = response.body().string();
            JokeDTO jokeDTO = gson.fromJson(res, JokeDTO.class);
            System.out.println(jokeDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @ToString
    @NoArgsConstructor
    private static class JokeDTO {
        private String value;
        private String url;
        private String icon_url;
        private String id;
        private String created_at;
        private String updated_at;
    }
}
