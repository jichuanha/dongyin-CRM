package com.hzkans.crm.modules.trade.dao;

import com.hzkans.crm.common.persistence.CrudDao;
import com.hzkans.crm.common.persistence.PagePara;
import com.hzkans.crm.common.persistence.annotation.MyBatisDao;
import com.hzkans.crm.modules.trade.entity.JoinActivity;

import java.util.List;

/**
 * 活动订单管理DAO接口
 * @author jc
 * @version 2018-12-01
 */
@MyBatisDao
public interface JoinActivityDao extends CrudDao<JoinActivity> {

    List<JoinActivity> selectJoinActivityPage(PagePara<JoinActivity> pagePara);

    List<JoinActivity> selectJoinActivityLotteryPage(PagePara<JoinActivity> pagePara);

    List<JoinActivity> selectAll(JoinActivity joinActivity);

    List<JoinActivity> selectRedPackInfo();

    int selectJoinActivityPageCount(PagePara<JoinActivity> pagePara);

    int selectJoinActivityLotteryPageCount(PagePara<JoinActivity> pagePara);

    int updateJoinActivityStatus(JoinActivity activity);

    int selectMobileNumByopenId(JoinActivity activity);

    int insertAllJoinActivity(List<JoinActivity> joinActivities);
}