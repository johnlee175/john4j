/**
 *QuickComparator.java
 *2007-8-23
 *@author zhanghui
 */
package com.johnsoft.library.raw;

import java.util.Comparator;

public class QuickComparator implements Comparator<Object> {

	public int compare(Object o1, Object o2) {
		if (o1 == null) {
			if (o2 == null) {
				return 0;
			}
			return -1;
		} else if (o2 == null) {
			return 1;
		} else {
			return o1.toString().compareTo(o2.toString());
		}

	}

}
