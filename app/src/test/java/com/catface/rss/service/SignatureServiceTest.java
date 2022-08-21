package com.catface.rss.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.catface.rss.BaseTest;
import com.catface.rss.api.util.ResourceUtil;
import com.catface.rss.http.rpc.controller.signature.convert.SignatureConvert;
import com.catface.rss.service.signature.SignatureService;
import com.catface.rss.service.signature.dto.SignatureDTO;
import com.catface.rss.service.signature.param.GetSignatureParam;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author by catface
 * @since 2021/8/25 下午1:00 . All rights reserved.
 */
public class SignatureServiceTest extends BaseTest {

    @Autowired
    private SignatureService signatureService;

    @Test
    public void testUpload() throws Exception {
        GetSignatureParam request = new GetSignatureParam();
        request.setCreator("1212");
        Map<String, String> param = new HashMap<>();
        param.put("warehouseId", "3434343");
        request.setPathParam(param);
        request.setResourceName("uploadDemo.txt");
        request.setCreateTask(true);
        request.setTaskDesc("测试文件上传并生成任务");
        request.setTypeCode("TEST_CODE");
        SignatureDTO signatureDTO = signatureService.getSignature(request);

        File file = new File(
            "/Users/lf/Documents/software/义勇.jpg");
        InputStream in = new FileInputStream(file);
        ResourceUtil.uploadFile(in, "uploadDemo.txt", null, SignatureConvert.convert(signatureDTO), 30000);
        System.out.println("xxx");
    }

}
