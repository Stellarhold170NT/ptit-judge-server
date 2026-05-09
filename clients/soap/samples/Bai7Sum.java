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
        DataService_Service service = new DataService_Service();
        DataService port = service.getDataServicePort();

        String studentCode = "B21DCCN001";
        String qCode = "yyhHZUpt";

        List<String> dataList = port.getData(studentCode, qCode);
        System.out.println("Data nhan duoc: " + dataList);

        int sum = 0;
        for (String s : dataList) {
            sum += Integer.parseInt(s);
        }
        System.out.println("Tong = " + sum);

        String status = port.submitDataInt(studentCode, qCode, sum);
        System.out.println("Ket qua tu Server: " + status);
    }
}
