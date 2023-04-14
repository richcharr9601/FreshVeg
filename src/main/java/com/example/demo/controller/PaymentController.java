package com.example.demo.controller;


import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.PaymentConfig;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.TransactionStatusDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.User;
import com.example.demo.model.Payment;
import com.example.demo.model.PaymentRes;
import com.example.demo.repository.entity.OrderRepository;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.service.contract.IOrderService;
import com.example.demo.service.contract.IUserService;
import com.example.demo.service.imp.OrderService;
import com.example.demo.service.imp.UserService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@RestController
public class PaymentController {

    final ModelMapper modelMapper;
    private final OrderService orderService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

  


    @PostMapping("/checkout/create-payment/{userId}")
    public ResponseEntity<?> createPayment(@PathVariable("userId") Long userId, @RequestBody Payment requestParams)  {
                    // String TXNREF = PaymentConfig.getRandomNumber(5);
                        User user =  userRepository.findByUserId(userId);
        Order order = orderRepository.findByOrderIdAndUserUserId(requestParams.getOrderId(),user.getUserId());
        
                    int amount = order.getAmount() * 100;
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
                    vnp_Params.put("vnp_TxnRef", String.valueOf(order.getOrderId()));
                    vnp_Params.put("vnp_OrderInfo", "Thanh toan hoa don" + order.getOrderId());
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
    return ResponseEntity.status(HttpStatus.OK).body(result); 
}






@GetMapping("/checkout/payment-information/{userId}")
public ResponseEntity<?>  transactionHandle (
    @RequestParam(value = "vnp_Amount", required = false) String amount,
    @RequestParam(value = "vnp_BankCode", required = false) String bankCode,
    @RequestParam(value = "vnp_BankTranNo", required = false) String bankTranNo,
    @RequestParam(value = "vnp_CardType", required = false) String cardType,
    @RequestParam(value = "vnp_PayDate", required = false) String payDate,
    @RequestParam(value = "vnp_OrderInfo", required = false) String orderInfo,
    @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
    @RequestParam(value = "vnp_TransactionNo", required = false) String transactionNo,
    @RequestParam(value = "vnp_TxnRef", required = false) String txnRef,
    @RequestParam(value = "vnp_SecureHash", required = false) String secureHash,
    @RequestParam(value = "vnp_SecureHashType", required = false) String secureHashType,
    @PathVariable("userId") Long userId
) throws MessagingException{
    TransactionStatusDTO result = new TransactionStatusDTO();
    if (!responseCode.equalsIgnoreCase("00")){
        result.setStatus("02");
        result.setMessage("Checkout failed");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    User user =  userRepository.findByUserId(userId);
 
    if(orderRepository.findByOrderIdAndUserUserId(Long.parseLong(txnRef),user.getUserId())==null){
        result.setStatus("01");
        result.setMessage("Cannot find order");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
        Order order = orderRepository.findByOrderIdAndUserUserId(Long.parseLong(txnRef),user.getUserId());
    if(order.getStatusPayment()==true){
        result.setStatus("01");
        result.setMessage("Order already paid");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    if(order.getOrderId().toString().equalsIgnoreCase(txnRef)){
        order.setStatusPayment(true);
        orderRepository.save(order);
        result.setStatus("00");
        result.setMessage("Checkout successfully");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    return ResponseEntity.status(HttpStatus.OK).body(result);
}
}

    