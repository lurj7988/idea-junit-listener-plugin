package com.original.idea.junit.listener;

import com.alibaba.fastjson.JSON;
import com.intellij.execution.testframework.AbstractTestProxy;
import com.intellij.execution.testframework.TestStatusListener;
import com.intellij.execution.testframework.sm.runner.SMTestProxy;
import com.original.idea.junit.entity.ReportTestCase;
import com.original.idea.junit.entity.ReportTestSuite;
import com.original.idea.junit.entity.SurefireUploadData;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class JunitTestStatusListener extends TestStatusListener {
    @Override
    public void testSuiteFinished(@Nullable AbstractTestProxy root) {
        SurefireUploadData surefireUploadData = new SurefireUploadData();
        surefireUploadData.setBuildTime(new Date());
        try {
            InetAddress addr = InetAddress.getLocalHost();
            surefireUploadData.setHostName(addr.getHostName());
            surefireUploadData.setHostAddress(addr.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        surefireUploadData.setBuildBy(System.getProperty("user.name"));
        surefireUploadData.setBuildOs(String.format("%s (%s; %s)", System.getProperty("os.name"),
                System.getProperty("os.version"), System.getProperty("os.arch")));
        surefireUploadData.setBuiltJdk(
                String.format("%s (%s)", System.getProperty("java.version"), System.getProperty("java.vendor")));
        List<ReportTestSuite> reportTestSuites = new ArrayList<>();
        if (root instanceof SMTestProxy.SMRootTestProxy smRootTestProxy) {
            String LocationUrl = Optional.ofNullable(smRootTestProxy.getLocationUrl()).orElse("");
            String fullClassName = LocationUrl.replace("java:suite://", "");
            //if the default package is root, it will not be parsed
            if ("<default package>".equals(fullClassName)) {
                //every test class
                List<? extends SMTestProxy> smTestProxies = smRootTestProxy.getChildren();
                for (SMTestProxy smTestProxy : smTestProxies) {
                    reportTestSuites.add(getReportTestSuite(smTestProxy));
                }
            } else {
                reportTestSuites.add(getReportTestSuite(smRootTestProxy));
            }
        }
        surefireUploadData.setReportTestSuites(reportTestSuites);
        //System.out.println(JSON.toJSONString(surefireUploadData, true));
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost httpPost = new HttpPost("https://fdoc.epoint.com.cn/SystemInfoDetectTool/rest/junit/report");
            String json = JSON.toJSONString(surefireUploadData);
            System.out.println(json);
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            //System.out.println("surefire reports upload " + httpClient.execute(httpPost, new BasicHttpClientResponseHandler()));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println("surefire reports upload " + EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ReportTestSuite getReportTestSuite(SMTestProxy smTestProxy) {
        ReportTestSuite reportTestSuite = new ReportTestSuite();
        String LocationUrl = Optional.ofNullable(smTestProxy.getLocationUrl()).orElse("");
        String fullClassName = LocationUrl.replace("java:suite://", "");
        reportTestSuite.setFullClassName(fullClassName);
        reportTestSuite.setPackageName(reportTestSuite.getFullClassName().substring(0, reportTestSuite.getFullClassName().lastIndexOf('.')));
        if (smTestProxy instanceof SMTestProxy.SMRootTestProxy) {
            reportTestSuite.setName(((SMTestProxy.SMRootTestProxy) smTestProxy).getPresentation());
        } else {
            reportTestSuite.setName(smTestProxy.getName());
        }
        reportTestSuite.setTimeElapsed(Optional.ofNullable(smTestProxy.getDuration()).orElse(0L).floatValue() / 1000);
        int numberOfTests = 0;
        int numberOfErrors = 0;
        int numberOfFailures = 0;
        int numberOfSkipped = 0;
        int numberOfFlakes = 0;
        List<ReportTestCase> reportTestCases = new ArrayList<>();
        for (SMTestProxy childSMTestProxy : smTestProxy.getChildren()) {
            if (childSMTestProxy.getChildren().size() == 0) {
                numberOfTests++;
                ReportTestCase reportTestCase = new ReportTestCase();
                reportTestCase.setClassName(reportTestSuite.getFullClassName().substring(reportTestSuite.getFullClassName().lastIndexOf('.') + 1));
                reportTestCase.setFullClassName(reportTestSuite.getFullClassName());
                reportTestCase.setName(childSMTestProxy.getName());
                reportTestCase.setFullName(Optional.ofNullable(childSMTestProxy.getLocationUrl()).orElse("").replace("java:test://", ""));
                reportTestCase.setTime(Optional.ofNullable(childSMTestProxy.getDuration()).orElse(0L).floatValue() / 1000);
                if (childSMTestProxy.isPassed()) {
                    reportTestCase.setSuccessful(true);
                } else {
                    numberOfErrors++;
                    reportTestCase.setSuccessful(false);
                    reportTestCase.setFailureDetail(childSMTestProxy.getErrorMessage());
                }
                if (childSMTestProxy.isIgnored()) {
                    numberOfSkipped++;
                    reportTestCase.setSuccessful(false);
                }
                reportTestCases.add(reportTestCase);
            }
            //如何还有子类
            for (SMTestProxy grandChildSMTestProxy : childSMTestProxy.getChildren()) {
                numberOfTests++;
                ReportTestCase reportTestCase2 = new ReportTestCase();
                reportTestCase2.setClassName(reportTestSuite.getFullClassName().substring(reportTestSuite.getFullClassName().lastIndexOf('.') + 1));
                reportTestCase2.setFullClassName(reportTestSuite.getFullClassName());
                reportTestCase2.setName(grandChildSMTestProxy.getName());
                reportTestCase2.setFullName(Optional.ofNullable(grandChildSMTestProxy.getLocationUrl()).orElse("").replace("java:test://", ""));
                reportTestCase2.setTime(Optional.ofNullable(grandChildSMTestProxy.getDuration()).orElse(0L).floatValue() / 1000);
                if (grandChildSMTestProxy.isPassed()) {
                    reportTestCase2.setSuccessful(true);
                } else {
                    numberOfErrors++;
                    reportTestCase2.setSuccessful(false);
                    reportTestCase2.setFailureDetail(grandChildSMTestProxy.getErrorMessage());
                }
                if (grandChildSMTestProxy.isIgnored()) {
                    numberOfSkipped++;
                    reportTestCase2.setSuccessful(false);
                }
                reportTestCases.add(reportTestCase2);
            }
        }
        reportTestSuite.setTestCases(reportTestCases);
        reportTestSuite.setNumberOfTests(numberOfTests);
        reportTestSuite.setNumberOfErrors(numberOfErrors);
        reportTestSuite.setNumberOfFailures(numberOfFailures);
        reportTestSuite.setNumberOfSkipped(numberOfSkipped);
        reportTestSuite.setNumberOfFlakes(numberOfFlakes);
        return reportTestSuite;
    }
}
