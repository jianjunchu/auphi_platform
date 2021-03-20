import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import javax.xml.namespace.QName;

public class Test {

    public static void main(String[] args) throws Exception {
        String webUrl ="http://localhost:8080/etl_platform/ws/Schedule?wsdl";
        String webSoap = "http://controller.webservice.firzjb.com/";
        Object[] paramValue = new Object[]{"aaa"};
        String result= callWebServiceByJaxWs(webUrl,webSoap,"execute",paramValue);
        System.out.println(result);
    }


    public static String callWebServiceByJaxWs(String wsdUrl,
                                               String namespace, String methodName, Object[] obj) throws Exception{
        String result = "";

        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client = dcf.createClient(wsdUrl);//wsdUrl为调用webService的wsdl地址
        QName name = new QName(namespace, methodName); //namespace是命名空间，methodName是方法名
        try {
            Object[] ss = client.invoke(name, obj);
            System.out.println(ss[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
