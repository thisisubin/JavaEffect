package Book;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class JsonMake {
    ArrayList<Book> makeArrayList(StringBuilder content) {
        //GSon으로 JSON -> Java
        Gson gson = new Gson();

        //1. 문자열 -> JSON으로 바꾼다.
        JsonObject jsonObject = gson.fromJson(content.toString(), JsonObject.class);
        //System.out.println(jsonObject);

        //2. JSON-> JSonArray
        // content.toString()에 API 응답 문자열이 들어 있다
        JsonArray jsonArray = jsonObject.getAsJsonArray("documents");
        //System.out.println(jsonArray);

        //3. JSonArray-> ArrayList
        ArrayList<Book> bookList = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jo = jsonElement.getAsJsonObject();
            Book b = new Book();
            if (jo.get("title") != null) {
                b.setTitle(jo.get("title").getAsString());
            }
            if (jo.get("authors") != null) {
                JsonArray jsonArray1 = jo.get("authors").getAsJsonArray();
                String result = "";
                for (JsonElement jsonElement1 : jsonArray1) {
                    result += jsonElement1.getAsString() + " ";
                }
                b.setAuthor(result);
            }
            if (jo.get("publisher") != null) {
                b.setPublisher(jo.get("publisher").getAsString());
            }
            if (jo.get("thumbnail") != null) {
                b.setThumbnail(jo.get("thumbnail").getAsString());
            }
            if (jo.get("sale_price") != null) {
                b.setPrice(jo.get("sale_price").getAsInt());
            }
            bookList.add(b);
        }
        for (Book b : bookList) {
            System.out.println(b);
        }
        return bookList;
    }
}
