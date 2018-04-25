package demo.spring.boot.demospringboot.sdxd.common.api.common.web.access;

import java.util.Calendar;
import java.util.Date;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.access
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 2017/7/11     melvin                 Created
 */
public class AccessEvent<T> {

    public static final String CATEGORY_HTTP = "HTTP";
    public static final String CATEGORY_RPC = "RPC";

    public static <T> AccessEvent<T> in(String category, String path, T content) {
        return new AccessEvent<>(category, path, IN, content);
    }

    public static <T> AccessEvent<T> out(String category, String path, T content) {
        return new AccessEvent<>(category, path, OUT, content);
    }

    public static <T> AccessEvent<T> err(String category, String path, T content) {
        return new AccessEvent<>(category, path, ERROR, content);
    }

    private static final int IN = 1;
    private static final int OUT = 2;
    private static final int ERROR = -1;

    private String category;
    private String path;
    private int type;
    private Date occurTime;

    private T content;

    private String timestamp;
    private long expireAt;

    private String message;

    private AccessEvent(String category, String path, int type, T content) {
        this.category = category;
        this.path = path;
        this.type = type;
        this.content = content;
        this.occurTime = new Date();

        Calendar calendar = calendar();
        this.timestamp = timestamp(calendar);
        this.expireAt = expireAt(calendar);

        this.message = String.format("%s:%s:%s:%s", category, path, type, occurTime);
    }

    public String getCategory() {
        return category;
    }

    public String getPath() {
        return path;
    }

    public boolean in() {
        return type == IN;
    }

    public boolean out() {
        return type == OUT;
    }

    public boolean err() {
        return type == ERROR;
    }

    public Date getOccurTime() {
        return occurTime;
    }

    public T getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public long getExpireAt() {
        return expireAt;
    }

    @Override
    public String toString() {
        return this.message;
    }

    private static Calendar calendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private static String timestamp(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int minute = currentMinute - currentMinute % 5;
        return String.format("%s.%s", hour, minute);
    }

    private static long expireAt(Calendar calendar) {
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        end.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        end.set(Calendar.DATE, calendar.get(Calendar.DATE));
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        end.add(Calendar.DATE, 1);
        return end.getTimeInMillis() / 1000;
    }
}
