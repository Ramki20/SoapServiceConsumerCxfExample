package com.example.soap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "soap.authorization.service")
public class SoapProperties {

    private String url;
    private long timeout = 30000;
    private long connectionTimeout = 10000;
    private long receiveTimeout = 30000;
    private boolean allowChunking = false;
    private int maxRetries = 3;
    private boolean enableLogging = true;

    // Getters and Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public boolean isAllowChunking() {
        return allowChunking;
    }

    public void setAllowChunking(boolean allowChunking) {
        this.allowChunking = allowChunking;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }

    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    @Override
    public String toString() {
        return "SoapProperties{" +
                "url='" + url + '\'' +
                ", timeout=" + timeout +
                ", connectionTimeout=" + connectionTimeout +
                ", receiveTimeout=" + receiveTimeout +
                ", allowChunking=" + allowChunking +
                ", maxRetries=" + maxRetries +
                ", enableLogging=" + enableLogging +
                '}';
    }
}