// ============================================
// CODE MAU — Chi dung sau khi da wsimport trong NetBeans
//
// Cac class CharacterService_Service, CharacterService duoc NetBeans tu sinh ra
// tu WSDL: http://localhost:2230/ws/CharacterService.wsdl
//
// Cach dung: Tao project Java → New → Web Service Client → nhap WSDL URL
// → NetBeans se tu tao cac class proxy, ban chi viec goi ham nhu duoi day
// ============================================

package vn.ptit.judge.client;

public class Bai8Reverse {
    public static void main(String[] args) {
        CharacterService_Service service = new CharacterService_Service();
        CharacterService port = service.getCharacterServicePort();

        String studentCode = "B21DCCN001";
        String qCode = "qvCGMEhW";

        String data = port.requestString(studentCode, qCode);
        System.out.println("Chuoi nhan duoc: " + data);

        String reversed = new StringBuilder(data).reverse().toString();
        System.out.println("Chuoi dao nguoc: " + reversed);

        String status = port.submitString(studentCode, qCode, reversed);
        System.out.println("Ket qua tu Server: " + status);
    }
}
