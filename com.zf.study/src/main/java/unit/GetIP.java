package unit;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetIP {
    public static void main(String[] args) {
        String strIP = "";
        String hostname ;
        GetIP getIP  = new GetIP();

//        hostname = "www.runoob.com";
//        hostname = "dn282";
//        hostname = "192.168.176.11";
        hostname = "node01";

        try {
            strIP = getIP.getIPOrHostName(hostname);
        }
        catch (Exception e) {
            System.exit(2);
        }
        System.out.println("strIP = " + strIP);
        System.exit(0);
    }

    public  String getIPOrHostName(String hostnameOrIP){
        InetAddress address;
        String returnStr = null;
        GetIP getIP  = new GetIP();

        try {
            address = InetAddress.getByName(hostnameOrIP);

            if(getIP.isIPAddressByRegex(hostnameOrIP)){
                returnStr = address.getHostName();
            }else{
                returnStr = address.getHostAddress();
            }
        }
        catch (UnknownHostException e) {
            System.exit(2);
        }
        return returnStr;
    }

    /**
     * 用正则表达式进行判断
     */
    public boolean isIPAddressByRegex(String str) {
        String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        // 判断ip地址是否与正则表达式匹配
        if (str.matches(regex)) {
            String[] arr = str.split("\\.");
            for (int i = 0; i < 4; i++) {
                int temp = Integer.parseInt(arr[i]);
                //如果某个数字不是0到255之间的数 就返回false
                if (temp < 0 || temp > 255) return false;
            }
            return true;
        } else return false;
    }

}
