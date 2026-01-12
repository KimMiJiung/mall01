package com.sbproject.mall01.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public String condition;
	
	public ConditionException(String condition) {
		this.condition = condition;
	}


}
