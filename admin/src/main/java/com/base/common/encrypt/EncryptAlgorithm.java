package com.base.common.encrypt;

/**
 * 加密算法接口
 * @ClassName:  EncryptAlgorithm
 * @Description:加密算法接口
 * @author:  huanw
 * @date:   2019年8月14日 下午3:09:28
 */
public interface EncryptAlgorithm {

    /**
     * 加密
     * @param content
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public String encrypt(String content, String encryptKey) throws Exception;

    /**
     * 解密
     * @param encryptStr
     * @param decryptKey
     * @return
     * @throws Exception
     */
    public String decrypt(String encryptStr, String decryptKey) throws Exception;

}
