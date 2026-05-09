// ============================================
// CODE MAU — Chi dung sau khi da wsimport trong NetBeans
//
// Cac class ObjectService_Service, ObjectService, ProductY duoc NetBeans tu sinh ra
// tu WSDL: http://localhost:2230/ws/ObjectService.wsdl
//
// Cach dung: Tao project Java → New → Web Service Client → nhap WSDL URL
// → NetBeans se tu tao cac class proxy, ban chi viec goi ham nhu duoi day
// ============================================

package vn.ptit.judge.client;

public class Bai9Object {
    public static void main(String[] args) {
        ObjectService_Service service = new ObjectService_Service();
        ObjectService port = service.getObjectServicePort();

        String studentCode = "B21DCCN001";
        String qCode = "7u07dNqg";

        ProductY product = port.requestProductY(studentCode, qCode);
        System.out.println("Product: " + product.getName()
                + " | price=" + product.getPrice()
                + " | taxRate=" + product.getTaxRate()
                + " | discount=" + product.getDiscount());

        double finalPrice = product.getPrice()
                * (1 + product.getTaxRate() / 100)
                * (1 - product.getDiscount() / 100);
        product.setFinalPrice(finalPrice);
        System.out.println("Gia cuoi = " + String.format("%.2f", finalPrice));

        String status = port.submitProductY(studentCode, qCode, product);
        System.out.println("Ket qua tu Server: " + status);
    }
}
