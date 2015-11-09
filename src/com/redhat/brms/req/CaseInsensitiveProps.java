package com.redhat.brms.req;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;


	public class CaseInsensitiveProps extends Properties {

		private static final long serialVersionUID = 1L;
		
		private String value = "";
	 public String getPropVal(String key) {

	  String value = getProperty(key);
	  if (value != null){
		  return value;
	  }
	  
	  Set<Entry<Object, Object>> propSet = entrySet();
	  Iterator<Entry<Object, Object>> propIter = propSet.iterator();
	  while (propIter.hasNext()) {
	   Entry<Object, Object> entry = propIter.next();
	   if (key.equalsIgnoreCase((String) entry.getKey())) {
	    return (String) entry.getValue();
	   }
	  }
	  return value;
	 }
}
