package com.chen.cases;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.chen.constants.Contants;
import com.chen.pojo.CaseInfo;
import com.chen.pojo.WriteBackData;
import com.chen.utils.AuthenticationUtils;
import com.chen.utils.ExcelUtils;
import com.chen.utils.HttpUtils;
import com.chen.utils.SQLUtis;

import ognl.SetPropertyAccessor;

/**
  * 注册测试类
 * @author 曦月女孩
 */
public class ResgisterCases extends BaseCase {
	
	@Test(dataProvider = "datas")
	public void test(CaseInfo caseInfo) throws Exception {
//		1、参数化替换(字符串的替换，把带有$符号的替换成正常值)
		replaceParams(caseInfo);
//		String sql = caseInfo.getSql();
//      2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
//		Object beforeSqlResult = getSqlResult(sql);
//		3、调用接口
		HttpResponse response = HttpUtils.call(caseInfo.getType(), caseInfo.getContentType(), caseInfo.getUrl(),
				caseInfo.getParams(), Contants.HEADERS);
//      打印响应
		String body = HttpUtils.getResponseBody(response);
		
//      4、断言响应结果
		boolean assertFlag = assertResponse(body, caseInfo.getExpectedData());
		System.out.println("断言结果 ：" + assertFlag);
//		5、添加接口响应回写内容
		WriteBackData wbd = new WriteBackData(sheetIndex, caseInfo.getCaseId(), Contants.RESPONES_CELL_NUM, body);
		ExcelUtils.wbdList.add(wbd);
//		6、数据库后置查询结果
//		Object afterSqlResult = getSqlResult(sql);
//	    7、数据库断言
//		boolean sqlAssertFlag = sqlAssert(beforeSqlResult, afterSqlResult);
//		8、添加断言回写内容
		String assertContent = assertFlag ? "pass" : "fail";
		WriteBackData wbd2 = new WriteBackData(sheetIndex, caseInfo.getCaseId(), Contants.ASSERT_CELL_NUM, assertContent);
		ExcelUtils.wbdList.add(wbd2);
//		9、添加日志
//		10、报表断言
		Assert.assertEquals(assertFlag, true);
	}
     /**
	 * 数据库断言
	 * @param beforeSqlResult
	 * @param afterSqlResult
	 * @return
	 */
//	public boolean sqlAssert(Object beforeSqlResult, Object afterSqlResult) {
//		// beforeSqlResult == 0 并且 afterSqlResult ==1
//		if (beforeSqlResult == null || afterSqlResult == null) {
//			return false;
//		}
//		if ("0".equals(beforeSqlResult.toString()) && "1".equals(afterSqlResult.toString())) {
//			return true;
//		} else {
//			return false;
//		}
//	}

	@DataProvider(name = "datas")
	public Object[][] datas() throws Exception {

		Object[][] datas2 = ExcelUtils.getDatas(sheetIndex, 1, CaseInfo.class);
		return datas2;
	}
}
