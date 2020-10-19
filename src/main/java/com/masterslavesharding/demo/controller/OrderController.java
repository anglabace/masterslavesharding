package com.masterslavesharding.demo.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.masterslavesharding.demo.mapper.GoodsMapper;
import com.masterslavesharding.demo.mapper.OrderMapper;
import com.masterslavesharding.demo.pojo.Goods;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.masterslavesharding.demo.pojo.Order;
//import io.seata.spring.annotation.GlobalTransactional;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

@Controller
@RequestMapping("/order")
public class OrderController {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";

    @Resource
    private OrderMapper orderMapper;

    //订单列表，列出分库分表的数据
    @GetMapping("/orderlist")
    public String list(Model model, @RequestParam(value="currentPage",required = false,defaultValue = "1") Integer currentPage){

        PageHelper.startPage(currentPage, 5);
        List<Order> orderList = orderMapper.selectAllOrder();
        model.addAttribute("orderlist",orderList);
        PageInfo<Order> pageInfo = new PageInfo<>(orderList);
        model.addAttribute("pageInfo", pageInfo);
        System.out.println("------------------------size:"+orderList.size());
        return "order/list";
    }


    //添加一个订单
    @GetMapping("/addorder")
    @ResponseBody
    public String addOrder(@RequestParam(value="orderid",required = true,defaultValue = "0") Long orderId
                           )  throws SQLException, IOException {

        String goodsId = "3";
        String goodsNum = "1";

        String goodsName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

        Order orderOne = new Order();
        orderOne.setOrderId(orderId);
        orderOne.setGoodsName(goodsName);

        int resIns = orderMapper.insertOneOrder(orderOne);
        System.out.println("orderId:"+orderOne.getOrderId());

        if (resIns>0) {
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

}

