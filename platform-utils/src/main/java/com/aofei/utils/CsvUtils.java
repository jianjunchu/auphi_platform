package com.aofei.utils;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {

    /**
     * 写cvs
     * @param filePath
     * @param headers
     * @param contents
     * @throws IOException
     */
    public static void write(String filePath, String[] headers, List<String[]> contents) throws IOException {


        // 创建CSV写对象
        CsvWriter csvWriter = new CsvWriter(filePath,',', Charset.forName("UTF-8"));
        //CsvWriter csvWriter = new CsvWriter(filePath);
        if(headers!=null){
            csvWriter.writeRecord(headers);
        }

        for(String[] content : contents ){
            csvWriter.writeRecord(content);
        }

        csvWriter.close();
    }

    /**
     *
     * @param filePath
     * @param readHeader 是否读表头 true 结果包含表头 false 接口不含糊表头
     * @throws IOException
     */
    public static List<String[]> read(String filePath,boolean readHeader) throws IOException {


        // 创建CSV读对象
        CsvReader csvReader = new CsvReader(filePath,',', Charset.forName("UTF-8"));


        // 读表头
        if(!readHeader){
            csvReader.readHeaders();
        }

        List<String[]> list = new ArrayList<>();

        while (csvReader.readRecord()){

            int count = csvReader.getColumnCount();
            String[] cols = new String[count];

            for(int i = 0; i < count ;i++){
                cols[i] = csvReader.get(i);
            }
            list.add(cols);
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
        String filePath = "test.csv";
        String[] headers = {"编号","姓名","年龄"};
        String[] content = {"12365","张山","34"};
        read(filePath,false);

    }
}
