Ext.onReady(function(){

    // 定义列模型
    var sm = new Ext.grid.CheckboxSelectionModel();
    var rownum = new Ext.grid.RowNumberer( {
        header : 'NO',
        width : 28
    });

    var cm = new Ext.grid.ColumnModel( [sm, rownum,
        {
            header : '映射分组',
            dataIndex : 'mappingGroupName'
        },{
            header : '源数据库',
            dataIndex : 'sourceDbName'
        },{
            header : '源模式名',
            dataIndex : 'sourceSchemaName'
        },{
            header : '源表名',
            dataIndex : 'sourceTableName'
        },{
            header : '目的数据库',
            dataIndex : 'destDbName'
        },{
            header : '目的模式名',
            dataIndex : 'destSchemaName'
        },{
            header : '目的表名',
            dataIndex: "destTableName"
        },{
            header : '最后开始时间',
            dataIndex : 'lastExtractStartTime'
        },{
            header : '最后结束时间',
            dataIndex : 'lastExtractEndTime'
        }
    ]);



    var store = new Ext.data.Store( {
        proxy : new Ext.data.HttpProxy( {
            url : '../metadataMapping/query.shtml',
            timeout:1800000000
        }),
        reader : new Ext.data.JsonReader( {
            totalProperty : 'total',
            root : 'rows'
        }, [ {
            name : 'id'
        },{
            name : 'mappingGroupId'
        },{
            name:'mappingGroupName'
        },{
            name:'sourceDbId'
        },{
            name : 'sourceDbName'
        },{
            name:'sourceSchemaName'
        },{
            name:'sourceTableName'
        },{
            name : 'sourceColumnName'
        },{
            name:'sourceColumnType'
        },{
            name : 'sourceColumnTypeEtl'
        },{
            name : 'sourceColumnLength'
        },{
            name:'sourceColumnScale'
        },{
            name:'sourceColumnComments'
        },{
            name:'sourceColumnOrder'
        },{
            name:'isPk'
        },{
            name:'destDbId'
        },{
            name:'destDbName'
        },{
            name:'destSchemaName'
        },{
            name:'destTableName'
        },{
            name:'destColumnName'
        },{
            name:'destColumnType'
        },{
            name:'destColumnTypeEtl'
        },{
            name:'destColumnLength'
        },{
            name:'destColumnScale'
        },{
            name:'extractStyle'
        },{
            name:'extractStatus'
        },{
            name:'lastExtractStartTime'
        },{
            name:'lastExtractEndTime'
        },{
            name:'createTime'
        },{
            name:'createUser'
        },{
            name:'updateTime'
        },{
            name:'updateUser'
        }]),
        listeners: {
            datachanged: function() {
                //autoCheckGridHead(Ext.getCmp('id_grid_sfxm'));
            }
        }
    });


    var pagesize_combo = new Ext.form.ComboBox({
        name : 'pagesize',
        hiddenName : 'pagesize',
        typeAhead : true,
        triggerAction : 'all',
        lazyRender : true,
        mode : 'local',
        store : new Ext.data.ArrayStore({
            fields : ['value', 'text'],
            data : [[10, '10条/页'], [20, '20条/页'],
                [50, '50条/页'], [100, '100条/页'],
                [250, '250条/页'], [500, '500条/页']]
        }),
        valueField : 'value',
        displayField : 'text',
        value : '50',
        editable : false,
        width : 100
    });

    var number = parseInt(pagesize_combo.getValue());
    // 改变每页显示条数reload数据
    pagesize_combo.on("select", function(comboBox) {
        bbar.pageSize = parseInt(comboBox.getValue());
        number = parseInt(comboBox.getValue());
        store_reload(false);
    });



    // 分页工具栏
    var bbar = new Ext.PagingToolbar( {
        id:'bbar',
        pageSize : number,
        store : store,
        displayInfo : true,
        displayMsg : '显示{0}条到{1}条,共{2}条',
        plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
        emptyMsg : "没有符合条件的记录",
        items : [ '-', '&nbsp;&nbsp;', pagesize_combo ]
    });



    var metadataMappingGroupComboBox = new Ext.form.ComboBox({
        emptyText:'请选择所属组',
        hiddenName : "profileTableId",
        forceSelection: true,
        anchor : '100%',
        store: new Ext.data.JsonStore({
            fields: ['id', 'mappingGroupName'],
            url : "../metadataMapping/getMappingGroupList.shtml",
            autoLoad:true,
            root : "data",
            listeners:{
                load : function( store, records, successful, operation){
                    if(successful){
                        var rec = new (store.recordType)();
                        rec.set('id', '');
                        rec.set('mappingGroupName', '全部分组');
                        store.insert(0,rec);
                    }
                }
            }
        }),
        valueField : "id",
        displayField : "mappingGroupName",
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        editable:false,
        selectOnFocus : true,// 设置用户能不能自己输入,true为只能选择列表中有的记录
        resizable : true,
        width: 200,
        listeners: {
            select : function(metadataMappingGroupComboBox, record,index){


                store_reload(false);
            }
        }
    });






    // 表格工具栏
    var tbar = new Ext.Toolbar({
        items : [{
            text : '新增',
            iconCls : 'page_addIcon',
            id : 'id_tbi_add',
            handler : function() {
                addItem();

            }
        },'-',{
            text : '编辑',
            id : 'tbi_del',
            iconCls : 'page_edit_1Icon',
            handler : function() {


                updateItem();
            }
        },'-',{
            text : '删除',
            id : 'tbi_edit',
            iconCls : 'page_delIcon',
            handler : function() {
                deleteItem();

            }
        },'-',"->",
            '所属分组:',metadataMappingGroupComboBox
            ,"-",{
                text :'刷新',
                id:'reset',
                handler : function() {
                    store_reload(true);
                }
            },'-',{
                text : '导出',
                id:'export',
                handler : function() {

                }
            }]
    });

    var table_id = '-1';
    var tmp = false;
    var cls = 'white-row';

    // 表格实例
    var grid = new Ext.grid.GridPanel( {
        height : 500,
        id : 'id_grid_sfxm',
        region : 'center',
        store : store,
        viewConfig : {
            forceFit : true,
            getRowClass:function(record,rowIndex,rowParams,store){
                var id_table = store.getAt(rowIndex).get('compareSql.compareSqlId');
                if(table_id != id_table ){
                    table_id = id_table
                    tmp = tmp?false:true
                    if(tmp){
                        cls = 'x-grid-record-rosybrown1';
                    }else{
                        cls = 'white-row';
                    }
                }
                return cls;
            }
        },
        cm : cm,
        sm : sm,
        tbar : tbar,
        bbar : bbar,
        loadMask : {
            msg : '正在加载表格数据,请稍等...'
        },
    });


    var viewport = new Ext.Viewport( {
        layout : 'border',
        items : [grid]
    });

    function store_reload(renovate){

        store.load( {
            params : {
                start : 0,
                limit :pagesize_combo.value,
                'mappingGroupId':metadataMappingGroupComboBox.value,

            },
            callback:function(records, options, success){
                if(!success){
                    Ext.Msg.alert('❌出错了',store.reader.jsonData.msg);
                }
            }
        });
    }

    store_reload(false);


    /**
     * 新增窗体初始化
     */
    function addItem() {
        clearForm(metadataMappingFromPanel.getForm());
        showCreateMetadataMappingFromWindow(store_reload);
    }


    function updateItem() {

        var record = grid.getSelectionModel().getSelections();
        if (Ext.isEmpty(record) || record.length > 1) {
            Ext.Msg.alert('提示:', '请先选中一条您要修改的数据');
            return;
        }

        Ext.Ajax.request( {
            url : '../metadataMapping/get.shtml',
            success : function(response) {
                var obj = Ext.util.JSON.decode(response.responseText);
                metadataMappingFromPanel.getForm().setValues(obj.data)
                sourceDbIdComboBox.setDisabled(true);
                sourceSchemaNameComboBox.setDisabled(true);
                sourceTableNameComboBox.setDisabled(true);
                showCreateMetadataMappingFromWindow(store_reload)


            },
            failure : function(response) {
                var obj = Ext.util.JSON.decode(response.responseText);
                Ext.Msg.alert('提示', obj.msg);
            },
            params : {
                id : record[0].get("id")
            }
        });

    }


    function deleteItem(){
        var record = grid.getSelectionModel().getSelections();
        if (Ext.isEmpty(record)) {
            Ext.Msg.alert('提示', '请先选中要删除的数据!');
            return;
        }
        var strChecked = jsArray2JsString(record, 'id');

        Ext.MessageBox.show({
            title:'删除提醒',
            msg: '请选择您要删除的数据',
            buttons:{yes:'确认',cancel:'取消'},
            fn: function(btn, text){

                if(btn == 'yes'){
                    Ext.Ajax.request( {
                        url : '../metadataMapping/delete.shtml',
                        success : function(response) {
                            Ext.MessageBox.alert('提示', '删除成功');

                            store_reload(false);
                        },
                        failure : function(response) {
                            var resultArray = Ext.util.JSON
                                .decode(response.responseText);
                            Ext.Msg.alert('提示', resultArray.msg);
                        },
                        params : {
                            ids : strChecked,
                        }
                    });
                }

            },
            icon: Ext.MessageBox.QUESTION
        });
    }



    function isContains(columnName,columns){

        var res = [];
        for(var i = 0 ; i < columns.length;i++){

            if(columnName == columns[i].columnName){

                res.push(true);
                res.push(columns[i].columnDesc);
                return res;
            }
        }

        res.push(false);
        res.push("");
        return res;
    }

});