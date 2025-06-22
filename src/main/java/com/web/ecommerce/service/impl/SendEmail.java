package com.web.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.web.ecommerce.entity.Order;
import com.web.ecommerce.entity.OrderDetail;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class SendEmail {
	@Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail,
                                String subject,
                                String body
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("trandinhhai200902@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Send...");


    }

    /**
     * Gửi email xác nhận đơn hàng thành công với mẫu HTML.
     *
     * @param toEmail      Email của khách hàng
     * @param order        Đối tượng Order chứa thông tin đơn hàng
     * @param orderDetails Danh sách các sản phẩm trong đơn hàng
     */
    public void sendOrderConfirmationEmail(String toEmail, Order order, List<OrderDetail> orderDetails) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = buildOrderEmailTemplate(order, orderDetails);

            helper.setText(htmlMsg, true); // true = bật chế độ HTML
            helper.setTo(toEmail);
            helper.setSubject("Xác nhận đơn hàng #" + order.getId());
            helper.setFrom("trandinhhai200902@gmail.com");

            mailSender.send(mimeMessage);
            System.out.println("HTML Mail Sent...");
        } catch (MessagingException e) {
            System.err.println("Error sending HTML email: " + e.getMessage());
        }
    }

    /**
     * Xây dựng nội dung HTML cho email xác nhận đơn hàng.
     *
     * @param order        Đối tượng Order
     * @param orderDetails Danh sách sản phẩm
     * @return Chuỗi HTML hoàn chỉnh
     */
    private String buildOrderEmailTemplate(Order order, List<OrderDetail> orderDetails) {
        StringBuilder itemsHtml = new StringBuilder();
        for (OrderDetail detail : orderDetails) {
            itemsHtml.append("<tr>")
                    .append("<td style='padding: 8px; border: 1px solid #ddd;'>").append(detail.getProductDetailId()).append("</td>")
                    .append("<td style='padding: 8px; border: 1px solid #ddd; text-align: center;'>").append(detail.getQuantity()).append("</td>")
                    .append("<td style='padding: 8px; border: 1px solid #ddd; text-align: right;'>").append(String.format("%,.0f", detail.getPrice())).append(" đ</td>")
                    .append("</tr>");
        }

        String fullAddress = String.join(", ", order.getShippingAddress(), order.getShippingWardName(), order.getShippingDistrictName(), order.getShippingCityName());

        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; color: #333; }"
                + "table { width: 100%; border-collapse: collapse; }"
                + "th, td { padding: 8px; border: 1px solid #ddd; }"
                + "th { background-color: #f2f2f2; text-align: left; }"
                + ".container { padding: 20px; border: 1px solid #eee; max-width: 600px; margin: auto; }"
                + ".header { font-size: 24px; font-weight: bold; margin-bottom: 20px; color: #4CAF50; }"
                + ".footer { margin-top: 20px; font-size: 12px; color: #777; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>Cảm ơn bạn đã đặt hàng!</div>"
                + "<p>Chào <strong>" + order.getShippingName() + "</strong>,</p>"
                + "<p>Cửa hàng đã nhận được đơn hàng của bạn. Chúng tôi sẽ nhanh chóng xử lý và gửi hàng cho bạn trong thời gian sớm nhất.</p>"
                + "<h3>Chi tiết đơn hàng #" + order.getId() + "</h3>"
                + "<table>"
                + "<thead>"
                + "<tr><th>Sản phẩm</th><th>Số lượng</th><th>Thành tiền</th></tr>"
                + "</thead>"
                + "<tbody>" + itemsHtml.toString() + "</tbody>"
                + "</table>"
                + "<p style='text-align: right;'><strong>Phí vận chuyển:</strong> " + String.format("%,.0f", order.getAmountShipping()) + " đ</p>"
                + "<p style='text-align: right;'><strong>Giảm giá:</strong> " + String.format("%,.0f", order.getDiscountAmount()) + " đ</p>"
                + "<p style='text-align: right; font-size: 18px;'><strong>Tổng cộng: " + String.format("%,.0f", order.getTotalPrice()) + " đ</strong></p>"
                + "<h4>Địa chỉ giao hàng</h4>"
                + "<p>" + fullAddress + "</p>"
                + "<p>Số điện thoại: " + order.getShippingPhone() + "</p>"
                + "<div class='footer'>"
                + "<p>Cảm ơn bạn đã tin tưởng và mua sắm tại cửa hàng của chúng tôi!</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}

