package edu.letu.lhkms.test;

import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public class TestClass {
	@Retention(RetentionPolicy.RUNTIME) static @interface TestCase {}
	
	boolean performTests(final PrintWriter out) {
		out.println("--- Invoking test " + getClass().getName() + " ---");
		boolean success = true;
		for (Method m : this.getClass().getDeclaredMethods()) {
			//if (m.getName().contains("performTests") || m.getName().contains("$")) continue;
			if (!m.isAnnotationPresent(TestCase.class)) continue; // Check for annotation
			try {
				out.println("* " + m.getName() + " *");
				boolean passed = (boolean) m.invoke(this, out);
				if (passed) {
					out.println("--> PASSED");
				} else {
					out.println("--> FAILED");
					success = false;
				}
			} catch (Exception e) {
				out.println(m.getName() + " threw exception " + e.getClass() + " - " + e.getMessage());
				success = false;
				e.printStackTrace();
			}
		}
		out.println("--- Test " + getClass().getName() + " " + (success ? "PASSED" : "FAILED") + " ---");
		return success;
	}
	
	public static interface SupplierExp<R> {
		R get() throws Exception;
	}
}
