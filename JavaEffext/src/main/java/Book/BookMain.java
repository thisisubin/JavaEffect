package Book;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BookMain {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.print("책 : ");
            String book = sc.next();
            if (book.equals("종료")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }
            //PDF 생성을 한번에 원큐
            StringBuilder stringBuilder = new BookAPICall().apiCall(book);
            ArrayList<Book> list = new JsonMake().makeArrayList(stringBuilder);
            new MakePDF().makePDF(list, book);
        }
    }
}
