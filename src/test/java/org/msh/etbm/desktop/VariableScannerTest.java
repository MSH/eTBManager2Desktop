/**
 * 
 */
package org.msh.etbm.desktop;

import java.util.List;

import org.junit.Test;
import org.msh.xview.impl.VariableScanner;
import static org.junit.Assert.*;

/**
 * @author Ricardo Memoria
 *
 */
public class VariableScannerTest {

	private boolean intest = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		VariableScannerTest app = new VariableScannerTest();
		app.intest = false;
		app.testScan();
	}


	
	@Test
	public void testScan() {
		testExpression("The case is = ${tbcase.notifDate} but user is #{user.name}", "tbcase.notifDate", "user.name");
		testExpression("${ exam.releaseDate >= exam.collectedDate}", "exam.releaseDate", "exam.collectedDate");
		testExpression("${not empty workspace.id}", "workspace.id");
		testExpression("${(ne user.email and model.getVariable('test')}", "user.email");
		testExpression("${tbcase.email");
	}
	
	/**
	 * @param exp
	 */
	public void testExpression(String exp, String... expected) {
		List<String> lst = VariableScanner.scan(exp);
		
		if (!intest) {
			System.out.println("Expression: " + exp);
			for (String var: lst) {
				System.out.println(var);
			}
		}
		
		assertEquals(expected.length, lst.size());
		int index = 0;
		for (String var: lst) {
			assertEquals(var, expected[index]);
			index++;
		}
	}
}
