Ext.onReady(function(){

    // 定义列模型
    var sm = new Ext.grid.CheckboxSelectionModel();
    var rownum = new Ext.grid.RowNumberer( {
        header : 'NO',
        width : 28
    });

    var cm = new Ext.grid.ColumnModel( [sm, rownum,
        {
            header : '调度名称',
            sortable: true,
            dataIndex : 'jobName'
        },{
            header : '调度文件',
            dataIndex : 'jobFile'
        },{
            header : '集群',
            width : 80,
            dataIndex : 'haName'
        },{
            header : '子服务器',
            width : 80,
            dataIndex : 'serverName'
        },{
            header : '状态',
            width : 40,
            sortable: true,
            dataIndex : 'jobStatus'
        },{
            header : '用时/秒',
            width : 60,
            sortable: true,
            dataIndex : 'continuedTime'
        },{
            header : '开始时间',
            sortable: true,
            dataIndex : 'startTime'
        },{
            header : '结束时间',
            sortable: true,
            dataIndex: "endTime"
        },{
            header : '操作',
            sortable : true,
            width : 70,
            renderer: function (data, metadata, record, rowIndex, columnIndex, store) {
                var id = store.getAt(rowIndex).get('id');
                return "<input type='button' onclick='resume("+id+")' value='重载' />";

            }
        }
    ]);



    var store = new Ext.data.Store( {
        proxy : new Ext.data.HttpProxy( {
            url : '../monitor/query.shtml',
            timeout:1800000000
        }),
        reader : new Ext.data.JsonReader( {
            totalProperty : 'total',
            root : 'rows'
        }, [ {
            name : 'id'
        },{
            name : 'jobName'
        },{
            name:'jobFile'
        },{
            name:'jobStatus'
        },{
            name : 'startTime'
        },{
            name:'endTime'
        },{
            name:'continuedTime'
        },{
            name : 'logMsg'
        },{
            name:'errMsg'
        },{
            name : 'haName'
        },{
            name : 'serverName'
        },{
            name:'id_batch'
        },{
            name:'id_logchannel'
        },{
            name:'lines_error'
        },{
            name:'lines_input'
        },{
            name:'lines_output'
        },{
            name:'lines_updated'
        },{
            name:'lines_read'
        },{
            name:'lines_written'
        },{
            name:'lines_deleted'
        },{
            name:'destColumnType'
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
            text : '停止',
            iconCls : 'deleteIcon',
            id : 'id_tbi_add',
            handler : function() {
                stopSelectJob();
            }
        },'-',{
            text : '强制停止',
            iconCls : 'deleteIcon',
            id : 'id_tbi_qztz',
            handler : function() {
                stopAllSelectJob();
            }
        },'-',{
            text : '删除',
            id : 'tbi_del',
            iconCls : 'page_edit_1Icon',
            handler : function() {
                deleteItem();
            }
        },'-',{
            text : '清除监控',
            id : 'tbi_edit',
            iconCls : 'page_delIcon',
            handler : function() {
                clear();

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

                return 'white-row';
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




   var logMsgTextArea = new Ext.form.TextArea({
       id:'logMsgTextArea',
       labelAlign:'right',
       height:'100%',
       name: 'instruction',
       width: '100%',
       preventScrollbars : true,
       border: false,
       editable: false,
       readOnly:true,
       style:'background:none; border-right: 0px solid;border-top: 0px solid;border-left: 0px solid;border-bottom: 0px;',

   }) ;

    var iCount = null;


    var getMonitorById = function(id){
        Ext.Ajax.request( {
            url : '../monitor/get.shtml',
            sync:false,
            success : function(response) {
                var obj = Ext.util.JSON.decode(response.responseText);

                logMsgTextArea.setValue(obj.data.logMsg);

                document.getElementById('logMsgTextArea').scrollTop = document.getElementById('logMsgTextArea').scrollHeight;
                if(obj.data.endTime != null && obj.data.endTime != ''){
                    clearInterval(iCount);
                }
            },
            failure : function(response) {
                var obj = Ext.util.JSON.decode(response.responseText);
                Ext.Msg.alert('提示', obj.msg);
            },
            params : {
                id : id
            }
        });
    }


    grid.on("cellclick",function( grid, rowIndex, colIndex,record,event ){
        if(iCount){
            clearInterval(iCount);
        }
        var id = store.getAt(rowIndex).get('id');

        getMonitorById(id);
        iCount = setInterval(function () {
            getMonitorById(id);
        }, 5000);


    });

   var logPen = {
       region: "south",
       title:"执行日志",
       split: true,
       border: true,
       collapsible: true,
       height: 150,
       items:[logMsgTextArea]
   };

    var viewport = new Ext.Viewport( {
        layout : 'border',
        items : [grid,logPen]
    });

    function store_reload(renovate){

        store.load( {
            params : {
                start : 0,
                limit :pagesize_combo.value,
                jobName:document.getElementById("jobName").value

            }
        });
    }

    store_reload(false);


    function stopSelectJob() {
        var record = grid.getSelectionModel().getSelections();
        if (Ext.isEmpty(record)) {
            Ext.Msg.alert('提示', '请先选中要停止的作业!');
            return;
        }
        var strChecked = jsArray2JsString(record, 'id');

        Ext.MessageBox.show({
            title:'确认提醒',
            msg: '请选择您要停止选择的监控？',
            buttons:{yes:'确认',cancel:'取消'},
            fn: function(btn, text){

                if(btn == 'yes'){
                    Ext.Ajax.request( {
                        url : '../monitor/stop.shtml',
                        success : function(response) {
                            Ext.MessageBox.alert('提示', '操作成功');
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

    function stopAllSelectJob() {
        var record = grid.getSelectionModel().getSelections();
        if (Ext.isEmpty(record)) {
            Ext.Msg.alert('提示', '请先选中要停止的作业!');
            return;
        }
        var strChecked = jsArray2JsString(record, 'id');

        Ext.MessageBox.show({
            title:'确认提醒',
            msg: '请选择您要停止选择的监控？',
            buttons:{yes:'确认',cancel:'取消'},
            fn: function(btn, text){

                if(btn == 'yes'){
                    Ext.Ajax.request( {
                        url : '../monitor/stopAllForcely.shtml',
                        success : function(response) {
                            Ext.MessageBox.alert('提示', '操作成功');
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


    function clear() {
        winClear.show();
    }

    function clearByDate(date) {
        Ext.Ajax.request( {
            url : '../monitor/clear.shtml',
            success : function(response) {
                Ext.MessageBox.alert('提示', '清除成功');
                winClear.hide()
                store_reload(false);
            },
            failure : function(response) {
                var resultArray = Ext.util.JSON
                    .decode(response.responseText);
                Ext.Msg.alert('提示', resultArray.msg);
            },
            params : {
                clearDate : date,
            }
        });
    }

    function resume(id) {
        Ext.Ajax.request( {
            url : '../monitor/resume.shtml',
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
                id : id,
            }
        });
    }

    function deleteItem(){
        var record = grid.getSelectionModel().getSelections();
        if (Ext.isEmpty(record)) {
            Ext.Msg.alert('提示', '请先选中要删除的日志!');
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
                        url : '../monitor/delete.shtml',
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

   var winClear =  new Ext.Window( {
        layout : 'fit', // 设置窗口布局模式
        width : 200, // 窗口宽度
        height : 150, // 窗口高度
        modal:true,
        title:'清除监控日志',
        html:'*清除日志将清除启动时间在所选日期之前的所有监控记录，请谨慎使用！',
        resizable : false,// 是否可以改变大小，默认可以
        maskdisabled : true,
        closeAction : 'hide',
        closable : true, // 是否可关闭
        collapsible : true, // 是否可收缩
        border : false, // 边框线设置
        constrain : true, // 设置窗口是否可以溢出父容器
        animateTarget : Ext.getBody(),
        pageY : 200, // 页面定位Y坐标
        pageX : document.body.clientWidth / 2 - 200 / 2, // 页面定位X坐标
        items : [{
            xtype:'datefield',
            id:'clearDate',
            labelSeparator:':',//分隔符
            msgTarget:'side',//在字段右边显示一个提示信息
            autoFitErrors:false,//展示错误信息时是否自动调整字段组件宽度
            format:'Y-m-d',//显示日期的格式
            maxValue:'12/31/2030',//允许选择的最大日期
            minValue:'01/01/2008',//允许选择的最小日期
            disableDdates:['2008年3月12日'],//禁止选择2008年03月12日
            disabledDatesText:'禁止选择该日期',
            disabledDays:[0,6],//禁止选择星期日和星期六
            disabledDaysText:'禁止选择该日期',
            width:220
        }], // 嵌入的表单面板
        buttons : [ {
            text : '确认',
            iconCls : 'acceptIcon',
            handler : function() {
                clearByDate(Ext.getCmp("clearDate").value);
            }
        },{
            text : '关闭',
            iconCls : 'deleteIcon',
            handler : function() {

            }
        } ]
    });


});
