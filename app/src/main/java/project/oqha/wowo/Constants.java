package project.oqha.wowo;

/**
 * Created by oqha on 7/14/15.
 */
public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "project.oqha.wowo.action.main";

        public static String STARTFOREGROUND_ACTION = "project.oqha.wowo.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "project.oqha.wowo.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}