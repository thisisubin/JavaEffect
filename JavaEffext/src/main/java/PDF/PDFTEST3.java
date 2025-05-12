package PDF;
//1. 이미지 다운로드 : ※ 이미지 파일을 쉽게 가지고 올 수 있게 만든다.
//2. 타이틀을 받아와서
//3. 타이틀과 이미지를 넣고 PDF를 만든다.

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
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;

public class PDFTEST3 {
    public static void main(String[] args) throws IOException {
        CrawlingEnter crawlingEnter = new CrawlingEnter();
        crawlingEnter.imageDownload();

        Elements elements = crawlingEnter.giveElements();

        //1. 엑셀
        int count = 1;
        //XSSFWorkBook -> Sheet -> Row -> Cell
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Sheet생성
        Sheet sheet = workbook.createSheet("엔터 뉴스");
        //헤더 생성
        Row headRow = sheet.createRow(0); //0번째 행 생성
        headRow.createCell(0).setCellValue("번호"); //0번째 행 0열 -> 번호
        headRow.createCell(1).setCellValue("이미지"); //0번째 행 1열 -> 이미지
        headRow.createCell(2).setCellValue("타이틀"); //0번째 행 2열 -> 타이틀
        headRow.createCell(3).setCellValue("하이퍼링크"); //0번째 행 3열 -> 하이퍼링크

        //타이틀 앞까지 Elements를 추출한 것을 대입해서 반복
        for (Element element : elements) {
            //Elements -> li -> div class : count_thumb -> a 객체츨 추출 Element
            //Element 있는대로 반복하면서 데이터를 하나씩 빼옴
            for (Element element1 : element.select("li div.cont_thumb a")) {
                Row r = sheet.createRow(count); //행 추가
                r.createCell(0).setCellValue(count); //0번째 열에 count 값 추가
                FileInputStream is; //FileInputStream 변수 선언
                if(count < 9) { //count가 9보다 작으면 실행
                    is = new FileInputStream("images/" + count+ ".jpg"); //images/count.jpg 이미지파일을 객체 만듦
                }
                else { //그 이후부터 실행
                    is = new FileInputStream("images/" + (count+1)+ ".jpg"); //images/count+1.jpg 이미지파일을 객체 만듦
                }
                //FileInputStream 객체를 ByteArray로 값을 변경
                byte[] bytes = IOUtils.toByteArray(is);
                //byte 배열값과 이미지 확장자명을 세팅
                int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                //FileInputStream 객체 종료
                is.close();
                //엑셀에 이미지 삽입을 위한 객체 생성
                XSSFCreationHelper helper = workbook.getCreationHelper();
                Drawing drawing = sheet.createDrawingPatriarch();

                 /*
                | 매개변수   | 값              | 설명                                    |
                | ------ | -------------- | ------------------------------------- |
                | `dx1`  | `0`            | 시작 셀의 왼쪽에서 **0**만큼 오른쪽으로 오프셋 (EMU 단위) |
                | `dy1`  | `0`            | 시작 셀의 위쪽에서 **0**만큼 아래로 오프셋 (EMU 단위)   |
                | `dx2`  | `1023`         | 종료 셀의 오른쪽 끝까지 이동 (1023이 최대 값)         |
                | `dy2`  | `255`          | 종료 셀의 아래쪽 끝까지 이동 (255가 최대 값)          |
                | `col1` | `1`            | 시작 셀의 열 번호 (엑셀 상으로는 **B 열**)          |
                | `row1` | `count`        | 시작할 행 번호 (`count` 값에 따라 다름)        |
                | `col2` | `2`            | 종료할 셀의 열 번호 (엑셀 상으로는 **C 열**)         |
                | `row2` | `count + 1`    | 종료할 행 번호 (`count + 1`)             |
                 */

                ClientAnchor anchor = drawing.createAnchor(0, 0, 1023, 255, 1, count, 2, count + 1);
                Picture pic = drawing.createPicture(anchor, pictureIdx);


                r.createCell(2).setCellValue(element1.text());
                System.out.println(element1.text());
                r.createCell(3).setCellValue(element1.attr("href"));
                System.out.println(element1.attr("href"));
                count++;
            }
        }

        String fileName = "enter.xlsx";
        //FileOutputStream 객체 생성
        FileOutputStream outputStream = new FileOutputStream(new File(fileName));
        //workbook과 FileOutputStream 연결
        workbook.write(outputStream);
        //완성되면 workbook 닫기
        workbook.close();


        //2. PDF
        //PdfWriter -> PdfDocument -> Document -> Table -> Cell(Paragraph) -> addCell
        PdfWriter writer = new PdfWriter(new FileOutputStream("enter.pdf"));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        PdfFont headerFont = PdfFontFactory.createFont("NanumYaGeunHaNeunGimJuIm.ttf",
                PdfEncodings.IDENTITY_H);

        PdfFont bodyFont = PdfFontFactory.createFont("NanumYaGeunHaNeunGimJuIm.ttf",
                PdfEncodings.IDENTITY_H);

        //실수 배열 생성-> 테[이블 생성
        float[] columnWidths = {1, 2, 2, 2}; //컬럼 넓이
        Table table = new Table(UnitValue.createPointArray(columnWidths)); //테이블 넓이 위에 배열 참조
        table.setWidth(UnitValue.createPercentValue(100));
        Cell hCell1 = new Cell().add(new Paragraph("순번")).setFont(headerFont);
        Cell hCell2 = new Cell().add(new Paragraph("이미지")).setFont(headerFont);
        Cell hCell3 = new Cell().add(new Paragraph("타이틀")).setFont(headerFont);
        Cell hCell4 = new Cell().add(new Paragraph("링크")).setFont(headerFont);

        table.addHeaderCell(hCell1);
        table.addHeaderCell(hCell2);
        table.addHeaderCell(hCell3);
        table.addHeaderCell(hCell4);

        count = 1;
        //타이틀 앞까지 Elements를 추출한 것을 대입해서 반복
        for (Element element : elements) {
            //Elements -> li -> div class : count_thumb -> a 객체츨 추출 Element
            //Element 있는대로 반복하면서 데이터를 하나씩 빼옴
            for (Element element1 : element.select("li div.cont_thumb a")) {
                //번호 테이블 추가
                Cell rowNumCell = new Cell().add(new Paragraph(String.valueOf(count))).setFont(bodyFont);
                table.addCell(rowNumCell);
                //이미지
                ImageData imageData;
                if(count < 9) { //count가 9보다 작으면 실행
                    imageData = ImageDataFactory.create(new File("images/" + count + ".jpg").toURI().toURL());

                }
                else {
                    imageData = ImageDataFactory.create(new File("images/" + (count+1) + ".jpg").toURI().toURL());

                }
                Image img = new Image(imageData);
                Cell imageCell = new Cell().add(img.setAutoScale(true));
                table.addCell(imageCell);
                //타이틀
                Cell titleCell = new Cell().add(new Paragraph(element1.text())).setFont(bodyFont);
                table.addCell(titleCell);
                //링크
                Cell urlCell = new Cell().add(new Paragraph(element1.attr("href"))).setFont(bodyFont);
                table.addCell(urlCell);

                count++;

            }
        }
        document.add(table);
        document.close();
    }
}
