import java.util.regex.Pattern;

public class MyUtil {


    //判断内容是否为小数或数字
    public static boolean isNumeric(String str, char keyChar) {
        if (str.trim().length() <= 0 && keyChar == '.') {
            return false;
        }
        if (keyChar == '.' && str.indexOf(".") > 0) {
            return false;
        } else if (keyChar != '.') {
            str = str + keyChar;
        }

            return isNumber(str.replace(".", ""));

    }

    //判断是否为数字
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

}
