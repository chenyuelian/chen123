package com.chen.constants;

import java.util.HashMap;
import java.util.Map;
//这属于变量的定义，不能在这里写代码
public class Contants {//常量都是要大写
   //excel路径	
	public static final String EXCEL_PATH = "src\\test\\resources\\keyou.xlsx";
   //默认请求头
	public static final Map<String, String> HEADERS = new HashMap<>();
	//响应回写列
		public static final int RESPONES_CELL_NUM = 7;
		//断言回写列
		public static final int ASSERT_CELL_NUM = 10;
}
