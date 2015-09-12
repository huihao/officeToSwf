package rx.whh.aoo.utils;

import java.util.regex.Pattern;

/**
 * Created by whh on 2015/9/12.
 * email:wuhuihao@qq.com
 */
public class ToolUtil {
    public static final String ENVIRONMENT_WIN = "win";
    public static final String ENVIRONMENT_LINUX = "linux";
    public static final String OPENOFFICE_WIN_PATH = "D:/Program Files/OpenOffice 4";
    public static final String OPENOFFICE_LINUX_PATH = "/opt/openoffice.org4";

    public ToolUtil() {
    }

    public static String getOfficeHome() {
        String env = getEnvironment();
        return env.equals("linux") ? "/opt/openoffice.org4" : (env.equals("win") ? "D:/Program Files/OpenOffice 4" : null);
    }

    public static String getEnvironment() {
        String osName = System.getProperty("os.name");
        return Pattern.matches("Linux.*", osName) ? "linux" : (Pattern.matches("Windows.*", osName) ? "win" : null);
    }
}
