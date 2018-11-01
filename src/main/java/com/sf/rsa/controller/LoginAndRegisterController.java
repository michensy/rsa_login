package com.sf.rsa.controller;

import com.sf.rsa.common.Utils.PasswordEncoder;
import com.sf.rsa.dao.UserRepository;
import com.sf.rsa.entity.User;
import com.sf.rsa.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@Validated
public class LoginAndRegisterController {

    @Autowired
    private UserRepository userRepository;

    private String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcd+0zTY9Gn94iqkQJTlxYnEnCeFsLkk0a7hoAvi2B74VzDVV3xH0ZO9RkXvo1SgCB+uzbEWdrgQkzTqyjfTtgOguu3OnkVxIMJF34ibchTY0LWHGxq1m2gLGuVVqrlu1LtdV0X7xo/5zc8Mr+46veWb86kSpqe6rOAm69WWo5GwIDAQAB";
    private String PRIVATEKEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJx37TNNj0af3iKqRAlOXFicScJ4WwuSTRruGgC+LYHvhXMNVXfEfRk71GRe+jVKAIH67NsRZ2uBCTNOrKN9O2A6C67c6eRXEgwkXfiJtyFNjQtYcbGrWbaAsa5VWquW7Uu11XRfvGj/nNzwyv7jq95ZvzqRKmp7qs4Cbr1ZajkbAgMBAAECgYAHp349EkA+DjgJrhah9elilFKvZr/dcwy+koNHIgaL4rG+jRpvP3d3MowTVOocjUA1G5dWqCVNBwTyM5kSbl/nIxSCYwdUoDid4r0JbqkXkTTsIq3euHG8eiWr9rr3SDmwDojWoJEc4liVlfme8dQuMfgxe1QKq7wTrJwCKwbeMQJBAPwpknRPRK8W9hefbbtEu8mlbzUy+ER8Puq6dvS+lnWzJ8n2chJcHRYQFwWpjl4+SZuKeEcDmYmuQ7xuqEIayO0CQQCe2YeaxcU4uuDC45RAwCcMaNw1nDJuA+Gi47lXbroBXoeOiNZunViSZVUgDgrV/Ku6V54TaZIzZ21QFjf7mXEnAkEA7dZwMpAJonOvzfwrzbQ4wyrsx2q5zC68UT1qsdGJrJ48azutwC9tp7+pV0fj5nQtjS1/4Ms+aCQb84ET5rXIyQJAM0m45tgEHZT5DPO94kooUXFp6EVOYwcNyzILnZc6p0aGLhcwZPaYqmvdWEQwa3bxW3D+sPXdJou2V61U1f9s8QJALccvYwwWlCTq1iTmegYk9fOoc+isZKH+Z0YW70kFi94AYEO+utYwmXBEAqQ5VC/bywa1O71xdL4/RGCOSxBf2A==";

    /**
     * 获取所有用户信息
     * @return
     */
    @RequestMapping("/getAll")
    @ResponseBody
    public List<User> index(){
        List<User> all = userRepository.findAll();
        return all;
    }

    /**
     * 获取公钥
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/getKey")
    @ResponseBody
    public String getKey(HttpSession session) throws Exception {
        Map<String, Object> keyMap = RSAUtils.genKeyPair();
        String publicKey = RSAUtils.getPublicKey(keyMap);
        String privateKey = RSAUtils.getPrivateKey(keyMap);
        // 私钥放入 session
        session.setAttribute("privateKey",privateKey);
        return publicKey;
    }

    /**
     * 登录
     * 如果是通过浏览器直接请求的方式，需要先将密码经过 base64加密，否则其中的一些特殊符号将会丢失
     * @param username
     * @param password
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/login")
    public String login(@NotBlank String username, @NotBlank String password, HttpSession session){
        // 如果是 get的方式提交，需要使用 base64加密，用于解决参数中的 +符号被过滤掉
        // try {
        //     byte[] decode = Base64Utils.decode(password);
        //     password = new String(decode, "UTF-8");
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        String privateKey = (String) session.getAttribute("privateKey");
        if (StringUtils.isEmpty(privateKey)) {
            return "登录失败 session失效";
        }
        // 根据 session中的私钥解密为原密码
        String passwordIn = RSAUtils.decryptDataOnJava(password, privateKey);

        // 从数据库中获取密码进行校验
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "用户不存在";
        }
        String passwordDb = user.getPassword();

        // rsa方案
        // String decryPasswordDb = RSAUtils.decryptDataOnJava(passwordDb, PRIVATEKEY);
        // if (!passwordIn.equals(decryPasswordDb)) {
        //     return "登录失败";
        // }

        // md5方案
        if (!PasswordEncoder.validPassword(passwordIn, passwordDb)) {
            return "登录失败";
        }

        // 认证成功，删除 session中的私钥
        session.removeAttribute("privateKey");
        return "登录成功";
    }

    /**
     * 注册
     * 如果是通过浏览器直接请求的方式，需要先将密码经过 base64加密，否则其中的一些特殊符号将会丢失
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/register")
    @ResponseBody
    public String register(@NotBlank String username, @NotBlank String password, HttpSession session){
        // 解决参数中的 +符号被过滤掉
        // try {
        //     byte[] decode = Base64Utils.decode(password);
        //     password = new String(decode, "UTF-8");
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // 用 session中的私钥解密
        String privateKey = (String) session.getAttribute("privateKey");
        if (StringUtils.isEmpty(privateKey)) {
            return "注册失败 session失效";
        }
        // 解密为原密码
        String passwordIn = RSAUtils.decryptDataOnJava(password, privateKey);

        // rsa方案
        // 根据后端的公钥加密（每次加密的结果都是不同的）
        // String passwordInEncrypt = RSAUtils.encryptedDataOnJava(passwordIn, PUBLICKEY);

        // md5方案
        String passwordInEncrypt = PasswordEncoder.getEncryptedPwd(passwordIn);

        // 入库
        User user = new User();
        user.setPassword(passwordInEncrypt);
        user.setUsername(username);
        userRepository.save(user);
        // 注册成功，删除 session中的私钥
        session.removeAttribute("privateKey");
        return "注册成功";
    }
}
