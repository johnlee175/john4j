package com.johnsoft.library.raw;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A text field which can limit content length,limit only English allowed, limit only upper case allowed 
 */
public class  JTextFieldLimit  extends PlainDocument {
	  private static final long serialVersionUID = 1L;
	  private int limit;
	  private boolean isUppercase = false;

	  public JTextFieldLimit (int limit) {
	   super();
	   this.limit = limit;
	   }

	  public JTextFieldLimit (int limit, boolean upper) {
	   super();
	   this.limit = limit;
	   isUppercase = upper;
	   }

	  public void insertString(int offset, String  str, AttributeSet attr)
	      throws BadLocationException {
	   Pattern pattern = Pattern.compile("[a-zA-Z;]");
	   Matcher matcher = pattern.matcher(str);
	   boolean b = matcher.matches();
	   if (str == null || !b) return;

	   if ((getLength() + str.length()) <= limit) {
	     if (isUppercase) str = str.toUpperCase();
	     super.insertString(offset, str, attr);
	     }
	   }
	}
