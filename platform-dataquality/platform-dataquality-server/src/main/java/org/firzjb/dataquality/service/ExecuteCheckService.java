package org.firzjb.dataquality.service;

import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.dataquality.entity.Rule;
import org.firzjb.dataquality.model.enums.RuleType;
import org.firzjb.dataquality.model.request.CheckResultErrRequest;
import org.firzjb.dataquality.model.request.CheckResultRequest;
import org.firzjb.kettle.App;
import org.firzjb.utils.DateUtils;
import org.firzjb.utils.StringUtils;
import lombok.extern.log4j.Log4j;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaBase;
import org.pentaho.di.core.row.value.ValueMetaDate;
import org.pentaho.di.core.row.value.ValueMetaTimestamp;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j
@Service
public class ExecuteCheckService {

    public static final LoggingObjectInterface loggingObject = new SimpleLoggingObject("CompareSqlResultService", LoggingObjectType.DATABASE, null);

    @Value("#{propertiesReader['regex.email']}")
    private String regexEmail ; // 邮箱验证正则

    @Value("#{propertiesReader['regex.mobile']}")
    private String regexMobile ; // 手机号验证正则

    @Value("#{propertiesReader['regex.idCard']}")
    private String regexIdCard ; // 邮箱验证正则

    @Autowired
    private ICheckResultService  checkResultService;

    @Autowired
    private IRuleAttrService  ruleAttrService;


    private static Map<Long,Integer> thread = new HashMap<>();

    public Integer getThreadStatus(Long userId){
        if(thread.get(userId) ==null){
            return 100;
        }else{
            return thread.get(userId);
        }
    }

    @Async(value = "refreshDataQualityCheckResult")
    public void refreshCheckResult(Map<Long, List<Rule>> map, CurrentUserResponse user) {
        Repository repository = App.getInstance().getRepository();
        thread.put(user.getUserId(),0);
        try{

            for(Long key:map.keySet()){
                Database database = null;
                ResultSet resultSet = null;
                try {
                    ObjectId database_id = new LongObjectId(key);
                    DatabaseMeta databaseMeta = repository.loadDatabaseMeta(database_id, null);
                    database = new Database(loggingObject, databaseMeta);
                    database.connect();



                    List<Rule> list = map.get(key);
                    for(Rule rule : list){
                        String sql = getSQl(databaseMeta,rule);
                        resultSet =  database.openQuery(sql);

                        if(RuleType.IS_EMPTY.getCode().equals(rule.getRuleType()) ){
                            checkIsEmpty(user,rule,resultSet);
                        }else if(RuleType.IS_NOT_EMPTY.getCode().equals(rule.getRuleType())){
                            checkIsNotEmpty(user,rule,resultSet);
                        }else if(RuleType.EMAIL_CHECK.getCode().equals(rule.getRuleType())){
                            //邮箱验证
                            String regex = regexEmail;
                            checkForRegex(user,rule,resultSet,regex);

                        }else if(RuleType.MOBILE_CHECK.getCode().equals(rule.getRuleType())){
                            //手机号验证
                            String regex = regexMobile;
                            checkForRegex(user,rule,resultSet,regex);

                        }else if(RuleType.ID_CARD_CHECK.getCode().equals(rule.getRuleType())){
                            //身份证验证
                            String regex = regexIdCard;
                            checkForRegex(user,rule,resultSet,regex);

                        }else if(RuleType.NUM_RANGE_CHECK.getCode().equals(rule.getRuleType())){

                            Map<String,String>  ruleAttrMap = ruleAttrService.getAttrMapByRuleId(rule.getRuleId());

                            checkForNumRange(user,rule,resultSet,ruleAttrMap,databaseMeta);
                        }else if(RuleType.DATE_RANGE_CHECK.getCode().equals(rule.getRuleType())){

                            Map<String,String>  ruleAttrMap = ruleAttrService.getAttrMapByRuleId(rule.getRuleId());

                            checkForDateRange(user,rule,resultSet,ruleAttrMap,databaseMeta);
                        }else if(RuleType.REGEX_CHECK.getCode().equals(rule.getRuleType())){

                            Map<String,String>  ruleAttrMap = ruleAttrService.getAttrMapByRuleId(rule.getRuleId());
                            String regex = ruleAttrMap.get("REGEX");
                            checkForRegex(user,rule,resultSet,regex);
                        }

                        resultSet.close();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                    log.error(e);
                }finally {
                    if(database !=null){
                        database.disconnect();
                    }
                    repository.disconnect();

                }



            }

        }catch (Exception e){
            log.error(e);
        }finally {
            thread.remove(user.getUserId());
            if(repository!=null){
                repository.disconnect();
            }
        }


    }



    /**
     * 日期范围检查
     * @param user
     * @param rule
     * @param resultSet
     * @param ruleAttrMap
     * @param databaseMeta
     */
    private void checkForDateRange(CurrentUserResponse user, Rule rule, ResultSet resultSet, Map<String, String> ruleAttrMap, DatabaseMeta databaseMeta) throws SQLException, KettleDatabaseException, KettleValueException {
        CheckResultRequest result = initCheckResult(user,rule);

        List<CheckResultErrRequest>  errors = new  ArrayList();
        ValueMetaBase obj;

        if(ValueMetaInterface.TYPE_TIMESTAMP == rule.getFieldType()){
            obj = new ValueMetaTimestamp(rule.getFieldName());
        }else{
            obj = new ValueMetaDate(rule.getFieldName(),rule.getFieldType());
        }


        while(resultSet.next()){

            Object value =  resultSet.getObject(1);

            if(value !=null){
                obj.setConversionMask(ruleAttrMap.get("VALUE_FORMAT"));
                if(rule.getFieldType() ==ValueMetaInterface.TYPE_STRING || rule.getFieldType()==ValueMetaInterface.TYPE_DATE || rule.getFieldType()== ValueMetaInterface.TYPE_TIMESTAMP ){
                    Date value2 =  obj.getDate(value);

                    Date min2 =  DateUtils.format(ruleAttrMap.get("MIN_VALUE"), DateUtils.FORMATTER_YYYY_MM_DD_HH_MM_SS);
                    Date max2 =  DateUtils.format(ruleAttrMap.get("MAX_VALUE"),DateUtils.FORMATTER_YYYY_MM_DD_HH_MM_SS);


                    if(min2.getTime() < value2.getTime() && value2.getTime() < max2.getTime() ){
                        result.setPassedNum(result.getPassedNum()+1L);
                    }else{
                        result.setNotPassedNum(result.getNotPassedNum()+1L);
                        CheckResultErrRequest err = new CheckResultErrRequest();
                        err.setErrorValue(String.valueOf(value));
                        err.setOrganizerId(rule.getOrganizerId());
                        errors.add(err);
                    }
                }

            }

            result.setTotalCheckedNum(result.getTotalCheckedNum()+1L);
        }

        result.setErrs(errors);
        result.setCheckEndTime(new Date());
        checkResultService.save(result);

    }

    /**
     * 数值范围检查
     *
     * @param user
     * @param rule
     * @param resultSet
     * @param ruleAttrMap
     * @param databaseMeta
     * @throws SQLException
     * @throws KettleDatabaseException
     */
    private void checkForNumRange(CurrentUserResponse user, Rule rule, ResultSet resultSet, Map<String, String> ruleAttrMap, DatabaseMeta databaseMeta) throws SQLException, KettleDatabaseException, KettleValueException {

        CheckResultRequest result = initCheckResult(user, rule);

        List<CheckResultErrRequest>  errors = new  ArrayList();
        ValueMetaBase obj = new ValueMetaBase(rule.getFieldName(),rule.getFieldType());

        while(resultSet.next()){

           Object value =  obj.getValueFromResultSet(databaseMeta.getDatabaseInterface(),resultSet,0);


            if(value !=null){
                switch (rule.getFieldType()){

                    case ValueMetaInterface.TYPE_NUMBER:
                        Double value1 = obj.getNumber(value);
                        Double min1 = Double.valueOf(ruleAttrMap.get("MIN_VALUE"));
                        Double max1 = Double.valueOf(ruleAttrMap.get("MAX_VALUE"));
                        if(min1 <= value1 && value1 <= max1 ){
                            result.setPassedNum(result.getPassedNum()+1L);
                        }else{
                            result.setNotPassedNum(result.getNotPassedNum()+1L);
                            CheckResultErrRequest err = new CheckResultErrRequest();
                            err.setErrorValue(String.valueOf(value));
                            err.setOrganizerId(rule.getOrganizerId());
                            errors.add(err);
                        }

                        break;

                    case ValueMetaInterface.TYPE_INTEGER:
                        Long value2 = obj.getInteger(value);
                        Long min2 = Long.valueOf(ruleAttrMap.get("MIN_VALUE"));
                        Long max2 = Long.valueOf(ruleAttrMap.get("MAX_VALUE"));
                        if(min2 <= value2 && value2 <= max2 ){
                            result.setPassedNum(result.getPassedNum()+1L);
                        }else{
                            result.setNotPassedNum(result.getNotPassedNum()+1L);
                            CheckResultErrRequest err = new CheckResultErrRequest();
                            err.setErrorValue(String.valueOf(value2));
                            err.setOrganizerId(rule.getOrganizerId());
                            errors.add(err);
                        }

                        break;
                    case ValueMetaInterface.TYPE_BIGNUMBER:
                        BigDecimal value5 = obj.getBigNumber(value);
                        BigDecimal min5 = new  BigDecimal(ruleAttrMap.get("MIN_VALUE"));
                        BigDecimal max5 = new BigDecimal(ruleAttrMap.get("MAX_VALUE"));
                         if( (value5.compareTo(min5) == 0 || value5.compareTo(min5) > -1 )  &&  (value5.compareTo(max5) == 0 || value5.compareTo(max5) < 1) ){
                            result.setPassedNum(result.getPassedNum()+1L);
                        }else{
                            result.setNotPassedNum(result.getNotPassedNum()+1L);
                            CheckResultErrRequest err = new CheckResultErrRequest();
                            err.setErrorValue(String.valueOf(value5));
                            err.setOrganizerId(rule.getOrganizerId());
                            errors.add(err);
                        }

                        break;
                }
            }

            result.setTotalCheckedNum(result.getTotalCheckedNum()+1L);
        }

        result.setErrs(errors);
        result.setCheckEndTime(new Date());
        checkResultService.save(result);

    }

    private void checkIsNotEmpty(CurrentUserResponse user, Rule rule, ResultSet resultSet) throws SQLException {
        CheckResultRequest result = initCheckResult(user, rule);

        List<CheckResultErrRequest>  errors = new  ArrayList();

        while(resultSet.next()){

            Object value = resultSet.getObject(1);
            if(value !=null){
                result.setPassedNum(result.getPassedNum()+1L);
            }else{
                result.setNotPassedNum(result.getNotPassedNum()+1L);
            }
            result.setTotalCheckedNum(result.getTotalCheckedNum()+1L);
        }

        result.setErrs(errors);
        result.setCheckEndTime(new Date());
        checkResultService.save(result);
    }


    private void checkIsEmpty(CurrentUserResponse user, Rule rule, ResultSet resultSet) throws SQLException {
        CheckResultRequest result = initCheckResult(user, rule);

        List<CheckResultErrRequest>  errors = new  ArrayList();

        while(resultSet.next()){

            Object value = resultSet.getObject(1);
            if(value !=null){
                result.setNotPassedNum(result.getNotPassedNum()+1L);

                CheckResultErrRequest err = new CheckResultErrRequest();
                err.setErrorValue(value.toString());
                err.setOrganizerId(rule.getOrganizerId());
                errors.add(err);
            }
            result.setTotalCheckedNum(result.getTotalCheckedNum()+1L);
        }

        result.setErrs(errors);
        result.setCheckEndTime(new Date());
        checkResultService.save(result);

    }





    private void checkForRegex(CurrentUserResponse user, Rule rule, ResultSet resultSet, String regex) throws SQLException {

        CheckResultRequest result = initCheckResult(user, rule);

        List<CheckResultErrRequest>  errors = new  ArrayList();

        while(resultSet.next()){

            String value = resultSet.getString(1);

            if(value !=null){
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);

                if(!matcher.matches() ){
                    result.setNotPassedNum(result.getNotPassedNum()+1L);

                    CheckResultErrRequest err = new CheckResultErrRequest();
                    err.setErrorValue(value);
                    err.setOrganizerId(user.getOrganizerId());
                    errors.add(err);

                }else{
                    result.setPassedNum(result.getPassedNum()+1L);

                }
                result.setTotalCheckedNum(result.getTotalCheckedNum()+1L);
            }

        }

        result.setErrs(errors);
        result.setCheckEndTime(new Date());
        checkResultService.save(result);
    }


    private CheckResultRequest initCheckResult(CurrentUserResponse user, Rule rule) {
        CheckResultRequest result = new CheckResultRequest();

        result.setOrganizerId(rule.getOrganizerId());
        result.setRuleId(rule.getRuleId());
        result.setCheckStartTime(new Date());
        result.setNotPassedNum(0L);
        result.setPassedNum(0L);
        result.setTotalCheckedNum(0L);
        result.setCreateUser(user.getUsername());
        result.setUpdateUser(user.getUsername());

        return result;
    }

    private String getSQl(DatabaseMeta databaseMeta, Rule rule) {

        String tableName =databaseMeta.getQuotedSchemaTableCombination(rule.getSchemaName(),rule.getTableName());

        StringBuffer sql = new StringBuffer("SELECT ")
                .append(rule.getFieldName())
                .append(" FROM ")
                .append(tableName);
        if(!StringUtils.isEmpty(rule.getCondition())){
            sql.append(" WHERE ").append(rule.getCondition());
        }

        return sql.toString();

    }
}
