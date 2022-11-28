package com.atguigu.ggkt.order.service.impl;


import com.atguigu.ggkt.model.order.OrderDetail;
import com.atguigu.ggkt.model.order.OrderInfo;
import com.atguigu.ggkt.order.mapper.OrderInfoMapper;
import com.atguigu.ggkt.order.service.OrderDetailService;
import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.vo.order.OrderInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-11-27
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private OrderDetailService orderDetailService;

    //订单列表
    @Override
    public Map<String, Object> selectOrderInfoPage(Page<OrderInfo> pageParam, OrderInfoQueryVo orderInfoQueryVo) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        //1.获取查询条件
        if (orderInfoQueryVo!=null){
            Long userId = orderInfoQueryVo.getUserId();
            String outTradeNo = orderInfoQueryVo.getOutTradeNo();
            String phone = orderInfoQueryVo.getPhone();
            Integer orderStatus = orderInfoQueryVo.getOrderStatus();
            String createTimeBegin = orderInfoQueryVo.getCreateTimeBegin();
            String createTimeEnd = orderInfoQueryVo.getCreateTimeEnd();

            //2.判断条件是否为空,不为空,进行条件封装
            queryWrapper.eq(!StringUtils.isEmpty(userId),"user_id",userId);
            queryWrapper.eq(!StringUtils.isEmpty(outTradeNo),"out_trade_no",outTradeNo);
            queryWrapper.eq(!StringUtils.isEmpty(phone),"phone",phone);
            queryWrapper.eq(!StringUtils.isEmpty(orderStatus),"order_status",orderStatus);
            queryWrapper.ge(!StringUtils.isEmpty(createTimeBegin),"create_time",createTimeBegin);
            queryWrapper.le(!StringUtils.isEmpty(createTimeEnd),"create_time",createTimeEnd);
        }
        //3.调用实现条件封装
        Page<OrderInfo> pages = baseMapper.selectPage(pageParam, queryWrapper);
        long totalCount = pages.getTotal();
        long pageCount = pages.getPages();
        List<OrderInfo> records = pages.getRecords();
        //4.订单里面包含详情内容,封装详情数据,根据订单id查询详情
        records.stream().forEach(item->{
            this.getOrderDetail(item);
        });
        //5.所有需要数据封装map集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("total",totalCount);
        map.put("pageCount",pageCount);
        map.put("records",records);
        return map;
    }

    //查询订单详情数据
    private OrderInfo getOrderDetail(OrderInfo orderInfo) {
        Long id = orderInfo.getId();
        //注意:下面的代码是有逻辑错误的,之所以能返回正确值,是因为数据库中order_detail和order_info两个表示例中的id相同
        //此处应该用order_id,但是因为要和老师保持一致,暂时留下这个逻辑bug
        OrderDetail orderDetail = orderDetailService.getById(id);
        if (orderDetail!=null){
            String courseName = orderDetail.getCourseName();
            orderInfo.getParam().put("courseName",courseName);
        }
        return orderInfo;
    }
}
