# Hệ thống Dynamic Permissions cho E-commerce

## Tổng quan

Hệ thống Dynamic Permissions cho phép quản lý quyền truy cập API một cách linh hoạt thông qua database, thay vì hard-code trong code. Hệ thống sử dụng AOP (Aspect-Oriented Programming) với custom annotation `@CheckPermission` để kiểm tra quyền truy cập.

## Cấu trúc hệ thống

### 1. Database Schema

```sql
-- Bảng api_permissions
CREATE TABLE `api_permissions` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `api_path` VARCHAR(255) NOT NULL COMMENT 'Đường dẫn API',
  `http_method` VARCHAR(10) NOT NULL COMMENT 'Phương thức HTTP',
  `role_id` INT(11) NOT NULL COMMENT 'ID của role',
  `description` VARCHAR(255) DEFAULT NULL COMMENT 'Mô tả quyền',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_api_method_role` (`api_path`, `http_method`, `role_id`),
  KEY `idx_role_id` (`role_id`),
  CONSTRAINT `fk_api_permissions_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
);
```

### 2. Các thành phần chính

#### Entity
- `ApiPermission.java` - Entity đại diện cho bảng api_permissions

#### DAO Layer
- `ApiPermissionDao.java` - Interface DAO
- `ApiPermissionDaoImpl.java` - Implementation DAO

#### Service Layer
- `ApiPermissionService.java` - Interface Service
- `ApiPermissionServiceImpl.java` - Implementation Service

#### Controller Layer
- `ApiPermissionController.java` - Controller quản lý permissions
- `SystemController.java` - Controller hệ thống (endpoints discovery)

#### AOP Components
- `@CheckPermission` - Custom annotation
- `PermissionAspect.java` - Aspect xử lý kiểm tra quyền

#### Utility Services
- `EndpointService.java` - Service quét và lưu trữ API endpoints

## Cách sử dụng

### 1. Áp dụng @CheckPermission vào API

Thay vì sử dụng `@PreAuthorize`, sử dụng `@CheckPermission`:

```java
@PostMapping("/{id}/change-status")
@CheckPermission(description = "Thay đổi trạng thái đơn hàng")
public ResponseEntity<BaseResponse<OrderResponse>> changeStatus(@PathVariable("id") int id,
        @Valid @RequestBody ChangeStatusOrderRequest wrapper) throws Exception {
    // Business logic
}
```

### 2. Thêm permission vào database

```sql
-- Thêm quyền cho ADMIN (role_id = 1)
INSERT INTO `api_permissions` (`api_path`, `http_method`, `role_id`, `description`) VALUES
('/api/v1/order/{id}/change-status', 'POST', 1, 'Quyền thay đổi trạng thái đơn hàng - ADMIN');

-- Thêm quyền cho MANAGER (role_id = 2)
INSERT INTO `api_permissions` (`api_path`, `http_method`, `role_id`, `description`) VALUES
('/api/v1/order/{id}/change-status', 'POST', 2, 'Quyền thay đổi trạng thái đơn hàng - MANAGER');
```

### 3. Quản lý permissions qua API

#### Lấy danh sách permissions
```bash
GET /api/v1/permissions
Authorization: Bearer <admin_token>
```

#### Tạo permission mới
```bash
POST /api/v1/permissions
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "apiPath": "/api/v1/order/{id}/change-status",
  "httpMethod": "POST",
  "roleId": 1,
  "description": "Quyền thay đổi trạng thái đơn hàng"
}
```

#### Lấy danh sách endpoints có sẵn
```bash
GET /api/v1/system/endpoints
Authorization: Bearer <admin_token>
```

## Workflow hoạt động

### 1. Khi API được gọi

1. **Request đến API** có annotation `@CheckPermission`
2. **PermissionAspect** được trigger
3. **Lấy thông tin request**: API path, HTTP method
4. **Lấy thông tin user**: Từ SecurityContext
5. **Kiểm tra quyền**: Query database với api_path, http_method, role_id
6. **Quyết định**:
   - Có quyền → Thực hiện method gốc
   - Không có quyền → Trả về 403 Forbidden

### 2. Khi quản lý permissions

1. **Admin truy cập** interface quản lý permissions
2. **Frontend gọi API** `/api/v1/system/endpoints` để lấy danh sách endpoints
3. **Admin chọn** API và role cần cấp quyền
4. **Frontend gọi API** `/api/v1/permissions` để tạo/cập nhật permission
5. **Permission được lưu** vào database

## Lợi ích

### 1. Linh hoạt
- Có thể thay đổi quyền mà không cần deploy lại code
- Dễ dàng thêm/xóa quyền cho từng role

### 2. Bảo trì dễ dàng
- Không cần hard-code quyền trong code
- Tập trung logic kiểm tra quyền vào một chỗ

### 3. Quản lý tập trung
- Tất cả quyền được lưu trong database
- Có thể export/import quyền
- Audit trail cho việc thay đổi quyền

### 4. Tự động hóa
- Tự động quét và phát hiện API endpoints
- Không cần nhập tay đường dẫn API

## Cấu hình bảo mật

### 1. Chỉ ADMIN mới có quyền quản lý permissions
```java
@PreAuthorize("hasAuthority('ADMIN')")
```

### 2. Validation dữ liệu
- Kiểm tra role_id tồn tại
- Kiểm tra api_path hợp lệ
- Kiểm tra http_method hợp lệ

### 3. Logging
- Log tất cả việc kiểm tra quyền
- Log việc tạo/cập nhật/xóa permissions

## Troubleshooting

### 1. Permission không hoạt động
- Kiểm tra api_path có đúng không
- Kiểm tra http_method có đúng không
- Kiểm tra role_id có tồn tại không
- Kiểm tra user có role_id đúng không

### 2. Endpoints không được quét
- Kiểm tra EndpointService có được khởi tạo không
- Kiểm tra RequestMappingHandlerMapping có sẵn không
- Gọi API `/api/v1/system/endpoints/refresh` để refresh

### 3. Performance issues
- Thêm cache cho permissions
- Sử dụng batch query thay vì query từng permission
- Index database cho các trường thường query

## Migration từ @PreAuthorize

### 1. Thay thế annotation
```java
// Cũ
@PreAuthorize("hasAuthority('ADMIN')")

// Mới
@CheckPermission(description = "Mô tả quyền")
```

### 2. Thêm permission vào database
```sql
INSERT INTO api_permissions (api_path, http_method, role_id, description) 
VALUES ('/api/v1/your-api-path', 'POST', 1, 'Mô tả quyền');
```

### 3. Test
- Test với user có quyền
- Test với user không có quyền
- Test với user chưa đăng nhập

## Best Practices

### 1. Naming Convention
- Sử dụng mô tả rõ ràng cho description
- Đặt tên api_path theo chuẩn RESTful
- Sử dụng UPPER_CASE cho http_method

### 2. Security
- Luôn validate input
- Sử dụng parameterized queries
- Log security events

### 3. Performance
- Cache permissions nếu cần
- Sử dụng batch operations
- Index database properly

### 4. Maintenance
- Backup permissions định kỳ
- Review permissions thường xuyên
- Document changes 