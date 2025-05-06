/**
 * 
 */
package com.web.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

/**
 * Base response wrapper
 */
@Schema(name = "BaseResponse", description = "Base response wrapper")
public class BaseResponse<T> {
	
	/**
	 * HTTP status code
	 */
    @Schema(description = "HTTP status code")
    private int status;
    
    /**
     * Thông báo trả về hoặc lỗi
     */
    @Schema(description = "Thông báo trả về hoặc lỗi")
    private String message;
    
    /**
     * Dữ liệu trả về
     */
    @Schema(description = "Dữ liệu trả về")
    private T data;
    
    public BaseResponse() {
		this.setStatus(HttpStatus.OK);
		this.setMessage(HttpStatus.OK);
		this.setData(null);
    }
    
	public int getStatus() {
		return status;
	}
	public void setStatus(HttpStatus statusEnum) {
		this.status = statusEnum.value();
		this.message = statusEnum.name();
	}
	public String getMessage() {
		return this.message;
	}
	public void setMessage(HttpStatus statusEnum) {
		this.message = statusEnum.name();
	}
	
	public void setMessageError(String message) {
		this.message = message;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}

}
