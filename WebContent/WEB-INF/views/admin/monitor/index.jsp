<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 2019-08-19
  Time: 10:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<skyform:html title="作业监控" uxEnabled="true">
    <skyform:import src="/resource/extjs3.1/ux/Ext.ux.form.LovCombo.css" />
    <skyform:import src="/styles/monitor.css" />
    <skyform:import src="/resource/DateTime/DateTimeField.js"/>
    <skyform:import src="/resource/extjs3.1/ux/ComboBoxTree.js"/>
    <skyform:import src="/admin/monitor/monitorGrid.js"/>

    <skyform:body>
        <input type="hidden" id="jobName" value="${jobName}">
    </skyform:body>

</skyform:html>