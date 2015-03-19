package org.msh.xview.swing.component;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Extension of the text field to incorporate maximum number of chars 
 * and char case
 * 
 * @author Ricardo Memoria
 *
 */
public class JTextFieldEx extends JTextField {
	private static final long serialVersionUID = -7949747104235576018L;

	public enum CharCase { NORMAL, UPPER, LOWER	};


	/**
	 * @return the charCase
	 */
	public CharCase getCharCase() {
		return getFieldFilter().getCharCase();
	}

	/**
	 * @param charCase the charCase to set
	 */
	public void setCharCase(CharCase charCase) {
		getFieldFilter().setCharCase(charCase);
	}

	/**
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return getFieldFilter().getMaxLength();
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(int maxLength) {
		getFieldFilter().setMaxLength(maxLength);
	}

	/**
	 * Return the instance of the {@link FieldFilter} in use
	 * @return
	 */
	protected FieldFilter getFieldFilter() {
		DocumentFilter df = ((AbstractDocument)getDocument()).getDocumentFilter();

		FieldFilter ff;
		if (df instanceof FieldFilter) {
			ff = (FieldFilter)df;
		}
		else {
			ff = new FieldFilter();
			((AbstractDocument)getDocument()).setDocumentFilter(ff);
		}
		
		return ff;
	}


	/**
	 * Document filter that limits the number of chars and char case in a text field
	 * 
	 * @author Ricardo Memoria
	 *
	 */
	private class FieldFilter extends DocumentFilter {
		private CharCase charCase = CharCase.NORMAL;
		private int maxLength;

		@Override
		public void insertString(DocumentFilter.FilterBypass fb, int offset,
				String string, AttributeSet attr) throws BadLocationException {
			if (fb.getDocument().getLength() + string.length() > 5) {
				return;
			}

			fb.insertString(offset, string, attr);
		}

		@Override
		public void remove(DocumentFilter.FilterBypass fb, int offset,
				int length) throws BadLocationException {
		    fb.remove(offset, length);
		}

		@Override
		public void replace(DocumentFilter.FilterBypass fb, int offset,	
				int length, String text, AttributeSet attrs) throws BadLocationException {
			if (text != null) {
				if (maxLength > 0) {
					int size = fb.getDocument().getLength() + text.length();
					if (size > maxLength) {
						size = size - maxLength;
						if ((size <= 1) || (size > text.length()))
							return;
						text = text.substring(0, text.length() - size);
					}
				}

				if (charCase == CharCase.UPPER)
					text = text.toUpperCase();
				else 
					if (charCase == CharCase.LOWER)
						text = text.toLowerCase();
			}
	
			fb.replace(offset, length, text, attrs);
//			fb.insertString(offset, text, attrs);
		}

		/**
		 * @return the charCase
		 */
		public CharCase getCharCase() {
			return charCase;
		}

		/**
		 * @param charCase the charCase to set
		 */
		public void setCharCase(CharCase charCase) {
			this.charCase = charCase;
		}

		/**
		 * @return the maxLength
		 */
		public int getMaxLength() {
			return maxLength;
		}

		/**
		 * @param maxLength the maxLength to set
		 */
		public void setMaxLength(int maxLength) {
			this.maxLength = maxLength;
		}
	}
}
