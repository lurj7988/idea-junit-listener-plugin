/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.original.idea.junit.entity;

import java.io.Serial;
import java.util.Date;
import java.util.List;

public class SurefireUploadData implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Date buildTime;
    private String buildBy;
    private String buildTool;
    private String builtJdk;
    private String buildOs;
    private String hostName;
    private String hostAddress;
    private Date gitCommitTime;
    private String gitRemoteOriginUrl;
    private String gitBranche;
    private String gitCommitId;
    private Date svnCommittedDate;
    private String svnRepository;
    private String svnPath;
    private String svnCommittedRevision;
    private List<ReportTestSuite> reportTestSuites;
    private List<Counter> counters;

    public Date getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
    }

    public String getBuildBy() {
        return buildBy;
    }

    public void setBuildBy(String buildBy) {
        this.buildBy = buildBy;
    }

    public String getBuildTool() {
        return buildTool;
    }

    public void setBuildTool(String buildTool) {
        this.buildTool = buildTool;
    }

    public String getBuiltJdk() {
        return builtJdk;
    }

    public void setBuiltJdk(String builtJdk) {
        this.builtJdk = builtJdk;
    }

    public String getBuildOs() {
        return buildOs;
    }

    public void setBuildOs(String buildOs) {
        this.buildOs = buildOs;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public Date getGitCommitTime() {
        return gitCommitTime;
    }

    public void setGitCommitTime(Date gitCommitTime) {
        this.gitCommitTime = gitCommitTime;
    }

    public String getGitRemoteOriginUrl() {
        return gitRemoteOriginUrl;
    }

    public void setGitRemoteOriginUrl(String gitRemoteOriginUrl) {
        this.gitRemoteOriginUrl = gitRemoteOriginUrl;
    }

    public String getGitBranche() {
        return gitBranche;
    }

    public void setGitBranche(String gitBranche) {
        this.gitBranche = gitBranche;
    }

    public String getGitCommitId() {
        return gitCommitId;
    }

    public void setGitCommitId(String gitCommitId) {
        this.gitCommitId = gitCommitId;
    }

    public Date getSvnCommittedDate() {
        return svnCommittedDate;
    }

    public void setSvnCommittedDate(Date svnCommittedDate) {
        this.svnCommittedDate = svnCommittedDate;
    }

    public String getSvnRepository() {
        return svnRepository;
    }

    public void setSvnRepository(String svnRepository) {
        this.svnRepository = svnRepository;
    }

    public String getSvnPath() {
        return svnPath;
    }

    public void setSvnPath(String svnPath) {
        this.svnPath = svnPath;
    }

    public String getSvnCommittedRevision() {
        return svnCommittedRevision;
    }

    public void setSvnCommittedRevision(String svnCommittedRevision) {
        this.svnCommittedRevision = svnCommittedRevision;
    }

    public List<ReportTestSuite> getReportTestSuites() {
        return reportTestSuites;
    }

    public void setReportTestSuites(List<ReportTestSuite> reportTestSuites) {
        this.reportTestSuites = reportTestSuites;
    }

    public List<Counter> getCounters() {
        return counters;
    }

    public void setCounters(List<Counter> counters) {
        this.counters = counters;
    }
}
