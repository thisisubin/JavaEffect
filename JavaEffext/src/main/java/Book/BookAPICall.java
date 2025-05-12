package Book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;

public class BookAPICall {

    StringBuilder apiCall(String book) throws IOException {
        String apiUrl = "https://dapi.kakao.com/v3/search/book?query=";
        String encode = URLEncoder.encode(book, "UTF-8");
        apiUrl += encode;//https://dapi.kakao.com/v3/search/book?query=자바

        URL url = new URL(apiUrl); //
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET"); //GET POST PUT DELETE Path
        con.setRequestProperty("Authorization", "KakaoAK " + "87805af0dfbefac0c387d26cd130c698"); // 내 REST API키로 인증

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        //서버쪽에서 준 걸 input

        String inputLine;
        StringBuilder content = new StringBuilder(); //StringBuilder : 스프링에서 많이 쓰이는 문자열 쌓기?
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content;
    }
}
