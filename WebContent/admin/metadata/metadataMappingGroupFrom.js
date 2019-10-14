var store_reload;//表格刷新方法


var mappingGroupName = {
    xtype:'textfield',
    id:'mappingGroupName',
    fieldLabel : '分组名称',
    name : 'mappingGroupName',

    allowBlank : false,
    anchor : '100%'
}

var mappingGroupDesc = {
    xtype:'textarea',
    id:'mappingGroupDesc',
    fieldLabel : '分组描述',
    name : 'mappingGroupDesc',

    allowBlank : false,
    anchor : '100%'
}





var metadataMappingGroupFromPanel = new Ext.form.FormPanel( {

    id : 'metadataMappingGroupFromPanel',

    autoScroll:true,
    collapsible : false,
    border : true,
    labelWidth : 80, // 标签宽度
    frame : true, // 是否渲染表单面板背景色
    labelAlign : 'right', // 标签对齐方式
    bodyStyle : 'padding:0 0 0 0', // 表单元素和表单面板的边距
    buttonAlign : 'center',
    items : [{
        columnWidth:.01,  //该列占用的宽度，标识为50％
        layout: 'form',
        defaultType : 'textfield',
        border:false,
        items: [{
            id : 'windowAction',
            name : 'windowAction',
            hidden : true
        },{
            id : 'id',
            name : 'id',
            hidden : true
        }]
    },{
        xtype:'fieldset',
        title: '分组信息',
        autoHeight:true,
        anchor : '97%',
        collapsed: false,
        items:[mappingGroupName,mappingGroupDesc]
    }]
});


/**
 * 编辑窗口
 */
var metadataMappingGroupFromWindow = new Ext.Window({
    layout: 'fit', // 设置窗口布局模式
    width: 400, // 窗口宽度
    height: 300, // 窗口高度
    modal: true,
    resizable: false,// 是否可以改变大小，默认可以
    maskdisabled: true,
    closeAction: 'hide',
    closable: true, // 是否可关闭
    collapsible: true, // 是否可收缩
    border: false, // 边框线设置
    constrain: true, // 设置窗口是否可以溢出父容器
    animateTarget: Ext.getBody(),
    pageY: 20, // 页面定位Y坐标
    pageX: document.body.clientWidth / 2 - 900 / 2, // 页面定位X坐标
    items: [metadataMappingGroupFromPanel], // 嵌入的表单面板
    buttons: [{
        text: '保存',
        iconCls: 'acceptIcon',
        handler: function () {
            submitTheForm();
        }
    }, {
        text: '重置',
        id: 'btnReset',
        iconCls: 'tbar_synchronizeIcon',
        handler: function () {
            clearForm(metadataMappingGroupFromPanel.getForm());
        }
    }, {
        text: '关闭',
        iconCls: 'deleteIcon',
        handler: function () {
            metadataMappingGroupFromWindow.hide();
        }
    }]
});


function showMetadataMappingGroupFromWindow(reload){
    store_reload = reload;

    metadataMappingGroupFromWindow.show();

}



function submitTheForm(){
    if (!metadataMappingGroupFromPanel.getForm().isValid())
        return;


    metadataMappingGroupFromPanel.form.submit({
        url : '../metadataMappingGroup/save.shtml',
        waitTitle : '提示',
        method : 'POST',
        waitMsg : '正在处理数据,请稍候...',
        success : function(form, action) { // 回调函数有2个参数
            metadataMappingGroupFromWindow.hide();
            store_reload(false);
        },
        failure : function(form, action) {
            Ext.Msg.alert('提示', action.result.msg);
        }
    });



}