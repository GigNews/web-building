package com.github.emailtohl.building.common.encryption;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.emailtohl.building.common.encryption.Hex;
/**
 * 16进制编码测试
 * @author HeLei
 * @date 2017.02.04
 */
public class HexTest {

	@Test
	public void testHex() {
		Hex h = new Hex();
		String srcStr = "待转换字符串";
		String encodeStr = h.encodeHexStr(srcStr.getBytes());
		String decodeStr = new String(h.decodeHex(encodeStr.toCharArray()));
		System.out.println("转换前：" + srcStr);
		System.out.println("转换后：" + encodeStr);
		System.out.println("还原后：" + decodeStr);
		assertEquals(srcStr, decodeStr);
	}

}
