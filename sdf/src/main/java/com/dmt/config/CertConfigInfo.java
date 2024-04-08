package com.dmt.config;

import org.bouncycastle.asn1.x500.X500Name;

import java.math.BigInteger;
import java.util.Date;


/**
 * 证书配置参数类
 * 作为生成主站证书和终端证书参数输入
 * 可用于配置证书生成所需的颁发者信息、使用者信息、证书生效日期、证书失效日期、证书序列号
 */
public class CertConfigInfo {

    private X500Name issuer;    //颁发者信息
    private X500Name subject;   //使用者信息
    private Date startDate;     //证书生效日期
    private Date endDate;       //证书失效日期、证书序列号
    private BigInteger serialNumber;   //证书序列号


    public CertConfigInfo(Builder builder) {

        this.issuer = builder.issuer;
        this.subject = builder.subject;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.serialNumber = builder.serialNumber;
    }

    public X500Name getIssuer() {
        return issuer;
    }

    public X500Name getSubject() {
        return subject;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public static class Builder{

        private X500Name issuer;
        private X500Name subject;
        private Date startDate;
        private Date endDate;
        private BigInteger serialNumber;

        public Builder(){}

        public Builder(X500Name issuer, X500Name subject, Date startDate, Date endDate, BigInteger serialNumber) {
            this.issuer = issuer;
            this.subject = subject;
            this.startDate = startDate;
            this.endDate = endDate;
            this.serialNumber = serialNumber;
        }

        /**
         * 配置颁发者信息
         * @param issuer
         * @return
         */
        public Builder setIssuer(X500Name issuer) {
            this.issuer = issuer;
            return this;
        }

        /**
         * 配置使用者信息
         * @param subject
         * @return
         */
        public Builder setSubject(X500Name subject) {
            this.subject = subject;
            return this;
        }

        /**
         * 配置证书生效日期
         * @param startDate
         * @return
         */
        public Builder setStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        /**
         * 配置证书失效日期
         * @param endDate
         * @return
         */
        public Builder setEndDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        /**
         * 配置证书序列号
         * @param serialNumber
         * @return
         */
        public Builder setSerialNumber(BigInteger serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        /**
         * 创建并返回外部类对象
         * @return
         */
        public CertConfigInfo build() {
            return new CertConfigInfo(this);
        }
    }
}
