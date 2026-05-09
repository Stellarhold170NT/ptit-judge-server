# Deployment Guide — DigitalOcean

Hướng dẫn triển khai Java Judge Server lên DigitalOcean Droplet (Ubuntu).

## Prerequisites

- Tài khoản DigitalOcean
- Domain (tùy chọn, nếu muốn cấu hình SSL)
- SSH key đã thêm vào DigitalOcean account

## Step 1: Create Droplet

1. Đăng nhập DigitalOcean → **Create** → **Droplets**.
2. Chọn image **Ubuntu 22.04 (LTS x64)**.
3. Chọn plan **Basic**,规格 **2 GB RAM / 1 CPU** (tối thiểu).
4. Chọn datacenter region gần bạn nhất (ví dụ: Singapore).
5. Chọn SSH key để xác thực.
6. Đặt hostname (ví dụ: `ptit-judge-server`).
7. Click **Create Droplet**.

> Droplet khởi tạo xong sẽ có một public IP (ví dụ: `203.0.113.10`).

### Cài đặt Java 17 (nếu chưa có)

SSH vào droplet:

```bash
ssh root@<droplet-ip>
```

Cài Java 17:

```bash
apt update
apt install -y openjdk-17-jdk
java -version
```

## Step 2: Install Docker & Docker Compose

```bash
apt update
apt install -y docker.io docker-compose
systemctl enable docker
systemctl start docker
docker --version
docker-compose --version
```

## Step 3: Clone Repo, Build and Run

```bash
git clone <repo-url>
cd judge-server
docker-compose up --build -d
```

> Lệnh trên build image và chạy container ở chế độ detached.

Kiểm tra container đang chạy:

```bash
docker ps
```

## Step 4: Open Firewall Ports

Mở port cho REST/Web UI (2230) và gRPC (2240):

```bash
ufw allow 2230/tcp
ufw allow 2240/tcp
ufw allow OpenSSH
ufw enable
```

> Nếu dùng DigitalOcean Cloud Firewall, cũng cần mở inbound rules cho port 2230 và 2240.

## Step 5 (Optional): Setup Reverse Proxy + SSL

Nếu bạn có domain và muốn dùng HTTPS:

### Cài Nginx

```bash
apt install -y nginx
```

### Cấu hình Nginx

Tạo file `/etc/nginx/sites-available/judge-server`:

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:2230;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Kích hoạt:

```bash
ln -s /etc/nginx/sites-available/judge-server /etc/nginx/sites-enabled/
nginx -t
systemctl reload nginx
```

### Cài SSL với Let's Encrypt (Certbot)

```bash
apt install -y certbot python3-certbot-nginx
certbot --nginx -d your-domain.com
```

> Certbot sẽ tự động cấu hình HTTPS và renew certificate.

## Step 6: Verify Health

```bash
curl http://<droplet-ip>:2230/
```

Nếu thấy phản hồi HTML của trang chủ, server đã chạy thành công.

Kiểm tra gRPC port:

```bash
telnet <droplet-ip> 2240
```

Hoặc dùng gRPC client từ máy local để test `JudgeService/Request`.

## Troubleshooting

### Port conflict

Nếu báo lỗi `Port 2230 is already allocated`:

```bash
lsof -i :2230
kill -9 <pid>
```

Hoặc đổi port mapping trong `docker-compose.yml`.

### Data directory permissions

Nếu container không ghi được JSON file vào `./data/`:

```bash
chmod -R 777 data/
```

Hoặc chạy container với user phù hợp trong `docker-compose.yml`.

### JVM out of memory

Nếu droplet 2GB RAM bị crash:

```bash
export JAVA_OPTS="-Xms512m -Xmx1024m"
```

Hoặc set trong `docker-compose.yml`:

```yaml
environment:
  - JAVA_OPTS=-Xms512m -Xmx1024m
```

### Container không khởi động

Xem log:

```bash
docker logs <container-id>
```

## Updating

Khi có code mới:

```bash
cd judge-server
git pull origin main
docker-compose down
docker-compose up --build -d
```

> Dữ liệu JSON trong `./data/` sẽ được giữ nguyên vì thư mục này được mount từ host.

---

Nếu gặp vấn đề không giải quyết được, hãy kiểm tra log chi tiết của Spring Boot trong container hoặc mở issue trên repo.
