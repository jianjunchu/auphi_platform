package com.auphi.ktrl.system.log;

import com.auphi.ktrl.conn.util.ConnectionPool;
import com.auphi.ktrl.system.log.domain.SystemLog;
import com.auphi.ktrl.system.user.util.UMStatus;

import java.sql.*;

public class SystemLogUtil {

    public static synchronized void insertSysLog(SystemLog systemLog) {

        final String insert_sql = "INSERT INTO `KDI_T_SYSTEM_LOG`(`USERNAME`, `MODULE`, `OPERATION`, `METHOD`, `PARAMS`, `IP`, `CREATE_DATE`, `DEL_FLAG`) VALUES (?, ?, ?, ?, ?, ?, ?, 0);";
        UMStatus status = UMStatus.SUCCESS ;

        Connection conn = null ;

        try
        {
            ResultSet rs = null ;
            Statement stt = null ;
            PreparedStatement ps = null ;
            conn = ConnectionPool.getConnection() ;

            // 3. Insert new user
            ps = conn.prepareStatement(insert_sql) ;

            ps.setString(1, systemLog.getUsername()) ;
            ps.setString(2, systemLog.getModule()) ;
            ps.setString(3, systemLog.getOperation()) ;
            ps.setString(4, systemLog.getMethod()) ;
            ps.setString(5, "") ;
            ps.setString(6, systemLog.getIp()); ;
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis())); ;

            ps.executeUpdate() ;
            ps.close() ;



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            ConnectionPool.freeConn(null, null, null, conn) ;
        }
    }
}
