package Book;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.jsoup.nodes.Element;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BookTest {
    public static void main(String[] args) throws IOException {
        String apiUrl = "https://dapi.kakao.com/v3/search/book?query=";
        String encode = URLEncoder.encode("자바", "UTF-8");
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
        System.out.println(content.toString());

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

        //PDF
        //PdfWriter -> PdfDocument -> Document -> Table -> Cell(Paragraph) -> addCell
        PdfWriter writer = new PdfWriter(new FileOutputStream("자바.pdf"));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        PdfFont headerFont = PdfFontFactory.createFont("NanumYaGeunHaNeunGimJuIm.ttf",
                PdfEncodings.IDENTITY_H);

        PdfFont bodyFont = PdfFontFactory.createFont("NanumYaGeunHaNeunGimJuIm.ttf",
                PdfEncodings.IDENTITY_H);

        //실수 배열 생성-> 테이블 생성
        float[] columnWidths = {2, 2, 2, 2, 2}; //컬럼 넓이
        Table table = new Table(UnitValue.createPointArray(columnWidths)); //테이블 넓이 위에 배열 참조
        table.setWidth(UnitValue.createPercentValue(100));

        //헤더
        Cell hCell1 = new Cell().add(new Paragraph("제목")).setFont(headerFont);
        Cell hCell2 = new Cell().add(new Paragraph("저자")).setFont(headerFont);
        Cell hCell3 = new Cell().add(new Paragraph("출판사")).setFont(headerFont);
        Cell hCell4 = new Cell().add(new Paragraph("가격")).setFont(headerFont);
        Cell hCell5 = new Cell().add(new Paragraph("썸네일")).setFont(headerFont);

        table.addHeaderCell(hCell1);
        table.addHeaderCell(hCell2);
        table.addHeaderCell(hCell3);
        table.addHeaderCell(hCell4);
        table.addHeaderCell(hCell5);

        //바디
        for (Book b : bookList) {
            Cell titleCell = new Cell().add(new Paragraph(b.getTitle())).setFont(bodyFont);
            table.addCell(titleCell);
            Cell authorsCell = new Cell().add(new Paragraph(b.getAuthor())).setFont(bodyFont);
            table.addCell(authorsCell);
            Cell publisherCell = new Cell().add(new Paragraph(b.getPublisher())).setFont(bodyFont);
            table.addCell(publisherCell);
            Cell priceCell = new Cell().add(new Paragraph(b.getPrice().toString())).setFont(bodyFont);
            table.addCell(priceCell);

            //이미지 썸네일 주소 -> ImageData로 변경
            ImageData imageData = ImageDataFactory.create(new URL(b.getThumbnail()));
            //ImageData => Image 변경
            Image image = new Image(imageData);
            //Image -> Cell
            Cell imageCell = new Cell().add(image.setAutoScale(true));
            table.addCell(imageCell);
        }
        document.add(table);
        document.close();

    }
}