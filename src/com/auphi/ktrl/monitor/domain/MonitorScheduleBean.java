/*******************************************************************************
 *
 * Auphi Data Integration PlatformKettle Platform
 * Copyright C 2011-2017 by Auphi BI : http://www.doetl.com 

 * Support：support@pentahochina.com
 *
 *******************************************************************************
 *
 * Licensed under the LGPL License, Version 3.0 the "License";
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    https://opensource.org/licenses/LGPL-3.0 

 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package com.auphi.ktrl.monitor.domain;

import com.auphi.ktrl.quality.base.BaseEntity;

import java.util.Date;

public class MonitorScheduleBean extends BaseEntity {
	private Integer id;
	private String jobName;
	private String jobGroup;
	private String jobFile;
	private String jobStatus;
	private Date startTime;
	private Date endTime;
	private float continuedTime;
	private String logMsg;
	private String errMsg;
	private String haName;
	private Integer serverId;
	private String serverName;
	private Integer id_batch;
	private String id_logchannel;
	private Integer lines_error;
	private Long lines_input;
	private Long lines_output;
	private Long lines_updated;
	private Long lines_read;
	private Long lines_written;
	private Long lines_deleted;
	private String userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobFile() {
		return jobFile;
	}

	public void setJobFile(String jobFile) {
		this.jobFile = jobFile;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public float getContinuedTime() {
		return continuedTime;
	}

	public void setContinuedTime(float continuedTime) {
		this.continuedTime = continuedTime;
	}

	public String getLogMsg() {
		return logMsg;
	}

	public void setLogMsg(String logMsg) {
		this.logMsg = logMsg;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getHaName() {
		return haName;
	}

	public void setHaName(String haName) {
		this.haName = haName;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Integer getId_batch() {
		return id_batch;
	}

	public void setId_batch(Integer id_batch) {
		this.id_batch = id_batch;
	}

	public String getId_logchannel() {
		return id_logchannel;
	}

	public void setId_logchannel(String id_logchannel) {
		this.id_logchannel = id_logchannel;
	}

	public Integer getLines_error() {
		return lines_error;
	}

	public void setLines_error(Integer lines_error) {
		this.lines_error = lines_error;
	}

	public Long getLines_input() {
		return lines_input;
	}

	public void setLines_input(Long lines_input) {
		this.lines_input = lines_input;
	}

	public Long getLines_output() {
		return lines_output;
	}

	public void setLines_output(Long lines_output) {
		this.lines_output = lines_output;
	}

	public Long getLines_updated() {
		return lines_updated;
	}

	public void setLines_updated(Long lines_updated) {
		this.lines_updated = lines_updated;
	}

	public Long getLines_read() {
		return lines_read;
	}

	public void setLines_read(Long lines_read) {
		this.lines_read = lines_read;
	}

	public Long getLines_written() {
		return lines_written;
	}

	public void setLines_written(Long lines_written) {
		this.lines_written = lines_written;
	}

	public Long getLines_deleted() {
		return lines_deleted;
	}

	public void setLines_deleted(Long lines_deleted) {
		this.lines_deleted = lines_deleted;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
