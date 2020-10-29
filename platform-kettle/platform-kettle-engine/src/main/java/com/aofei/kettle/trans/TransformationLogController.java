package com.aofei.kettle.trans;

import com.aofei.base.annotation.CurrentUser;
import com.aofei.base.model.response.CurrentUserResponse;
import com.aofei.kettle.App;
import com.aofei.kettle.PluginFactory;
import com.aofei.kettle.base.GraphCodec;
import com.aofei.kettle.utils.JSONObject;
import com.aofei.kettle.utils.JsonUtils;
import com.aofei.kettle.utils.StringEscapeHelper;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.logging.LogStatus;
import org.pentaho.di.core.logging.TransLogTable;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryObjectType;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.trans.dialog.TransDialog;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value="/trans/log")
public class TransformationLogController {

    @RequestMapping("/generateDDL")
    public void generateDDL(String graphXml, @CurrentUser CurrentUserResponse user) throws Exception {
        GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
        TransMeta transMeta = (TransMeta) codec.decode(graphXml, user);
        TransLogTable transLogTable = transMeta.getTransLogTable();

        boolean allOK = true;
        if (transLogTable.getDatabaseMeta() != null && !Utils.isEmpty(transLogTable.getTableName())) {
            Database db = null;

            try {
                db = new Database(transMeta, transLogTable.getDatabaseMeta());
                db.shareVariablesWith(transMeta);
                db.connect();
                StringBuilder ddl = new StringBuilder();
                RowMetaInterface fields = transLogTable.getLogRecord(LogStatus.START, (Object) null, (Object) null).getRowMeta();
                String tableName = db.environmentSubstitute(transLogTable.getTableName());
                String schemaTable = transLogTable.getDatabaseMeta().getQuotedSchemaTableCombination(db.environmentSubstitute(transLogTable.getSchemaName()), tableName);
                String createTable = db.getDDL(schemaTable, fields);
                if (!Utils.isEmpty(createTable)) {
                    ddl.append("-- ").append(transLogTable.getLogTableType()).append(Const.CR);
                    ddl.append("--").append(Const.CR).append(Const.CR);
                    ddl.append(createTable).append(Const.CR);
                }

                java.util.List<RowMetaInterface> indexes = transLogTable.getRecommendedIndexes();

                for (int i = 0; i < indexes.size(); ++i) {
                    RowMetaInterface index = (RowMetaInterface) indexes.get(i);
                    if (!index.isEmpty()) {
                        String createIndex = db.getCreateIndexStatement(schemaTable, "IDX_" + tableName + "_" + (i + 1), index.getFieldNames(), false, false, false, true);
                        if (!Utils.isEmpty(createIndex)) {
                            ddl.append(createIndex);
                        }
                    }
                }

                if (ddl.length() > 0) {
                    allOK = false;
                    ddl.toString();

                    JsonUtils.success(ddl.toString());
                }
            } finally {
                if (db != null) {
                    db.disconnect();
                }
            }
        }

        if(allOK) {
            JsonUtils.fail(BaseMessages.getString(TransDialog.class, "TransDialog.NoSqlNedds.DialogMessage", new String[0]));
        }
    }

    @ResponseBody
    @RequestMapping( "/getTransLogs")
    protected List getTransLogs(@RequestParam String path, @CurrentUser CurrentUserResponse user) throws Exception {
        String dir = path.substring(0, path.lastIndexOf("/"));
        String name = path.substring(path.lastIndexOf("/") + 1);

        Repository repository = App.getInstance().getRepository();
        RepositoryDirectoryInterface directory = repository.findDirectory(dir);
        if(directory == null)
            directory = repository.getUserHomeDirectory();

        TransMeta transMeta = repository.loadTransformation(name, directory, null, true, null);
        TransLogTable transLogTable = transMeta.getTransLogTable();

        ArrayList list = new ArrayList();
        if (transLogTable.getDatabaseMeta() != null && !Utils.isEmpty(transLogTable.getTableName())) {
            Database db = null;

            try {
                db = new Database(transMeta, transLogTable.getDatabaseMeta());
                db.shareVariablesWith(transMeta);
                db.connect();
                StringBuilder ddl = new StringBuilder();
                RowMetaInterface fields = transLogTable.getLogRecord(LogStatus.START, (Object) null, (Object) null).getRowMeta();
                String tableName = db.environmentSubstitute(transLogTable.getTableName());
                String schemaTable = transLogTable.getDatabaseMeta().getQuotedSchemaTableCombination(db.environmentSubstitute(transLogTable.getSchemaName()), tableName);

                String sql = "select * from " + schemaTable;
                List<Object[]> rows = db.getRows(sql, 100);
                RowMetaInterface rowMeta = db.getReturnRowMeta();

                for (Object[] row : rows) {

                    HashMap record = new HashMap();
                    for (int i = 0; i < rowMeta.size(); i++) {
                        ValueMetaInterface valueMeta = rowMeta.getValueMeta(i);
                        record.put(valueMeta.getName(), row[i]);
                    }
                    list.add(record);
                }



            } finally {
                if (db != null) {
                    db.disconnect();
                }
            }
        }

        return list;

    }

}
