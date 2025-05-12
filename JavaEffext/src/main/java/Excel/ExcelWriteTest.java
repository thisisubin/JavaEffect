package Excel;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ExcelWriteTest {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("이름 : ");
        String name = sc.next();
        System.out.print("나이 : ");
        int age = sc.nextInt();
        System.out.print("전화번호 : ");
        String tel = sc.next();

        //워크북 객체 생성
        XSSFWorkbook workbook = new XSSFWorkbook();
        //sheet 생성
        XSSFSheet sheet = workbook.createSheet("멤버 정보");
        //Row 객체 생성
        Row headRow = sheet.createRow(0);
        //Cell 객체 생성
        headRow.createCell(0).setCellValue("이름");
        headRow.createCell(1).setCellValue("나이");
        headRow.createCell(2).setCellValue("전화번호");

        //Row 객체 생성
        Row r = sheet.createRow(1);
        //Cell 객체 생성
        headRow.createCell(0).setCellValue(name);
        headRow.createCell(1).setCellValue(age);
        headRow.createCell(2).setCellValue(tel);

        //File 생성하기 위해서 File 객체 생성
        //-> FileOuputStream으로 엑셀 쓸 수 있게 함
        FileOutputStream outputStream = new FileOutputStream(new File("member.xlsx"));
        //FileOutputStream 객체를 workbook에 넣어서 write
        workbook.write(outputStream);
        //workbook종료
        workbook.close();
        System.out.println("엑셀 완성!");
    }
}
