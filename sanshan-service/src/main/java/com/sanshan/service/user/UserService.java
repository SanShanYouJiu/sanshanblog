package com.sanshan.service.user;

import com.sanshan.dao.UserMapper;
import com.sanshan.pojo.dto.UserDTO;
import com.sanshan.pojo.entity.UserDO;
import com.sanshan.service.BaseServiceImpl;
import com.sanshan.service.convent.UserConvert;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.PosCodeEnum;
import com.sanshan.util.info.UserStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class UserService  extends BaseServiceImpl<UserDO>{

    @Autowired
  private   UserMapper userMapper;

    /**
     * 根据email查询用户
     *
     * @param email 邮箱
     * @return 用户
     */
    public UserDTO findByEmail(String email) {
        UserDO userDO = userMapper.findByEmail(email);
        return UserConvert.doToDto(userDO);
    }

    ///**
    // * 检查用户是否可登陆
    // *
    // * @param userDTO  用户
    // * @param passwd   用户密码(明文)
    // * @param ip       登陆ip
    // * @param resultVO 错误写回实体
    // * @return true 可登陆
    // */
    //public  Boolean checkCanLogin(UserDTO userDTO, String passwd, String ip, ResponseMsgVO resultVO){
    //    UserDO userDO = userMapper.selectByPrimaryKey(userDTO.getId());
    //    Setting setting = settingService.getSetting();
    //    //验证是否冻结
    //    if (userDO.getStatus() == UserStatusEnum.FREEZE.value) {
    //        resultVO.buildWithPosCode(PosCodeEnum.USER_FREEZE);
    //        return false;
    //    }
    //    //验证锁定状态
    //    if (userDO.getIsLock() == 1) {
    //        int accountLockTime = setting.getAccountLockTime();
    //        //锁定时间0,则永久锁定
    //        if (accountLockTime == 0) {
    //            resultVO.buildWithPosCode(PosCodeEnum.USER_LOCKED);
    //            return false;
    //        }
    //        Date lockdate = userDO.getLockdate();
    //        Date unlockdate = DateUtils.addMinutes(lockdate, accountLockTime);
    //        if (new Date().after(unlockdate)) {
    //            userDO.setIsLock(0);
    //            userDO.setLoginfail(0);
    //            userDO.setLockdate(null);
    //            userMapper.updateByPrimaryKeySelective(userDO);
    //        } else {
    //            resultVO.buildWithPosCode(PosCodeEnum.USER_LOCKED);
    //            return false;
    //        }
    //    }
    //
    //}


}
