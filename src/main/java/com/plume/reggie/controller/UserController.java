package com.plume.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.plume.reggie.common.R;
import com.plume.reggie.entity.User;
import com.plume.reggie.service.UserService;
import com.plume.reggie.utils.SMSUtils;
import com.plume.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    // 前端发送phone和code，我们直接采用Map类型接收，采用get方法获得值
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        // 保存手机号
        String phone = user.getPhone();

        // 判断手机号是否存在
        if (phone != null){
            // 随机生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            // 直接打印验证码
            log.info(code);
            // 采用阿里云发送验证码
            // SMSUtils.sendMessage("签名","模板",phone,code);

            // 将数据放在session中待对比
            // session.setAttribute(phone,code);

            // 将生成的验证码缓存到redis,并设置有效期为5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            return R.success("验证码发送成功");
        }
        return R.error("验证码发送失败");

    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        log.info(map.toString());

        // 获取手机号
        String phone = map.get("phone").toString();
        // 获得验证码
        String code = map.get("code").toString();

        // 从session中获取保存的验证码
        //String codeInSession = session.getAttribute(phone).toString();

        // 从redis中获取缓存的验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);

        // 进行验证码对比
        if (codeInSession != null && codeInSession.equals(code)){
            // 如果对比成功,说明登录成功
            log.info("验证码正确");

            // 判断是否为新用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            // 是新用户
            if (user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            // 设置session里的user值为用户id,判断用户是否登录
            session.setAttribute("user",user.getId());

            // 如果用户登录成功,删除redis中缓存的验证码
            redisTemplate.delete(phone);

            // 返回User信息,前端需要布置页面
            return R.success(user);
        }
        // 验证码对比失败
        return R.error("登陆失败");
    }
}
