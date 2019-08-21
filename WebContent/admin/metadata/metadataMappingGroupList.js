Ext.onReady(function(){

    // 定义列模型
    var sm = new Ext.grid.CheckboxSelectionModel();
    var rownum = new Ext.grid.RowNumberer( {
        header : 'NO',
        width : 28
    });

    var cm = new Ext.grid.ColumnModel( [sm, rownum,
        {
            header : '分组名称',
            sortable : true,
            dataIndex : 'mappingGroupName',
        },{
            header : '分组描述',
            sortable : true,
            dataIndex : 'mappingGroupDesc',
        },{
            header : '创建时间',
            dataIndex : 'createTime',
        }
    ]);



    var store = new Ext.data.Store( {
        proxy : new Ext.data.HttpProxy( {
            url : '../metadataMappingGroup/query.shtml',
            timeout:1800000000
        }),
        reader : new Ext.data.JsonReader( {
            totalProperty : 'total',
            root : 'rows'
        }, [ {
            name : 'id'
        },{
            name : 'mappingGroupName'
        },{
            name:'mappingGroupDesc'
        },{
            name:'createTime'
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
        },'-',"->",{
                text :'刷新',
                id:'reset',
                handler : function() {
                    store_reload(true);
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
                var id_table = store.getAt(rowIndex).get('metadataMappingGroup.id');
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
                limit :pagesize_combo.value
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
        clearForm(metadataMappingGroupFromPanel.getForm());
        metadataMappingGroupFromWindow.setTitle('<span class="commoncss">新增</span>');
        showMetadataMappingGroupFromWindow(store_reload);
    }


    function updateItem() {

        var record = grid.getSelectionModel().getSelections();
        if (Ext.isEmpty(record) || record.length > 1) {
            Ext.Msg.alert('提示:', '请先选中一条您要修改的数据');
            return;
        }

        clearForm(metadataMappingGroupFromPanel.getForm());
        metadataMappingGroupFromWindow.setTitle('<span class="commoncss">编辑</span>');
        showMetadataMappingGroupFromWindow(store_reload);


        Ext.Ajax.request( {
            url : '../metadataMappingGroup/get.shtml',
            success : function(response) {
                var obj = Ext.util.JSON.decode(response.responseText);
                metadataMappingGroupFromPanel.getForm().setValues(obj.data)
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
            msg: '您确认要删除选择的数据?',
            buttons:{ok:'确定',cancel:'取消'},
            fn: function(btn, text){

                if(btn == 'ok'){
                    Ext.Ajax.request( {
                        url : '../metadataMappingGroup/delete.shtml',
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
                            ids : strChecked
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