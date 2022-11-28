package com.atguigu.ggkt.activity.service.impl;


import com.atguigu.ggkt.activity.mapper.CouponInfoMapper;
import com.atguigu.ggkt.activity.service.CouponInfoService;
import com.atguigu.ggkt.activity.service.CouponUseService;
import com.atguigu.ggkt.client.user.UserInfoFeignClient;
import com.atguigu.ggkt.model.activity.CouponInfo;
import com.atguigu.ggkt.model.activity.CouponUse;
import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.vo.activity.CouponUseQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-11-28
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {
    @Autowired
    private CouponUseService couponUseService;

    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

    //获取已经使用的优惠券列表
    @Override
    public IPage<CouponUse> selectCouponUsePage(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo) {

        LambdaQueryWrapper<CouponUse> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (couponUseQueryVo!=null){
            //获取条件
            Long couponId = couponUseQueryVo.getCouponId();
            String couponStatus = couponUseQueryVo.getCouponStatus();
            String getTimeBegin = couponUseQueryVo.getGetTimeBegin();
            String getTimeEnd = couponUseQueryVo.getGetTimeEnd();
            //封装条件
            lambdaQueryWrapper.eq(!StringUtils.isEmpty(couponId),CouponUse::getCouponId,couponId);
            lambdaQueryWrapper.eq(!StringUtils.isEmpty(couponStatus),CouponUse::getCouponStatus,couponStatus);
            lambdaQueryWrapper.ge(!StringUtils.isEmpty(getTimeBegin),CouponUse::getGetTime,getTimeBegin);
            lambdaQueryWrapper.le(!StringUtils.isEmpty(getTimeEnd),CouponUse::getGetTime,getTimeEnd);
        }
        IPage<CouponUse> pageModel = couponUseService.page(pageParam, lambdaQueryWrapper);
        List<CouponUse> couponUseList = pageModel.getRecords();
        //遍历
        couponUseList.stream().forEach(item->{
            this.getUserInfoById(item);
        });
        return pageModel;
    }

    //根据用户id,通过远程调用得到用户信息
    private CouponUse getUserInfoById(CouponUse couponUse) {
        //获取用户id
        Long userId = couponUse.getUserId();
        if (!StringUtils.isEmpty(userId)){
            UserInfo userInfo = userInfoFeignClient.getById(userId);
            if (userInfo!=null){
                couponUse.getParam().put("nickName",userInfo.getNickName());
                couponUse.getParam().put("phone",userInfo.getPhone());

            }
        }
        return couponUse;
    }
}
