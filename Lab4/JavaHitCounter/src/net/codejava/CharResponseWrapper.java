package net.codejava;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class CharResponseWrapper extends HttpServletResponseWrapper {
	private CharArrayWriter writer;
	
	public CharResponseWrapper(HttpServletResponse response) {
		super(response);
		writer = new CharArrayWriter();
	}
	
	public PrintWriter getWriter() {
		return new PrintWriter(writer);
	}
	
	public String toString() {
		return writer.toString();
	}

}
