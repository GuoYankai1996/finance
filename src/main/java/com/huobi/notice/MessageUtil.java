package com.huobi.notice;

import com.alibaba.fastjson.JSON;
import com.huobi.sdk.model.UserEntity;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import com.zjiecode.wxpusher.client.bean.MessageResult;
import com.zjiecode.wxpusher.client.bean.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by guoyankai on 2021/4/18
 */
@Slf4j
public class MessageUtil {

    public static void sendMessage(UserEntity u, List<String> messages) {
        try {
            Message message = new Message();
            //这里是申请的应用名。不用改。
            message.setAppToken("AT_o2gKOCnaxzZx0NUWrjVQEyZmjxuNLT12");
            message.setContentType(Message.CONTENT_TYPE_TEXT);
            message.setContent(JSON.toJSONString(messages));
            //这里是向不同的用户发送消息
            message.setUid(u.getUid());
            Result<List<MessageResult>> result = WxPusher.send(message);
            result.getCode();
        } catch (Exception e) {
            log.error("消息发送失败：{}", JSON.toJSONString(messages));
        }
    }
}
