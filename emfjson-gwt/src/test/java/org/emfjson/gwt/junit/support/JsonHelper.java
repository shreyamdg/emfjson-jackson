package org.emfjson.gwt.junit.support;

import com.google.gwt.core.client.JavaScriptObject;

public class JsonHelper {

	public static native String stringify(JavaScriptObject object) /*-{
		return JSON.stringify(object, null, '\t');
	}-*/;
	
}