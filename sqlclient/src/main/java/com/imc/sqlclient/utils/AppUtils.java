package com.imc.sqlclient.utils;

import java.util.Arrays;
import java.util.stream.Stream;

public class AppUtils {
	
	
	public static <T> Stream<T> toStream(T[] arr) {
		return Arrays.stream(arr);
	}

}
