package com.example;

import static org.junit.Assert.*;

import java.util.function.LongBinaryOperator;

import org.junit.Test;

public class TestLong {

	private static LongBinaryOperator addLongs = (t,u) -> t+u;
	
	public static long add(int a, int b) {
		return addLongs.applyAsLong(a, b);
	}
	
	@Test
	public void test() {
		int a = Integer.MAX_VALUE;
		int b = Integer.MAX_VALUE;
		System.out.println(a);
		System.out.println(b);
		Object expected = Integer.MAX_VALUE * 2L;
		assertEquals(expected, add(a, b));
	}

}
