// ============================================
// CODE MAU — Chi dung sau khi da wsimport trong NetBeans
//
// Cac class DataService_Service, DataService duoc NetBeans tu sinh ra
// tu WSDL: http://localhost:2230/ws/DataService.wsdl
//
// Cach dung: Tao project Java → New → Web Service Client → nhap WSDL URL
// → NetBeans se tu tao cac class proxy, ban chi viec goi ham nhu duoi day
// ============================================

package vn.ptit.judge.client;

import java.util.List;

public class Bai7Sum {
    public static void main(String[] args) {
        DataService service = new DataService();
        SoapDataService port = service.getSoapDataServicePort();

        String studentCode = "B22DCCN393";
        String qCode = "yyhHZUpt";

        List<Integer> dataList = port.getData(studentCode, qCode);
        System.out.println("Data nhan duoc: " + dataList);

        int sum = 0;
        for (int s : dataList) {
            sum += s;
        }
        System.out.println("Tong = " + sum);

        port.submitDataInt(studentCode, qCode, sum);
        System.out.println("Ket qua da duoc gui.");
    }
}
