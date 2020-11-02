package com.chen.cases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.chen.constants.Contants;
import com.chen.pojo.CaseInfo;
import com.chen.pojo.WriteBackData;
import com.chen.utils.AuthenticationUtils;
import com.chen.utils.ExcelUtils;
import com.chen.utils.HttpUtils;
	/**
            * 新增项目测试类
	* @author 曦月女孩
	*/
public class ConfiguresCases extends BaseCase {
//	private int sheetIndex = 2;
	
	@Test(dataProvider = "datas")
	public void test(CaseInfo caseInfo) throws Exception {
//		1、参数化替换
		replaceParams(caseInfo);
//		2、数据库前置查询结果
//		3、调用接口
		//添加token鉴权头
		Map<String, String> headers = getAuthenticationHeaders();
		System.out.println(headers);
		HttpResponse response = HttpUtils.call(caseInfo.getType(), caseInfo.getContentType(),
		caseInfo.getUrl(), caseInfo.getParams(), headers);
		// 打印响应
		String body = HttpUtils.getResponseBody(response);
//      3.2 获取load id
//		AuthenticationUtils.getValue2ENV(body, "$.data.id", "${loan_id}");
//		4、断言响应结果
		boolean assertFlag = assertResponse(body, caseInfo.getExpectedData());
//		5、添加接口响应回写内容
		WriteBackData wbd = new WriteBackData(sheetIndex, caseInfo.getCaseId(), Contants.RESPONES_CELL_NUM, body);
		ExcelUtils.wbdList.add(wbd);
//		7、数据库断言
//		8、添加断言回写内容
		String assertContent = assertFlag? "pass" :"fail";
		WriteBackData wbd2 = new WriteBackData(sheetIndex, caseInfo.getCaseId(),  Contants.ASSERT_CELL_NUM, assertContent);
		ExcelUtils.wbdList.add(wbd2);
//		9、添加日志
//		10、报表断言	
		Assert.assertEquals(assertFlag, true);
	}

	@DataProvider(name = "datas")
	public Object[][] datas() throws Exception {
		Object[][] datas2 = ExcelUtils.getDatas(sheetIndex, 1, CaseInfo.class);
		return datas2;
	}
}
