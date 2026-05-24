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
        ObjectService service = new ObjectService();
        SoapObjectService port = service.getSoapObjectServicePort();

        String studentCode = "B22DCCN393";
        String qCode = "7u07dNqg";

        ProductY product = port.requestProductY(studentCode, qCode);
        System.out.println("Product: price=" + product.getPrice()
                + " | taxRate=" + product.getTaxRate()
                + " | discount=" + product.getDiscount());

        float finalPrice = product.getPrice()
                * (1 + product.getTaxRate() / 100.0f)
                * (1 - product.getDiscount() / 100.0f);
        product.setFinalPrice(finalPrice);
        System.out.println("Gia cuoi = " + String.format(java.util.Locale.US, "%.2f", finalPrice));

        port.submitProductY(studentCode, qCode, product);
        System.out.println("Ket qua da duoc gui.");
    }
}
