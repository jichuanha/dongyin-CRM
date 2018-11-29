package com.hzkans.crm.modules.pswmanage.service.ChangePasswordImpl;

import com.hzkans.crm.modules.pswmanage.dao.ChangePasswordADO;
import com.hzkans.crm.modules.pswmanage.entity.ChangePasswordDO;
import com.hzkans.crm.modules.pswmanage.service.ChangePasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IDEA
 * author:dengtm
 * Date:2018/11/23
 * Time:21:59
 */
public class ChangePasswordImpl implements ChangePasswordService {
    Logger log = LoggerFactory.getLogger(ChangePasswordImpl.class);
    @Autowired
    ChangePasswordADO changePasswordADO;

    @Override
    public int insterChangePassword(ChangePasswordDO changePasswordDO) throws Exception {

        try {
            if (null == changePasswordDO) {
                log.error("ChangePasswordImpl.insterChangePassword error : changePasswordDO is null ");
                throw new Exception("changePasswordDO is null ");
            }
            changePasswordADO.inster(changePasswordDO);
            log.info("[{}]changePasswordId",changePasswordDO.getId());
            return changePasswordDO.getId();
        } catch (Exception e) {
            log.error("memberAccount error", e);
            throw  new Exception("changePasswordADO.inster is error");
        }
    }

    @Override
    public List<ChangePasswordDO> selectChangePassword(ChangePasswordDO changePasswordDO) throws Exception {
        try {
            if (null == changePasswordDO) {
                log.error("ChangePasswordImpl.selectChangePassword error : id is null ");
                throw new Exception("selectChangePassword.id is null ");
            }
            List<ChangePasswordDO> changePasswordDOtemp = changePasswordADO.selectChangePassword(changePasswordDO);
            return changePasswordDOtemp;
        } catch (Exception e) {
            log.error("memberAccount error", e);
            throw  new Exception("changePasswordADO.inster is error");
        }
    }

    @Override
    public ChangePasswordDO selectNewChangePassword(String userId) throws Exception {
        return null;
    }
}
