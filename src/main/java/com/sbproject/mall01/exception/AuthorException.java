package com.sbproject.mall01.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public String author;
	
	public AuthorException(String author) {
		this.author = author;
	}
}
