-- Tạo bảng api_permissions để lưu trữ quyền truy cập API
CREATE TABLE `api_permissions` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `api_path` VARCHAR(255) NOT NULL COMMENT 'Đường dẫn API (ví dụ: /api/v1/order/{id}/change-status)',
  `http_method` VARCHAR(10) NOT NULL COMMENT 'Phương thức HTTP (GET, POST, PUT, DELETE)',
  `role_id` INT(11) NOT NULL COMMENT 'ID của role',
  `description` VARCHAR(255) DEFAULT NULL COMMENT 'Mô tả quyền',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_api_method_role` (`api_path`, `http_method`, `role_id`),
  KEY `idx_role_id` (`role_id`),
  CONSTRAINT `fk_api_permissions_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Bảng lưu trữ quyền truy cập API theo role';

-- Thêm dữ liệu mẫu cho ADMIN (giả sử role_id = 1 là ADMIN)
INSERT INTO `api_permissions` (`api_path`, `http_method`, `role_id`, `description`) VALUES
('/api/v1/order/{id}/change-status', 'POST', 1, 'Quyền thay đổi trạng thái đơn hàng - ADMIN'),
('/api/v1/order/{id}/change-payment-status', 'POST', 1, 'Quyền thay đổi trạng thái thanh toán - ADMIN'),
('/api/v1/products', 'POST', 1, 'Quyền tạo sản phẩm mới - ADMIN'),
('/api/v1/products', 'PUT', 1, 'Quyền cập nhật sản phẩm - ADMIN'),
('/api/v1/products', 'DELETE', 1, 'Quyền xóa sản phẩm - ADMIN'),
('/api/v1/permissions', 'GET', 1, 'Quyền xem danh sách permissions - ADMIN'),
('/api/v1/permissions', 'POST', 1, 'Quyền tạo permission mới - ADMIN'),
('/api/v1/permissions', 'PUT', 1, 'Quyền cập nhật permission - ADMIN'),
('/api/v1/permissions', 'DELETE', 1, 'Quyền xóa permission - ADMIN'),
('/api/v1/system/endpoints', 'GET', 1, 'Quyền xem danh sách endpoints - ADMIN');

-- Thêm dữ liệu mẫu cho MANAGER (giả sử role_id = 2 là MANAGER)
INSERT INTO `api_permissions` (`api_path`, `http_method`, `role_id`, `description`) VALUES
('/api/v1/order/{id}/change-status', 'POST', 2, 'Quyền thay đổi trạng thái đơn hàng - MANAGER'),
('/api/v1/order/{id}/change-payment-status', 'POST', 2, 'Quyền thay đổi trạng thái thanh toán - MANAGER'),
('/api/v1/products', 'GET', 2, 'Quyền xem danh sách sản phẩm - MANAGER'),
('/api/v1/order', 'GET', 2, 'Quyền xem danh sách đơn hàng - MANAGER'); 