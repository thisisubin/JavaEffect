package Book;

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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MakePDF {
    void makePDF(ArrayList<Book> bookList, String book) throws IOException {
        //PDF
        //PdfWriter -> PdfDocument -> Document -> Table -> Cell(Paragraph) -> addCell
        PdfWriter writer = new PdfWriter(new FileOutputStream(book+".pdf"));
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
