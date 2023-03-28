package com.example.demo.controller;


import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.PaymentConfig;
import com.example.demo.entities.Order;
import com.example.demo.model.Payment;
import com.example.demo.model.PaymentRes;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.service.contract.IOrderService;
import com.example.demo.service.contract.IUserService;
import com.example.demo.service.imp.OrderService;
import com.example.demo.service.imp.UserService;


@RestController
public class PaymentController {

    ModelMapper modelMapper;
    IOrderService orderService;
    IUserService userService;
    UserRepository userRepository;

    public PaymentController(ModelMapper modelMapper, OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;

    }


    @PostMapping("/checkout/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody Payment requestParams)  {
                    String TXNREF = PaymentConfig.getRandomNumber(5);
                    int amount = requestParams.getAmount() * 100;
                    Map<String, String> vnp_Params = new HashMap<>();
                    vnp_Params.put("vnp_Version", PaymentConfig.VERSIONVNPAY);
                    vnp_Params.put("vnp_Command", PaymentConfig.COMMAND);
                    vnp_Params.put("vnp_TmnCode", PaymentConfig.TMNCODE);
                    vnp_Params.put("vnp_Amount", String.valueOf(amount));
                    vnp_Params.put("vnp_CurrCode", PaymentConfig.CURRCODE);
                    // if (requestParams.getBankCode() != null && !requestParams.getBankCode().isEmpty()) {
                    vnp_Params.put("vnp_BankCode", requestParams.getBankCode());
                    vnp_Params.put("vnp_Locale", PaymentConfig.LOCALEDEFAULT);
                    // vnp_Params.put("vnp_CardType", PaymentConfig.CARDTYPE);
                    vnp_Params.put("vnp_TxnRef", TXNREF);
                    vnp_Params.put("vnp_OrderInfo", "Thanh toan hoa don" + TXNREF);
                    vnp_Params.put("vnp_OrderType", PaymentConfig.ORDERTYPE);
                    vnp_Params.put("vnp_ReturnUrl", PaymentConfig.RETURNURL);
                    vnp_Params.put("vnp_IpAddr", PaymentConfig.IPDEFAULT);

    Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String vnp_CreateDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
    
    cld.add(Calendar.MINUTE, 15);
    String vnp_ExpireDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
    
    List fieldNames = new ArrayList(vnp_Params.keySet());
    Collections.sort(fieldNames);
    StringBuilder hashData = new StringBuilder();
    StringBuilder query = new StringBuilder();
    Iterator itr = fieldNames.iterator();
    while (itr.hasNext()) {
        String fieldName = (String) itr.next();
        String fieldValue = (String) vnp_Params.get(fieldName);
        if ((fieldValue != null) && (fieldValue.length() > 0)) {
            //Build hash data
            hashData.append(fieldName);
            hashData.append('=');
            try {
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Build query
            try {
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            query.append('=');
            try {
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (itr.hasNext()) {
                query.append('&');
                hashData.append('&');
            }
        }
    }
    String queryUrl = query.toString();
    String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.CHECKSUM, hashData.toString());
    queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
    String paymentUrl = PaymentConfig.VNPAYURL + "?" + queryUrl;
    PaymentRes result = new PaymentRes();
    result.setStatus("00");
    result.setMessage("Success");
    result.setUrl(paymentUrl);
    Optional<Order> order = orderService.findByID(requestParams.getOrderId());
    order.get().setStatus(1);
    return ResponseEntity.status(HttpStatus.OK).body(result); 

}

}

    