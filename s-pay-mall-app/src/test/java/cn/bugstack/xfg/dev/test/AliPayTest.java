package cn.bugstack.xfg.dev.test;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

@Slf4j
public class AliPayTest {

    // 「沙箱环境」应用ID - 您的APPID，收款账号既是你的APPID对应支付宝账号。获取地址；https://open.alipay.com/develop/sandbox/app
    public static String app_id = "2021000147677741";
    // 「沙箱环境」商户私钥，你的PKCS8格式RSA2私钥 -【秘钥工具】所创建的公户私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCaO6Nfj/2fzmdbFs54Ol0VzXnmXF/lOPihT6ZlNhVM9Ne9q7AK45nGwGz/bDBB+kFlSH3p86dk36rWoObu+pu49DM190xTvGySmLWEs1vDjWsF01AH4k6MwpjEagYopnuYwHMIdFKmmiykjsUVjp59l0e65CQd4ptFfBJLFMV/n9HiKGHXHAxaNsW/jzxfMmPajgXQGw9NAhu2nNZEN1hQcgu0VKsLms8p8D6MwxKqu+Rusj+YC3Rt6nDbiYaSK4GVfA8cHOwfx8vpthuk84CeTF4OGIF4BlOJetcWQy3QqFJY3+hSQnlGOVLsnmHhEHXesBm+Mc764CaDnD0/nljxAgMBAAECggEAcGPXKJMYAbT6EiGrcWcuX+3sz5Yu9y2t2HbfTU+88+hTsk6DKVwzueaNRxACloGT2AecYbiBGfv3VRHJVwpeIcgp7UB5aWecQrNvSvUcgIL0IzmaTzR1gxNnGXIdbM/sryJGcTm65Nbig0PakFvuWJy9043eV7zC4z6HYhkP1nBvSsrksnsSRuievp1Z2oaFeKtRDK3AtCLM+BVJ1n8Nhphsxf9Oa7eEsRPz25QF2U1pjOkrgHlT779PiRhs3scuJqdc9xNWMBq+fZpgrktgkiVWSc6eYV3N7yk+d5oiXkl3QO2Z5X4XWvQEJA48IdjQNPKk1DwG7Me+s3Sxz18/sQKBgQDgsHJGuUFFxjsAWs5scpWOQ43vTzLjE6pR15InccOfwbJOcTHpMQNlOlmWO/WfLJcN42V2A5nbgwQc/iIavwnO5LU5M9w6RqZkDQdltnBtbZ1n+CGGLMcsqp+/qHj21TN5mu7vqY4pf1o4TcjR36E3B4tDw15SwDhNfOE9kspsrQKBgQCvubzXZrGkB6m5vncq5GHegjas0mc5fePRz4xwWjDB/9FR1g4coMH2ibAX3/na4N8kWuFcgyQcCiZiOo73L2axaQeHHH/mC8rsC0Vhmj6+6lgF2EWIb2mswlpudljr39szskGZXurjLGhU307WONMYPE636d8o1lowI2Kq30JB1QKBgQDI2REd5UXSsFNXitSRHpBPvlVnAR3U3GRYimGsfsZGzWetNQRPli+Z0mH4qJp0cfCJSqcMqkVjcqc/n3AvBmwnFExm2NPZCAR+1SKyqjWvq9eT87tL7EUSyWbJdTjENGqyHmvNsnxQ3fVw9R85dDTxMv4rUb3ELO1nXuUdHFV0dQKBgH4Wx1LEBDkPqpB2A2oKf9M6GeM+AgV1uG/G/h1sCcpaNicDchLGowa02172PoD4lASxFT3dd56LBem73POu1n/8PgOLxpYZ5hwjJkCXfehdnJVtDs+MKgZlWqAiWUG+2bmpcXqMafxh0+b6aMY79fRQ6ab7sD3MOOCLs3OqSQ+VAoGAOTRpdf+qS6TDgjD7GHzijurHhoYg5UDh0KbatNWCe0aUCJeczf19leWO/w+c+FAj38SC9s6sb71uObo9LgkaPPKFFYwmksWOaXMIJkdWda3X2ZyQJaQsRlXv7vzEjE0w8s0YGInxHmt3UDlc4LiPZEnDWc0WYzINXX3PEFJS3bM=";
    // 「沙箱环境」支付宝公钥 -【秘钥填写】后提供给你的支付宝公钥
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApUwCEXSpgsqk/w8vBBuH5SsFSUc2i3d3u2tTFRPLxzIfM9R4T5iDhjYXfSKmWyiTprq1kEA/ItuWuJP4Za/H76FfHyZ+89vYzJ5gKWfBe/04AlTnv530J7T3N1gy7TpfrlbjFqgNcoAB3yAlbdypPMhdkTKdSqI/7hnIOZlR0glI8hWd4lArqKl9vl+0USrpWFP04hBU5sdiJxcmA517H4DENzLUm1UjbsP+zGBrs/EQgKfAPIsjXdH293h2GUU1cMZkswpTaaFVzSBpJriNIPUNh4HxvvbMrlLaZmvV89C3sq60adScY4Z5eMY8Gtz856aVQ2Dzae51PaS/1VicwwIDAQAB";
    // 「沙箱环境」服务器异步通知回调地址
    public static String notify_url = " http://lyz1010.natapp1.cc/api/v1/alipay/alipay_notify_url";
    // 「沙箱环境」页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "https://gaga.plus";
    // 「沙箱环境」
    public static String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    // 签名方式
    public static String sign_type = "RSA2";
    // 字符编码格式
    public static String charset = "utf-8";

    private AlipayClient alipayClient;

    @Before
    public void init() {
        this.alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id,
                merchant_private_key,
                "json",
                charset,
                alipay_public_key,
                sign_type);
    }

    @Test
    public void test_aliPay_pageExecute() throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();  // 发送请求的 Request类
        request.setNotifyUrl(notify_url);
        request.setReturnUrl(return_url);

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", "xfg2024092709120006");  // 我们自己生成的订单编号
        bizContent.put("total_amount", "0.01"); // 订单的总金额
        bizContent.put("subject", "测试商品");   // 支付的名称
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
        request.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(request).getBody();
        log.info("测试结果\r\n\n把我复制到 index.html ->：{}", form);

    }

    /**
     * 查询订单
     */
    @Test
    public void test_alipay_certificateExecute() throws AlipayApiException {

        AlipayTradeQueryModel bizModel = new AlipayTradeQueryModel();
        bizModel.setOutTradeNo("xfg2024092709120006");

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(bizModel);

        String body = alipayClient.execute(request).getBody();
        log.info("测试结果：{}", body);
    }

    /**
     * 退款接口
     */
    @Test
    public void test_alipay_refund() throws AlipayApiException {
        AlipayTradeRefundRequest request =new AlipayTradeRefundRequest();
        AlipayTradeRefundModel refundModel =new AlipayTradeRefundModel();
        refundModel.setOutTradeNo("daniel82AAAA000032333361X03");
        refundModel.setRefundAmount("1.00");
        refundModel.setRefundReason("退款说明");
        request.setBizModel(refundModel);

        AlipayTradeRefundResponse execute = alipayClient.execute(request);
        log.info("测试结果：{}", execute.isSuccess());
    }

}