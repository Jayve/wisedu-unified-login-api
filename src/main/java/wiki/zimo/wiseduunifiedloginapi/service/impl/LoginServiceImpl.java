package wiki.zimo.wiseduunifiedloginapi.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wiki.zimo.wiseduunifiedloginapi.process.CasLoginProcess;
import wiki.zimo.wiseduunifiedloginapi.process.CumtCasLoginProcess;
import wiki.zimo.wiseduunifiedloginapi.process.IapLoginProcess;
import wiki.zimo.wiseduunifiedloginapi.service.LoginService;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Value("${LOGIN_API}")
    private String LOGIN_API;// 登陆接口

    @Override
    public Map<String, String> login(String login_url, String username, String password) throws Exception {
        if (StringUtils.isEmpty(login_url)) {
            login_url = LOGIN_API;
        }

        if (StringUtils.isAllBlank(username) || StringUtils.isAllBlank(password)) {
            throw new RuntimeException("用户名或者密码为空");
        }

        // 封装参数
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        // 根据login_url判断类型
        if (login_url.trim().contains("/iap")) {
            IapLoginProcess process = new IapLoginProcess(login_url, params);
            return process.login();
        } else if (login_url.trim().contains("authserver.cumt.edu.cn")) {
            CumtCasLoginProcess process = new CumtCasLoginProcess(login_url, params);
            return process.login();
        } else {
            CasLoginProcess process = new CasLoginProcess(login_url, params);
            return process.login();
        }
    }


    @Override
    public Map<String, String> login(String username, String password) throws Exception {
        return login(null, username, password);
    }

}
