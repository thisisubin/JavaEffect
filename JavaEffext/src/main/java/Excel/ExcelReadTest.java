package Excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil; // ← 요거!
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelReadTest {
    public static void main(String[] args) {
        try {
            //파일을 받기 위한 FileInputStream 객체 생성
            FileInputStream file = new FileInputStream(new File("example.xlsx"));
            //WorkbookFactory 생성 -> FileInputStream 객체를 받아서 생성 -> Workbook
            Workbook workbook = WorkbookFactory.create(file);
            //workbook-> 0번째 sheet의 객체를 생성해서 sheet 변수에 대입
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case NUMERIC:  //cell의 값이 날짜면 실행
                            if (DateUtil.isCellDateFormatted(cell)) {
                                Date dateValue = cell.getDateCellValue();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String formattedDate = dateFormat.format(dateValue);
                                System.out.print(formattedDate + "\t");
                            } else {
                                double numericValue = cell.getNumericCellValue();
                                if (numericValue == Math.floor(numericValue)) {
                                    int intValue = (int) numericValue;
                                    System.out.print(intValue + "\t");
                                } else {
                                    System.out.print(numericValue + "\t");
                                }
                            }
                            break;
                        case STRING: //문자열
                            String stringValue = cell.getStringCellValue();
                            System.out.print(stringValue + "\t");
                            break;
                        case BOOLEAN: //불리언
                            boolean booleanValue = cell.getBooleanCellValue();
                            System.out.print(booleanValue + "\t");
                            break;
                        case FORMULA: //엑셀 공식이 있는 경우
                            String formulaValue = cell.getCellFormula();
                            System.out.print(formulaValue + "\t");
                            break;
                        case BLANK: //공백인 경우
                            System.out.print("\t");
                            break;
                        default:
                            System.out.print("\t");
                            break;
                    }
                }
                System.out.println(); // 한 줄 출력 끝
            }
            file.close();
            System.out.println("엑셀에서 데이터 읽어 오기 성공");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
