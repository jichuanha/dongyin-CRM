package com.hzkans.crm.modules.pswmanage.service;

import com.hzkans.crm.modules.pswmanage.entity.ChangePasswordDO;
import com.hzkans.crm.modules.sys.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IDEA
 * author:dengtm
 * Date:2018/11/23
 * Time:21:54
 */
@Service
@Transactional
public interface ChangePasswordService {
    String sendMail(User user);

    String verification(Integer cid,String userId,String userName,String sid);

    List<ChangePasswordDO> selectChangePassword(ChangePasswordDO changePasswordDO) throws Exception;

}
