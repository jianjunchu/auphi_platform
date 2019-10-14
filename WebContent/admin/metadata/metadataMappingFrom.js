var store_reload;//表格刷新方法

var fromAllowBlank = true;

var mappingGroupIdStore = new Ext.data.JsonStore({
    fields: ['id', 'mappingGroupName'],
    url : "../metadataMapping/getMappingGroupList.shtml",
    autoLoad:true,
    root : "data"
})

var extractStyleStore = new Ext.data.SimpleStore({
        fields:['value','text'],
        data:[[0,'全量'], [1,'增量']]
    }
)

var yesNoStore = new Ext.data.SimpleStore({
        fields:['value','text'],
        data:[[0,'否'], [1,'是']]
    }
)

//字段类型Store
var fieldTypeStore = new Ext.data.JsonStore({
    fields: ['fieldTypeShow', 'fieldTypeValue'],
    url : "../mdmModelAttribute/getFieldType.shtml",
    autoLoad:true,
    root : ""
});

var dbIdStore = new Ext.data.JsonStore({
    fields: ['sourceId', 'sourceName'],
    url : "../datasource/getDataSourceList.shtml",
    autoLoad:true,
    root : ""
})

var sourceColumnTypeStore = new Ext.data.JsonStore({
    fields:['value','text'],
    url : "../metadataMapping/getTypeInfo.shtml",
    root : "data"
});

var destColumnTypeStore = new Ext.data.JsonStore({
    fields:['value','text'],
    url : "../metadataMapping/getTypeInfo.shtml",
    root : "data"
});


var mappingGroupIdComboBox = new Ext.form.ComboBox({
    fieldLabel : '所属组',
    emptyText:'请选择所属组',
    xtype:'combobox',
    hiddenName : "mappingGroupId",
    name:'mappingGroupId',
    forceSelection: true,
    anchor : '100%',
    store: mappingGroupIdStore,
    valueField : "id",
    displayField : "mappingGroupName",
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    editable:false,
    allowBlank : false,
    selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
    resizable : true,
    width: 120
});



var extractStyleComboBox = new Ext.form.ComboBox({
    fieldLabel : '抽取方式',
    emptyText : '请选择抽取方式',
    hiddenName : 'extractStyle',
    forceSelection: true,
    anchor : '100%',
    store: extractStyleStore,
    valueField : "value",
    displayField : "text",
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    editable:false,
    allowBlank : false,
    selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
    resizable : true,
    width: 120
});



var isIncrementalColumnComboBox = new Ext.form.ComboBox({
    fieldLabel : '增量字段',
    hiddenName : 'isIncrementalColumn',
    forceSelection: true,
    anchor : '100%',
    store: yesNoStore,
    valueField : "value",
    displayField : "text",
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    editable:false,
    allowBlank : fromAllowBlank,
    selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
    resizable : true,
    width: 120
});

var isPkComboBox = new Ext.form.ComboBox({
    fieldLabel : '主键',
    hiddenName : 'isPk',
    forceSelection: true,
    anchor : '100%',
    store: yesNoStore,
    valueField : "value",
    displayField : "text",
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    editable:false,
    allowBlank : fromAllowBlank,
    selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
    resizable : true,
    width: 120
});



// 数据源
var sourceDbIdComboBox = new Ext.form.ComboBox({
    fieldLabel : '源数据库',
    emptyText:'请选择源数据库',
    hiddenName : "sourceDbId",
    forceSelection: true,
    anchor : '100%',
    store: dbIdStore,
    valueField : "sourceId",
    displayField : "sourceName",
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
    allowBlank : false,
    resizable : true,
    editable:false,
    listeners: {
        select : function(comboBox, record,index){
            sourceSchemaNameComboBox.clearValue();
            sourceTableNameComboBox.clearValue();
            var id_database = comboBox.value;
            sourceSchemaNameComboBox.store.load({
                params : {id_database : id_database}
            });
        }
    }
});



var schemaNameStore = new Ext.data.JsonStore({
    fields: ['value', 'text'],
    url : "../mdmTable/getSchemaName.shtml",
    autoLoad:true,
    root : ""
});

var sourceSchemaNameComboBox = new Ext.form.ComboBox({
    fieldLabel : '源模式名',
    emptyText:'请选择源模式名',
    hiddenName : "sourceSchemaName",
    forceSelection: true,
    anchor : '100%',
    store: schemaNameStore,
    valueField : "value",
    displayField : "text",
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
    resizable : true,
    editable:false
});





var sourceTableNameComboBox =  new Ext.form.ComboBoxTree ({
    name : 'sourceTableName',
    treeUrl:"../datasource/tables.shtml",
    fieldLabel : '源表名',
    rootText:"源表列表",
    rootId : '-1',
    valueField : "id",
    anchor : '100%'
});

sourceTableNameComboBox.on('expand',function (combo) {

    var loader = sourceTableNameComboBox.tree.getLoader();
    loader.on('beforeload', function(loader, node) {
        this.baseParams.repId = 1; // 通过这个传递参数，这样就可以点一个节点出来它的子节点来实现异步加载
        this.baseParams.databaseId = sourceDbIdComboBox.getValue();
        this.baseParams.schemanamein = sourceSchemaNameComboBox.getValue();
        this.baseParams.tableName = sourceTableNameComboBox.emp.toString();
    }, loader);


    loader.load(sourceTableNameComboBox.tree.root);
    sourceTableNameComboBox.tree.expandAll();
});


var destDbIdComboBox = new Ext.form.ComboBox({
    fieldLabel : '目的数据库',
    emptyText:'请选择目的数据库',
    hiddenName : "destDbId",
    forceSelection: true,
    anchor : '100%',
    store: dbIdStore,
    valueField : "sourceId",
    displayField : "sourceName",
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
    resizable : true,
    editable:false,
    listeners: {
        select : function(comboBox, record,index){
            destSchemaNameComboBox.clearValue();
            var id_database = comboBox.value;
            destSchemaNameComboBox.store.load({
                params : {id_database : id_database}

            });
        }
    }
});



var destSchemaNameComboBox = new Ext.form.ComboBox({
    fieldLabel : '目的模式名',
    emptyText:'请选择目的模式名',
    hiddenName : "destSchemaName",
    forceSelection: true,
    anchor : '100%',
    store: schemaNameStore,
    valueField : "value",
    displayField : "text",
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
    resizable : true,
    editable:false
});




var destTableNameTextField = {
    xtype:'textfield',
    fieldLabel : '目的表名',
    name : 'destTableName',
    anchor : '100%'
}

var sourceColumnNameTextField = {
    xtype:'textfield',
    fieldLabel : '源列名',
    name : 'sourceColumnName',
    anchor : '100%'
}

var destColumnNameTextField = {
    xtype:'textfield',
    fieldLabel : '目标列名',
    name : 'destColumnName',
    anchor : '100%'
}

var sourceColumnTypeComboBox = new Ext.form.ComboBox({
    name:'sourceColumnType',
    fieldLabel:'源列类型',
    hiddenName : "sourceColumnType",
    forceSelection: true,
    anchor : '100%',
    store: sourceColumnTypeStore,
    valueField : "value",
    displayField : "text",
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
    resizable : true,
    editable:false

})

var destColumnTypeComboBox = new Ext.form.ComboBox({
    name:'destColumnType',
    fieldLabel:'目标列类型',
    hiddenName : "sourceColumnType",
    forceSelection: true,
    anchor : '100%',
    store: destColumnTypeStore,
    valueField : "value",
    displayField : "text",
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
    resizable : true,
    editable:false

})



var  sourceColumnTypeEtlComboBox = new Ext.form.ComboBox({
    fieldLabel : '源列Kettle数据类型',
    anchor : '100%',
    store: fieldTypeStore,
    hiddenName : 'sourceColumnTypeEtl',
    valueField : "fieldTypeValue",
    displayField : "fieldTypeShow",
    mode: 'local',
    triggerAction: 'all',
    forceSelection: true,
    typeAhead: true,
    editable:false,
    selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
    resizable : true
})

var  destColumnTypeEtlComboBox = new Ext.form.ComboBox({
    fieldLabel : '目标列Kettle数据类型',
    anchor : '100%',
    store: fieldTypeStore,
    hiddenName : 'destColumnTypeEtl',
    valueField : "fieldTypeValue",
    displayField : "fieldTypeShow",
    mode: 'local',
    triggerAction: 'all',
    forceSelection: true,
    typeAhead: true,
    editable:false,
    selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
    resizable : true
})

var sourceColumnLengthTextField = {
    xtype:'textfield',
    fieldLabel : '源列长度',
    name : 'sourceColumnLength',
    anchor : '100%'
}

var destColumnLengthTextField = {
    xtype:'textfield',
    fieldLabel : '目标列长度',
    name : 'destColumnLength',
    anchor : '100%'
}

var sourceColumnScaleTextField = {
    xtype:'textfield',
    fieldLabel : '源列精度',
    name : 'sourceColumnScale',
    anchor : '100%'
}

var destColumnScaleTextField = {
    xtype:'textfield',
    fieldLabel : '目标列精度',
    name : 'destColumnScale',
    anchor : '100%'
}

var sourceColumnCommentsTextField = {
    xtype:'textfield',
    fieldLabel : '列注释',
    name : 'sourceColumnComments',
    anchor : '100%'
}

var sourceColumnOrderTextField = new Ext.form.NumberField({
    fieldLabel : '列排序',
    name : 'sourceColumnOrder',
    anchor : '100%'
})


var metadataMappingFromPanel = new Ext.form.FormPanel( {
    id : 'metadataMappingFromPanel',
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
            name : 'id',
            hidden : true
        }]
    },{
        xtype:'fieldset',
        title: '分组信息',
        autoHeight:true,
        anchor : '99.5%',
        collapsed: false,
        items:[mappingGroupIdComboBox,extractStyleComboBox]
    },{
        xtype:'fieldset',
        title: '表信息',
        autoHeight:true,
        anchor : '99.5%',
        collapsed: false,
        items:[{
            layout : 'column',
            border : false,
            anchor : '100%',
            items:[{
                columnWidth:.5,
                layout: 'form',
                border:false,
                items: [{
                    xtype:'fieldset',
                    title: '源表信息',
                    autoHeight:true,
                    anchor : '99%',
                    collapsed: false,
                    items:[sourceDbIdComboBox,sourceSchemaNameComboBox,sourceTableNameComboBox]
                }]//分组
            },{
                columnWidth:.5,
                layout: 'form',
                border:false,
                items: [{
                    xtype:'fieldset',
                    title: '目标表信息',
                    autoHeight:true,
                    anchor : '99%',
                    collapsed: false,
                    items:[destDbIdComboBox,destSchemaNameComboBox,destTableNameTextField]
                }]

            }]
        }]
    },{
        id:'column_fieldset',
        xtype:'fieldset',
        title: '列信息',
        autoHeight:true,
        anchor : '99.5%',
        collapsed: false,
        items:[{
            layout : 'column',
            border : false,
            anchor : '100%',
            items:[{
                columnWidth:.5,
                layout: 'form',
                border:false,
                items: [isPkComboBox,sourceColumnCommentsTextField]//分组
            },{
                columnWidth:.5,
                layout: 'form',
                border:false,
                items: [isIncrementalColumnComboBox,sourceColumnOrderTextField]

            }]
        },{
            layout : 'column',
            border : false,
            anchor : '100%',
            items:[{
                columnWidth:.5,
                layout: 'form',
                border:false,
                items: [{
                    xtype:'fieldset',
                    title: '源表列信息',
                    autoHeight:true,
                    anchor : '99%',
                    collapsed: false,
                    items:[
                        sourceColumnNameTextField,
                        sourceColumnTypeComboBox,
                        sourceColumnTypeEtlComboBox,
                        sourceColumnLengthTextField,
                        sourceColumnScaleTextField
                    ]
                }]//分组
            },{
                columnWidth:.5,
                layout: 'form',
                border:false,
                items: [{
                    xtype: 'fieldset',
                    title: '目标表列信息',
                    autoHeight: true,
                    anchor: '99%',
                    collapsed: false,
                    items: [
                        destColumnNameTextField,
                        destColumnTypeComboBox,
                        destColumnTypeEtlComboBox,
                        destColumnLengthTextField,
                        destColumnScaleTextField
                    ]
                }]

            }]
        }]
    }]
});


/**
 * 编辑窗口
 */
var metadataMappingFromWindow = new Ext.Window({
    layout: 'fit', // 设置窗口布局模式
    title:'<span class="commoncss">新增</span>',
    width: 760, // 窗口宽度
    height: 530, // 窗口高度
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
    items: [metadataMappingFromPanel], // 嵌入的表单面板
    buttons: [{
        text: '保存',
        iconCls: 'acceptIcon',
        handler: function () {
            submitCreateForm();
        }
    }, {
        text: '重置',
        id: 'btnReset',
        iconCls: 'tbar_synchronizeIcon',
        handler: function () {
            clearForm(metadataMappingFromPanel.getForm());
        }
    }, {
        text: '关闭',
        iconCls: 'deleteIcon',
        handler: function () {
            metadataMappingFromWindow.hide();
        }
    }]
});


function showCreateMetadataMappingFromWindow(reload){
    store_reload = reload;

    metadataMappingFromWindow.show();

}



function submitCreateForm(){
    if (!metadataMappingFromPanel.getForm().isValid())
        return;

    metadataMappingFromPanel.form.submit({
        url : '../metadataMapping/save.shtml',
        waitTitle : '提示',
        method : 'POST',
        waitMsg : '正在处理数据,请稍候...',
        success : function(form, action) { // 回调函数有2个参数
            metadataMappingFromWindow.hide();
            store_reload(false);
        },
        failure : function(form, action) {
            Ext.Msg.alert('提示', action.result.msg);
        }
    });

}


