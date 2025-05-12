package PDF;

import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class CrawlingEnter {
    void imageDownload() throws IOException {
        String url = "https://entertain.daum.net/";
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select("div.feature_home");
        Elements elements1 = elements.get(0).select("ul.list_thumb");
        int count = 1;
        for(Element element : elements1){
            for(Element element1 : element.select("li a img")){
                System.out.println(element1.attr("src"));
                if(count != 9){
                    InputStream inputStream = new URL(element1.attr("src")).openStream();
                    Files.copy(inputStream, Path.of("images/"+(count++)+".jpg"));
                }
                else{
                    count++;
                }
            }
        }
    }

    Elements giveElements() throws IOException {
        String url = "https://entertain.daum.net/";
        Document document = Jsoup.connect(url).get();
        //System.out.println(document);
        Elements elements = document.select("div.feature_home");
        //System.out.println(elements.text());
        Elements elements1 = elements.get(0).select("ul.list_thumb");

        return elements1;
    }


}
