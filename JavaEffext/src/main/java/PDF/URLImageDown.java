package PDF;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class URLImageDown {
    public static void main(String[] args) throws IOException {

        String url = "https://entertain.daum.net/";
        Document document = Jsoup.connect(url).get();
        //System.out.println(document);
        Elements elements = document.select("div.feature_home");
       // System.out.println(elements.get(0));
        Elements elements1 = elements.get(0).select("ul.list_thumb");
        //System.out.println(elements1);
        //System.out.println(elements1.get(0).select("li div.cont_thumb a").get(0).text());

        for (Element element : elements1) {
            for (Element element1 : element.select("li div.cont_thumb a")){
                System.out.println(element1.text());
            }
            for (Element element1 : element.select("li a img")){
                System.out.println(element1.attr("src"));
            }
        }



        /*int count = 1;
        for(Element element : elements1) {
            for(Element element1 : elements.select("li a img")){
                System.out.println(element1.attr("src"));
                InputStream inputStream = new URL(element1.attr("src"))
                        .openStream();
                Files.copy(inputStream, Path.of("images/" + (count++) + ".jpg"));
            }
        }*/
       /* for(Element element : elements1.get(0).select("li a img")){
            System.out.println(element.attr("src"));
        }*/



        /*Elements elements = document.select("div.Home_image__NAsCq");
        System.out.println(elements.text());*/
       /* for (Element element : elements) {
            System.out.println(element.attr("src"));
        }
*/
        /*
        InputStream inputStream = new URL("https://s.pstatic.net/dthumb.phinf/?src=https://mimgnews.pstatic.net/image/origin/609/2025/05/12/985376.jpg&type=ff353_353&service=enter")
                .openStream();
        Files.copy(inputStream, Path.of("a.jpg"));
         */
    }
}
