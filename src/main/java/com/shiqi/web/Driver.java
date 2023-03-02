package com.shiqi.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Driver {
	private static void symbolic(HttpServletRequest request, HttpServletResponse response, String searchName) {
		StudentControllerServlet servlet = new StudentControllerServlet();
		try {
			servlet.symbolicSearchStudents(request, response, searchName);
		} catch (Exception e) {

		}
	}

	public static void main(String[] args) {
		symbolic(null, null, "walawala");
	}
}