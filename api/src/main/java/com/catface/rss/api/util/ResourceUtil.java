package com.catface.rss.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.catface.rss.api.signature.vo.SignatureVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StringUtils;

/**
 * @author catface
 * @since 2019-02-02
 */
@Slf4j
public class ResourceUtil {

    private static final String OSS_ACCESS_KEY_ID = "OSSAccessKeyId";
    private static final String POLICY = "policy";
    private static final String SIGNATURE = "Signature";
    private static final String KEY = "key";
    private static final String SUCCESS_ACTION_STATUS = "success_action_status";
    private static final String FILE = "file";
    private static final String CALL_BACK = "callback";
    private static final String RESPONSE_STATUS = "200";
    public static final String CONTENT_TYPE_MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * 上传文件流到阿里云oss
     *
     * @param inputStream 文件流
     * @param fileName    文件名称
     * @param contentType 内容的类型,允许为空,默认是: multipart/form-data
     * @param signatureVO 临时访问阿里云授权签名
     * @return true 上传成功,false 失败
     * @throws IOException 关闭文件流或httpClient连接时可能会异常
     */
    public static boolean uploadFile(InputStream inputStream, String fileName, String contentType,
                                     SignatureVO signatureVO, int timeout) throws IOException {
        RequestConfig requestConfig = RequestConfig.custom()
            //connectTimeout：指客户端和服务器建立连接的timeout，
            .setConnectTimeout(2000)
            //connectionRequestTimout：指从连接池获取连接的timeout
            .setConnectionRequestTimeout(2000)
            //socketTimeout：指客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException,根据需要自定义
            .setSocketTimeout(timeout)
            .build();
        HttpPost post = new HttpPost(signatureVO.getHost());
        post.setConfig(requestConfig);
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            // 参数顺序问题,该死的回调,千万要注意参数的顺序,死坑死坑
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            StringBody accessKeyId = new StringBody(signatureVO.getAccessId(),
                ContentType.MULTIPART_FORM_DATA);
            builder.addPart(OSS_ACCESS_KEY_ID, accessKeyId);

            StringBody policy = new StringBody(signatureVO.getPolicy(),
                ContentType.MULTIPART_FORM_DATA);
            builder.addPart(POLICY, policy);

            String callback = signatureVO.getCallback();
            if (!StringUtils.isEmpty(callback)) {
                StringBody callBackStr = new StringBody(callback, ContentType.MULTIPART_FORM_DATA);
                builder.addPart(CALL_BACK, callBackStr);
            }

            StringBody signature = new StringBody(signatureVO.getSignature(),
                ContentType.MULTIPART_FORM_DATA);
            builder.addPart(SIGNATURE, signature);

            // 兼容key中的中文名称
            StringBody key = new StringBody(signatureVO.getKey(),
                ContentType.APPLICATION_JSON);
            builder.addPart(KEY, key);

            if (!StringUtils.isEmpty(contentType)) {
                builder.addBinaryBody(FILE, inputStream, ContentType.create(contentType), fileName);
            } else {
                builder.addBinaryBody(FILE, inputStream, ContentType.create(CONTENT_TYPE_MULTIPART_FORM_DATA),
                    fileName);
            }

            StringBody successActionStatus = new StringBody(RESPONSE_STATUS,
                ContentType.MULTIPART_FORM_DATA);
            builder.addPart(SUCCESS_ACTION_STATUS, successActionStatus);

            HttpEntity entity = builder.build();
            post.setEntity(entity);

            //发送请求
            HttpResponse response = client.execute(post);
            int status = response.getStatusLine().getStatusCode();
            if (!Integer.valueOf(RESPONSE_STATUS).equals(status)) {
                entity = response.getEntity();
                if (entity != null) {
                    inputStream = entity.getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Consts.UTF_8));
                    StringBuilder message = new StringBuilder();
                    String body;
                    while ((body = br.readLine()) != null) {
                        message.append(body);
                    }
                    log.error("文件上传失败!message:{}", message);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("文件上传异常!", e);
        } finally {
            inputStream.close();
        }
        return false;
    }
}
