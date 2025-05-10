package com.web.ecommerce.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "option_values")
public class OptionValues extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "option_value_id")
	private int id;
	
	@Column(name = "option_id")
	private Integer optionId;

	@Column(name = "value", nullable = false, length = 50)
	private String value;

	@Column(name = "status", nullable = false, columnDefinition = "int(1) default 1")
	private int status;
}
