package com.chen.cases;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.chen.constants.Contants;
import com.chen.pojo.CaseInfo;
import com.chen.pojo.WriteBackData;
import com.chen.utils.AuthenticationUtils;
import com.chen.utils.ExcelUtils;
import com.chen.utils.HttpUtils;

import io.qameta.allure.Description;

/**
 * 登录测试类
 * @author 曦月女孩
 */
public class LoginCases extends BaseCase {
	
	@Test(dataProvider = "datas",description = "description属性=============")
	@Description("@Description注解")
	public void test(CaseInfo caseInfo) throws Exception {
//		1、参数化替换
		replaceParams(caseInfo);
//		2、数据库前置查询结果		
//		3、调用接口
		HttpResponse response = HttpUtils.call(caseInfo.getType(), caseInfo.getContentType(), caseInfo.getUrl(),
		caseInfo.getParams(), Contants.HEADERS);
//       打印响应
		String body = HttpUtils.getResponseBody(response);
//      3.1获取token
		AuthenticationUtils.getValue2ENV(body, "$.token", "token");
//      3.2 获取menber_id
//		AuthenticationUtils.getValue2ENV(body, "$.data.id", "${member_id}");
//		4、断言响应结果
		boolean assertFlag = assertResponse(body, caseInfo.getExpectedData());
		System.out.println("断言结果 ：" + assertFlag);
//		5、添加接口响应回写内容
		WriteBackData wbd = new WriteBackData(sheetIndex, caseInfo.getCaseId(), Contants.RESPONES_CELL_NUM, body);
		ExcelUtils.wbdList.add(wbd);
//		6、数据库后置查询结果
//		7、数据库断言		
//		8、添加断言回写内容
		String assertContent = assertFlag ? "pass" : "fail";
		WriteBackData wbd2 = new WriteBackData(sheetIndex, caseInfo.getCaseId(), Contants.ASSERT_CELL_NUM, assertContent);
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
