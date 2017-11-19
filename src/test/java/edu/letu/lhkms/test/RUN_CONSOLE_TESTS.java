package edu.letu.lhkms.test;

import java.io.PrintWriter;

public class RUN_CONSOLE_TESTS {
	public static void main(String[] args) {
		PrintWriter out = new PrintWriter(System.out, true);
		out.println("Testing");
		new SerialTest().performTests(out);
		new ServerTest().performTests(out);
		
	}
}
