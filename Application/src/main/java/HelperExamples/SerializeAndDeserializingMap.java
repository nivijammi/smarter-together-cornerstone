package HelperExamples;

import java.util.HashMap;
import java.util.Map;

public class SerializeAndDeserializingMap {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("name", "nivi");
        map.put("city", "galena");
        String serializedMap = map.toString();
        System.out.println(serializedMap);
        String[] split = serializedMap.substring(1, serializedMap.length()-1).split(",");
        System.out.println(split[0] +" " +split[1]);

    }
}
