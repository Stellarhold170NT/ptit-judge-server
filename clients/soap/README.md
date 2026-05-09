# SOAP Client — Hướng dẫn Apache NetBeans + wsimport

> Dành cho sinh viên PTIT dùng **Apache NetBeans** (hoặc bất kỳ IDE nào có JDK + wsimport)

## Yêu cầu

- Apache NetBeans (phiên bản 12+ hoặc 8.2)
- JDK 17+ (có sẵn `wsimport` trong `JDK/bin/`)
- Server Java Judge đang chạy tại `http://localhost:2230`

## Cách 1: Tự động bằng NetBeans Web Service Client (Khuyên dùng)

### Bước 1: Tạo Project Java mới

```
File → New Project → Java with Ant → Java Application
Tên project: Bai7Sum (hoặc Bai8Reverse, Bai9Object)
```

### Bước 2: Thêm Web Service Client

```
Chuột phải project → New → Web Service Client...
```

- **WSDL URL**: nhập địa chỉ WSDL tương ứng:
  - Bài 7 (tính tổng): `http://localhost:2230/ws/DataService.wsdl`
  - Bài 8 (đảo chuỗi): `http://localhost:2230/ws/CharacterService.wsdl`
  - Bài 9 (giá sản phẩm): `http://localhost:2230/ws/ObjectService.wsdl`
- **Package**: `vn.ptit.judge.client`
- Bấm **Finish**

NetBeans sẽ tự động chạy `wsimport` và tạo ra các class Java trong thư mục `generated-sources/jax-ws/`. Bạn sẽ có sẵn các class như:
- `DataService_Service`, `DataService` (cho Bài 7)
- `CharacterService_Service`, `CharacterService` (cho Bài 8)
- `ObjectService_Service`, `ObjectService` (cho Bài 9)

### Bước 3: Viết code Main

Tạo class `Main.java` trong package `vn.ptit.judge.client` và dán code mẫu bên dưới.

---

## Cách 2: Dùng lệnh wsimport trong Terminal

Nếu không dùng NetBeans, mở Terminal / CMD trong thư mục src của project:

```bash
# Bài 7 — DataService
wsimport -keep -p vn.ptit.judge.client http://localhost:2230/ws/DataService.wsdl

# Bài 8 — CharacterService
wsimport -keep -p vn.ptit.judge.client http://localhost:2230/ws/CharacterService.wsdl

# Bài 9 — ObjectService
wsimport -keep -p vn.ptit.judge.client http://localhost:2230/ws/ObjectService.wsdl
```

Sau đó import các class đã sinh vào code Java.

---

## Code mẫu cho từng bài

### Bài 7 — Tính tổng list số nguyên (yyhHZUpt)

Sau khi `wsimport` từ `DataService.wsdl`, bạn sẽ có các class:
- `DataService_Service`
- `DataService`
- `GetDataRequest`, `GetDataResponse`
- `SubmitDataIntRequest`, `SubmitDataIntResponse`

```java
package vn.ptit.judge.client;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Tạo service proxy (Smart way — không cần viết SOAP XML)
        DataService_Service service = new DataService_Service();
        DataService port = service.getDataServicePort();

        String studentCode = "B21DCCN001";
        String qCode = "yyhHZUpt";

        // 2. Lấy data (Trả về List<String> luôn, không cần parse XML thủ công)
        List<String> dataList = port.getData(studentCode, qCode);
        System.out.println("Data nhận được: " + dataList);

        // 3. Xử lý logic: tính tổng
        int sum = 0;
        for (String s : dataList) {
            sum += Integer.parseInt(s);
        }
        System.out.println("Tổng = " + sum);

        // 4. Gửi kết quả (Gọi hàm Java bình thường, không cần quan tâm Envelope/Body)
        String status = port.submitDataInt(studentCode, qCode, sum);
        System.out.println("Kết quả từ Server: " + status);
    }
}
```

### Bài 8 — Đảo ngược chuỗi (qvCGMEhW)

Sau khi `wsimport` từ `CharacterService.wsdl`:

```java
package vn.ptit.judge.client;

public class Main {
    public static void main(String[] args) {
        CharacterService_Service service = new CharacterService_Service();
        CharacterService port = service.getCharacterServicePort();

        String studentCode = "B21DCCN001";
        String qCode = "qvCGMEhW";

        // 1. Lấy chuỗi từ server
        String data = port.requestString(studentCode, qCode);
        System.out.println("Chuỗi nhận được: " + data);

        // 2. Đảo ngược
        String reversed = new StringBuilder(data).reverse().toString();
        System.out.println("Chuỗi đảo ngược: " + reversed);

        // 3. Gửi kết quả
        String status = port.submitString(studentCode, qCode, reversed);
        System.out.println("Kết quả từ Server: " + status);
    }
}
```

### Bài 9 — Tính giá cuối sản phẩm (7u07dNqg)

Sau khi `wsimport` từ `ObjectService.wsdl`:

```java
package vn.ptit.judge.client;

public class Main {
    public static void main(String[] args) {
        ObjectService_Service service = new ObjectService_Service();
        ObjectService port = service.getObjectServicePort();

        String studentCode = "B21DCCN001";
        String qCode = "7u07dNqg";

        // 1. Lấy ProductY từ server (trả về object Java, không phải XML)
        ProductY product = port.requestProductY(studentCode, qCode);
        System.out.println("Product: " + product.getName()
                + " | price=" + product.getPrice()
                + " | taxRate=" + product.getTaxRate()
                + " | discount=" + product.getDiscount());

        // 2. Tính giá cuối
        double finalPrice = product.getPrice()
                * (1 + product.getTaxRate() / 100)
                * (1 - product.getDiscount() / 100);
        product.setFinalPrice(finalPrice);
        System.out.println("Giá cuối = " + String.format("%.2f", finalPrice));

        // 3. Gửi kết quả (truyền lại ProductY đã cập nhật finalPrice)
        String status = port.submitProductY(studentCode, qCode, product);
        System.out.println("Kết quả từ Server: " + status);
    }
}
```

---

## Tóm tắt lợi ích của wsimport

| Cách cũ (thủ công) | Cách mới (wsimport) |
|---|---|
| Tự viết SOAP Envelope XML | NetBeans tự sinh class Java từ WSDL |
| Tự parse XML response bằng DOM | Gọi hàm Java, trả về kiểu dữ liệu gốc (String, int, List, Object) |
| Dễ sai namespace, dễ lỗi định dạng | Server và client đồng bộ theo WSDL |
| Code dài, khó đọc | Code ngắn, sạch, giống gọi hàm local |

## Lưu ý

- **Server phải đang chạy** khi bạn `wsimport` hoặc chạy client (để đọc WSDL và gọi SOAP endpoint)
- Nếu server đổi IP/port, sửa lại URL trong `wsimport` và `Service` constructor
- Nếu muốn build project không cần server chạy, có thể lưu file WSDL local rồi `wsimport -keep -p vn.ptit.judge.client file:/path/to/DataService.wsdl`
