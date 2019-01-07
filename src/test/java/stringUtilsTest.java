import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.StringUtils;
import org.junit.Test;

public class stringUtilsTest {

    @Test
    public void testIdCard() {

        String idNumber = "23210119810209382X";
        boolean isRight = StringUtils.isIdNumber(idNumber);

        System.out.println(isRight);

    }

    @Test
    public void tt(){

        String s = DateUtils.formatDateByUnixTime(Long.parseLong("1546917005"), "yyyy-MM-dd HH:mm:ss");
        System.out.println(s);

    }
}