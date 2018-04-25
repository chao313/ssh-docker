package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import com.google.common.collect.Maps;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.util
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/11/10     melvin                 Created
 */
public class IDCardUtil {

    private static final String AREA_DATA_PATH = "/data/chinese-id-card-area.csv";
    private static final String ID_NO_REGEX = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";

    private static Map<String, String> AREA_MAP = Maps.newHashMap();
//    static {
//        InputStream in = IDCardUtil.class.getClass().getResourceAsStream(AREA_DATA_PATH);
//        if (in != null) {
//            BufferedReader br = null;
//            String line = null;
//            try {
//                br = new BufferedReader(new InputStreamReader(in));
//                while ((line = br.readLine()) != null) {
//                    String[] splits = line.split(",");
//                    AREA_MAP.put(splits[0], splits[1]);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (br != null) {
//                    try {
//                        br.close();
//                    } catch (IOException e) {
//                    }
//                }
//            }
//        }
//    }

    public static class Area {
        private String id;
        private String name;

        public Area(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public enum Gender {
        MALE(1, "男"), FEMALE(0, "女");

        private int code;
        private String value;

        Gender(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public static boolean isValidIDNo(String no) {
        return no.matches(ID_NO_REGEX);
    }

    public static Area getArea(String no) {
        String areaId = no.substring(0, 6);
        String areaName = AREA_MAP.get(areaId);
        return new Area(areaId, areaName);
    }

    public static Date getBirthday(String no) {
        String body = no.substring(6, no.length());
        if (no.length() == 15) {
            body = "19" + body;
        }
        String year = body.substring(0, 4);
        String month = body.substring(4, 6);
        String date = body.substring(6, 8);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date));
        return calendar.getTime();
    }

    public static Gender getGender(String no) {
        int bit = no.length() == 18 ? 16 : 14;
        String n = no.substring(bit, bit + 1);
        Integer i = Integer.parseInt(n);
        return i % 2 != 0 ? Gender.MALE : Gender.FEMALE;
    }
}
