package com.hzkans.crm.modules.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.hzkans.crm.common.service.ServiceException;
import com.hzkans.crm.common.utils.CacheUtils;
import com.hzkans.crm.common.utils.StringUtils;
import com.hzkans.crm.modules.wechat.constants.ReplyTypeEnum;
import com.hzkans.crm.modules.wechat.entity.*;
import com.hzkans.crm.modules.wechat.message.*;
import com.hzkans.crm.modules.wechat.utils.HttpRequestUtil;
import com.hzkans.crm.modules.wechat.utils.WechatCofig;
import com.hzkans.crm.modules.wechat.utils.WechatUtils;
import com.hzkans.crm.modules.wxapi.WxApiObserver;
import org.springframework.transaction.annotation.Transactional;
import com.hzkans.crm.common.utils.JsonUtil;
import com.hzkans.crm.modules.wechat.constants.MessageTypeEnum;
import com.hzkans.crm.modules.wechat.utils.MessageUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jc
 * @description 处理微信消息接口
 * @create 2018/12/11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatInfoService {

    private Logger logger = LoggerFactory.getLogger(WechatInfoService.class);

    private static final String URL = "http://yiyezi.yyzws.com/ex/";


    @Autowired
    private MemberAssociationService memberAssociationService;
    @Autowired
    private WechatPlatfromService wechatPlatfromService;
    @Autowired
    private WxApiObserver wxApiObserver;

    /**
     * 微信消息处理
     * @param requestMap
     * @return
     */
    public String messageDeal(Map<String, String> requestMap) {
        //默认返回参数
        String resultXml = "";
        logger.info("requestMap {}", JsonUtil.toJson(requestMap));
        String msgType = requestMap.get("MsgType");
        String wechatNo = requestMap.get("ToUserName");
        String openId = requestMap.get("FromUserName");
        String createTime = requestMap.get("CreateTime");
        String msgId = requestMap.get("MsgId");
        String mediaId = requestMap.get("MediaId");
        try {
            //根据wechatNo获取对应的数据库id
            WechatPlatfrom platfrom = new WechatPlatfrom();
            platfrom.setWechatNo(wechatNo);
            WechatPlatfrom wechatPlatform = wechatPlatfromService.getWechatPlatform(platfrom);
            Long wechatId = wechatPlatform.getId();
            //如果是事件推送
            if(MessageTypeEnum.EVENT.getCode().equals(msgType)) {
                String event = requestMap.get("Event");

                switch (event) {
                    //关注事件
                    case EventMessage.EVENT_TYPE_SUBSCRIBE :
                        MemberAssociation association = new MemberAssociation();
                        association.setOpenId(openId);
                        association.setWechatId(wechatNo);
                        association.setDeleted(0);
                        association.setCreateDate(new Date());
                        association.setUpdateDate(new Date());
                        memberAssociationService.save(association);

                        //获取主表id,然后根据主表id获取素材内容
                        WechatReplyNew replyNew = new WechatReplyNew();
                        replyNew.setWechatId(wechatId);
                        replyNew.setRuleType(ReplyTypeEnum.RECEIVED.getCode());
                        List<WechatMaterial> matetials = wxApiObserver.getAttentionMaterial(replyNew);
                        logger.info(" matetials {}",JsonUtil.toJson(matetials));
                        if(CollectionUtils.isEmpty(matetials)) {
                            break;
                        }
                        //根据素材的类型决定回复方法
                        resultXml = dealType(matetials.get(0), wechatNo, openId);
                        break;
                    //取消关注
                    case EventMessage.EVENT_TYPE_UNSUBSCRIBE :
                        MemberAssociation ass = new MemberAssociation();
                        ass.setOpenId(openId);
                        ass.setWechatId(wechatNo);
                        ass.setDeleted(1);
                        memberAssociationService.save(ass);
                        break;

                    case EventMessage.EVENT_TYPE_CLICK :
                        String eventKey = requestMap.get("EventKey");
                        //根据关键字获取主表id,再根据主表id获取素材内容
                        resultXml = keyWordDeal(wechatId, eventKey, wechatNo, openId);
                        break;

                }
            }else if (MessageTypeEnum.TEXT.getCode().equals(msgType)) {
                //如果是文本内容的消息
                String content = requestMap.get("Content");
                MessageRecord record = new MessageRecord();
                record.setMsgId(msgId);
                record.setMsgType(msgType);
                record.setContent(content);
                record.setToUserName(wechatNo);
                record.setFromUserName(openId);
                record.setMediaId(mediaId);
                //判断该回复是否已经处理过
                MessageRecord messageRecord = (MessageRecord) CacheUtils.get(WechatCofig.EHCACHE, msgId);
                if(null != messageRecord) {
                    return resultXml;
                }
                memberAssociationService.saveMessageRecord(record);
                CacheUtils.put(WechatCofig.EHCACHE, msgId, record);
                resultXml = keyWordDeal(wechatId, content, wechatNo, openId);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        logger.info("resultXML  {}",JsonUtil.toJson(resultXml));

        return resultXml;
    }

    /**
     * 关键字处理
     * @param wechatId
     * @param keyWord
     * @param wechatNo
     * @param openId
     * @return
     */
    private String keyWordDeal(Long wechatId, String keyWord, String wechatNo, String openId) throws ServiceException{
        WechatReplyKeyword keyword = new WechatReplyKeyword();
        keyword.setKeyword(keyWord);
        keyword.setWechatId(wechatId);
        List<WechatMaterial> material = wxApiObserver.getKeyWordMaterial(keyword);
        logger.info(" matetials {}",JsonUtil.toJson(material));
        if(CollectionUtils.isEmpty(material)) {
            return "";
        }
        return dealType(material.get(0), wechatNo, openId);
    }


    private String dealType(WechatMaterial wechatMaterial, String wechatNo, String openId) {
        Integer type = wechatMaterial.getType();
        switch (type) {
            case 0 :
                //文本回复
                ContentMessage message = new ContentMessage();
                message.setToUserName(openId);
                message.setFromUserName(wechatNo);
                message.setCreateTime(System.currentTimeMillis());
                message.setMsgType(MessageTypeEnum.TEXT.getCode());
                message.setContent(wechatMaterial.getContent());
                logger.info(" imageMessage {}",JsonUtil.toJson(message));
                return MessageUtil.messageToXml(message);
            case 1 :
                //图文回复
                ImageMessage imageMessage = new ImageMessage();
                imageMessage.setToUserName(openId);
                imageMessage.setFromUserName(wechatNo);
                imageMessage.setCreateTime(System.currentTimeMillis());
                imageMessage.setMsgType(MessageTypeEnum.IMAGE.getCode());
                Image image = new Image();
                image.setMediaId(wechatMaterial.getMediaId());
                imageMessage.setImage(image);
                logger.info(" imageMessage {}",JsonUtil.toJson(imageMessage));
                return  MessageUtil.messageToXml(imageMessage);
            case 2 :
                //视频回复现在不做
                return "";
            case 3 :
                //语音回复
                VoiceMessage voiceMessage = new VoiceMessage();
                voiceMessage.setToUserName(openId);
                voiceMessage.setFromUserName(wechatNo);
                voiceMessage.setCreateTime(System.currentTimeMillis());
                voiceMessage.setMsgType(MessageTypeEnum.VOICE.getCode());
                Image im = new Image();
                im.setMediaId(wechatMaterial.getMediaId());
                voiceMessage.setVoice(im);
                logger.info(" voiceMessage {}",JsonUtil.toJson(voiceMessage));
                return MessageUtil.messageToXml(voiceMessage);
            case 4 :
                //图文回复
                NewsMessage newsMessage = new NewsMessage();
                newsMessage.setToUserName(openId);
                newsMessage.setFromUserName(wechatNo);
                newsMessage.setCreateTime(System.currentTimeMillis());
                newsMessage.setMsgType(MessageTypeEnum.NEWS.getCode());
                newsMessage.setArticleCount("1");
                List<Article> articles = new ArrayList<>();
                Article article = new Article();
                article.setPicUrl(URL+wechatMaterial.getCoverPicture());
                article.setTitle(wechatMaterial.getTitle());
                article.setDescription(wechatMaterial.getBrief());
                article.setUrl(wechatMaterial.getUri());
                articles.add(article);
                newsMessage.setArticles(articles);
                logger.info(" newsMessage {}",JsonUtil.toJson(newsMessage));
                return MessageUtil.messageToXml(newsMessage);
            default:
                return "";
        }


    }


    /**
     * 微信获取用户信息(根据code)
     * @param code
     * @return
     */
    public Map<String, Object> getUserInfo(String code, String appId, String appSecret) throws Exception {
        logger.info("code {}",code);
        String accUrl = WechatCofig.GET_CODE_ACCESS_TOKEN.replace("APPID",appId)
                .replace("SECRET", appSecret).replace("CODE", code);
        //根据code换取access_token
        String accResult = HttpRequestUtil.HttpsDefaultExecute(HttpRequestUtil.POST_METHOD, accUrl,
                "", "", 0, "false");
        JSONObject object = JSONObject.parseObject(accResult);
        String accessToken = object.getString("access_token");
        String openId = object.getString("openid");
        if(StringUtils.isEmpty(accessToken)) {
            throw new Exception(" code error");
        }

        //校验access_token的有效性
        String checkUrl = WechatCofig.CHECK_ACCESS_TOKEN.replace("ACCESS_TOKEN", accessToken)
                .replace("OPENID", openId);
        String checkResult = HttpRequestUtil.HttpsDefaultExecute(HttpRequestUtil.POST_METHOD, checkUrl,
                "", "", 0, "false");
        JSONObject checkObject = JSONObject.parseObject(checkResult);
        String errmsg = checkObject.getString("errmsg");
        if(!"ok".equals(errmsg)) {
            throw new Exception("accessToken invalid");
        }

        //根据access_token获取用户信息
        String infoUrl = WechatCofig.GET_USER_INFO.replace("ACCESS_TOKEN", accessToken)
                .replace("OPENID", openId);
        String infoResult = HttpRequestUtil.HttpsDefaultExecute(HttpRequestUtil.POST_METHOD, infoUrl,
                "", "", 0, "false");

        return (Map<String, Object>) JSONObject.parse(infoResult);
    }


}
