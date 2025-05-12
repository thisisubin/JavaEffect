package KaKaoMap;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.Scanner;

public class KakaoMapMain {
    public static void main(String[] args) throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);
        System.out.print("주소를 입력하세요 : ");
        String address = sc.next();

        double[] coordinates = KakaoMapAPI.getAddressCoordinate(address);

        if (coordinates != null) {
            System.out.println("주소 : " + address);
            System.out.println("위도 : " + coordinates[0]);
            System.out.println("경도 : " + coordinates[1]);
        }
        else {
            System.out.println("주소를 찾을 수 없습니다.");
        }
    }
}
