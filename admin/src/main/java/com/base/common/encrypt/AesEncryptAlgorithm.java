package com.base.common.encrypt;

import com.base.common.utils.AesEncryptUtils;

/**
 * Aes加密算法实现
 *
 * @author yinjihuan
 *
 * @date 2019-01-12
 *
 * @about http://cxytiandi.com/about
 *
 */
public class AesEncryptAlgorithm implements EncryptAlgorithm {

    @Override
    public String encrypt(String content, String encryptKey) throws Exception{
        try {
            return  AesEncryptUtils.aesEncrypt(content, encryptKey);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("参数加密失败。");
        }
    }

    @Override
    public String decrypt(String encryptStr, String decryptKey) throws Exception {
        try {
            return AesEncryptUtils.aesDecrypt(encryptStr, decryptKey);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("参数解密失败。");
        }
    }

}
